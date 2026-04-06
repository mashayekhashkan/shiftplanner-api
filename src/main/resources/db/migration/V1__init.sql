CREATE TABLE store (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    timezone TEXT NOT NULL
);

CREATE TABLE employee (
    id UUID PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    weekly_hours_target NUMERIC(4,1) NOT NULL,
    saturday_only BOOLEAN NOT NULL,
    only_early_shift BOOLEAN NOT NULL,
    only_late_shift BOOLEAN NOT NULL,
    store_id UUID NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT fk_employee_store
        FOREIGN KEY (store_id)
        REFERENCES store(id)
);

CREATE TABLE availability (
    id UUID PRIMARY KEY,
    employee_id UUID NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    shift_code VARCHAR(20) NOT NULL,
    available BOOLEAN NOT NULL,

    CONSTRAINT fk_availability_employee
        FOREIGN KEY (employee_id)
        REFERENCES employee(id),

    CONSTRAINT uq_availability_employee_day_shift
        UNIQUE (employee_id, day_of_week, shift_code)
);

CREATE TABLE employee_block_day (
    id UUID PRIMARY KEY,
    employee_id UUID NOT NULL,
    date_of_block DATE NOT NULL,

    CONSTRAINT fk_employee_block_day_employee
        FOREIGN KEY (employee_id)
        REFERENCES employee(id),

    CONSTRAINT uq_employee_block_day_employee_date
        UNIQUE (employee_id, date_of_block)
);

CREATE TABLE schedule (
    id UUID PRIMARY KEY,
    store_id UUID NOT NULL,
    start_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,

    CONSTRAINT fk_schedule_store
        FOREIGN KEY (store_id)
        REFERENCES store(id),

    CONSTRAINT uq_schedule_store_status
        UNIQUE (store_id, start_date)
);

CREATE TABLE schedule_issue (
    id UUID PRIMARY KEY,
    schedule_id UUID NOT NULL,
    type VARCHAR(40) NOT NULL,
    message VARCHAR(500) NOT NULL,
    issue_date DATE,
    shift_code VARCHAR(20) NOT NULL,

    CONSTRAINT fk_schedule_schedule_issue
        FOREIGN KEY (schedule_id)
        REFERENCES schedule(id)
);

CREATE TABLE shift_assignment (
    id UUID PRIMARY KEY,
    schedule_id UUID NOT NULL,
    assignment_date DATE NOT NULL,
    shift_code VARCHAR(20) NOT NULL,
    employee_id UUID NOT NULL,
    assignment_hours NUMERIC(5,2) NOT NULL,

    CONSTRAINT fk_shift_assignment_schedule
        FOREIGN KEY (schedule_id)
        REFERENCES schedule(id),

    CONSTRAINT fk_shift_assignment_employee
        FOREIGN KEY (employee_id)
        REFERENCES employee(id)
);

CREATE TABLE weekly_requirement (
    id UUID PRIMARY KEY,
    store_id UUID NOT NULL,
    week_start DATE NOT NULL,
    day_of_week VARCHAR(10) NOT NULL,
    shift_code VARCHAR(20) NOT NULL,
    required_headcount INT NOT NULL,

    CONSTRAINT fk_weekly_requirement_store
        FOREIGN KEY (store_id)
        REFERENCES store(id),

    CONSTRAINT uq_weekly_requirement
        UNIQUE (store_id, week_start, day_of_week, shift_code),

    CONSTRAINT chk_weekly_requirement_headcount
        CHECK (required_headcount BETWEEN 0 AND 10)
);

CREATE TABLE flyway_test (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL
);
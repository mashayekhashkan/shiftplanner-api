CREATE TABLE flyway_test (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL
);

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
CREATE TABLE app_user (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rolle VARCHAR(255) NOT NULL,
    store_id UUID NOT NULL,
    CONSTRAINT fk_app_user_store
        FOREIGN KEY (store_id)
        REFERENCES store(id)
);
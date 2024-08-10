CREATE TABLE balance (
    account_id VARCHAR NOT NULL,
    category INT2 NOT NULL,
    total_amount NUMERIC(19, 2) CHECK (total_amount >= 0),
    PRIMARY KEY (account_id, category)
);
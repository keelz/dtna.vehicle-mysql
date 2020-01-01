
DROP TABLE IF EXISTS vehicle;
CREATE TABLE vehicle (
    id varchar(36) NOT NULL PRIMARY KEY,
    vin varchar(255) NOT NULL,
    created TIMESTAMP,
    modified TIMESTAMP,
    completed BOOLEAN
);

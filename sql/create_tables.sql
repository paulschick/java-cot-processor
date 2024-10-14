-- Table for Cot entity
-- Timestamp Str Expected: 2006-01-02 00:00:00.000
CREATE TABLE Cot
(
    id                   INTEGER PRIMARY KEY AUTOINCREMENT,
    date                 TEXT,
    market               TEXT,
    contract_name        TEXT,
    commodity_name       TEXT,
    commodity_subgroup   TEXT,
    commodity_group      TEXT,
    open_interest        TEXT,
    non_commercial_long  TEXT,
    non_commercial_short TEXT,
    non_commercial_net   TEXT,
    comm_long            TEXT,
    comm_short           TEXT,
    comm_net             TEXT,
    non_rept_long        TEXT,
    non_rept_short       TEXT,
    non_rept_net         TEXT
);

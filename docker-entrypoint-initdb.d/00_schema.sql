CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    login    TEXT        NOT NULL UNIQUE,
    password TEXT        NOT NULL,
    role     TEXT        NOT NULL,
    removed  BOOLEAN     NOT NULL DEFAULT FALSE,
    created  timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE spare_parts
(
    id          BIGSERIAL PRIMARY KEY,
    owner_id    BIGINT      NOT NULL REFERENCES users,
    name        TEXT        NOT NULL,
    description TEXT        NOT NUll,
    quantity    BIGINT      NOT NUll,
    image       TEXT        NOT NULL,
    removed     BOOLEAN     NOT NULL DEFAULT FALSE,
    created     timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE history
(
    id        BIGSERIAL PRIMARY KEY,
    owner_id  BIGINT NOT NULL REFERENCES spare_parts,
    part_id   BIGINT NOT NULL REFERENCES spare_parts,
    type TEXT NOT NULL ,
    part_name TEXT NOT NULL ,
    part_qty  BIGINT NOT NULL,
    part_created timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
)


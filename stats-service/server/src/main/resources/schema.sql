CREATE TABLE IF NOT EXISTS STATS (
    id      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    app     VARCHAR(255) NOT NULL,
    uri     VARCHAR(255) NOT NULL,
    ip      VARCHAR(255) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_stat PRIMARY KEY (id)
);

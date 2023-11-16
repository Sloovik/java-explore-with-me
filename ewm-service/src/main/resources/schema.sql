CREATE TABLE IF NOT EXISTS USERS
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(250)                            NOT NULL,
    email VARCHAR(254)                            NOT NULL,

    CONSTRAINT PK_USERS_ID PRIMARY KEY (id),
    CONSTRAINT UQ_USERS_EMAIL UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS CATEGORIES
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50)                             NOT NULL,

    CONSTRAINT PK_CATEGORIES_ID PRIMARY KEY (id),
    CONSTRAINT UQ_CATEGORIES_NAME UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS LOCATIONS
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat DOUBLE PRECISION                        NOT NULL,
    lon DOUBLE PRECISION                        NOT NULL,

    CONSTRAINT PK_LOCATIONS_ID PRIMARY KEY (id),
    CONSTRAINT UQ_LOCATIONS UNIQUE (lat, lon)
    );

CREATE TABLE IF NOT EXISTS EVENTS
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    location_id        BIGINT                                  NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INT                                     NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN DEFAULT TRUE,
    state              VARCHAR(20)                             NOT NULL,
    title              VARCHAR(120)                            NOT NULL,

    CONSTRAINT PK_EVENTS PRIMARY KEY (id),
    CONSTRAINT FK_EVENTS_CATEGORY_ID FOREIGN KEY (category_id) REFERENCES CATEGORIES (id) ON DELETE CASCADE,
    CONSTRAINT FK_EVENTS_INITIATOR_ID FOREIGN KEY (initiator_id) REFERENCES USERS (id) ON DELETE CASCADE,
    CONSTRAINT FK_EVENTS_LOCATION_ID FOREIGN KEY (location_id) REFERENCES LOCATIONS (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS REQUESTS
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event     BIGINT                                  NOT NULL,
    requester BIGINT                                  NOT NULL,
    status    VARCHAR(20)                             NOT NULL,

    CONSTRAINT PK_REQUESTS PRIMARY KEY (id),
    CONSTRAINT FK_REQUESTS_EVENTS FOREIGN KEY (event) REFERENCES EVENTS (id) ON DELETE CASCADE,
    CONSTRAINT FK_REQUESTS_REQUESTER FOREIGN KEY (requester) REFERENCES USERS (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS COMPILATIONS
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN DEFAULT FALSE,
    title  VARCHAR(50)                             NOT NULL,

    CONSTRAINT PK_COMPILATIONS PRIMARY KEY (id),
    CONSTRAINT UQ_COMPILATIONS_TITLE UNIQUE (title)
    );

CREATE TABLE IF NOT EXISTS COMPILATIONS_EVENTS
(
    event_id       BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,

    CONSTRAINT PK_COMPILATIONS_EVENTS PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT FK_COMPILATIONS_EVENTS_EVENTS FOREIGN KEY (event_id) REFERENCES EVENTS (id) ON DELETE CASCADE,
    CONSTRAINT FK_COMPILATIONS_EVENTS_COMPILATIONS FOREIGN KEY (compilation_id) REFERENCES COMPILATIONS (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS COMMENTS
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text       VARCHAR(5000)                           NOT NULL,
    author_id  BIGINT                                  NOT NULL,
    event_id   BIGINT                                  NOT NULL,
    created_on TIMESTAMP                               NOT NULL,
    status     VARCHAR(20)                             NOT NULL,

    CONSTRAINT PK_COMMENTS PRIMARY KEY (id),
    CONSTRAINT FK_COMMENTS_EVENT_ID FOREIGN KEY (event_id) REFERENCES EVENTS (id) ON DELETE CASCADE,
    CONSTRAINT FK_COMMENTS_AUTHOR_ID FOREIGN KEY (author_id) REFERENCES USERS (id) ON DELETE CASCADE
    );
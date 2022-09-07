interface EmailInterface {
    RequestResponse:
        sendEmail(undefined)(undefined),
}

constants {
    LOCATION_SERVICE_EMAIL = "socket://host.docker.internal:9055",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "host.docker.internal",
    SQL_DATABASE = "postgres",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_EMAIL_INFO = "CREATE TABLE emails (
                                    id varchar(128) NOT NULL,
                                    subject varchar(128) NOT  NULL,
                                    message varchar(255) NOT NULL,
                                    toEmail varchar(255) NOT NOT NULL,
                                    CONSTRAINT emails_pkey PRIMARY KEY (id)
                            );
                            COMMENT ON TABLE emails IS 'Table to keep a record of the emails sent.';
                            "
}
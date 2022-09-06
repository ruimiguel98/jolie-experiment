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
}
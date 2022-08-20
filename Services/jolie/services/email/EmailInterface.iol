interface EmailInterface {
    RequestResponse:
        sendEmail(undefined)(undefined),
}

constants {
    LOCATION_SERVICE_EMAIL = "socket://localhost:9055",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    // SQL_HOST = "172.30.0.1",
    SQL_HOST = "localhost",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",
}
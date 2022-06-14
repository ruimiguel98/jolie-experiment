interface PaymentInterface {
    RequestResponse:
        processPayment(undefined)(undefined),
}

constants {
    LOCATION_SERVICE_CART = "socket://localhost:9002",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "172.30.0.1",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",
}
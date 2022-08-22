type PaymentWithdrawlRequest {
    .cardNumber: int
    .amount: double
}

interface PaymentInterface {
    RequestResponse:
        withdrawlAccount(PaymentWithdrawlRequest)(undefined),
}

constants {
    LOCATION_SERVICE_PAYMENT = "socket://localhost:9056",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    // SQL_HOST = "172.30.0.1",
    SQL_HOST = "localhost",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",


    SQL_CREATE_PAYMENT_INFO = "CREATE TABLE public.payment (
                                    card_number int4 NOT NULL,
                                    cvv varchar(255) NULL,
                                    account_balance varchar(255) NULL,
                                    card_type varchar(255) NULL,
                                    expire_date varchar(255) NULL,
                                    real_name varchar(255) NULL,
                                    CONSTRAINT payment_pkey PRIMARY KEY (card_number)
                                );"
}
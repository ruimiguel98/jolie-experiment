type PaymentWithdrawlRequest {
    .cardNumber: string
    .amount: double
}

interface PaymentInterface {
    RequestResponse:
        withdrawlAccount(PaymentWithdrawlRequest)(undefined),
}

constants {
    LOCATION_SERVICE_PAYMENT = "socket://host.docker.internal:9056",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    // SQL_HOST = "172.30.0.1",
    SQL_HOST = "host.docker.internal",
    SQL_DATABASE = "postgres",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_PAYMENT_INFO = "CREATE TABLE public.payment (
                                    card_number varchar(20) NOT NULL,
                                    cvv varchar(3) NULL,
                                    account_balance varchar(255) NULL,
                                    card_type varchar(50) NULL,
                                    expire_date varchar(50) NULL,
                                    real_name varchar(100) NULL,
                                    CONSTRAINT payment_pkey PRIMARY KEY (card_number)
                                );"
}
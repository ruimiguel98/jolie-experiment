type CreateRequest {
    .id: string // to accept UUID values
    .realName: string
    .email: string
    .phone: string
    .address: string
    .gender: string
}

type CreateResponse {
    .message: string
}


interface UserInterface {
    RequestResponse:
        all(void)(undefined),
        user(undefined)(undefined),
        create(CreateRequest)(CreateResponse),
        update(undefined)(undefined),
        delete(undefined)(undefined)
}

constants {
    LOCATION_SERVICE_USER = "socket://localhost:9052",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "localhost",
    // SQL_HOST = "172.30.0.1",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_TABLE_USER = "CREATE TABLE public.users (
                                id VARCHAR(40),
                                real_name VARCHAR(50),
                                email VARCHAR(50),
                                phone VARCHAR(50),
                                address VARCHAR(50),
                                gender VARCHAR(50),
                                CONSTRAINT users_pkey PRIMARY KEY (id)
                            );"
}
type CreateRequest {
    .realName: string
    .email: string
    .phone: string
    .address: string
    .gender: string
}

interface UserInterface {
    RequestResponse:
        all(void)(undefined),
        user(undefined)(undefined),
        create(CreateRequest)(undefined),
        update(undefined)(undefined),
        delete(undefined)(undefined)
}

constants {
    LOCATION_SERVICE_USER = "socket://host.docker.internal:9052",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "host.docker.internal",
    SQL_DATABASE = "postgres",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_TABLE_USER = "CREATE TABLE public.users (
                                id UUID,
                                real_name VARCHAR(50),
                                email VARCHAR(50),
                                phone VARCHAR(50),
                                address VARCHAR(50),
                                gender VARCHAR(50),
                                CONSTRAINT users_pkey PRIMARY KEY (id)
                            );"
}
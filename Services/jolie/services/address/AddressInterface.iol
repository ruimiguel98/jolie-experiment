type AddressAddRequest {
    .id: int
    .user_owner: int
    .address: string
    .postal_code: string
    .city: string
}

type AddressDeleteRequest {
    .id: int
}

type AddressListRequest {
    .user_owner: int
}


interface AddressInterface {
    RequestResponse:
        addAddressInfo(AddressAddRequest)(undefined),
        deleteAddressInfo(AddressDeleteRequest)(undefined),
        getSavedAddressInfoList(AddressListRequest)(undefined),
}

constants {
    LOCATION_SERVICE_PAYMENT = "socket://localhost:9004",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    // SQL_HOST = "172.30.0.1",
    SQL_HOST = "localhost",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",


    SQL_CREATE_PAYMENT_INFO = "CREATE TABLE public.address (
                                    id numeric NOT NULL,
                                    user_owner numeric NULL,
                                    address text NOT NULL,
                                    postal_code text NOT NULL,
                                    city text NOT NULL
                                );
                                COMMENT ON TABLE public.address IS 'Table that holds the several address information for the user to not have to input it all the time, one or more address info can be associated with the same user.';"
}
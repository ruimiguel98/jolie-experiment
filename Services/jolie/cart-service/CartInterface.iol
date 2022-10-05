type Cart {
    .id: string
    .cartPriceTotal: double
    .userId: string
    .products[1, *]: Product
}

type Carts {
    .carts[1, *]: Cart // an array of Cart
}

type CartProducts {
    .productId: string
    .cartId: string
    .priceTotal: double
    .quantity: int
}

type CreateCartRequest {
    .userId: string // to accept UUID values
}

type AddProductToCartRequest {
    .cartId: string // to accept UUID values
    .productId: string // to accept UUID values
    .quantity: int
}

type RemoveProductFromCartRequest {
    .cartId: string // to accept UUID values
    .productId: string // to accept UUID values
}

interface CartInterface {
    RequestResponse:
        // CRUD operations
        all(void)(undefined),
        cart(undefined)(undefined),
        create(CreateCartRequest)(undefined),
        update(undefined)(undefined),
        delete(undefined)(string),

        // OTHER operations
        addProduct(AddProductToCartRequest)(undefined),
        removeProduct(RemoveProductFromCartRequest)(undefined)
}


constants {
    LOCATION_SERVICE_CART = "socket://host.docker.internal:9057",
    LOCATION_SERVICE_PRODUCT = "socket://host.docker.internal:9051", // used by cart orchestrator

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "host.docker.internal",
    SQL_DATABASE = "postgres",
    SQL_DRIVER = "postgresql",


    SQL_CREATE_CART_INFO = "CREATE TABLE public.cart (
                                id UUID NOT NULL,
                                user_id UUID null,
                                CONSTRAINT cart_pkey PRIMARY KEY (id)
                            );
                            CREATE TABLE public.cart_products (
                                cart_id UUID NOT NULL,
                                product_id UUID NOT NULL,
                                quantity numeric NOT NULL DEFAULT 1,
                                price_total NOT NULL
                            );
                            COMMENT ON TABLE public.cart IS 'Table that holds the cart information.';
                            COMMENT ON TABLE public.cart_products IS 'Table that holds the cart products information.';"
}
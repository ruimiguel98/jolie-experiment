include "console.iol"
include "database.iol"
include "string_utils.iol"

include "./CartInterface.iol"

execution { concurrent }

// deployment info
inputPort CartPort {
    Location: LOCATION_SERVICE_CART
    Protocol: http { .format = "json" }
    Interfaces: CartInterface
}

// prepare database connection (creates table if does not exist)
init
{
    with (connectionInfo) {
        .username = SQL_USERNAME;
        .password = SQL_PASSWORD;
        .host = SQL_HOST;
        .database = SQL_DATABASE; // "." for memory-only
        .driver = SQL_DRIVER
    };

    connect@Database(connectionInfo)();
    println@Console("Successfull connection to the PostgreSQL database")();

    // create cart table if it does not exist
    scope (createTable) {
        install (SQLException => println@Console("Cart table already exists")());
        update@Database(SQL_CREATE_TABLE_CART)(ret)
    }
}

// behaviour info
main
{
    [ 
        cartRetrieve(request)(response) {
            println@Console( "Fetching cart with id " + request.id )(  )

            query@Database(
                "SELECT * FROM cart WHERE id=:id::numeric" {
                    .id = request.id
                }
            )(sqlResponse);

            response.cart -> sqlResponse.row
        }
    ]

    [ 
        cartCreate(request)(response) {

            println@Console("Creating cart " + request.id + " for user " + request.user)()

            update@Database(
                "INSERT INTO cart(id, products, user_owner) VALUES (:id, array[]::integer[], :user)" {
                    .id = request.id,
                    .user = request.user
                }
            )(response.status)
        } 
    ]

    [
        cartAppendProduct(request)(response) {

            println@Console("Appending product " + request.product + " to cart " + request.id)()

            update@Database(
                "UPDATE cart SET products = ARRAY_APPEND(products, :product) WHERE id = :id;" {
                    .id = request.id,
                    .product = request.product
                }
            )(response.status)
        }
    ]

    [ 
        cartRemoveProduct(request)(response) {

            println@Console("Deleting product " + request.product + " from cart " + request.id)()

            update@Database(
                "UPDATE cart SET products = ARRAY_REMOVE(products, :product) WHERE id = :id;" {
                    .id = request.id,
                    .product = request.product
                }
            )(response.status)
        } 
    ]

    [ 
        cartDelete(request)(response) {

            println@Console("Deleting cart " + request.id)()

            update@Database(
                "DELETE FROM cart WHERE id=:id" {
                    .id = request.id
                }
            )(response.status)
        } 
    ]
}
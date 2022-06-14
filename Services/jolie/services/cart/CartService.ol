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
            println@Console( "The request on cart service is " + request.id )(  )

            query@Database(
                "select * from cart where id=:id" {
                    .id = request.id
                }
            )(sqlResponse);

            response.cart -> sqlResponse.row
        }
    ]

    [ 
        cartAdd(request)(response) {
            update@Database(
                "insert into cart(id, products) values (:id, ARRAY[:product])" {
                    .id = request.id,
                    .product = request.product
                }
            )(response.status)
        } 
    ]

    [
        cartAppend(request)(response) {

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
        cartDelete(request)(response) {

            println@Console("Deleting cart " + request.id)()

            update@Database(
                "delete from cart where id=:id" {
                    .id = request.id
                }
            )(response.status)
        } 
    ]
}
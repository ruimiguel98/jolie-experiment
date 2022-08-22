include "console.iol"
include "database.iol"
include "string_utils.iol"
include "time.iol"

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

    // create check table if it does not exist
    scope (createTable) {
        install (SQLException => println@Console("Cart table already exists")());
        update@Database(SQL_CREATE_CART_INFO)(ret)
    }

    getCurrentDateTime@Time( )( currentDateTime ) // call this API to create a GLOBAL VARIABLE for current datetime to be present in logs
}

// behaviour info
main
{
    [
        all()(response) {
            println@Console( "[CART] - [" + currentDateTime + "] - [/all] -  fetch all carts" )(  )

            query@Database(
                "SELECT * FROM cart"
            )(sqlResponse);

            if (#sqlResponse.row >= 1) {
                response -> sqlResponse
            }
        }
    ]

    [
        cart(request)(response) {
            println@Console( "[CART] - [" + currentDateTime + "] - [/cart] -  fetch cart with id " + request.id )(  )

            query@Database(
                "SELECT * FROM cart WHERE id=:id" {
                    .id = request.id
                }
            )(sqlResponse);

            if (#sqlResponse.row >= 1) {
                response -> sqlResponse
            }
        }
    ]

    [
        create(request)(response) {
            println@Console( "[CART] - [" + currentDateTime + "] - [/create] - create cart" )(  )

            update@Database(
                "INSERT INTO public.cart(id, user_id, cart_produtcs, cart_total)
                 VALUES(:id, :userId, :cartProducts, :cartTotal::numeric);" {
                    .id = request.id,
                    .userId = request.userId,
                    .cartProducts = request.cartProducts,
                    .cartTotal = request.cartTotal,
                }
            )(response.status)
        }
    ]

    [
        update(request)(response) {
            println@Console( "[CART] - [" + currentDateTime + "] - [/update] -  update cart with id " + request.id )(  )

            update@Database(
                "UPDATE cart SET
                    address_to_ship = :addressToShip,
                    order_amount = :orderAmount,
                    order_products = :orderProducts,
                    status = :status,
                    user_id= :userId::numeric
                WHERE id=:id;" {
                    .id = request.id,
                    .addressToShip = request.addressToShip,
                    .orderAmount = request.orderAmount,
                    .orderProducts = request.orderProducts,
                    .status = request.status,
                    .userId = request.userId
                }
            )(response.status)
        }
    ]

    [ 
        delete(request)(response) {
            println@Console( "[CART] - [" + currentDateTime + "] - [/delete] -  delete cart with id " + request.id )(  )

            update@Database(
                "DELETE FROM cart WHERE id=:id" {
                    .id = request.id
                }
            )(response.status)
        } 
    ]

}
include "console.iol"
include "database.iol"
include "string_utils.iol"
include "time.iol"

include "./OrderInterface.iol"

execution { concurrent }

// deployment info
inputPort OrderPort {
    Location: LOCATION_SERVICE_ORDER
    Protocol: http { .format = "json" }
    Interfaces: OrderInterface
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
        install (SQLException => println@Console("Order table already exists")());
        update@Database(SQL_CREATE_ORDER_INFO)(ret)
    }

    getCurrentDateTime@Time( )( currentDateTime ) // call this API to create a GLOBAL VARIABLE for current datetime to be present in logs
}

// behaviour info
main
{
    [
        all()(response) {
            println@Console( "[ORDER] - [" + currentDateTime + "] - [/all] -  fetch all orders" )(  )

            query@Database(
                "SELECT * FROM orders"
            )(sqlResponse);

            if (#sqlResponse.row >= 1) {
                response -> sqlResponse
            }
        }
    ]

    [
        order(request)(response) {
            println@Console( "[ORDER] - [" + currentDateTime + "] - [/order] -  fetch order with id " + request.id )(  )

            query@Database(
                "SELECT * FROM orders WHERE id=:id" {
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
            println@Console( "[ORDER] - [" + currentDateTime + "] - [/create] -  create order" )(  )

            update@Database(
                "INSERT INTO public.orders
                (id, address_to_ship, order_amount, order_products, status, user_id)
                VALUES(:id, :addressToShip, :orderAmount, :orderProducts, :status, :userId);" {
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
        update(request)(response) {
            println@Console( "[ORDER] - [" + currentDateTime + "] - [/update] -  update order with id " + request.id )(  )

            update@Database(
                "UPDATE orders SET
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
            println@Console( "[ORDER] - [" + currentDateTime + "] - [/delete] -  delete order with id " + request.id )(  )

            update@Database(
                "DELETE FROM orders WHERE id=:id" {
                    .id = request.id
                }
            )(response.status)
        } 
    ]

}
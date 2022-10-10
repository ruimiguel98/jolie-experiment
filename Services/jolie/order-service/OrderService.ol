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
                "SELECT * FROM orders WHERE id = :id::uuid" {
                    .id = randomUUID
                }
            )(sqlResponse);

            if (#sqlResponse.row >= 1) {
                response -> sqlResponse.row[0]
            }
        }
    ]

    [
        create(request)(response) {
            println@Console( "[ORDER] - [" + currentDateTime + "] - [/create] - create order" )(  )

            getRandomUUID@StringUtils(  )( randomUUID )

            update@Database(
                "INSERT INTO orders (id, address_to_ship, status, user_id)
                 VALUES(:id::uuid, :addressToShip, :status, :userId::uuid);" {
                    .id = randomUUID,
                    .addressToShip = request.addressToShip,
                    .status = request.status,
                    .userId = request.userId
                }
            )(sqlResponseOrder.status)

            // iterate the products array and insert one row in the database per product ID in the array
            for( product in request.products ) {
                update@Database(
                    "INSERT INTO order_products (order_id, product_id, quantity)
                     VALUES(:orderId::uuid, :productId::uuid, :quantity::numeric);" {
                        .orderId = randomUUID,
                        .productId = product.id,
                        .quantity = product.quantity
                    }
                )(sqlResponseOrderProducts.status)
            }

            // verify if the request was successfull
            if ( #sqlResponseOrder.status == 1 && #sqlResponseOrderProducts == 1) {
                println@Console( "[ORDER] - [" + currentDateTime + "] - [/create] - order created with ID " + randomUUID )(  )
                customResponse.message = "Order created with success"
                customResponse.orderId = randomUUID
            }
            else {
                println@Console( "[ORDER] - [" + currentDateTime + "] - [/create] - ERROR creating a new order" )(  )
                customResponse.error = "Error while creating the new order"
            }

            response -> customResponse
        }
    ]

    [
        update(request)(response) {
            println@Console( "[ORDER] - [" + currentDateTime + "] - [/update] -  update order with id " + request.id )(  )

            update@Database(
                "UPDATE orders SET
                    address_to_ship = :addressToShip,
                    status = :status,
                    user_id = :userId::uuid
                WHERE id = :id::uuid;" {
                    .id = request.id,
                    .addressToShip = request.addressToShip,
                    .status = request.status,
                    .userId = request.userId
                }
            )(sqlResponse.status)

            
            // verify if the request was successfull
            if ( #sqlResponse.status == 1) {
                println@Console( "[ORDER] - [" + currentDateTime + "] - [/create] - order created with ID " + randomUUID )(  )
                customResponse.message = "Order updated created with success"
            }
            else {
                println@Console( "[ORDER] - [" + currentDateTime + "] - [/create] - ERROR creating a new order" )(  )
                customResponse.error = "Error while creating the new order"
            }

            response -> customResponse
        }
    ]

    [ 
        delete(request)(response) {
            println@Console( "[ORDER] - [" + currentDateTime + "] - [/delete] -  delete order with id " + request.id )(  )

            update@Database(
                "DELETE FROM orders WHERE id = :id::uuid" {
                    .id = request.id
                }
            )(sqlResponse.status)

            // verify if the request was successfull
            if ( #sqlResponse.status == 1) {
                println@Console( "[ORDER] - [" + currentDateTime + "] - [/create] - order created with ID " + randomUUID )(  )
                customResponse.message = "Order delete with sucess, with id " + request.id
            }
            else {
                println@Console( "[ORDER] - [" + currentDateTime + "] - [/create] - ERROR creating a new order" )(  )
                customResponse.error = "Error while deleting the order" + request.id
            }

            response -> customResponse
        } 
    ]

    [ 
        userOrders(request)(response) {
            println@Console( "[ORDER] - [" + currentDateTime + "] - [/userOrders] - getting orders from user with id " + request.id )(  )

            query@Database(
                "SELECT * FROM orders WHERE user_id = :userId::uuid" {
                    .userId = request.userId
                }
            )(sqlResponse)

            if (#sqlResponse.row >= 1) {
                response -> sqlResponse
            }
        } 
    ]

}
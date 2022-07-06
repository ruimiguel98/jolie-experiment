include "console.iol"
include "database.iol"
include "string_utils.iol"

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
}



// behaviour info
main
{
    [   
        placeOrder(request)(response) {

            println@Console( "Placing a new order for cart " + request.card_id + " for user with ID " + request.user_id )(  )

            update@Database(
                "INSERT INTO orders(id, cart_id, user_id, address_to_ship, payment_used, status) 
                  VALUES (:id::numeric, :cart_id::numeric, :user_id::numeric, :address_to_ship, :payment_used, 1);" { // status 1 is PLACED (1.PLACED - 2.PROCESSED - 3.SHIPPED - 4.DELIVERED)
                    .id = request.id,
                    .cart_id = request.cart_id,
                    .user_id = request.user_id,
                    .address_to_ship = request.address_to_ship,
                    .payment_used = request.payment_used
                }
            )(response.status)
        }
    ]

    [   
        processOrder(request)(response) {

            println@Console( "Changing order status to PROCESSED for order with ID " + request.id )(  )

            // status 2 is PROCESSED (1.PLACED - 2.PROCESSED - 3.SHIPPED - 4.DELIVERED)
            update@Database(
                "UPDATE orders SET status = 2 WHERE id=:id::numeric;" {
                    .id = request.id
                }
            )(response.status)
        }
    ]

    [   
        shipOrder(request)(response) {

            println@Console( "Changing order status to SHIPPED for cart " + request.card_id + " for user with ID " + request.user_id )(  )

            // status 3 is SHIPPED (1.PLACED - 2.PROCESSED - 3.SHIPPED - 4.DELIVERED)
            update@Database(
                "UPDATE orders SET status = 3 WHERE id=:id::numeric;" {
                    .id = request.id
                }
            )(response.status)
        }
    ]

    [   
        finishOrder(request)(response) {

            println@Console( "Changing order status to DELIVERED for cart " + request.card_id + " for user with ID " + request.user_id )(  )

            // status 4 is DELIVERED (1.PLACED - 2.PROCESSED - 3.SHIPPED - 4.DELIVERED)
            update@Database(
                "UPDATE orders SET status = 4 WHERE id=:id::numeric;" {
                    .id = request.id
                }
            )(response.status)
        }
    ]

    [   
        getOrderStatus(request)(response) {

            ORDER_DOES_NOT_EXIST_MESSAGE = "Please provide the correct order id"

            println@Console( "Fetching the status of the order with ID " + request.id )(  )

            query@Database(
                "SELECT * FROM orders WHERE id=:id::numeric" {
                    .id = request.id
                }
            )(sqlResponse)

            //reponse data structure building
            serviceResponse.id = sqlResponse.row.id
            serviceResponse.status = sqlResponse.row.status

            if (#sqlResponse.row >= 1) {
                response -> serviceResponse
            }
            else {
                response.message -> ORDER_DOES_NOT_EXIST_MESSAGE
            }

            // --------------------------------------------------------------------------------------
            // --------------------------------------------------------------------------------------
            // --------------------------------------------------------------------------------------
            // -------------EXAMPLE HANDLING EXCEPTION OF TYPE WRONG IN REQUEST----------------------
            // --------------------------------------------------------------------------------------
            // --------------------------------------------------------------------------------------
            // --------------------------------------------------------------------------------------
            // install( TypeMismatch =>
            //     /* this fault handler will be executed first, then the fault will be re-thrown */
            //     println@Console( "Wrong!" )();
                
            //     /* the fault will be re-thrown here */
            //     throw( TypeMismatch )
            // )

        }
    ]

    [   
        getMyOrders(request)(response) {

            USER_DOES_NOT_EXIST_MESSAGE = "The provided user id " + request.user_owner + " is not in the system"

            println@Console( "Fetching the orders of user with ID " + request.user_id )(  )

            query@Database(
                "SELECT * FROM orders WHERE user_id=:user_id::numeric" {
                    .user_id = request.user_id
                }
            )(sqlResponse)

            if (#sqlResponse.row >= 1) {
                response -> sqlResponse
            }
            else {
                response.message -> USER_DOES_NOT_EXIST_MESSAGE
            }
        }
    ]

}
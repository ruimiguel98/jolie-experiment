include "console.iol"
include "database.iol"
include "string_utils.iol"

include "./PaymentInterface.iol"

execution { concurrent }

// deployment info
inputPort PaymentPort {
    Location: LOCATION_SERVICE_PAYMENT
    Protocol: http { .format = "json" }
    Interfaces: PaymentInterface
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
        install (SQLException => println@Console("Payment table already exists")());
        update@Database(SQL_CREATE_PAYMENT_INFO)(ret)
    }
}

// behaviour info
main
{
    [ 
        addPaymentInfo(request)(response) {

            println@Console( "Adding payment information" )(  )

            update@Database(
                "INSERT INTO payment(id, type_payment, user_owner, card_number, cart_expire_date, name_on_card, card_security_code) 
                  VALUES (:id::int4, :type_payment, :user_owner, :card_number, :cart_expire_date, :name_on_card, :card_security_code::numeric);" {
                    .id = request.id,
                    .type_payment = request.type_payment,
                    .user_owner = request.user_owner,
                    .card_number = request.card_number,
                    .cart_expire_date = request.cart_expire_date,
                    .name_on_card = request.name_on_card,
                    .card_security_code = request.card_security_code,
                }
            )(response.status)
        }
    ]

    [ 
        deletePaymentInfo(request)(response) {

            println@Console( "Deleting payment information" )(  )

            update@Database(
                "DELETE FROM payment WHERE id=:id::int4;" {
                    .id = request.id,
                }
            )(response.status)
            
        }
    ]

    [ 
        getSavedPaymentInfoList(request)(response) {

            USER_DOES_NOT_EXIST_MESSAGE = "The provided user id " + request.user_owner + " is not in the system"

            println@Console( "Fetching list of saved payments information for user " + request.user_owner )(  )

            query@Database(
                "SELECT * FROM payment WHERE user_owner=:user_owner::int4" {
                    .user_owner = request.user_owner
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
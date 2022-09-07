include "console.iol"
include "database.iol"
include "string_utils.iol"
include "time.iol"

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

    getCurrentDateTime@Time( )( currentDateTime ) // call this API to create a GLOBAL VARIABLE for current datetime to be present in logs
}

// behaviour info
main
{
    [
        withdrawlAccount(request)(response) {
            println@Console( "[PAYMENT] - [" + currentDateTime + "] - [/withdrawlAccount] - withdrawling account with number " +
             request.cardNumber+ " for the amount " + request.amount )(  )


            //----------------------------- 1. CHECK IF ACCOUNT EXISTS IN THE MOCK BANK DATABASE --------------------------------
            query@Database( 
                "SELECT account_balance FROM payment WHERE card_number = :cardNumber;" {
                    .cardNumber = request.cardNumber
                }
            )( sqlResponse )
            
            accountBalance = sqlResponse.row[0].account_balance

            println@Console( "[PAYMENT] - [" + currentDateTime + "] - [/withdrawlAccount] - this account has the amount " + accountBalance )(  )


            //----------------------------- 2. CHECK ACCOUNT BALANCE IN THE MOCK BANK DATABASE --------------------------------
            if ( double(accountBalance) > double(request.amount) ) { // always force types when using math
                println@Console( "[PAYMENT] - [" + currentDateTime + "] - [/withdrawlAccount] - balance is enough " )(  )

                //----------------------------- 3. WITHDRAWL FROM THE ACCOUNT IN THE MOCK BANK DATABASE --------------------------------
                update@Database(
                    "UPDATE payment SET account_balance = :newBalance
                        WHERE card_number = :cardNumber;" {
                        .cardNumber = request.cardNumber,
                        .newBalance = double(accountBalance) - double(request.amount)
                    }
                )(sqlResponse)

                println@Console( "[PAYMENT] - [" + currentDateTime + "] - [/withdrawlAccount] - account withdrawled with success " )(  )
                customResponse.message = "Account withdrawled with success"
                customResponse.status = "SUCCESS"
            }
            else {
                println@Console( "[PAYMENT] - [" + currentDateTime + "] - [/withdrawlAccount] - balance is NOT enough " )(  )
                customResponse.error = "Not enough balance in the account"
                customResponse.status = "ERROR"
            }

            response -> customResponse
        }
    ]

}
include "console.iol"
include "database.iol"
include "string_utils.iol"
include "time.iol"


include "./EmailInterface.iol"

execution { concurrent }

// deployment info
inputPort EmailPort {
    Location: LOCATION_SERVICE_EMAIL
    Protocol: http { .format = "json" }
    Interfaces: EmailInterface
}

// prepare database connection (creates table if does not exist)
// configure SMTP
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
        install (SQLException => println@Console("Email table already exists")());
        update@Database(SQL_CREATE_EMAIL_INFO)(ret)
    }

    // provide SMTP connection info 
    // with (smtpInfo) {
    //     .subject = "Payment confirmed";
    //     .authenticate = {
    //         .password = "mypassword";
    //         .username = "myusername";
    //     }
    //     .host = "myhost";
    //     .from = "eCommerce";
    //     .to = "utreyheor"
    // }

    // scope (smtpConfiguration) {
    //     sendMail@SendMailRequest( smtpInfo )();
    //     println@Console("Successfull connection to the SMTP server")();
    //     install ( SMTPFault => println@Console("Something went wrong while configuring the SMTP")())
    // }
    getCurrentDateTime@Time( )( currentDateTime ) // call this API to create a GLOBAL VARIABLE for current datetime to be present in logs

}

// behaviour info
main
{
    [ 
        sendEmail(request)(response) {
            // sendMail@SMTP( request )( response )
            customResponse.status = "SEND"

            response -> customResponse
        } 
    ]
}
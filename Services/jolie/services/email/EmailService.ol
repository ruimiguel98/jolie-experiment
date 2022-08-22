include "console.iol"
include "database.iol"
include "string_utils.iol"
include "smtp.iol"
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
        sendEmail()(response) {
            // sendMail@SMTP( request )( response )
            response.emailSent = "This was the email sent"
            
        } 
    ]
}
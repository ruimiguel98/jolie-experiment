include "console.iol"
include "database.iol"
include "string_utils.iol"
include "time.iol"

include "./CartInterface.iol"
include "../product/ProductInterface.iol"

execution { concurrent }

// deployment info
outputPort ProductService {
    Location: LOCATION_SERVICE_PRODUCT
    Protocol: http { .format = "json" }
    Interfaces: ProductInterface
}

inputPort CartPort {
    Location: LOCATION_SERVICE_CART
    Protocol: http { .format = "json" }
    Interfaces: CartInterface
    Redirects: 
        Product => ProductService
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
                "SELECT c.id, c.user_id, cp.product_id, cp.quantity, cp.price_total  FROM cart c, cart_products cp WHERE cp.cart_id = :id AND c.id = cp.cart_id;" {
                    .id = request.id
                }
            )(sqlResponse);

            if (#sqlResponse.row >= 1) {
                customResponse.id = sqlResponse.row[0].id
                customResponse.userId = sqlResponse.row[0].user_id
                customResponse.cartPriceTotal = 0

                for( i = 0, i < #sqlResponse.row, i++ ){
                    customResponse.products[i].productId = sqlResponse.row[i].product_id
                    customResponse.products[i].quantity = sqlResponse.row[i].quantity
                    customResponse.products[i].price = sqlResponse.row[i].price_total
                    customResponse.cartPriceTotal += sqlResponse.row[i].price_total
                }
            }

            response -> customResponse
        }
    ]

    [
        create(request)(response) {
            println@Console( "[CART] - [" + currentDateTime + "] - [/create] - create cart" )(  )

            getRandomUUID@StringUtils(  )( randomUUID )

            update@Database(
                "INSERT INTO public.cart(id, user_id)
                 VALUES(:id, :userId);" {
                    .id = randomUUID, // UUID auto generated
                    .userId = request.userId
                }
            )(sqlResponse.status)

            // verify if the request was successfull
            if ( #sqlResponse.status == 1 ) {
                println@Console( "[CART] - [" + currentDateTime + "] - [/create] - cart created with ID " + randomUUID )(  )
            }
            else {
                println@Console( "[CART] - [" + currentDateTime + "] - [/create] - ERROR creating a new cart" )(  )
            }

        }
    ]

    [
        update(request)(response) {
            println@Console( "[CART] - [" + currentDateTime + "] - [/update] -  update cart with id " + request.id )(  )

            update@Database(
                "UPDATE cart SET user_id = :userId WHERE id = :id;" {
                    .id = request.id
                    .userId = request.userId
                }
            )(response.status)

            // verify if the request was successfull
            if ( response.status == 1 ) {
                println@Console( "[CART] - [" + currentDateTime + "] - [/update] - cart updated with ID " + request.id )(  )
            }
            else {
                println@Console( "[CART] - [" + currentDateTime + "] - [/update] - ERROR - failed to update cart with ID " + request.id )(  )
            }
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

    [
        addProduct(request)(response) {
            println@Console( "[CART] - [" + currentDateTime + "] - [/addProductToCart] -  adding product with id " +
             request.productId + " to cart with id " + request.cartId )(  )

            requestProduct.id = request.productId
            product@ProductService( requestProduct )( responseProduct )
            
            println@Console( "This product costs " + responseProduct.price )(  )

            // if the product exists in the database add it to the cart
            // if ( responseProduct.id == null ) {

                //----------------------------- 1. CHECK IF PRODUCT ALREADY EXISTS IN THE CART -------------------------------- 
                query@Database(
                    "SELECT * FROM cart_products WHERE cart_id = :cartId AND product_id = :productId;" {
                        .cartId = request.cartId
                        .productId = request.productId
                    }
                )(sqlResponseProductCheck);

                if (#sqlResponseProductCheck.row >= 1) {
                    println@Console( "[CART] - [" + currentDateTime + "] - [/addProductToCart] - ERROR - the product already exists in the cart, can't be added" )(  )
                }
                else {
                    println@Console( "[CART] - [" + currentDateTime + "] - [/addProductToCart] - this product does not exist in the provided cart, adding it... " )(  )
                }

                
                //----------------------------- 2. ADD PRODUCT TO THE CART -------------------------------- 
                update@Database(
                "INSERT INTO cart_products(cart_id, product_id, quantity, price_total)
                 VALUES(:cartId, :productId, :quantity, :priceTotal);" {
                        .cartId = request.cartId, // UUID auto generated
                        .productId = request.productId,
                        .quantity = request.quantity,
                        .priceTotal = double(responseProduct.price) * request.quantity
                    }
                )(sqlResponseProductAdd.status)

                if ( sqlResponseProductAdd.status == 1 ) {
                    println@Console( "[CART] - [" + currentDateTime + "] - [/addProductToCart] - new product " + request.productId +
                     " added to cart with ID " + request.cartId )(  )

                    customResponse.message = "Product added to the cart"
                }
                else {
                    println@Console( "[CART] - [" + currentDateTime + "] - [/addProductToCart] - ERROR - new product " + request.productId +
                     " could NOT be added to cart with ID " + request.cartId )(  )

                    customResponse.error = "Error adding product to the cart"
                }


                //----------------------------- 3. SEND CUSTOM RESPONSE TO CLIENT -------------------------------- 
                response -> customResponse
            // }
            // else {
            //     println@Console( "There is not product with such ID" )(  )
            // }
        }
    ]
    
    [
        removeProduct(request)(response) {
            println@Console( "[CART] - [" + currentDateTime + "] - [/removeProductFromCart] -  removing product with id " +
             request.productId + " from cart with id " + request.cartId )(  )

            //----------------------------- 1. REMOVE PRODUCT FROM THE CART -------------------------------- 
            update@Database(
            "DELETE FROM cart_products WHERE cart_id = :cartId AND product_id = :productId;" {
                .cartId = request.cartId, // UUID auto generated
                .productId = request.productId
                }
            )(sqlResponseProductRemove.status)

            if ( sqlResponseProductRemove.status == 1 ) {
                println@Console( "[CART] - [" + currentDateTime + "] - [/removeProductFromCart] - product " + request.productId +
                    " revemoed from cart with id " + request.cartId )(  )

                customResponse.message = "Product removed from the cart"
            }
            else {
                println@Console( "[CART] - [" + currentDateTime + "] - [/removeProductFromCart] - ERROR - product with id " + request.productId +
                    " could NOT be removed from cart with id " + request.cartId )(  )

                customResponse.error = "Error removing product from the cart"
                customResponse.tip = "Make sure the product exists in the database"
            }

            //----------------------------- 2. SEND CUSTOM RESPONSE TO CLIENT -------------------------------- 
            response -> customResponse
        }
    ]

}
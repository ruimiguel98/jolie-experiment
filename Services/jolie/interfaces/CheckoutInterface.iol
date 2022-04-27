type CheckoutRequest:void {
	.cart?:void {
		.id:int
	}

    .shipment?:void {
		.id:int
	}
}

interface CheckoutInterface {
RequestResponse:
    checkoutPay(CheckoutRequest)(undefined)
}
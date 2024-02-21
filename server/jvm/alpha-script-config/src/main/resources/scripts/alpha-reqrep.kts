requestReplies {
    requestReply("TRADE_DETAILS",TRADE){
        request {
            COUNTERPARTY_ID withTransformation { type, _ ->
                if (type == "2") {
                    "UNKOWN"
                } else {
                    type
                }
            }
        }

        reply {
            TRADE_ID
            COUNTERPARTY_ID
            INSTRUMENT_ID
            QUANTITY
            PRICE
        }
    }
}
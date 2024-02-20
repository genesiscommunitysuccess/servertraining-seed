import global.genesis.gen.config.fields.Fields

/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide request reply config for multi-pro-code-test.
 *
 * Modification History
 */
requestReplies {
    requestReply("COUNTERPARTY_DETAILS",COUNTERPARTY){
        request {
            COUNTERPARTY_ID withTransformation { type, _ ->
                if (type == "2") {
                    "UNKOWN"
                } else {
                    type
                }
            }
        }
    }
}
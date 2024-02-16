import global.genesis.gen.config.fields.Fields

/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide dataserver config for multi-pro-code-test.
 *
 * Modification History
 */
dataServer {
    query("ALL_TRADES", TRADE_VIEW) {}
    query("ALL_PRICES", TRADE) {
        fields {
            TRADE_ID
            INSTRUMENT_ID
            PRICE
            SYMBOL
        }
        where { trade ->
            trade.price!! > 0.0
        }
    }
    query("ALL_COUNTERPARTIES" , COUNTERPARTY_VIEW){
        enrich(USER_COUNTERPARTY_HIDE_LEI){
            join {userName, row ->
                UserCounterpartyHideLei.byUserNameCounterpartyCounterpartyId(userName, row.counterpartyId)
            }
            hideFields { counterpartyView, _, userData ->
                if(userData?.hideLei == true){
                    listOf(COUNTERPARTY_LEI)
                } else{
                    emptyList()
                }
            }
            fields {
                USER_COUNTERPARTY_HIDE_LEI.HIDE_LEI
            }
        }
    }
    query("ALL_INSTRUMENTS", INSTRUMENT)
    query("ALL_POSITIONS", POSITION)
    query("ALL_TRADES_AUDIT",TRADE_AUDIT)
}
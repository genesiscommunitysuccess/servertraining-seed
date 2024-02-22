import global.genesis.message.core.HttpStatusCode

webHandlers("BASE-PATH"){
    endpoint(GET, "ALL-TRADES"){
        val searchTradeId by header("SEARCH_TRADE_ID")
        handleRequest {
            require(searchTradeId != "1") { "searchTradeId cannot be 1"}
            db.get(Trade.byId(searchTradeId))
        }
        exceptionHandler<IllegalArgumentException>(HttpStatusCode.BadRequest){
            exception.message ?: "Error performing the search"
        }
    }

}
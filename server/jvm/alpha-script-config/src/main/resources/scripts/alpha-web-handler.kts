webHandlers("BASE-PATH") {
    endpoint(GET, "ALL-TRADES") {
        handleRequest {
            db.getBulk(TRADE)
        }
    }
}
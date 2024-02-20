webHandlers("BASE-PATH"){
    config{
        requiresAuth = true
        logLevel = DEBUG
    }

    endpoint(GET, "ALL-TRADES"){
        config{
            requiresAuth = true
            logLevel = DEBUG
        }

        handleRequest {
            db.getBulk(TRADE)
        }
    }
}
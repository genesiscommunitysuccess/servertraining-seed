webHandlers("my-base-path") {
    endpoint(GET, "all-trades") {
        handleRequest {
            db.getBulk(TRADE)
        }
    }
}


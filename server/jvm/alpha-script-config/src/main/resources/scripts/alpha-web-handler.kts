import global.genesis.commons.standards.GenesisPaths
import global.genesis.message.core.HttpStatusCode
import java.nio.file.*

webHandlers("BASE-PATH"){

    endpoint<Trade,Trade>(GET,"TEST", HttpStatusCode.Not){
        handleRequest{
            val trade = db.insert(body).record
            triggerEvent("TRADE_AUDIT_STREAM")
            trade
        }
    }
}


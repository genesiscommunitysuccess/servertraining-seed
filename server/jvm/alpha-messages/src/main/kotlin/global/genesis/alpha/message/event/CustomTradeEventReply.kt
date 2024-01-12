package global.genesis.alpha.message.event

import global.genesis.message.core.Outbound

sealed class CustomTradeEventReply: Outbound() {
    class TradeEventValidateAck : CustomTradeEventReply()
    data class TradeEventAck(val ackMessage: String) : CustomTradeEventReply()
    data class TradeEventNack(val error: String) : CustomTradeEventReply()
}
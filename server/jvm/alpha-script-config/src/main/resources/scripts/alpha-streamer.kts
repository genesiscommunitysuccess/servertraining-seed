import global.genesis.gen.dao.Trade


streams {
    stream("TRADE_AUDIT_STREAM", TRADE_AUDIT.BY_TIMESTAMP){
        fields{
            TRADE_AUDIT.TRADE_AUDIT_ID
        }
    }
}
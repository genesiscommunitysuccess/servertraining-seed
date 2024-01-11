package global.genesis.alpha.eventhandler
import global.genesis.db.rx.entity.multi.AsyncMultiEntityReadWriteGenericSupport
import global.genesis.gen.dao.Counterparty
import global.genesis.gen.dao.Instrument
import global.genesis.gen.dao.Trade
import global.genesis.message.core.event.Event
class ValidateTrade {

    companion object {
        suspend fun validateInsert(
            event: Event<Trade>,
            entityDb: AsyncMultiEntityReadWriteGenericSupport
        ) {
            val counterpartyId = event.details.counterpartyId!!
            val instrumentId = event.details.instrumentId

            //validate is counterpartyId and instrumentId exist in the database

            val counterparty = entityDb.get(Counterparty.byId(counterpartyId))
            val instrument = entityDb.get(Instrument.byId(instrumentId))

            if (counterparty == null || instrument == null) {
                throw IllegalArgumentException("Counterparty or Instrument does not exist")
            }

        }
    }
}
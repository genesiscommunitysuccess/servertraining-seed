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

           // validate if there is a counterparty in the database
            val counterparty = entityDb.get(Counterparty.byId(counterpartyId))
                ?: throw IllegalArgumentException("Counterparty not found")

            // validate if there is an instrument in the database
            val instrument = entityDb.get(Instrument.byId(instrumentId))
                ?: throw IllegalArgumentException("Instrument not found")

        }
    }
}
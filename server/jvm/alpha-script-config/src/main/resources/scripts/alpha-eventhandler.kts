import java.io.File
import java.time.LocalDate
import global.genesis.TradeStateMachine
import global.genesis.gen.dao.Trade
import global.genesis.alpha.message.event.TradeAllocated
import global.genesis.alpha.message.event.TradeCancelled
import global.genesis.alpha.message.event.PositionReport
import global.genesis.commons.standards.GenesisPaths
import global.genesis.db.rx.entity.multi.RxEntityDb
import global.genesis.gen.view.repository.TradeViewAsyncRepository
import global.genesis.jackson.core.GenesisJacksonMapper

/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide event handler config for multi-pro-code-test.
 *
 * Modification History
 */
val tradeViewRepo = inject<TradeViewAsyncRepository>()

eventHandler {
    val stateMachine = inject<TradeStateMachine>()

    eventHandler<Trade>(name = "TRADE_INSERT") {
        schemaValidation = false
        permissionCodes = listOf("INSERT_TRADE")
        onValidate { event ->
            val message = event.details
            verify {
                entityDb hasEntry Counterparty.byId(message.counterpartyId.toString())
                entityDb hasEntry Instrument.byId(message.instrumentId.toString())
            }
            ack()
        }
        onCommit { event ->
            val trade = event.details
            entityDb.insert(trade)
            ack()
        }
    }

    eventHandler<Trade>(name = "TRADE_MODIFY", transactional = true) {
        onValidate { event ->
            val message = event.details
            verify {
                entityDb hasEntry Counterparty.ById(message.counterpartyId.toString())
                entityDb hasEntry Instrument.byId(message.instrumentId.toString())
            }
            ack()
        }
        onCommit { event ->
            val trade = event.details
            entityDb.modify(trade)
            ack()
        }
    }

    eventHandler<Counterparty>(name = "COUNTERPARTY_INSERT", transactional = true) {
        onCommit { event ->
            entityDb.insert(event.details)
            ack()
        }
    }

    eventHandler<Counterparty>(name = "COUNTERPARTY_DELETE", transactional = true) {
        onCommit { event ->
            entityDb.delete(event.details)
            ack()
        }
    }

    eventHandler<Counterparty>(name = "COUNTERPARTY_MODIFY", transactional = true) {
        onCommit { event ->
            entityDb.modify(event.details)
            ack()
        }
    }

    eventHandler<Instrument>(name = "INSTRUMENT_INSERT", transactional = true) {
        onCommit { event ->
            entityDb.insert(event.details)
            ack()
        }
    }

    eventHandler<Instrument>(name = "INSTRUMENT_DELETE", transactional = true) {
        onCommit { event ->
            entityDb.delete(event.details)
            ack()
        }
    }

    eventHandler<Instrument>(name = "INSTRUMENT_MODIFY", transactional = true) {
        onCommit { event ->
            entityDb.modify(event.details)
            ack()
        }
    }

    eventHandler<TradeCancelled>(name = "TRADE_CANCELLED", transactional = true) {
        onCommit { event ->
            val message = event.details
            stateMachine.modify(entityDb, message.tradeId){ trade ->
                trade.tradeStatus = TradeStatus.CANCELLED
            }
            ack()
        }
    }

    eventHandler<TradeAllocated>(name = "TRADE_ALLOCATED", transactional = true) {
        onCommit { event ->
            val message = event.details
            stateMachine.modify(entityDb, message.tradeId) { trade ->
                trade.tradeStatus = TradeStatus.ALLOCATED
            }
            ack()
        }
    }

    eventHandler<PositionReport> {
        schemaValidation = false
        onCommit {
            val mapper = GenesisJacksonMapper.csvWriter<TradeView>()
            val today = LocalDate.now().toString()
            val positionReportFolder = File(GenesisPaths.runtime()).resolve("position-minute-report")
            if (!positionReportFolder.exists()) positionReportFolder.mkdirs()

            tradeViewRepo.getBulk()
                .toList()
                .groupBy { it.counterpartyName }
                .forEach { (counterParty, trades) ->
                    val file = positionReportFolder.resolve("${counterParty}_$today.csv")
                    if (file.exists()) file.delete()
                    mapper.writeValues(file).use { it.writeAll(trades) }
                }

            ack()
        }
    }

    eventHandler<Operations>(name="TEST_OPERATIONS"){
        onCommit {
            LOG.info("******************* RESULT:  ****************************")

            ack()
        }
    }

    eventHandler<TradeStandardization>(transactional = true) {
        onCommit {
            val tradesNegativePrices = entityDb.
            getBulk(TRADE).toList()
                .filter { it.price < 0 }

            tradesNegativePrices.forEach { t ->
                t.price = 0.0
            }

            entityDb.modifyAll(*tradesNegativePrices.toList().toTypedArray())
            ack()
        }
    }

}

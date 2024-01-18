import java.io.File
import java.time.LocalDate
import global.genesis.TradeStateMachine
import global.genesis.gen.dao.Trade
import global.genesis.alpha.message.event.TradeAllocated
import global.genesis.alpha.message.event.TradeCancelled
import global.genesis.alpha.message.event.PositionReport
import global.genesis.commons.standards.GenesisPaths
import global.genesis.gen.view.repository.TradeViewAsyncRepository
import global.genesis.jackson.core.GenesisJacksonMapper
import global.genesis.alpha.eventhandler.validate.*
import global.genesis.alpha.eventhandler.commit.*
import global.genesis.alpha.message.event.*
import global.genesis.clustersupport.service.ServiceDiscovery
import  global.genesis.message.core.event.*
import global.genesis.commons.model.GenesisSet.Companion.genesisSet


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

    eventHandler<Trade, CustomTradeEventReply>(name = "TRADE_INSERT") {
        schemaValidation = false

        onException{event, throwable ->
            CustomTradeEventReply.TradeNack("ERROR: ${throwable.message}")
        }


        onValidate {event ->
            ValidateTrade.validateInsert(event, entityDb)
            LOG.info(event.userName)
            CustomTradeEventReply.ValidationTradeAck()
        }

        onCommit { event ->
            val trade = event.details

            if (trade.quantity!! > 0) {
                trade.enteredBy = event.userName
                stateMachine.insert(entityDb, trade)
                CustomTradeEventReply.TradeAck("Trade was successfully inserted: ${trade.tradeId}")
            }
            else {
                CustomTradeEventReply.TradeNack("Quantity must be greater than zero")
            }
        }
    }

    eventHandler<Trade>(name = "TRADE_MODIFY", transactional = true) {

        onCommit { event ->
            val trade = event.details
            stateMachine.modify(entityDb, trade)
            ack()
        }
    }

    eventHandler<Trade>(name="TRADE_UPSERT"){
        schemaValidation = false
        onValidate {
            ValidateTrade.validateUpsert(it,entityDb)
            ack()
        }
        onCommit {
            CommitTrade.upsert(it,entityDb)
            ack()
        }
    }

    eventHandler<Counterparty, CustomCounterpartyEventReply>(name="COUNTERPARTY_INSERT", transactional = true){
        schemaValidation = false

        onException{ _ , throwable ->
            CustomCounterpartyEventReply.CounterpartyNack("ERROR: ${throwable.message}")
        }
        onValidate {
            require(!it.details.counterpartyId.contains("1234")){
                "COUNTERPARTY_ID cannot contain 1234"
            }
            CustomCounterpartyEventReply.ValidationCounterpartyAck()
        }
        onCommit {
            entityDb.insert(it.details)
            CustomCounterpartyEventReply.CounterpartyAck("Counterparty successfully inserted: ${it.details.counterpartyId}")
        }
    }

    eventHandler<Counterparty>(name="COUNTERPARTY_UPSERT"){
        schemaValidation = false

        onCommit {
            CommitCounterparty.upsert(it,entityDb)
            ack()
        }
    }

    eventHandler<Instrument>(name="INSTRUMENT_UPSERT"){
        schemaValidation = false
        onCommit {
            CommitInstrument.upsert(it,entityDb)
            ack()
        }
    }

    eventHandler<Counterparty>(name = "COUNTERPARTY_DELETE", transactional = true) {
        onValidate{
            ValidateCounterparty.validateDelete(it, entityDb)
            ack()
        }
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


    eventHandler<Instrument, CustomInstrumentEventReply>(name="INSTRUMENT_INSERT", transactional = true){
        schemaValidation = false

        onException{ _ , throwable ->
            CustomInstrumentEventReply.InstrumentNack("ERROR: ${throwable.message}")
        }

        onValidate {
            require(!it.details.instrumentId.contains("1234")){
                "INSTRUMENT_ID cannot contain 1234"
            }
            CustomInstrumentEventReply.ValidationInstrumentAck()
        }
        onCommit {
            entityDb.insert(it.details)
            CustomInstrumentEventReply.InstrumentAck("Instrument successfully inserted: ${it.details.instrumentId}")
        }
    }

    contextEventHandler<Instrument, List<Trade>>(name = "INSTRUMENT_DELETE_CASCADE", transactional = true){
        onValidate{ event ->
            val trades = ValidateInstrument.validateCascadeDelete(event, entityDb)
            validationResult(ack(),trades)
        }

        onCommit{ event, trades ->
            if(trades != null){
                for (trade in trades){
                    LOG.info("Deleting trade ${trade.tradeId}")
                    entityDb.delete(Trade.byId(trade.tradeId))
                }
            }
            entityDb.delete(event.details)
            ack()
        }
    }

    contextEventHandler<Counterparty, List<Trade>>(name = "COUNTERPARTY_DELETE_CASCADE", transactional = true){
        onException{ _ , throwable ->
            nack("ERROR: ${throwable.message}")
        }

        onValidate{ event ->
            val trades = ValidateCounterparty.validateCascadeDelete(event, entityDb)
            validationResult(ack(),trades)
        }

        onCommit{ event, trades ->
            if(trades != null){
                for (trade in trades){
                    LOG.info("Deleting trade ${trade.tradeId}")
                    entityDb.delete(Trade.byId(trade.tradeId))
                }
            }
            entityDb.delete(event.details)
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

    eventHandler<Company>("COMPANY_INSERT", transactional = true) {
            requiresPendingApproval { event ->
            event.userName != "system.user"
            event.approvalMessage = "My Custom Approval Message"
            true
            }

        onValidate { event ->
            val company = event.details

            approvableAck(
                entityDetails = listOf(
                    ApprovalEntityDetails(
                        entityTable = "COMPANY",
                        entityId = event.details.companyId,
                        approvalType = ApprovalType.NEW,
                       )
                ),
                approvalMessage = "Company update for ${company.companyId} has been sent for approval.",
                approvalType = ApprovalType.NEW,
                additionalDetails = "Sensitive update, tread carefully"
            )
        }
        onCommit { event ->
            val company = event.details
            LOG.info("COMPANY HAS BEEN INSERTED: ${company.companyId}")
            entityDb.insert(company)
            ack()
        }
    }

    eventHandler<RejectPendingApproval>("REJECT_PENDING_APPROVALS", transactional = true) {
        onValidate {
            val client = serviceDiscovery.resolveClientByResource("EVENT_PENDING_APPROVAL_SYSTEM_REJECT")
            if (client != null) {
                nack("EVENT_PENDING_APPROVAL_SYSTEM_REJECT is not available")
            }
            val approvals = entityDb.getBulk(APPROVAL)
                .filter { it.approvalStatus == ApprovalStatus.PENDING }
                .collect{ approval ->
                    val reply: EventReply? = client?.suspendRequest(
                        Event(
                            messageType = "EVENT_PENDING_APPROVAL_SYSTEM_REJECT",
                            userName = "SYSTEM",
                            details = ApprovalSystemRejectMessage(
                                approvalKey = approval.approvalKey,
                                approvalMessage = "Rejected by system"
                            )
                        )
                    )
                    when (reply) {
                        is EventReply.EventAck ->
                            LOG.info("Successfully rejected APPROVAL_ID: ${approval.approvalId}")

                        is EventReply.EventNack ->
                            LOG.error("Failed to rejected APPROVAL_ID: ${approval.approvalId}: $reply")

                        else ->
                            LOG.error("Unexpected response from pending approval system: $reply")
                    }
                }
            ack()
        }

        onCommit {
            ack()
        }
    }
}

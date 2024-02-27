import global.genesis.commons.standards.GenesisPaths
import global.genesis.gen.config.tables.COUNTERPARTY.

pipelines{
    csvSource("cp-pipeline"){
        location = "file:${GenesisPaths.runtime()}/fileIngress?fileName=cp-pipeline.csv"

        map("mapper-name",COUNTERPARTY){
            COUNTERPARTY_NAME{
                property = "Counterparty Name"
            }
            COUNTERPARTY_LEI{
                property = "Counterparty Lei"
            }
            COUNTERPARTY_ID{
                property = "Id"
            }
        }
    }
}
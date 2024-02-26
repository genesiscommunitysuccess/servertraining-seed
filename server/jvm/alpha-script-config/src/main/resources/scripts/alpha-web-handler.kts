import global.genesis.commons.standards.GenesisPaths
import global.genesis.message.core.HttpStatusCode
import java.nio.file.*
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

webHandlers("BASE-PATH"){


    multipartEndpoint("test") {
        config{
            multiPart {
                maxFileSize = 10_000_000
                useDisk = true
                baseDir = "GUI_TEST/files"
            }
        }

        handleRequest {
            body.fileUploads.forEach{
                val pasta = Paths.get("${GenesisPaths.genesisHome()}/runtime/GUI_TEST/files")
                it.copyTo(pasta.resolve(it.fileName))
                "Deu Boa"
                LOG.info("DEU BOA")
            }
        }
    }
}
import global.genesis.message.core.HttpStatusCode
import java.nio.file.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

webHandlers("BASE-PATH"){

    val tmp = Files.createTempDirectory("test")
    multipartEndpoint("test") {
        config{
            multiPart {
                maxFileSize = 10_000_000
                useDisk = true
                baseDir = "Gui_TEST/fileuploadtemp"
            }
        }

        handleRequest {
            body.fileUploads.forEach{
                it.copyTo(tmp.resolve(it.fileName))
            }
            "Very Good"
        }
    }

}
data class InputClass(val field1: String, val field2: Int?)
data class OutputClass(val message: String,val details: String)

requestReplies {
    requestReply<InputClass, OutputClass>("CUSTOM_REQUEST") {
        replySingle { input ->
            OutputClass(message = input.field1, details = "This number: ${input.field2}")
        }
    }
}
package global.genesis.alpha.message.event

class TrainingClass(
    val field1: String,
    val field2: String?
) {

    fun  learning(): Boolean {
        return field2.equals(field1)
    }
}
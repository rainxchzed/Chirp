package zed.rainxch.chirp

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}

data class DataClass(
    val example: String
)

object Object {
    var example = "Object"
}

data object DataObject {
    var example = "DataObject"
}
package zed.rainxch.chirp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
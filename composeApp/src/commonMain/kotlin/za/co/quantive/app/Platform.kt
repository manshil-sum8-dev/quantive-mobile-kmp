package za.co.quantive.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
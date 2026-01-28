package es.dam.bookest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
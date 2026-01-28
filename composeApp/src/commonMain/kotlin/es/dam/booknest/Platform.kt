package es.dam.booknest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
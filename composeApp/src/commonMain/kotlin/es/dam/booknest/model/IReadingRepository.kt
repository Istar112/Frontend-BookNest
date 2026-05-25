package es.dam.booknest.model

interface IReadingRepository {
    suspend fun getReadings(): Result<List<Reading>>
}

package es.dam.booknest.ui.reading

import es.dam.booknest.model.Reading
import es.dam.booknest.model.ReadingStatus

data class MyReadingsState(
    val readings: List<Reading> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedReadingStatus: ReadingStatus? = null,
    val isStatusLoading: Boolean = false,
    val sessionExpired: Boolean = false
)
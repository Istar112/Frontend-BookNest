package es.dam.booknest.ui.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.dam.booknest.aplication.book.status.GetReadingStatusUseCase
import es.dam.booknest.aplication.book.status.UpdateReadingUseCase
import es.dam.booknest.aplication.reading.GetUserReadingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyReadingsViewModel(
    private val getUserReadingsUseCase: GetUserReadingsUseCase,
    private val getReadingStatusUseCase: GetReadingStatusUseCase,
    private val updateReadingUseCase: UpdateReadingUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyReadingsState())
    val uiState = _uiState.asStateFlow()

    init {
        loadReadings()
    }

    fun loadReadings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getUserReadingsUseCase()
                .onSuccess { readings ->
                    _uiState.update { it.copy(readings = readings, isLoading = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
        }
    }

    fun toggleReadingDetails(bookId: Int) {
        val currentReading = _uiState.value.readings.find { it.idBook == bookId } ?: return
        
        if (currentReading.statusDetails == null) {
            viewModelScope.launch {
                getReadingStatusUseCase(bookId)
                    .onSuccess { status ->
                        _uiState.update { state ->
                            state.copy(
                                readings = state.readings.map { 
                                    if (it.idBook == bookId) it.copy(statusDetails = status) else it 
                                }
                            )
                        }
                    }
                    .onFailure { e ->
                        _uiState.update { it.copy(error = e.message) }
                    }
            }
        }
    }

    fun updateReadingStatus(bookId: Int, readingStatus: String, numPag: Int, date: String, rating: Int? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isStatusLoading = true) }
            val result = if (readingStatus == "finished") {
                updateReadingUseCase.updateFinished(bookId, date, rating ?: 5)
            } else {
                updateReadingUseCase.updateProcess(bookId, numPag, date)
            }

            result.onSuccess {
                // Actualizar localmente o recargar
                getReadingStatusUseCase(bookId).onSuccess { newStatus ->
                    _uiState.update { state ->
                        state.copy(
                            isStatusLoading = false,
                            readings = state.readings.map { 
                                if (it.idBook == bookId) {
                                    it.copy(
                                        statusDetails = newStatus,
                                        readingStatus = readingStatus
                                    )
                                } else it
                            }
                        )
                    }
                }
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message, isStatusLoading = false) }
            }
        }
    }
}

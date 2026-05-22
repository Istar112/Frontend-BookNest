package es.dam.booknest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.dam.booknest.aplication.book.list.GetAllBooksUseCase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class HomeViewModel(
    private val getAllBooksUseCase: GetAllBooksUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

        getAllBooksUseCase()
            .onSuccess { books ->
                _uiState.update { it.copy(books = books, isLoading = false) }
            }
            .onFailure { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}

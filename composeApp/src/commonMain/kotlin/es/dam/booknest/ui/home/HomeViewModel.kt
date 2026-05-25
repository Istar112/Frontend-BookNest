package es.dam.booknest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.dam.booknest.aplication.book.list.GetAllBooksUseCase
import es.dam.booknest.aplication.book.status.AddBookToFinishedUseCase
import es.dam.booknest.aplication.book.status.AddBookToProcessUseCase
import es.dam.booknest.model.Book

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class HomeViewModel(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val addBookToProcessUseCase: AddBookToProcessUseCase,
    private val addBookToFinishedUseCase: AddBookToFinishedUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        loadBooks()
    }

    fun selectBook(book: Book) {
        _uiState.update {
            it.copy(selectedBook = book, error = null, successMessage = null)
        }
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

    fun addBookToTracking(bookId: Int, numPag: Int = 0, dateStart: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, successMessage = null) }
            addBookToProcessUseCase(bookId, numPag, dateStart)
                .onSuccess {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            successMessage = "Book added to your list as in progress!"
                        ) 
                    }
                }
                .onFailure { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = e.message ?: "Failed to add book"
                        ) 
                    }
                }
        }
    }

    fun addBookToFinished(bookId: Int, rating: Int = 5, finishDate: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, successMessage = null) }
            addBookToFinishedUseCase(bookId, rating, finishDate)
                .onSuccess {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            successMessage = "Book marked as finished!"
                        ) 
                    }
                }
                .onFailure { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = e.message ?: "Failed to mark as finished"
                        ) 
                    }
                }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}

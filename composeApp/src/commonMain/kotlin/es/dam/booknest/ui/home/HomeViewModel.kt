package es.dam.booknest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.dam.booknest.aplication.book.list.GetAllBooksUseCase
import es.dam.booknest.aplication.book.status.AddBookToFinishedUseCase
import es.dam.booknest.aplication.book.status.AddBookToProcessUseCase
import es.dam.booknest.aplication.reading.GetUserReadingsUseCase
import es.dam.booknest.aplication.user.streak.GetUserStreakUseCase
import es.dam.booknest.infraestructure.user.SessionExpiredException
import es.dam.booknest.model.Book
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val getUserReadingsUseCase: GetUserReadingsUseCase,
    private val addBookToProcessUseCase: AddBookToProcessUseCase,
    private val addBookToFinishedUseCase: AddBookToFinishedUseCase,
    private val getUserStreakUseCase: GetUserStreakUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun selectBook(book: Book) {
        _uiState.update {
            it.copy(
                selectedBook = book,
                error = null,
                successMessage = null
            )
        }
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    sessionExpired = false
                )
            }

            val allBooksDeferred = async { getAllBooksUseCase() }
            val userReadingsDeferred = async { getUserReadingsUseCase() }
            val userStreakDeferred = async { getUserStreakUseCase() }

            val allBooksResult = allBooksDeferred.await()
            val userReadingsResult = userReadingsDeferred.await()
            val userStreakResult = userStreakDeferred.await()

            if (allBooksResult.isFailure) {
                val error = allBooksResult.exceptionOrNull()
                if (error is SessionExpiredException) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sessionExpired = true,
                            error = null
                        )
                    }
                    return@launch
                }
            }

            if (userReadingsResult.isFailure) {
                val error = userReadingsResult.exceptionOrNull()
                if (error is SessionExpiredException) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sessionExpired = true,
                            error = null
                        )
                    }
                    return@launch
                }
            }

            if (userStreakResult.isFailure) {
                val error = userStreakResult.exceptionOrNull()
                if (error is SessionExpiredException) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sessionExpired = true,
                            error = null
                        )
                    }
                    return@launch
                }
            }

            val allBooks = allBooksResult.getOrElse { emptyList() }
            val readings = userReadingsResult.getOrElse { emptyList() }
            val streak = userStreakResult.getOrNull()

            val yourBooks = readings.mapNotNull { it.book }

            val yourBookIds = yourBooks.mapNotNull { it.id }.toSet()

            val recommendedBooks = allBooks.filter { book ->
                book.id !in yourBookIds
            }

            _uiState.update {
                it.copy(
                    yourBooks = yourBooks,
                    recommendedBooks = recommendedBooks,
                    streakDays = streak?.streakDays ?: 0,
                    isLoading = false,
                    error = if (allBooksResult.isFailure || userReadingsResult.isFailure || userStreakResult.isFailure) {
                        "Some content could not be loaded."
                    } else {
                        null
                    }
                )
            }
        }
    }

    fun addBookToTracking(bookId: Int, numPag: Int = 0, dateStart: String? = null) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    successMessage = null,
                    sessionExpired = false
                )
            }

            addBookToProcessUseCase(bookId, numPag, dateStart)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Book added to your list as in progress!"
                        )
                    }
                    loadHomeData()
                }
                .onFailure { e ->
                    if (e is SessionExpiredException) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                sessionExpired = true,
                                error = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "Failed to add book"
                            )
                        }
                    }
                }
        }
    }

    fun addBookToFinished(bookId: Int, rating: Int = 5, finishDate: String? = null) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    successMessage = null,
                    sessionExpired = false
                )
            }

            addBookToFinishedUseCase(bookId, rating, finishDate)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Book marked as finished!"
                        )
                    }
                    loadHomeData()
                }
                .onFailure { e ->
                    if (e is SessionExpiredException) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                sessionExpired = true,
                                error = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "Failed to mark as finished"
                            )
                        }
                    }
                }
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                error = null,
                successMessage = null
            )
        }
    }

    fun clearSessionExpired() {
        _uiState.update {
            it.copy(sessionExpired = false)
        }
    }
}
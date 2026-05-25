package es.dam.booknest.ui.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import es.dam.booknest.ui.home.HomeViewModel
import es.dam.booknest.ui.theme.ErrorRed
import es.dam.booknest.ui.theme.InkBlack
import es.dam.booknest.ui.theme.LeatherBrown
import es.dam.booknest.ui.theme.OldPaper
import es.dam.booknest.ui.theme.SoftPaper
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetalle(
    vm: HomeViewModel,
    onBack: () -> Unit,
    onAddToTracking: (numPag: Int, dateStart: String) -> Unit,
    onMarkAsFinished: (finishDate: String, rating: Int) -> Unit
) {
    val uiState by vm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showMenu by remember { mutableStateOf(false) }
    var showAddProcessDialog by remember { mutableStateOf(false) }
    var showAddFinishedDialog by remember { mutableStateOf(false) }

    val book = uiState.selectedBook

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            vm.clearMessages()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            vm.clearMessages()
        }
    }

    if (showAddProcessDialog) {
        AddProcessDialog(
            onDismiss = { showAddProcessDialog = false },
            onConfirm = { numPag, dateStart ->
                showAddProcessDialog = false
                onAddToTracking(numPag, dateStart)
            }
        )
    }

    if (showAddFinishedDialog) {
        AddFinishedDialog(
            onDismiss = { showAddFinishedDialog = false },
            onConfirm = { finishDate, rating ->
                showAddFinishedDialog = false
                onMarkAsFinished(finishDate, rating)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Book details",
                        color = InkBlack,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to home",
                            tint = LeatherBrown
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = LeatherBrown
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(SoftPaper)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Add to tracking (In process)", color = InkBlack) },
                                onClick = {
                                    showMenu = false
                                    showAddProcessDialog = true
                                },
                                leadingIcon = { Icon(Icons.Default.BookmarkAdd, contentDescription = null, tint = LeatherBrown) }
                            )
                            DropdownMenuItem(
                                text = { Text("Mark as finished", color = InkBlack) },
                                onClick = {
                                    showMenu = false
                                    showAddFinishedDialog = true
                                },
                                leadingIcon = { Icon(Icons.Default.AutoStories, contentDescription = null, tint = LeatherBrown) }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = OldPaper
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = OldPaper
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(OldPaper)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = LeatherBrown
                    )
                }

                uiState.error != null && uiState.successMessage == null -> {
                     // No mostramos error central si hay snackbar
                }

                book == null -> {
                    Text(
                        text = "No book selected",
                        color = InkBlack,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = SoftPaper),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(190.dp)
                                        .height(280.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(LeatherBrown.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = book.coverImage,
                                        contentDescription = "Cover of ${book.title}",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    if (book.coverImage.isEmpty()) {
                                        Icon(
                                            imageVector = Icons.Default.AutoStories,
                                            contentDescription = null,
                                            tint = LeatherBrown.copy(alpha = 0.5f),
                                            modifier = Modifier.size(48.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = book.title,
                                    color = InkBlack,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                BookDetailRow(label = "ISBN", value = book.isbn)
                                BookDetailRow(label = "Category", value = book.category)
                                BookDetailRow(label = "Pages", value = "${book.totalPages}")
                                BookDetailRow(label = "Publication date", value = book.publicationDate)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = LeatherBrown,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = value,
            color = InkBlack,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

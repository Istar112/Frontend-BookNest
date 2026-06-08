package es.dam.booknest.ui.reading

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import es.dam.booknest.model.Reading
import es.dam.booknest.model.ReadingStatus
import es.dam.booknest.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReadings(
    vm: MyReadingsViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by vm.uiState.collectAsState()
    var processExpanded by remember { mutableStateOf(true) }
    var finishedExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            vm.clearSessionExpired()
            onLogout()
        }
    }

    LaunchedEffect(Unit) {
        vm.loadReadings()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Readings", color = InkBlack) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = LeatherBrown
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = OldPaper)
            )
        },
        containerColor = OldPaper
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(OldPaper)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = LeatherBrown
                )
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = ErrorRed,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                val inProcess = uiState.readings.filter { it.readingStatus == "process" }
                val finished = uiState.readings.filter { it.readingStatus == "finished" }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SectionHeader(
                            title = "In Process",
                            count = inProcess.size,
                            isExpanded = processExpanded,
                            onClick = { processExpanded = !processExpanded }
                        )
                    }

                    if (processExpanded) {
                        if (inProcess.isEmpty()) {
                            item { EmptySectionMessage("No books currently in process") }
                        } else {
                            items(inProcess) { reading ->
                                ReadingItem(
                                    reading = reading,
                                    onToggle = { vm.toggleReadingDetails(reading.idBook) },
                                    onUpdate = { status, numPag, date, rating ->
                                        vm.updateReadingStatus(reading.idBook, status, numPag, date, rating)
                                    }
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    item {
                        SectionHeader(
                            title = "Finished",
                            count = finished.size,
                            isExpanded = finishedExpanded,
                            onClick = { finishedExpanded = !finishedExpanded }
                        )
                    }

                    if (finishedExpanded) {
                        if (finished.isEmpty()) {
                            item { EmptySectionMessage("No finished books yet") }
                        } else {
                            items(finished) { reading ->
                                ReadingItem(
                                    reading = reading,
                                    onToggle = { vm.toggleReadingDetails(reading.idBook) },
                                    onUpdate = { status, numPag, date, rating ->
                                        vm.updateReadingStatus(reading.idBook, status, numPag, date, rating)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    count: Int,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = InkBlack,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                color = LeatherBrown.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "$count",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = LeatherBrown
                )
            }
        }
        Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = null,
            tint = LeatherBrown
        )
    }
}

@Composable
fun EmptySectionMessage(message: String) {
    Text(
        text = message,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = InkBlack.copy(alpha = 0.5f),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}

@Composable
fun ReadingItem(
    reading: Reading,
    onToggle: () -> Unit,
    onUpdate: (String, Int, String, Int?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var editingStatus by remember { mutableStateOf(reading.readingStatus) }

    LaunchedEffect(reading.readingStatus) {
        editingStatus = reading.readingStatus
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!isEditing) {
                    expanded = !expanded
                    if (expanded) onToggle()
                }
            },
        colors = CardDefaults.cardColors(containerColor = SoftPaper),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 50.dp, height = 75.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(LeatherBrown.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (reading.book?.coverImage?.isNotEmpty() == true) {
                        AsyncImage(
                            model = reading.book.coverImage,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Book,
                            contentDescription = null,
                            tint = LeatherBrown.copy(alpha = 0.5f)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = reading.book?.title ?: "Unknown Book",
                        style = MaterialTheme.typography.titleMedium,
                        color = InkBlack,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = reading.readingStatus.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (reading.readingStatus == "finished") SuccessGreen else LeatherBrown
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = LeatherBrown
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 74.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(
                        color = LeatherBrown.copy(alpha = 0.1f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (reading.statusDetails != null) {
                        if (isEditing) {
                            EditStatusContent(
                                readingStatus = editingStatus,
                                status = reading.statusDetails,
                                onSave = { numPag, date, rating ->
                                    onUpdate(editingStatus, numPag, date, rating)
                                    isEditing = false
                                },
                                onCancel = {
                                    isEditing = false
                                    editingStatus = reading.readingStatus
                                }
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                ViewStatusContent(reading.statusDetails)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            editingStatus = reading.readingStatus
                                            isEditing = true
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = LeatherBrown,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            editingStatus =
                                                if (reading.readingStatus == "finished") "process" else "finished"
                                            isEditing = true
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.SwapHoriz,
                                            contentDescription = "Change Status",
                                            tint = LeatherBrown,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterHorizontally),
                            strokeWidth = 2.dp,
                            color = LeatherBrown
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ViewStatusContent(status: ReadingStatus) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (status.numPag != null) {
            StatusRow(label = "Pages read:", value = "${status.numPag}")
        }
        if (status.dateStart != null) {
            StatusRow(label = "Start date:", value = status.dateStart)
        }
        if (status.finishDate != null) {
            StatusRow(label = "Finish date:", value = status.finishDate)
        }
        if (status.rating != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rating:",
                    style = MaterialTheme.typography.bodySmall,
                    color = LeatherBrown,
                    fontWeight = FontWeight.Bold
                )
                StarRating(rating = status.rating, onRatingChange = {}, clickable = false)
            }
        }
    }
}

@Composable
fun EditStatusContent(
    readingStatus: String,
    status: ReadingStatus,
    onSave: (Int, String, Int?) -> Unit,
    onCancel: () -> Unit
) {
    var editedPages by remember { mutableStateOf(status.numPag ?: 0) }
    var editedDate by remember { mutableStateOf(status.dateStart ?: status.finishDate ?: "2024-01-01") }
    var editedRating by remember { mutableStateOf(status.rating ?: 5) }

    val isFinished = readingStatus == "finished"

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = if (isFinished) "Edit finished status" else "Edit reading status",
            style = MaterialTheme.typography.labelMedium,
            color = LeatherBrown
        )

        if (!isFinished) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Pages:",
                    modifier = Modifier.width(60.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .weight(1f)
                ) {
                    NumberPicker(
                        value = editedPages,
                        range = 0..2000,
                        onValueChange = { editedPages = it }
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Rating:",
                    modifier = Modifier.width(60.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    StarRating(
                        rating = editedRating,
                        onRatingChange = { editedRating = it }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Date:",
                modifier = Modifier.width(60.dp),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            DatePickerSwipeable(
                currentDate = editedDate,
                onDateChange = { editedDate = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text("Cancel", color = LeatherBrown)
            }
            Button(
                onClick = {
                    onSave(editedPages, editedDate, if (isFinished) editedRating else null)
                },
                colors = ButtonDefaults.buttonColors(containerColor = LeatherBrown)
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun StarRating(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    maxStars: Int = 5,
    clickable: Boolean = true
) {
    Row {
        for (i in 1..maxStars) {
            Icon(
                imageVector = if (i <= rating) Icons.Default.Star else Icons.Outlined.StarBorder,
                contentDescription = null,
                tint = BookGold,
                modifier = Modifier
                    .size(if (clickable) 32.dp else 16.dp)
                    .then(if (clickable) Modifier.clickable { onRatingChange(i) } else Modifier)
            )
        }
    }
}

@Composable
fun NumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    val items = range.toList()
    val initialIndex = items.indexOf(value).coerceAtLeast(0)
    val itemHeight = 34.dp

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val currentIndex = listState.firstVisibleItemIndex
            if (currentIndex in items.indices) {
                onValueChange(items[currentIndex])
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val halfHeight = maxHeight / 2
        val padding = halfHeight - (itemHeight / 2)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .padding(horizontal = 8.dp),
            color = LeatherBrown.copy(alpha = 0.05f),
            shape = RoundedCornerShape(8.dp)
        ) {}

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = padding)
        ) {
            items(items.size) { index ->
                val number = items[index]
                val isSelected = index == listState.firstVisibleItemIndex
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isSelected) LeatherBrown else Color.Gray.copy(alpha = 0.5f),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = if (isSelected) 20.sp else 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DatePickerSwipeable(
    currentDate: String,
    onDateChange: (String) -> Unit
) {
    val parts = currentDate.split("-")
    var year by remember { mutableStateOf(parts.getOrNull(0)?.toIntOrNull() ?: 2024) }
    var month by remember { mutableStateOf(parts.getOrNull(1)?.toIntOrNull() ?: 1) }
    var day by remember { mutableStateOf(parts.getOrNull(2)?.toIntOrNull() ?: 1) }

    LaunchedEffect(year, month, day) {
        onDateChange(
            "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
        )
    }

    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            NumberPicker(value = day, range = 1..31, onValueChange = { day = it })
        }
        Box(modifier = Modifier.weight(1f)) {
            NumberPicker(value = month, range = 1..12, onValueChange = { month = it })
        }
        Box(modifier = Modifier.weight(1f)) {
            NumberPicker(value = year, range = 2000..2100, onValueChange = { year = it })
        }
    }
}

@Composable
private fun StatusRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = LeatherBrown,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = InkBlack
        )
    }
}
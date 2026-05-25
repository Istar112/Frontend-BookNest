package es.dam.booknest.ui.book

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.dam.booknest.ui.reading.StarRating
import es.dam.booknest.ui.theme.InkBlack
import es.dam.booknest.ui.theme.LeatherBrown
import es.dam.booknest.ui.theme.SoftPaper

@Composable
fun AddFinishedDialog(
    onDismiss: () -> Unit,
    onConfirm: (finishDate: String, rating: Int) -> Unit
) {
    var finishDate by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Mark as Finished", color = InkBlack) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = finishDate,
                    onValueChange = { finishDate = it },
                    label = { Text("Finish Date (YYYY-MM-DD)") },
                    placeholder = { Text("2024-05-25") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LeatherBrown,
                        focusedLabelColor = LeatherBrown
                    )
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("Rating", style = MaterialTheme.typography.labelMedium, color = LeatherBrown)
                    Spacer(modifier = Modifier.height(8.dp))
                    StarRating(rating = rating, onRatingChange = { rating = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val date = if (finishDate.isBlank()) "2024-01-01" else finishDate
                    onConfirm(date, rating)
                },
                colors = ButtonDefaults.buttonColors(containerColor = LeatherBrown)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = LeatherBrown)
            }
        },
        containerColor = SoftPaper
    )
}

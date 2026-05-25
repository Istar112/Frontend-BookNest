package es.dam.booknest.ui.book

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import es.dam.booknest.ui.theme.InkBlack
import es.dam.booknest.ui.theme.LeatherBrown
import es.dam.booknest.ui.theme.SoftPaper

@Composable
fun AddProcessDialog(
    onDismiss: () -> Unit,
    onConfirm: (numPag: Int, dateStart: String) -> Unit
) {
    var numPag by remember { mutableStateOf("") }
    var dateStart by remember { mutableStateOf("") } // Idealmente un DatePicker, pero por ahora texto para cumplir con el DTO

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add to Tracking", color = InkBlack) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = numPag,
                    onValueChange = { if (it.all { char -> char.isDigit() }) numPag = it },
                    label = { Text("Pages read") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LeatherBrown,
                        focusedLabelColor = LeatherBrown
                    )
                )
                OutlinedTextField(
                    value = dateStart,
                    onValueChange = { dateStart = it },
                    label = { Text("Start Date (YYYY-MM-DD)") },
                    placeholder = { Text("2024-05-25") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LeatherBrown,
                        focusedLabelColor = LeatherBrown
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val pages = numPag.toIntOrNull() ?: 0
                    val date = if (dateStart.isBlank()) "2024-01-01" else dateStart // Fallback simple
                    onConfirm(pages, date)
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

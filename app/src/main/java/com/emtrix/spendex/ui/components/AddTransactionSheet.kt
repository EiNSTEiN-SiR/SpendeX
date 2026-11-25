package com.emtrix.spendex.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.emtrix.spendex.model.Category
import com.emtrix.spendex.model.Transaction
import com.emtrix.spendex.model.Currency
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionSheet(currency: Currency, onDismiss: () -> Unit, onSave: (Transaction) -> Unit) {
    val context = LocalContext.current
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var income by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf(Category.FOOD) }
    var date by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val dateText = remember(date) { java.text.SimpleDateFormat("EEE, MMM d, yyyy", java.util.Locale.getDefault()).format(java.util.Date(date)) }

    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = MaterialTheme.colorScheme.surface) {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 22.dp).padding(bottom = 28.dp)) {
            Text("New transaction", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = !income, onClick = { income = false }, label = { Text("Expense") })
                FilterChip(selected = income, onClick = { income = true }, label = { Text("Income") })
            }
            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                value = amount, onValueChange = { amount = it.filter { c -> c.isDigit() || c == '.' }.take(12) },
                label = { Text("Amount") }, leadingIcon = { Text(currency.symbol) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(14.dp))
            Text("Category", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Category.entries.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    row.forEach { item ->
                        FilterChip(selected = category == item, onClick = { category = item }, label = { Text(item.label) })
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it.take(200) },
                label = { Text("Note (optional)") },
                placeholder = { Text("Add a description for this transaction") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${note.length}/200",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),
                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End)
            )
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = {
                val cal = Calendar.getInstance().apply { timeInMillis = date }
                DatePickerDialog(context, { _, year, month, day ->
                    cal.set(year, month, day)
                    date = cal.timeInMillis
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }, modifier = Modifier.fillMaxWidth()) { Text("Date · $dateText") }
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = {
                    val value = amount.toDoubleOrNull()
                    if (value != null && value > 0) {
                        onSave(Transaction(amount = value, isIncome = income, category = category, note = note.trim(), dateMillis = date))
                        onDismiss()
                    }
                },
                enabled = (amount.toDoubleOrNull() ?: 0.0) > 0,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Save transaction") }
        }
    }
}

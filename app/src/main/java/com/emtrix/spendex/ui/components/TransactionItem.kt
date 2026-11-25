package com.emtrix.spendex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.emtrix.spendex.model.Currency
import com.emtrix.spendex.model.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionItem(item: Transaction, currency: Currency) {
    val color = if (item.isIncome) Color(0xFF059669) else Color(0xFFDC2626)
    Row(Modifier.fillMaxWidth().padding(vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(42.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            Icon(if (item.isIncome) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward, null, tint = color)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(item.note.ifBlank { item.category.label }, style = MaterialTheme.typography.titleMedium)
            Text("${item.category.label} · ${SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(item.dateMillis))}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = .55f))
        }
        MoneyText(if (item.isIncome) item.amount else -item.amount, currency, color = color)
    }
}

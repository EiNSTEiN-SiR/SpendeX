package com.emtrix.spendex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DeleteOutline
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
fun TransactionItem(item: Transaction, currency: Currency, onDelete: () -> Unit) {
    val color = if (item.isIncome) Color(0xFF059669) else Color(0xFFDC5656)
    Row(
        Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp)).padding(horizontal = 13.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(44.dp).background(if (item.isIncome) Color(0xFFDCFCE7) else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
            Icon(if (item.isIncome) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward, null, tint = color)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(item.category.label, style = MaterialTheme.typography.titleMedium)
            Text(SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(item.dateMillis)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = .55f))
            if (item.note.isNotBlank()) {
                Spacer(Modifier.height(3.dp))
                Text(item.note, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = .75f), maxLines = 2)
            }
        }
        Spacer(Modifier.width(6.dp))
        Column(horizontalAlignment = Alignment.End) {
            MoneyText(if (item.isIncome) item.amount else -item.amount, currency, color = color)
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.DeleteOutline, "Delete transaction", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(19.dp))
            }
        }
    }
}

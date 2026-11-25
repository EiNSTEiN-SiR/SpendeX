package com.emtrix.spendex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.emtrix.spendex.model.Category
import com.emtrix.spendex.model.Currency

private val palette = listOf(Color(0xFF059669), Color(0xFF64748B), Color(0xFFD97706), Color(0xFFDC2626), Color(0xFF0D9488), Color(0xFF8B5E3C))

@Composable
fun CategoryBreakdownCard(data: List<Pair<Category, Double>>, currency: Currency) {
    val total = data.sumOf { it.second }
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(18.dp)
    ) {
        Text("Spending breakdown", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        if (data.isEmpty()) {
            Text("Your spending categories will appear here.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = .55f), style = MaterialTheme.typography.bodyMedium)
        } else {
            Row(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp))) {
                data.forEachIndexed { index, item ->
                    Box(Modifier.weight((item.second / total).toFloat().coerceAtLeast(.01f)).fillMaxHeight().background(palette[index % palette.size]))
                }
            }
            Spacer(Modifier.height(16.dp))
            data.forEachIndexed { index, item ->
                Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row {
                        Box(Modifier.padding(top = 5.dp).size(8.dp).clip(RoundedCornerShape(3.dp)).background(palette[index % palette.size]))
                        Spacer(Modifier.width(9.dp))
                        Text(item.first.label, style = MaterialTheme.typography.bodyMedium)
                    }
                    Text("${currency.symbol}${"%.2f".format(item.second)}  ·  ${((item.second / total) * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

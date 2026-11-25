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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emtrix.spendex.model.Currency

private val Mint = Color(0xFF34D399)
private val DeepGreen = Color(0xFF064E3B)

@Composable
fun MoneyText(value: Double, currency: Currency, modifier: Modifier = Modifier, large: Boolean = false, color: Color = MaterialTheme.colorScheme.onSurface) {
    Text(
        text = "${currency.symbol}${"%,.2f".format(value)}",
        modifier = modifier,
        color = color,
        fontSize = if (large) 32.sp else 17.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun SummarySection(balance: Double, income: Double, expense: Double, currency: Currency) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(26.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF047857), DeepGreen, Color(0xFF022C22))))
                .padding(22.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("TOTAL BALANCE", color = Color.White.copy(alpha = .78f), style = MaterialTheme.typography.labelLarge, letterSpacing = 1.4.sp, modifier = Modifier.weight(1f))
                Box(Modifier.clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = .14f)).padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Text("MY WALLET", color = Color.White, style = MaterialTheme.typography.labelSmall, letterSpacing = .8.sp)
                }
            }
            Spacer(Modifier.height(10.dp))
            MoneyText(balance, currency, large = true, color = Color.White)
            Spacer(Modifier.height(22.dp))
            HorizontalDivider(color = Color.White.copy(alpha = .18f))
            Spacer(Modifier.height(15.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowDownward, null, tint = Mint, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(5.dp))
                        Text("INCOME", color = Color.White.copy(alpha = .72f), style = MaterialTheme.typography.labelSmall, letterSpacing = .8.sp)
                    }
                    Spacer(Modifier.height(6.dp))
                    MoneyText(income, currency, color = Color.White)
                }
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowUpward, null, tint = Color(0xFFFCA5A5), modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(5.dp))
                        Text("EXPENSES", color = Color.White.copy(alpha = .72f), style = MaterialTheme.typography.labelSmall, letterSpacing = .8.sp)
                    }
                    Spacer(Modifier.height(6.dp))
                    MoneyText(expense, currency, color = Color.White)
                }
            }
        }
        Text("This month at a glance", style = MaterialTheme.typography.titleMedium)
    }
}

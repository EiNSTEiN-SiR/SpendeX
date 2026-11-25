package com.emtrix.spendex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emtrix.spendex.model.Currency

private val Green = Color(0xFF059669)
private val Red = Color(0xFFDC2626)

@Composable
fun MoneyText(value: Double, currency: Currency, modifier: Modifier = Modifier, large: Boolean = false, color: Color = MaterialTheme.colorScheme.onSurface) {
    Text(
        text = "${currency.symbol}${"%,.2f".format(value)}",
        modifier = modifier,
        color = color,
        fontSize = if (large) 32.sp else 17.sp,
        fontWeight = FontWeight.SemiBold,
        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontFeatureSettings = "tnum")
    )
}

@Composable
fun SummarySection(balance: Double, income: Double, expense: Double, currency: Currency) {
    Column {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                Text("TOTAL BALANCE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = .55f), letterSpacing = 1.sp)
                Spacer(Modifier.height(8.dp))
                MoneyText(balance, currency, large = true)
                Spacer(Modifier.height(22.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text("Income", color = MaterialTheme.colorScheme.onSurface.copy(alpha = .58f), fontSize = 13.sp)
                        Spacer(Modifier.height(5.dp))
                        MoneyText(income, currency, color = Green)
                    }
                    Column(Modifier.weight(1f)) {
                        Text("Expenses", color = MaterialTheme.colorScheme.onSurface.copy(alpha = .58f), fontSize = 13.sp)
                        Spacer(Modifier.height(5.dp))
                        MoneyText(expense, currency, color = Red)
                    }
                }
            }
        }
    }
}

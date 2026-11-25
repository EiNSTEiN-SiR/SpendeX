package com.emtrix.spendex

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emtrix.spendex.model.Currency
import com.emtrix.spendex.model.Transaction
import com.emtrix.spendex.ui.components.*
import com.emtrix.spendex.ui.theme.SpendeXTheme
import com.emtrix.spendex.viewmodel.FinanceViewModel
import com.emtrix.spendex.viewmodel.TransactionFilter
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { SpendeXApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpendeXApp(vm: FinanceViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    var splash by remember { mutableStateOf(true) }
    var showAdd by remember { mutableStateOf(false) }
    var undoItem by remember { mutableStateOf<Transaction?>(null) }
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    SpendeXTheme(darkTheme = state.darkMode) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Crossfade(targetState = splash, label = "launch") { isSplash ->
                if (isSplash) {
                    SplashScreen { splash = false }
                } else {
                    Scaffold(
                        containerColor = MaterialTheme.colorScheme.background,
                        snackbarHost = { SnackbarHost(snackbar) },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); showAdd = true },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) { Icon(Icons.Default.Add, contentDescription = "Add transaction") }
                        }
                    ) { padding ->
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(padding),
                            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 100.dp),
                            verticalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            item {
                                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text("SpendeX", style = MaterialTheme.typography.headlineSmall)
                                        Text("Personal finance, made clear", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = .55f))
                                    }
                                    IconButton(onClick = { vm.setCurrency(if (state.currency == Currency.BDT) Currency.USD else Currency.BDT) }) {
                                        Text(if (state.currency == Currency.BDT) "৳" else "$", style = MaterialTheme.typography.titleMedium)
                                    }
                                    IconButton(onClick = vm::toggleTheme) {
                                        Icon(if (state.darkMode) Icons.Default.LightMode else Icons.Default.DarkMode, "Toggle theme")
                                    }
                                }
                            }
                            item {
                                val totals = vm.totals()
                                SummarySection(totals.first, totals.second, totals.third, state.currency)
                            }
                            item { CategoryBreakdownCard(vm.categoryTotals(), state.currency) }
                            item {
                                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                    Text("Transactions", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                                    TextButton(onClick = {
                                        runCatching {
                                            val file = java.io.File(context.cacheDir, "SpendeX-transactions.csv")
                                            file.writeText(vm.csv())
                                            val uri = androidx.core.content.FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                                            val intent = Intent(Intent.ACTION_SEND).apply {
                                                type = "text/csv"
                                                putExtra(Intent.EXTRA_STREAM, uri)
                                                putExtra(Intent.EXTRA_SUBJECT, "SpendeX transactions")
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                            context.startActivity(Intent.createChooser(intent, "Export transactions"))
                                        }.onFailure { Toast.makeText(context, "Unable to share CSV", Toast.LENGTH_SHORT).show() }
                                    }) {
                                        Icon(Icons.Default.FileDownload, null, modifier = Modifier.size(17.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Export CSV")
                                    }
                                }
                                OutlinedTextField(
                                    value = state.query, onValueChange = vm::setQuery,
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("Search transactions") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf(TransactionFilter.ALL, TransactionFilter.INCOME, TransactionFilter.EXPENSE).forEach { filter ->
                                        FilterChip(
                                            selected = state.filter == filter,
                                            onClick = { vm.setFilter(filter) },
                                            label = { Text(filter.name.lowercase().replaceFirstChar { it.uppercase() }) }
                                        )
                                    }
                                }
                            }
                            val transactions = vm.visibleTransactions()
                            if (transactions.isEmpty()) {
                                item {
                                    Text("No transactions yet. Add your first entry to get started.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = .55f), modifier = Modifier.padding(vertical = 24.dp))
                                }
                            } else {
                                items(transactions, key = { it.id }) { transaction ->
                                    var dragAmount by remember(transaction.id) { mutableFloatStateOf(0f) }
                                    Column(
                                        Modifier.fillMaxWidth().pointerInput(transaction.id) {
                                            detectHorizontalDragGestures(
                                                onHorizontalDrag = { _, amount -> dragAmount += amount },
                                                onDragEnd = {
                                                    if (dragAmount < -110f) {
                                                        undoItem = vm.delete(transaction)
                                                        scope.launch {
                                                            val result = snackbar.showSnackbar("Transaction deleted", actionLabel = "Undo", withDismissAction = true)
                                                            if (result == SnackbarResult.ActionPerformed) undoItem?.let(vm::restore)
                                                            undoItem = null
                                                        }
                                                    }
                                                    dragAmount = 0f
                                                }
                                            )
                                        }
                                    ) {
                                        TransactionItem(transaction, state.currency)
                                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = .65f))
                                    }
                                }
                            }
                            item {
                                Box(Modifier.fillMaxWidth().padding(top = 10.dp), contentAlignment = Alignment.Center) {
                                    Text("SpendeX by Emtrix", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f))
                                }
                            }
                        }
                    }
                }
            }
            if (showAdd) {
                AddTransactionSheet(currency = state.currency, onDismiss = { showAdd = false }, onSave = { vm.add(it); haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) })
            }
        }
    }
}

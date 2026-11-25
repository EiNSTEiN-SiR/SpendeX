package com.emtrix.spendex.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.emtrix.spendex.data.TransactionStorage
import com.emtrix.spendex.model.Category
import com.emtrix.spendex.model.Currency
import com.emtrix.spendex.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Calendar

enum class TransactionFilter { ALL, INCOME, EXPENSE }

data class FinanceState(
    val transactions: List<Transaction> = emptyList(),
    val currency: Currency = Currency.BDT,
    val darkMode: Boolean = false,
    val filter: TransactionFilter = TransactionFilter.ALL,
    val query: String = "",
    val loaded: Boolean = false
)

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = TransactionStorage(application)
    private val preferences = application.getSharedPreferences("spendex_settings", Application.MODE_PRIVATE)
    private val persistenceMutex = Mutex()

    private val savedCurrency = runCatching {
        Currency.valueOf(preferences.getString("currency", Currency.BDT.name) ?: Currency.BDT.name)
    }.getOrDefault(Currency.BDT)

    private val _state = MutableStateFlow(
        FinanceState(
            currency = savedCurrency,
            darkMode = preferences.getBoolean("dark_mode", false)
        )
    )
    val state: StateFlow<FinanceState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(transactions = storage.load(), loaded = true) }
        }
    }

    fun setCurrency(value: Currency) {
        preferences.edit().putString("currency", value.name).apply()
        _state.update { it.copy(currency = value) }
    }

    fun toggleTheme() {
        val newMode = !_state.value.darkMode
        preferences.edit().putBoolean("dark_mode", newMode).apply()
        _state.update { it.copy(darkMode = newMode) }
    }
    fun setFilter(value: TransactionFilter) = _state.update { it.copy(filter = value) }
    fun setQuery(value: String) = _state.update { it.copy(query = value) }

    fun add(transaction: Transaction) {
        val updated = listOf(transaction) + _state.value.transactions
        _state.update { it.copy(transactions = updated) }
        persist(updated)
    }

    fun delete(transaction: Transaction): Transaction? {
        val updated = _state.value.transactions.filterNot { it.id == transaction.id }
        _state.update { it.copy(transactions = updated) }
        persist(updated)
        return transaction
    }

    fun restore(transaction: Transaction) {
        val updated = listOf(transaction) + _state.value.transactions
        _state.update { it.copy(transactions = updated) }
        persist(updated)
    }

    private fun persist(items: List<Transaction>) {
        viewModelScope.launch {
            persistenceMutex.withLock {
                // Always write the latest state, preventing overlapping writes from
                // leaving an older transaction list on disk.
                storage.save(_state.value.transactions)
            }
        }
    }

    fun visibleTransactions(): List<Transaction> {
        val s = _state.value
        return s.transactions.filter {
            val matchesFilter = when (s.filter) {
                TransactionFilter.ALL -> true
                TransactionFilter.INCOME -> it.isIncome
                TransactionFilter.EXPENSE -> !it.isIncome
            }
            matchesFilter && (s.query.isBlank() ||
                it.note.contains(s.query, true) ||
                it.category.label.contains(s.query, true) ||
                it.amount.toString().contains(s.query))
        }.sortedByDescending { it.dateMillis }
    }

    fun totals(): Triple<Double, Double, Double> {
        val now = Calendar.getInstance()
        val monthItems = _state.value.transactions.filter {
            val date = Calendar.getInstance().apply { timeInMillis = it.dateMillis }
            date.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                date.get(Calendar.MONTH) == now.get(Calendar.MONTH)
        }
        val income = monthItems.filter { it.isIncome }.sumOf { it.amount }
        val expense = monthItems.filterNot { it.isIncome }.sumOf { it.amount }
        val balance = _state.value.transactions.sumOf { if (it.isIncome) it.amount else -it.amount }
        return Triple(balance, income, expense)
    }

    fun categoryTotals(): List<Pair<Category, Double>> {
        val expenses = _state.value.transactions.filterNot { it.isIncome }
        return Category.entries.map { category ->
            category to expenses.filter { it.category == category }.sumOf { it.amount }
        }.filter { it.second > 0.0 }.sortedByDescending { it.second }
    }

    fun csv(): String = storage.exportCsv(_state.value.transactions)
}

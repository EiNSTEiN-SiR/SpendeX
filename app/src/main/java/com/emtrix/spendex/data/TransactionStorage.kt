package com.emtrix.spendex.data

import android.content.Context
import com.emtrix.spendex.model.Transaction
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionStorage(context: Context) {
    private val file = File(context.filesDir, "spendex_transactions.json")
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    suspend fun load(): List<Transaction> = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        if (!file.exists()) emptyList()
        else runCatching {
            json.decodeFromString(ListSerializer(Transaction.serializer()), file.readText())
        }.getOrDefault(emptyList())
    }

    suspend fun save(items: List<Transaction>) = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val temp = File(file.parentFile, "transactions.tmp")
        temp.writeText(json.encodeToString(ListSerializer(Transaction.serializer()), items))
        if (!temp.renameTo(file)) {
            file.writeText(temp.readText())
            temp.delete()
        }
    }

    fun exportCsv(items: List<Transaction>): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val rows = items.sortedByDescending { it.dateMillis }.map {
            val note = it.note.replace("\"", "\"\"")
            "\"${dateFormat.format(Date(it.dateMillis))}\",${if (it.isIncome) "Income" else "Expense"},${it.category.label},${it.amount},\"$note\""
        }
        return (listOf("Date,Type,Category,Amount,Note") + rows).joinToString("\n")
    }
}

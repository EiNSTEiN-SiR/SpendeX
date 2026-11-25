package com.emtrix.spendex.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
enum class Category(val label: String) {
    FOOD("Food"), TRANSPORT("Transport"), BILLS("Bills"),
    SHOPPING("Shopping"), SALARY("Salary"), MISC("Misc")
}

@Serializable
enum class Currency(val symbol: String, val label: String) {
    BDT("৳", "BDT"), USD("$", "USD")
}

@Serializable
data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val isIncome: Boolean,
    val category: Category,
    val note: String = "",
    val dateMillis: Long = System.currentTimeMillis()
)

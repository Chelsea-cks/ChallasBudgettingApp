package com.example.challasbudgettingapp.data.offline

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.challasbudgettingapp.data.model.Expense
import com.example.challasbudgettingapp.data.model.Income
import com.example.challasbudgettingapp.data.repository.ExpenseRepository
import com.example.challasbudgettingapp.data.repository.IncomeRepository

/**
 * Local SQLite queue for actions performed offline.
 * Rows stay in the queue until the REST API accepts them (sync on reconnect).
 */
private class OfflineDbHelper(context: Context) :
    SQLiteOpenHelper(context, "offline_queue.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE pending_expense(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userID INTEGER, category TEXT, amount REAL,
                date TEXT, notes TEXT, receiptImageUrl TEXT)"""
        )
        db.execSQL(
            """CREATE TABLE pending_income(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userID INTEGER, source TEXT, amount REAL,
                date TEXT, notes TEXT)"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldV: Int, newV: Int) {
        db.execSQL("DROP TABLE IF EXISTS pending_expense")
        db.execSQL("DROP TABLE IF EXISTS pending_income")
        onCreate(db)
    }
}

class OfflineSyncManager(private val context: Context) {

    private val tag = "OfflineSyncManager"
    private val helper = OfflineDbHelper(context.applicationContext)
    private val expenseRepository = ExpenseRepository()
    private val incomeRepository = IncomeRepository()

    /** True when the device has an internet-capable network. */
    fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun saveOfflineExpense(expense: Expense) {
        val values = ContentValues().apply {
            put("userID", expense.userID)
            put("category", expense.category)
            put("amount", expense.amount)
            put("date", expense.date)
            put("notes", expense.notes)
            put("receiptImageUrl", expense.receiptImageUrl)
        }
        helper.writableDatabase.insert("pending_expense", null, values)
        Log.d(tag, "Expense queued offline. Pending: ${getPendingExpenses().size}")
    }

    fun saveOfflineIncome(income: Income) {
        val values = ContentValues().apply {
            put("userID", income.userID)
            put("source", income.source)
            put("amount", income.amount)
            put("date", income.date)
            put("notes", income.notes)
        }
        helper.writableDatabase.insert("pending_income", null, values)
        Log.d(tag, "Income queued offline. Pending: ${getPendingIncome().size}")
    }

    /** Returns queued expenses paired with their row id. */
    private fun queryExpenses(): List<Pair<Long, Expense>> {
        val list = mutableListOf<Pair<Long, Expense>>()
        helper.readableDatabase.rawQuery("SELECT * FROM pending_expense", null).use { c ->
            while (c.moveToNext()) {
                list.add(
                    c.getLong(0) to Expense(
                        userID = c.getInt(1),
                        category = c.getString(2),
                        amount = c.getDouble(3),
                        date = c.getString(4),
                        notes = c.getString(5),
                        receiptImageUrl = c.getString(6)
                    )
                )
            }
        }
        return list
    }

    private fun queryIncome(): List<Pair<Long, Income>> {
        val list = mutableListOf<Pair<Long, Income>>()
        helper.readableDatabase.rawQuery("SELECT * FROM pending_income", null).use { c ->
            while (c.moveToNext()) {
                list.add(
                    c.getLong(0) to Income(
                        userID = c.getInt(1),
                        source = c.getString(2),
                        amount = c.getDouble(3),
                        date = c.getString(4),
                        notes = c.getString(5)
                    )
                )
            }
        }
        return list
    }

    fun getPendingExpenses(): List<Expense> = queryExpenses().map { it.second }
    fun getPendingIncome(): List<Income> = queryIncome().map { it.second }

    /** Pushes queued rows to the API; rows that fail stay queued. Returns true if anything synced. */
    suspend fun syncPendingData(): Boolean {
        if (!isOnline()) {
            Log.d(tag, "Offline - sync skipped")
            return false
        }
        var syncedAny = false

        for ((id, expense) in queryExpenses()) {
            try {
                expenseRepository.createExpense(expense)
                helper.writableDatabase.delete("pending_expense", "id=?", arrayOf(id.toString()))
                syncedAny = true
                Log.d(tag, "Synced expense: ${expense.category}")
            } catch (e: Exception) {
                Log.e(tag, "Expense sync failed, keeping in queue: ${e.message}", e)
            }
        }

        for ((id, income) in queryIncome()) {
            try {
                incomeRepository.createIncome(income)
                helper.writableDatabase.delete("pending_income", "id=?", arrayOf(id.toString()))
                syncedAny = true
                Log.d(tag, "Synced income: ${income.source}")
            } catch (e: Exception) {
                Log.e(tag, "Income sync failed, keeping in queue: ${e.message}", e)
            }
        }
        return syncedAny
    }
}

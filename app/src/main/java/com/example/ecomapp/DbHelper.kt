package com.example.ecomapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, "app", factory, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, email TEXT, password TEXT, address TEXT, number TEXT)"
        db!!.execSQL(query)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun addUser(user: User): Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE login = ?", arrayOf(user.login))

        if (cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return false // Пользователь уже существует
        }

        cursor.close()

        val values = ContentValues()
        values.put("login", user.login)
        values.put("email", user.email)
        values.put("password", user.password)

        val writabledb = this.writableDatabase
        writabledb.insert("users", null, values)

        writabledb.close()
        return true
    }

    fun getUser(login: String, password: String): Boolean {
        val db = this.readableDatabase


        val query = "SELECT * FROM users WHERE login = ? AND password = ?"
        val result = db.rawQuery(query, arrayOf(login, password))
        return result.moveToFirst()
    }

    fun updateAddressAndNumber(login: String, address: String, number: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("address", address)
        values.put("number", number)

        val rowsAffected = db.update("users", values, "login = ?", arrayOf(login))
        db.close()

        return rowsAffected > 0
    }

    fun deleteDatabase() {
        context.deleteDatabase("app")
    }
}
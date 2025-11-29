package com.example.usandosqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.usandosqlite.entity.Cadastro

class DatabaseHandler private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val DATABASE_NAME = "bdfile.sqlite"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "cadastro"
        const val COLUMN_ID = "0"
        const val COLUMN_NOME = "1"
        const val COLUMN_TELEFONE = "2"

        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getInstance(context: Context): DatabaseHandler {
            if(INSTANCE == null) {
                INSTANCE = DatabaseHandler(context)
            }
            return INSTANCE as DatabaseHandler
        }
    }

    override fun onCreate(banco: SQLiteDatabase?) {
        banco?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT)")
    }

    override fun onUpgrade(
        banco: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        banco?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(banco)
    }

    fun inserir(cadastro: Cadastro) {
        val registro = ContentValues()
        registro.put("nome", cadastro.nome)
        registro.put("telefone", cadastro.telefone)

        writableDatabase.insert(TABLE_NAME, null, registro)
    }

    fun alterar(cadastro: Cadastro) {
        val registro = ContentValues()
        registro.put("nome", cadastro.nome)
        registro.put("telefone", cadastro.telefone)

        writableDatabase.update(TABLE_NAME, registro, "_id = ?", arrayOf(cadastro._id.toString()))
    }

    fun excluir(id: Int) {
        writableDatabase.delete(TABLE_NAME, "_id = ?", arrayOf(id.toString()))
    }

    fun pesquisar(id: Int): Cadastro? {
        val cursor = writableDatabase.query(
            TABLE_NAME,
            null,
            "_id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var retorno: Cadastro? = null

        if(cursor.moveToNext()) {
            var nome = cursor.getString(1)
            var telefone = cursor.getString(2)

            retorno = Cadastro(id, nome, telefone)
        }

        return retorno
    }

    fun listar(): Cursor {
        val registros = writableDatabase.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        return registros
    }
}
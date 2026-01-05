package com.example.usandosqlite.adapter

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.example.usandosqlite.MainActivity
import com.example.usandosqlite.R
import com.example.usandosqlite.database.DatabaseHandler
import com.example.usandosqlite.entity.Cadastro

class MeuAdapter(val context: Context, val cursor: Cursor): BaseAdapter() {

    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any? {
        cursor.moveToPosition(position)

        val cadastro = Cadastro(
            cursor.getInt(DatabaseHandler.COLUMN_ID.toInt()),
            cursor.getString(DatabaseHandler.COLUMN_NOME.toInt()),
            cursor.getString(DatabaseHandler.COLUMN_TELEFONE.toInt())
        )

        return cadastro
    }

    override fun getItemId(position: Int): Long {
        cursor.moveToPosition(position)
        return cursor.getInt(DatabaseHandler.COLUMN_ID.toInt()).toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.elemento_lista, null)

        val tvNomeElementoLista = v.findViewById<TextView>(R.id.tvNomeElementoLista)
        val tvTelefoneElementoLista = v.findViewById<TextView>(R.id.tvTelefoneElementoLista)
        val btEditarElementoLista = v.findViewById<ImageButton>(R.id.btEditarElementoLista)

        cursor.moveToPosition(position)

        tvNomeElementoLista.text = cursor.getString(DatabaseHandler.COLUMN_NOME.toInt())
        tvTelefoneElementoLista.text = cursor.getString(DatabaseHandler.COLUMN_TELEFONE.toInt())

        btEditarElementoLista.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)

            cursor.moveToPosition(position)

            intent.putExtra("cod", cursor.getInt(DatabaseHandler.COLUMN_ID.toInt()))
            intent.putExtra("nome", cursor.getString(DatabaseHandler.COLUMN_NOME.toInt()))
            intent.putExtra("telefone", cursor.getString(DatabaseHandler.COLUMN_TELEFONE.toInt()))

            context.startActivity(intent)
        }

        return v
    }

}
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

class MeuAdapter(val context: Context, val registros: List<Cadastro>): BaseAdapter() {

    override fun getCount(): Int {
        return registros.size
    }

    override fun getItem(position: Int): Any? {
        val cadastro = Cadastro(
            registros[position]._id,
            registros[position].nome,
            registros[position].telefone
        )

        return cadastro
    }

    override fun getItemId(position: Int): Long {
        return registros[position]._id.toLong()
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

        tvNomeElementoLista.text = registros[position].nome
        tvTelefoneElementoLista.text = registros[position].telefone

        btEditarElementoLista.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)

            intent.putExtra("cod", registros[position]._id)
            intent.putExtra("nome", registros[position].nome)
            intent.putExtra("telefone", registros[position].telefone)

            context.startActivity(intent)
        }

        return v
    }

}
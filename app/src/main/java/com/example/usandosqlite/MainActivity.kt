package com.example.usandosqlite

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.usandosqlite.database.DatabaseHandler
import com.example.usandosqlite.database.DatabaseHandler.Companion.COLUMN_NOME
import com.example.usandosqlite.database.DatabaseHandler.Companion.COLUMN_TELEFONE
import com.example.usandosqlite.databinding.ActivityMainBinding
import com.example.usandosqlite.entity.Cadastro

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
    }

    private fun initView() {
        if (intent.getIntExtra("cod", 0) != 0) {
            binding.etCod.setText(intent.getIntExtra("cod", 0).toString())
            binding.etNome.setText(intent.getStringExtra("nome"))
            binding.etTelefone.setText(intent.getStringExtra("telefone"))
        } else {

        }
    }

    fun btSalvarOnClick(view: View) {
        var msg = ""
        if(binding.etCod.text.isEmpty()) {
            val cadastro = Cadastro(
                0,
                binding.etNome.text.toString(),
                binding.etTelefone.text.toString()
            )
            msg = "Registro inserido com sucesso!"


            banco.inserir(cadastro)
        } else {
            val cadastro = Cadastro(
                binding.etCod.text.toString().toInt(),
                binding.etNome.text.toString(),
                binding.etTelefone.text.toString()
            )
            msg = "Registro alterado com sucesso!"

            banco.alterar(cadastro)
        }

        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }
    fun btExcluirOnClick(view: View) {
        if (binding.etCod.text.isEmpty()) {
            Toast.makeText(
                this,
                "Preencha o código!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        banco.excluir(binding.etCod.text.toString().toInt())

        Toast.makeText(
            this,
            "Registro excluído com sucesso!",
            Toast.LENGTH_SHORT
        ).show()
    }
    fun btPesquisarOnClick(view: View) {
        if (binding.etCod.text.isEmpty()) {
            Toast.makeText(
                this,
                "Preencha o código!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val cadastro = banco.pesquisar(binding.etCod.text.toString().toInt())

        if(cadastro != null) {
            binding.etNome.setText(cadastro.nome)
            binding.etTelefone.setText(cadastro.telefone)
        } else {
            binding.etNome.text.clear()
            binding.etTelefone.text.clear()

            Toast.makeText(
                this,
                "Registro não encontrado!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
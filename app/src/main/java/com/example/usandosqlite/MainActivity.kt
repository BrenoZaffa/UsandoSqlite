package com.example.usandosqlite

import android.content.ContentValues
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
    }

    fun btIncluirOnClick(view: View) {
        if (binding.etNome.text.isEmpty() || binding.etTelefone.text.isEmpty()) {
            Toast.makeText(
                this,
                "Preencha todos os campos!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val cadastro = Cadastro(
            0,
            binding.etNome.text.toString(),
            binding.etTelefone.text.toString()
        )

        banco.inserir(cadastro)

        Toast.makeText(
            this,
            "Registro inserido com sucesso!",
            Toast.LENGTH_SHORT
        ).show()
    }
    fun btAlterarOnClick(view: View) {
        if (binding.etCod.text.isEmpty() || binding.etNome.text.isEmpty() || binding.etTelefone.text.isEmpty()) {
            Toast.makeText(
                this,
                "Preencha todos os campos!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val cadastro = Cadastro(
            binding.etCod.text.toString().toInt(),
            binding.etNome.text.toString(),
            binding.etTelefone.text.toString()
        )

        banco.alterar(cadastro)

        Toast.makeText(
            this,
            "Registro alterado com sucesso!",
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
    fun btListarOnClick(view: View) {
        val registros = banco.listar()

        val saida = StringBuilder()

        while(registros.moveToNext()) {
            var nome = registros.getString(1)
            var telefone = registros.getString(2)

            saida.append("Nome: $nome " + "Telefone: $telefone\n\n")
        }

        Toast.makeText(
            this,
            saida.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }
    fun btLimparOnClick(view: View) {
        binding.etCod.text.clear()
        binding.etNome.text.clear()
        binding.etTelefone.text.clear()
    }
}
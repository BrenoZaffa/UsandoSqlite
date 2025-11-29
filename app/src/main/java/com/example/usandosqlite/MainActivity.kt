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
import com.example.usandosqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = openOrCreateDatabase(
            "bdfile.sqlite",
            MODE_PRIVATE,
            null
        )

        banco.execSQL("CREATE TABLE IF NOT EXISTS cadastro (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT)")

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

        val registro = ContentValues()
        registro.put("nome", binding.etNome.text.toString())
        registro.put("telefone", binding.etTelefone.text.toString())

        banco.insert("cadastro", null, registro)

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

        val registro = ContentValues()
        registro.put("nome", binding.etNome.text.toString())
        registro.put("telefone", binding.etTelefone.text.toString())

        banco.update("cadastro", registro, "_id = ?", arrayOf(binding.etCod.text.toString()))

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

        banco.delete("cadastro", "_id = ?", arrayOf(binding.etCod.text.toString()))

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

        val cursor = banco.query(
            "cadastro",
            null,
            "_id = ?",
            arrayOf(binding.etCod.text.toString()),
            null,
            null,
            null
        )

        if(cursor.moveToNext()) {
            var nome = cursor.getString(1)
            var telefone = cursor.getString(2)

            binding.etNome.setText(nome)
            binding.etTelefone.setText(telefone)
        } else {
            Toast.makeText(
                this,
                "Registro não encontrado!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    fun btListarOnClick(view: View) {
        val registros = banco.query(
            "cadastro",
            null,
            null,
            null,
            null,
            null,
            null
        )

        val saida = StringBuilder()

        while(registros.moveToNext()) {
            var cod = registros.getInt(0)
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
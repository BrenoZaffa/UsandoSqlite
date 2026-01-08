package com.example.usandosqlite

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.usandosqlite.database.DatabaseHandler
import com.example.usandosqlite.databinding.ActivityMainBinding
import com.example.usandosqlite.entity.Cadastro
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: DatabaseHandler

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
    }

    private fun initView() {
        if (!intent.getStringExtra("cod").isNullOrEmpty()) {
            binding.etCod.setText(intent.getStringExtra("cod"))
            binding.etNome.setText(intent.getStringExtra("nome"))
            binding.etTelefone.setText(intent.getStringExtra("telefone"))
        } else {
            binding.btExcluir.visibility = View.GONE
            binding.btPesquisar.visibility = View.GONE
        }
    }

    fun btSalvarOnClick(view: View) {
        lifecycleScope.launch {
            var msg = ""
            if(binding.etCod.text.isEmpty()) {
                val lista = banco.listar()
                val cadastro = Cadastro(
                    (lista.size + 1).toString(),
                    binding.etNome.text.toString(),
                    binding.etTelefone.text.toString()
                )
                msg = "Registro inserido com sucesso!"


                banco.inserir(cadastro)
            } else {
                val cadastro = Cadastro(
                    binding.etCod.text.toString(),
                    binding.etNome.text.toString(),
                    binding.etTelefone.text.toString()
                )
                msg = "Registro alterado com sucesso!"

                banco.alterar(cadastro)
            }

            Toast.makeText(
                this@MainActivity,
                msg,
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
    fun btExcluirOnClick(view: View) {
        lifecycleScope.launch {
            if (binding.etCod.text.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Preencha o código!",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }

            banco.excluir(binding.etCod.text.toString())

            Toast.makeText(
                this@MainActivity,
                "Registro excluído com sucesso!",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
    fun btPesquisarOnClick(view: View) {
        val etCodPesquisar = EditText(this)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Digite o Código")
        builder.setView(etCodPesquisar)
        builder.setCancelable(false)
        builder.setNegativeButton("Fechar", null)
        builder.setPositiveButton(
            "Pesquisar"
        ) { dialog, which ->
            lifecycleScope.launch {
                val cadastro = banco.pesquisar(etCodPesquisar.text.toString())

                if(cadastro != null) {
                    binding.etCod.setText(cadastro._id)
                    binding.etNome.setText(cadastro.nome)
                    binding.etTelefone.setText(cadastro.telefone)
                } else {
                    binding.etCod.text.clear()
                    binding.etNome.text.clear()
                    binding.etTelefone.text.clear()

                    Toast.makeText(
                        this@MainActivity,
                        "Registro não encontrado!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        builder.show()
    }
}
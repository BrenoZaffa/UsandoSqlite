package com.example.usandosqlite.database

import com.example.usandosqlite.entity.Cadastro
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class DatabaseHandler () {

    private val firestore = Firebase.firestore

    companion object{
        private const val COLLECTION_NAME = "cadastro"

        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getInstance(): DatabaseHandler {
            return INSTANCE ?: synchronized(this) {
                val instance = DatabaseHandler()
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun inserir(cadastro: Cadastro) {
        firestore.collection(COLLECTION_NAME)
            .document(cadastro._id)
            .set(cadastro).await()
    }

    suspend fun alterar(cadastro: Cadastro) {
        firestore.collection(COLLECTION_NAME)
            .document(cadastro._id)
            .set(cadastro).await()
    }

    suspend fun excluir(id: String) {
        firestore.collection(COLLECTION_NAME)
            .document(id)
            .delete().await()
    }

    suspend fun pesquisar(id: String): Cadastro? {
        val document = firestore.collection(COLLECTION_NAME)
            .document(id)
            .get().await()

        return document.toObject(Cadastro::class.java)
    }

    suspend fun listar(): List<Cadastro> {
        val query = firestore.collection(COLLECTION_NAME)
        val snapshot = query.get().await()
        return snapshot.toObjects<Cadastro>()
    }
}
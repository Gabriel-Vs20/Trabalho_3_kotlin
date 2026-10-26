package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estabelecimentos")
data class Estabelecimento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val categoria: String,
    val nota: String,
    val endereco: String
)

@Entity(tableName = "agendamentos")
data class Agendamento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idEstabelecimento: Int,
    val servico: String,
    val data: String,
    val hora: String,
    val preco: Double,
    val observacao: String = "",
    val concluido: Boolean = false
)

data class Notificacao(
    val id: Int,
    val titulo: String,
    val mensagem: String,
    val quando: String,
    val lida: Boolean = false
)

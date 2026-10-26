package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EstabelecimentoDAO {

    @Insert
    suspend fun inserir(estabelecimento: Estabelecimento)

    @Query("SELECT * FROM estabelecimentos")
    suspend fun buscarTodos(): List<Estabelecimento>

    @Query("SELECT * FROM estabelecimentos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Estabelecimento?

    @Update
    suspend fun atualizar(estabelecimento: Estabelecimento)

    @Delete
    suspend fun deletar(estabelecimento: Estabelecimento)

}

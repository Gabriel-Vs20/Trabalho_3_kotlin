package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AgendamentoDAO {

    @Insert
    suspend fun inserir(agendamento: Agendamento)

    @Query("SELECT * FROM agendamentos")
    suspend fun buscarTodos(): List<Agendamento>

    @Query("SELECT * FROM agendamentos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Agendamento?

    @Query("SELECT * FROM agendamentos WHERE idEstabelecimento = :idEstabelecimento")
    suspend fun buscarPorEstabelecimento(idEstabelecimento: Int): List<Agendamento>

    @Update
    suspend fun atualizar(agendamento: Agendamento)

    @Delete
    suspend fun deletar(agendamento: Agendamento)

    @Query("DELETE FROM agendamentos WHERE idEstabelecimento = :idEstabelecimento")
    suspend fun deletarPorEstabelecimento(idEstabelecimento: Int)

}

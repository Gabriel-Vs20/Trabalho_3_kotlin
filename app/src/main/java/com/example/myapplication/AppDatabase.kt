package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Estabelecimento::class, Agendamento::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun estabelecimentoDAO(): EstabelecimentoDAO
    abstract fun agendamentoDAO(): AgendamentoDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            val temInstance = INSTANCE

            if (temInstance != null) {
                return temInstance
            } else {

                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "bora_agendar_database"
                    ).build()
                    INSTANCE = instance
                    return instance
                }

            }

        }

    }

}

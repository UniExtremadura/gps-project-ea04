package es.unex.nbafantasy.bd.roomBD

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.unex.nbafantasy.bd.elemBD.Jugador
import es.unex.nbafantasy.bd.elemBD.Usuario

@Database(entities = [Usuario::class, Jugador::class], version = 1)
abstract class BD : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun jugadorDao(): JugadorDao
    companion object {
        private var INSTANCE: BD? = null
        fun getInstance(context: Context): BD? {
            if (INSTANCE == null) {
                synchronized(BD::class) {
                    INSTANCE =
                        Room.databaseBuilder(context, BD::class.java, "nbafantasy.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
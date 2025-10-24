package com.example.gonzaloaliaga.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gonzaloaliaga.data.products.ProductoDao
import com.example.gonzaloaliaga.data.users.UsuarioDao
import com.example.gonzaloaliaga.model.Producto
import com.example.gonzaloaliaga.model.Usuario

@Database(
    entities = [Producto::class, Usuario::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "productos.db"
                ).build().also { INSTANCE = it }
            }
    }
}

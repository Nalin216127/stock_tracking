package com.example.task_02.Dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.task_02.data.SmartPhone

@Database(entities = [SmartPhone::class], version = 1, exportSchema = false)
abstract class SmartDatabase:RoomDatabase() {

    abstract fun smartphoneDao(): SmartphoneDao

    companion object{
        @Volatile
        private var INSTANCE: SmartDatabase? = null

        fun getDatabase(context: Context):SmartDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartDatabase::class.java,
                    "smart_database"
                ).build()
                INSTANCE=instance
                return instance
            }
        }
    }
}
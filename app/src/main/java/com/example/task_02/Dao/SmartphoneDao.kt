package com.example.task_02.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.task_02.data.SmartPhone

@Dao
interface SmartphoneDao {

    @Query("SELECT * FROM smart_phone")
    fun getAllSmartPhones():LiveData<List<SmartPhone>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSmartPhone(smartphone:SmartPhone)

    @Update
    suspend fun updateSmartPhone(smartPhone: SmartPhone)

}
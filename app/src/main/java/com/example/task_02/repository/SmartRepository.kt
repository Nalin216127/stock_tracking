package com.example.task_02.repository

import androidx.lifecycle.LiveData
import com.example.task_02.Dao.SmartphoneDao
import com.example.task_02.data.SmartPhone

class SmartRepository(private val smartphoneDao: SmartphoneDao) {

        val getAllSmartPhone: LiveData<List<SmartPhone>>  = smartphoneDao.getAllSmartPhones()

        suspend fun addSmartPhone(smartPhone: SmartPhone){
                smartphoneDao.addSmartPhone(smartPhone)
        }

        suspend fun updateSmartPhone(smartPhone: SmartPhone){
                smartphoneDao.updateSmartPhone(smartPhone)
        }
}
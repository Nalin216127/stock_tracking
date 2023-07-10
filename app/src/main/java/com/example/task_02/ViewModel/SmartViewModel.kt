package com.example.task_02.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.task_02.Dao.SmartDatabase
import com.example.task_02.data.SmartPhone
import com.example.task_02.repository.SmartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmartViewModel(application: Application):AndroidViewModel(application) {
    private val repository:SmartRepository
    val getAllSmartPhone:LiveData<List<SmartPhone>>
    val smList=ArrayList<SmartPhone>()
    init {
        val smartphoneDao = SmartDatabase.getDatabase(application).smartphoneDao()
        repository = SmartRepository(smartphoneDao)
        getAllSmartPhone = repository.getAllSmartPhone

    }

    fun addSmartPhone(smartPhone: SmartPhone){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSmartPhone(smartPhone)
        }
    }

    fun updateSmartPhone(smartPhone: SmartPhone){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSmartPhone(smartPhone)
        }
    }
}
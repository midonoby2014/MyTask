package com.noby.mytasks.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.noby.mytasks.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Created by Ahmed Noby Ahmed on 5/19/21.
 */


class TaskViewModel @ViewModelInject constructor(
    private val taskDao :TaskDao
) :ViewModel() {


    val searchQuery = MutableStateFlow("")
    private  val taskflow =  searchQuery.flatMapLatest {
        taskDao.getTasks(it)
    }
    val tasks =  taskflow.asLiveData()

}
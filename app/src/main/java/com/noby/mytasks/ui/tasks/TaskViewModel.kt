package com.noby.mytasks.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.noby.mytasks.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Created by Ahmed Noby Ahmed on 5/19/21.
 */


class TaskViewModel @ViewModelInject constructor(
    private val taskDao :TaskDao
) :ViewModel() {


    val searchQuery = MutableStateFlow("")

    val sortOrder =  MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted =  MutableStateFlow(false)

        /// single observiing
//    private  val taskflow =  searchQuery.flatMapLatest {
//        taskDao.getTasks(it)
//    }

    private val taskflow  = combine(searchQuery,sortOrder,hideCompleted){
        query,sortOrder,hideCompleted ->
        Triple(query,sortOrder,hideCompleted)
    }.flatMapLatest { (query,sortOrder,hideCompleted) ->
        taskDao.getTasks(query,sortOrder,hideCompleted)
    }
    val tasks =  taskflow.asLiveData()

    enum class SortOrder {BY_NAME  , BY_DATE}



}
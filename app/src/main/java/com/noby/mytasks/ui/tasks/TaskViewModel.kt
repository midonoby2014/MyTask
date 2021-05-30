package com.noby.mytasks.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.noby.mytasks.data.Task
import com.noby.mytasks.data.TaskDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Created by Ahmed Noby Ahmed on 5/19/21.
 */


class TaskViewModel @ViewModelInject constructor(
    private val taskDao :TaskDao
) :ViewModel() {

    val searchQuery = MutableStateFlow("")
    val sortOrder =  MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted =  MutableStateFlow(false)
   private val taskEventChannel =  Channel<TaskEvent>()
    val taskEvent = taskEventChannel.receiveAsFlow()
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



    fun onTaskSelected(task: Task) {

    }

    fun onTasKChekedChanged(task: Task, checked: Boolean) = viewModelScope.launch {
    taskDao.update(task.copy(completed = checked))
    }

    fun onTaskSwipe(task: Task) =  viewModelScope.launch {
      taskDao.delete(task)
        taskEventChannel.send(TaskEvent.ShowUndoDeleteTaskMessage(task))
    }
    fun onUndoDeleteClick(task :Task) =  viewModelScope.launch {
        taskDao.insert(task)
    }


    sealed class TaskEvent{
        data class  ShowUndoDeleteTaskMessage(val task :Task) :TaskEvent()
    }


    enum class SortOrder {BY_NAME  , BY_DATE}



}
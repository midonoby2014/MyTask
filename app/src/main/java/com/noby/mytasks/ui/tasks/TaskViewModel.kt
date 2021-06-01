package com.noby.mytasks.ui.tasks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.noby.mytasks.data.Task
import com.noby.mytasks.data.TaskDao
import com.noby.mytasks.ui.ADD_Task_Result_OK
import com.noby.mytasks.ui.EDIT_Task_Result_OK
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
    private val taskDao :TaskDao ,
    @Assisted private val state :SavedStateHandle
) :ViewModel() {
   // this is searchquery without using SavedStateHandle and we will use it to save querysearch
    // if application is gone or user go another application and back to this application will found
    // the same searchQuery he left

    //val searchQuery = MutableStateFlow("")

    val searchQuery = state.getLiveData("searchQuery","")

    val sortOrder =  MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted =  MutableStateFlow(false)
   private val taskEventChannel =  Channel<TaskEvent>()
    val taskEvent = taskEventChannel.receiveAsFlow()
        /// single observiing
//    private  val taskflow =  searchQuery.flatMapLatest {
//        taskDao.getTasks(it)
//    }

    private val taskflow  = combine(searchQuery.asFlow(),sortOrder,hideCompleted){
        query,sortOrder,hideCompleted ->
        Triple(query,sortOrder,hideCompleted)
    }.flatMapLatest { (query,sortOrder,hideCompleted) ->
        taskDao.getTasks(query,sortOrder,hideCompleted)
    }
    val tasks =  taskflow.asLiveData()





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
    fun onAddNewTaskClick() =  viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToAddTaskScreen)
    }
    fun onTaskSelected(task: Task) =  viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToEditTaskScreen(task))
    }

    fun onAddEditResult(result: Int) {
         when(result){
             ADD_Task_Result_OK ->  showTaskConfirmationMessage("Task Add")

             EDIT_Task_Result_OK -> showTaskConfirmationMessage("Task Edit")

         }
    }

    private fun showTaskConfirmationMessage(Message: String) =  viewModelScope.launch {
        taskEventChannel.send(TaskEvent.ShowTaskSavedConfirmationMessage(Message))
    }


    sealed class TaskEvent{
        object NavigateToAddTaskScreen : TaskEvent()
        data class NavigateToEditTaskScreen(val  task: Task) :TaskEvent()
        data class  ShowUndoDeleteTaskMessage(val task :Task) :TaskEvent()
        data class ShowTaskSavedConfirmationMessage(val msg :String) :TaskEvent()
    }


    enum class SortOrder {BY_NAME  , BY_DATE}



}
package com.noby.mytasks.ui.edittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noby.mytasks.data.Task
import com.noby.mytasks.data.TaskDao
import com.noby.mytasks.ui.ADD_Task_Result_OK
import com.noby.mytasks.ui.EDIT_Task_Result_OK
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Created by Ahmed Noby Ahmed on 5/30/21.
 */


class AddEditTaskViewModle @ViewModelInject constructor(
    private  val taskDao: TaskDao,
    @Assisted private val  state : SavedStateHandle
)
    : ViewModel() {



    val task =  state.get<Task>("task")

    var taskname = state.get<String>("taskname") ?: task?.name ?: ""
    set(value) {
        field = value
        state.set("taskname", value)
    }
    var taskImportance = state.get<Boolean>("taskImportance ") ?: task?.imaportant ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }
     private val addEditTaskEventChannel =  Channel<AddEditTaskEvent>()
     val addEditTaskEvent  =  addEditTaskEventChannel.receiveAsFlow()


    fun onSaveClick() {
          if (taskname.isBlank()){
            showInvalidInputMessage("Name Cannot be empty")
              return
          }

        if (task !=null) {
            val updateTask =  task.copy(name = taskname ,  imaportant = taskImportance)
            updateTask(updateTask)
        }else {
            val newTask =  Task(name = taskname  , imaportant = taskImportance)
            createTask(newTask)
        }

    }

    private fun createTask(newTask: Task) =  viewModelScope.launch {
      taskDao.insert(newTask!!)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackwithResult(ADD_Task_Result_OK))
    }

    private fun updateTask(updateTask: Task)=  viewModelScope.launch {
     taskDao.update(updateTask!!)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackwithResult(EDIT_Task_Result_OK))
    }

    private fun showInvalidInputMessage (text :String) =  viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg :String) :AddEditTaskEvent()
        data class NavigateBackwithResult(val result :Int ) :AddEditTaskEvent()
    }

}
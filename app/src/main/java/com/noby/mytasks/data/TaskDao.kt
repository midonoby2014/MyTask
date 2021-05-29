package com.noby.mytasks.data

import androidx.room.*
import com.noby.mytasks.ui.tasks.TaskViewModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmed Noby Ahmed on 5/19/21.
 */

@Dao
interface TaskDao {

//    completed = 0   -> meaning in sql completed = false

    // this is explain what =  is do
//    fun getTasks(searchQuery:String ,sortOrder :TaskViewModel.SortOrder  ,hideCompleted :Boolean) : Flow<List<Task>> {
//        when(sortOrder){
//            TaskViewModel.SortOrder.BY_DATE -> {
//               return getTasksSortedByDate(searchQuery, hideCompleted)
//            }
//            TaskViewModel.SortOrder.BY_NAME -> {
//                return  getTasksSortedByName(searchQuery, hideCompleted)
//            }
//        }
//
//    }

    fun getTasks(searchQuery:String ,sortOrder :TaskViewModel.SortOrder  ,hideCompleted :Boolean) : Flow<List<Task>> =
        when(sortOrder){
            TaskViewModel.SortOrder.BY_DATE -> getTasksSortedByDate(searchQuery, hideCompleted)
            TaskViewModel.SortOrder.BY_NAME -> getTasksSortedByName(searchQuery, hideCompleted)

        }




    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0 ) AND name LIKE '%'|| :searchQuery || '%' ORDER BY imaportant DESC,name ")
    fun getTasksSortedByName(searchQuery:String ,  hideCompleted :Boolean) : Flow<List<Task>>


    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0 ) AND name LIKE '%'|| :searchQuery || '%' ORDER BY imaportant DESC,created ")
    fun getTasksSortedByDate(searchQuery:String,  hideCompleted :Boolean) : Flow<List<Task>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

}
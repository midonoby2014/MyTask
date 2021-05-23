package com.noby.mytasks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmed Noby Ahmed on 5/19/21.
 */

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table WHERE name LIKE '%'|| :searchQuery || '%' ORDER BY imaportant DESC ")
    fun getTasks(searchQuery:String) : Flow<List<Task>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

}
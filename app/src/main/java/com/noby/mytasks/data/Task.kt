package com.noby.mytasks.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

/**
 * Created by Ahmed Noby Ahmed on 5/19/21.
 */

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    val name: String,
    val imaportant: Boolean = false,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int  = 0

) : Parcelable {

    val createdDateFormatted :String
        get() = DateFormat.getDateTimeInstance().format(created)

}
package com.noby.mytasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noby.mytasks.adapter.TaskAdapter.*
import com.noby.mytasks.data.Task
import com.noby.mytasks.databinding.ItemTaskBinding

/**
 * Created by Ahmed Noby Ahmed on 5/21/21.
 */
class TaskAdapter   : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =  ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem  =  getItem(position)
        holder.bind(currentItem)
    }


    class  TaskViewHolder (private  val binding :ItemTaskBinding) :RecyclerView.ViewHolder(binding.root){
      fun bind (task :Task){
          binding.apply {
              cbCompleted.isChecked =  task.completed
              txtName.text =  task.name
              txtName.paint.isStrikeThruText =  task.completed
              labelPriority.isVisible = task.imaportant

          }

      }
    }
    class DiffCallback : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task)=
           oldItem.id ==  newItem.id


        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem


    }


}
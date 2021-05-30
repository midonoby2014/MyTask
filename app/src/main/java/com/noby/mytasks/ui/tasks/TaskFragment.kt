package com.noby.mytasks.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.noby.mytasks.R
import com.noby.mytasks.adapter.TaskAdapter
import com.noby.mytasks.data.Task
import com.noby.mytasks.databinding.FragmentTasksBinding
import com.noby.mytasks.utils.OnQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.coroutines.flow.collect

/**
 * Created by Ahmed Noby Ahmed on 5/19/21.
 */

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_tasks), TaskAdapter.onItemClickListener {

    private val viewModel: TaskViewModel by viewModels()
    lateinit var taskApater: TaskAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTasksBinding.bind(view)
        taskApater = TaskAdapter(this)

        binding.apply {
            recViewTasks.apply {
                adapter = taskApater
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

        }
        viewModel.tasks.observe(viewLifecycleOwner) {
            taskApater.submitList(it)
        }
        SwipeRecycle()
        ShowMessageUndo()
        setHasOptionsMenu(true)

    }

    fun ShowMessageUndo() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.taskEvent.collect { event ->
                when (event){
                 is TaskViewModel.TaskEvent.ShowUndoDeleteTaskMessage ->{
                  Snackbar.make(requireView(),"Task Deleted",Snackbar.LENGTH_LONG)
                      .setAction("UNDO") {
                       viewModel.onUndoDeleteClick(event.task)
                      }.show()
                 }
                }
            }
        }
    }

    fun SwipeRecycle() {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = taskApater.currentList[viewHolder.adapterPosition]
                viewModel.onTaskSwipe(task)
            }
        }).attachToRecyclerView(rec_view_tasks)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_fragment_task, menu)
        val searchItem = menu.findItem(R.id.action_seatch)
        val searchView = searchItem.actionView as SearchView

        searchView.OnQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.sortOrder.value = TaskViewModel.SortOrder.BY_NAME
                true
            }
            R.id.action_sort_by_Created -> {

                viewModel.sortOrder.value = TaskViewModel.SortOrder.BY_DATE
                true
            }
            R.id.action_seatch -> {

                true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.hideCompleted.value = item.isChecked
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckboxClick(task: Task, isChecked: Boolean) {
        viewModel.onTasKChekedChanged(task, isChecked)
    }


}
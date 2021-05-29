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
import androidx.recyclerview.widget.LinearLayoutManager
import com.noby.mytasks.R
import com.noby.mytasks.adapter.TaskAdapter
import com.noby.mytasks.databinding.FragmentTasksBinding
import com.noby.mytasks.utils.OnQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Ahmed Noby Ahmed on 5/19/21.
 */

@AndroidEntryPoint
class TaskFragment: Fragment(R.layout.fragment_tasks) {

    private  val viewModel : TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding  = FragmentTasksBinding.bind(view)
        val taskApater =  TaskAdapter()

        binding.apply {
            recViewTasks.apply {
                adapter =  taskApater
                layoutManager =  LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

        }
        viewModel.tasks.observe(viewLifecycleOwner){
            taskApater.submitList(it)
        }
   setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_fragment_task , menu)
        val searchItem  = menu.findItem(R.id.action_seatch)
        val searchView = searchItem.actionView as SearchView

        searchView.OnQueryTextChanged {
           viewModel.searchQuery.value =  it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when (item.itemId){
          R.id.action_sort_by_name ->{
             viewModel.sortOrder.value =  TaskViewModel.SortOrder.BY_NAME
              true
          }
          R.id.action_sort_by_Created ->{

              viewModel.sortOrder.value =  TaskViewModel.SortOrder.BY_DATE
              true
          }
          R.id.action_seatch ->{

              true
          }
          R.id.action_hide_completed_tasks ->{
              item.isChecked=  !item.isChecked
              viewModel.hideCompleted.value  = item.isChecked
              true
          }

          else -> super.onOptionsItemSelected(item)
        }
    }


}
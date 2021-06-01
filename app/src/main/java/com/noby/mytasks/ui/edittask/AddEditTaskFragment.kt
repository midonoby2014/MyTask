package com.noby.mytasks.ui.edittask

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.noby.mytasks.R
import com.noby.mytasks.databinding.FragmentAddEditTaskBinding
import com.noby.mytasks.utils.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_edit_task.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.item_task.*
import kotlinx.coroutines.flow.collect

/**
 * Created by Ahmed Noby Ahmed on 5/30/21.
 */
@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {


    private val viewModle: AddEditTaskViewModle by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding =  FragmentAddEditTaskBinding.bind(view)

        binding.apply {

            etTask.setText(viewModle.taskname)
            chImportant.isChecked =  viewModle.taskImportance
            chImportant.jumpDrawablesToCurrentState()
            txtcreated.isVisible =  viewModle.task  !=null
            txtcreated.text =  "Created : ${viewModle.task?.createdDateFormatted}"

             etTask.addTextChangedListener {
              viewModle.taskname =  it.toString()
             }
             chImportant.setOnCheckedChangeListener { _, isCheked ->
                 viewModle.taskImportance =  isCheked
             }
            fb_save_task.setOnClickListener {
                viewModle.onSaveClick()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModle.addEditTaskEvent.collect {   event ->
                when (event){
                    is AddEditTaskViewModle.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView() , event.msg ,Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditTaskViewModle.AddEditTaskEvent.NavigateBackwithResult -> {
                        binding.etTask.clearFocus()
                        setFragmentResult(
                            "add_edit_task" ,
                            bundleOf("add_edit_result" to event.result)

                        )
                        findNavController().popBackStack()
                    }
                    }

                }.exhaustive

            }

        }


    }


package com.example.todoapp.fragments.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.FragmentUpdateBinding
import com.example.todoapp.viewmodel.SharedViewModel
import com.example.todoapp.viewmodel.TodoViewModel

class UpdateFragment : Fragment() {
    //binding
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    //other
    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: TodoViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set menu
        setHasOptionsMenu(true)
        //init view
        binding.apply {
            currentTitleEt.setText(args.currentItem.title)
            currentDescriptionEt.setText(args.currentItem.description)
            currentPrioritiesSpinner.setSelection(mSharedViewModel.parsePriority(args.currentItem.priority))
            currentPrioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            updateItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val title = binding.currentTitleEt.text.toString()
        val description = binding.currentDescriptionEt.text.toString()
        val priority = binding.currentPrioritiesSpinner.selectedItem.toString()
        val validation = mSharedViewModel.verifyDataFromUser(title, description)
        if (validation) {
            //Update current item
            val updatedItem = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(priority),
                description
            )
            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_SHORT).show()
            //Navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
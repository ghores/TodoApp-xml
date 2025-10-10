package com.example.todoapp.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.fragments.list.adapter.ListAdapter
import com.example.todoapp.viewmodel.SharedViewModel
import com.example.todoapp.viewmodel.TodoViewModel
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment() {
    //binding
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    //other
    private val listAdapter: ListAdapter by lazy { ListAdapter() }
    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        //Set menu
        setHasOptionsMenu(true)
        //init adapter
        binding.apply {
            //setup recyclerview
            recyclerView.adapter = listAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.itemAnimator = LandingAnimator().apply {
                addDuration = 300
            }
            //swipe to delete
            swipeToDelete(recyclerView)
            //get all data from database
            mTodoViewModel.getAllData.observe(viewLifecycleOwner) { data ->
                mSharedViewModel.checkIfDatabaseEmpty(data)
                listAdapter.setData(data)
            }
            //observe empty database
            mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner) {
                showEmptyDatabaseViews(it)
            }
        }
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        } else {
            binding.noDataImageView.visibility = View.INVISIBLE
            binding.noDataTextView.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            confirmRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    //Show AlertDialog to confirm removal of all items from database table
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mTodoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed Everything!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = listAdapter.dataList[viewHolder.adapterPosition]
                //Delete Item
                mTodoViewModel.deleteItem(deletedItem)
                listAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                Toast.makeText(requireContext(), "Successfully Removed: '${deletedItem.title}'", Toast.LENGTH_SHORT).show()
                //Restore Deleted Item
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            mTodoViewModel.insertData(deletedItem)
          //  listAdapter.notifyItemChanged(position)
        }
        snackBar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
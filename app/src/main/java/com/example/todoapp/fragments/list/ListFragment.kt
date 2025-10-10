package com.example.todoapp.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.viewmodel.TodoViewModel

class ListFragment : Fragment() {
    //binding
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    //other
    private val listAdapter: ListAdapter by lazy { ListAdapter() }
    private val mTodoViewModel: TodoViewModel by viewModels()

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
            recyclerView.adapter = listAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            mTodoViewModel.getAllData.observe(viewLifecycleOwner) { data ->
                listAdapter.setData(data)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
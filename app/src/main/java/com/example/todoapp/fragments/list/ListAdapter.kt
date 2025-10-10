package com.example.todoapp.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    //binding
    private lateinit var binding: RowLayoutBinding

    private var dataList = emptyList<ToDoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        binding.titleTxt.text = dataList[position].title
        binding.descriptionTxt.text = dataList[position].description
        when (dataList[position].priority) {
            Priority.HIGH -> binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.red
                )
            )

            Priority.MEDIUM -> binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.yellow
                )
            )

            Priority.LOW -> binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.green
                )
            )
        }
    }

    inner class MyViewHolder() : RecyclerView.ViewHolder(binding.root) {
    }

    fun setData(toDoData: List<ToDoData>) {
        this.dataList = toDoData
        notifyDataSetChanged()
    }
}
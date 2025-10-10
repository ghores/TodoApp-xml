package com.example.todoapp.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.RowLayoutBinding
import com.example.todoapp.fragments.list.ListFragmentDirections

 class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<ToDoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

     override fun getItemCount(): Int {
         return dataList.size
     }

     override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.binding.titleTxt.text = currentItem.title
        holder.binding.descriptionTxt.text = currentItem.description

        val colorRes = when (currentItem.priority) {
            Priority.HIGH -> R.color.red
            Priority.MEDIUM -> R.color.yellow
            Priority.LOW -> R.color.green
        }

        holder.binding.priorityIndicator.setCardBackgroundColor(
            ContextCompat.getColor(holder.binding.root.context, colorRes)
        )

        holder.binding.rowBackground.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

     inner class MyViewHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root)


     fun setData(toDoData: List<ToDoData>) {
        val diffUtil = ToDoDiffUtil(dataList, toDoData)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        dataList = toDoData
        diffResult.dispatchUpdatesTo(this)
    }
}
package com.example.anch_kotiln.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anch_kotiln.*
import com.example.anch_kotiln.Model.DTO.ModelDTO
import kotlinx.android.synthetic.main.item_category.view.*

class ItemRecyclerAdapter(val context: Context, list: ArrayList<ModelDTO>) :
    RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder>(), Filterable {
    private val rawData: ArrayList<ModelDTO> = list
    private var filteredData: ArrayList<ModelDTO> = rawData
    private var filter: ListFilter = ListFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    fun getItemSize(): Int {
        var size = 0
        filteredData.forEach {
            size += it.mData.size
        }
        return size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemCategoryTextView.text = filteredData[position].title
        holder.itemListRecyclerView.layoutManager = GridLayoutManager(context, 5)
        holder.itemListRecyclerView.adapter = CategoryRecyclerAdapter(context, filteredData[position].mData)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemCategoryTextView: TextView = itemView.itemCategoryTextView
        val itemListRecyclerView: RecyclerView = itemView.itemCategoryRecyclerView
    }

    override fun getFilter(): ListFilter {
        return filter
    }

    inner class ListFilter() : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var result: FilterResults = FilterResults()
            var filterList: ArrayList<ModelDTO> = ArrayList()

            if (constraint == null || constraint.isEmpty()) {
                result.values = rawData;
                result.count = rawData.size;
            } else {
                rawData.forEach {
                    var filterDataObject = it.filter(constraint)
                    if(filterDataObject.size > 0) {
                        filterList.add(ModelDTO(filterDataObject))
                    }
                }
                result.values = filterList
                result.count = filterList.size
            }
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredData = results?.values as ArrayList<ModelDTO>
            notifyDataSetChanged()
        }
    }
}
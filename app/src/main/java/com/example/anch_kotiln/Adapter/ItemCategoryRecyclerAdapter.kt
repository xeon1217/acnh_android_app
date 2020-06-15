package com.example.anch_kotiln.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anch_kotiln.*
import com.example.anch_kotiln.Model.DTO.ArtDTO
import com.example.anch_kotiln.Model.DTO.ModelDTO
import com.example.anch_kotiln.Model.DTO.ObjectDTO
import kotlinx.android.synthetic.main.item_category.view.*

class ItemCategoryRecyclerAdapter(val context: Context, val rawData: ArrayList<ModelDTO>, private val spanCount: Int) :
    RecyclerView.Adapter<ItemCategoryRecyclerAdapter.ViewHolder>(), Filterable {
    private var filteredData: ArrayList<ModelDTO> = rawData
    private var filter: ListFilter = ListFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getItemSize(): Int {
        var size = 0
        filteredData.forEach {
            size += it.mData.size
        }
        return size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (filteredData[position].mData[0].type == ObjectDTO.Type.ART) {
            holder.itemCategoryTextView.text = filteredData[position].title
            holder.itemListRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
            holder.itemListRecyclerView.adapter = ArtRecyclerAdapter(context, filteredData[position].mData as ArrayList<ArtDTO>)
        } else {
            holder.itemCategoryTextView.text = filteredData[position].title
            holder.itemListRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
            holder.itemListRecyclerView.adapter = ItemObjectRecyclerAdapter(context, filteredData[position].mData)
        }
    }

    override fun getFilter(): ListFilter {
        return filter
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemCategoryTextView: TextView = itemView.itemCategoryTextView
        val itemListRecyclerView: RecyclerView = itemView.itemCategoryRecyclerView
    }

    inner class ListFilter() : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var result = FilterResults()
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
package com.example.anch_kotiln.Adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anch_kotiln.*
import com.example.anch_kotiln.Utility.Common
import com.example.anch_kotiln.Model.DTO.ReactionDTO
import com.example.anch_kotiln.Utility.IO
import kotlinx.android.synthetic.main.item_reaction.view.*

class ReactionRecyclerAdapter(val context: Context, list: ArrayList<ReactionDTO>) :
    RecyclerView.Adapter<ReactionRecyclerAdapter.ViewHolder>(), Filterable {
    private val rawData: ArrayList<ReactionDTO> = list
    private var filteredData: ArrayList<ReactionDTO> = rawData
    private var filter: ListFilter = ListFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reaction, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load("${IO.file.path}/${filteredData[position].imageIconResource}").into(holder.imageView)
        holder.nameValueTextView.text = filteredData[position].name
        holder.sourceValueTextView.text = "\"${filteredData[position].source}\" 성격을 가진 동물이 가르쳐 줌"
        if(filteredData[position].sourceNote.isNotEmpty()) {
            holder.sourceNoteValueTextView.visibility = View.VISIBLE
            holder.sourceNoteValueTextView.text = "단, ${filteredData[position].sourceNote}"
        }
    }

    override fun getFilter(): ListFilter {
        return filter
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.reactionImageView
        val nameValueTextView: TextView = itemView.reactionNameTextView
        val sourceValueTextView: TextView = itemView.reactionSourceTextView
        val sourceNoteValueTextView: TextView = itemView.reactionSourceNoteTextView
    }

    inner class ListFilter() : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var result: FilterResults = FilterResults()
            var filterList: ArrayList<ReactionDTO> = ArrayList()

            if (constraint == null || constraint.isEmpty()) {
                result.values = rawData;
                result.count = rawData.size;
            } else {
                rawData.forEach {
                    if(Common.soundSearcher.matchString(it.name, constraint.toString()) || it.name.toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filterList.add(it)
                    }
                }
                result.values = filterList
                result.count = filterList.size
            }
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredData = results?.values as ArrayList<ReactionDTO>
            notifyDataSetChanged()
        }
    }
}
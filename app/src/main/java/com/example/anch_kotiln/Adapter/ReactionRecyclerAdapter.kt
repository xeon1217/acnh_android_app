package com.example.anch_kotiln.Adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
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
        holder.reactionImageView.setImageBitmap(BitmapFactory.decodeFile("${IO.file.path}/${filteredData[position].imageIconResource}"))
        holder.reactionNameValueTextView.text = filteredData[position].name
        holder.reactionSoruceValueTextView.text = "\"${filteredData[position].source}\" 성격을 가진 동물이 가르쳐 줌"
        if(filteredData[position].sourceNote.length > 0) {
            holder.reactionSourceNoteValueTextView.isVisible = true
            holder.reactionSourceNoteValueTextView.text = "단, ${filteredData[position].sourceNote}"
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reactionImageView = itemView.reactionImageView
        val reactionNameValueTextView = itemView.reactionNameTextView
        val reactionSoruceValueTextView = itemView.reactionSourceTextView
        val reactionSourceNoteValueTextView = itemView.reactionSourceNoteTextView
    }

    override fun getFilter(): ListFilter {
        return filter
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
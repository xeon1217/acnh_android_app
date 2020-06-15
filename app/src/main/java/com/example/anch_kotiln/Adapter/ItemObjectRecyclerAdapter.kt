package com.example.anch_kotiln.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.anch_kotiln.Activity.CreatureBook.CreatureBookFishPopupActivity
import com.example.anch_kotiln.Activity.CreatureBook.CreatureBookInsectPopupActivity
import com.example.anch_kotiln.Model.DTO.ObjectDTO
import com.example.anch_kotiln.R
import com.example.anch_kotiln.Activity.VillagerList.VillagerListPopupActivity
import com.example.anch_kotiln.Utility.IO
import kotlinx.android.synthetic.main.item_object.view.*

class ItemObjectRecyclerAdapter(val context: Context, rawData: ArrayList<ObjectDTO>) :
    RecyclerView.Adapter<ItemObjectRecyclerAdapter.ViewHolder>() {
    private var filteredData: ArrayList<ObjectDTO> = rawData

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_object, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemImageView.setImageBitmap(BitmapFactory.decodeFile("${IO.file.path}/${filteredData[position].imageIconResource}"))
        holder.itemNameTextView.text = filteredData[position].name
        holder.itemView.setOnClickListener {
            when (filteredData[position].type) {
                ObjectDTO.Type.VILLAGER -> context.startActivity(Intent(context, VillagerListPopupActivity::class.java).putExtra("item", filteredData[position]))
                ObjectDTO.Type.INSECT -> context.startActivity(Intent(context, CreatureBookInsectPopupActivity::class.java).putExtra("item", filteredData[position]))
                ObjectDTO.Type.FISH -> context.startActivity(Intent(context, CreatureBookFishPopupActivity::class.java).putExtra("item", filteredData[position]))
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImageView: ImageView = itemView.itemImageView
        val itemNameTextView: TextView = itemView.itemNameTextView
    }
}
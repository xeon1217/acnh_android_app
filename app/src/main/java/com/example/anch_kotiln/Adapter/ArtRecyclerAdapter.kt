package com.example.anch_kotiln.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.anch_kotiln.Model.DTO.ArtDTO
import com.example.anch_kotiln.R
import com.example.anch_kotiln.Utility.IO
import kotlinx.android.synthetic.main.item_art.view.*

class ArtRecyclerAdapter(val context: Context, rawData: ArrayList<ArtDTO>) :
    RecyclerView.Adapter<ArtRecyclerAdapter.ViewHolder>() {
    private var filteredData: ArrayList<ArtDTO> = rawData

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_art, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = filteredData[position].name
        holder.realArtworkNameTextView.text = filteredData[position].realArtworkName
        holder.artistTextView.text = filteredData[position].artist
        holder.genuineCardView.visibility = View.VISIBLE
        holder.genuineImageView.setImageBitmap(BitmapFactory.decodeFile("${IO.file.path}/${filteredData[position].imageGenuineResource}"))
        if(filteredData[position].existFake) {
            holder.fakeCardView.visibility = View.VISIBLE
            holder.fakeImageView.setImageBitmap(BitmapFactory.decodeFile("${IO.file.path}/${filteredData[position].imageFakeResource}"))
        } else {
            holder.fakeCardView.visibility = View.GONE
        }
        holder.descriptionTextView.text = filteredData[position].description
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.artBookNameTextView
        val realArtworkNameTextView: TextView = itemView.artBookRealArtworkNameTextView
        val artistTextView: TextView = itemView.artBookArtistTextView
        val genuineCardView: CardView = itemView.artBookGenuineCardView
        val genuineImageView: ImageView = itemView.artBookGenuineImageView
        val fakeCardView: CardView = itemView.artBookFakeCardView
        val fakeImageView: ImageView = itemView.artBookFakeImageView
        val descriptionTextView: TextView = itemView.artBookDescriptionTextView
    }
}
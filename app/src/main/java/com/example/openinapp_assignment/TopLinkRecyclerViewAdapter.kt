package com.example.openinapp_assignment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.openinapp_assignment.model.TopLink

class TopLinkRecyclerViewAdapter(private val linksList: List<TopLink>) : RecyclerView.Adapter<TopLinkRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.link_list_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return linksList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = linksList[position]

        Glide.with(holder.itemView.context)
            .load(currentItem.original_image)
            .apply(RequestOptions().centerCrop())
            .into(holder.ivLink)

        holder.tvLinkName.text = currentItem.app
        holder.tvLinkDate.text = currentItem.times_ago
        holder.tvLinkClickCount.text = currentItem.total_clicks.toString()
        holder.tvLink.text = currentItem.smart_link
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivLink : ImageView = itemView.findViewById(R.id.ivLink)
        val tvLinkName : TextView = itemView.findViewById(R.id.tvLinkName)
        val tvLinkDate : TextView = itemView.findViewById(R.id.tvLinkDate)
        val tvLinkClickCount : TextView = itemView.findViewById(R.id.tvLinkClickCount)
        val tvLink : TextView = itemView.findViewById(R.id.tvLink)
        private val imgBtnCopy: ImageButton = itemView.findViewById(R.id.imgBtnCopy)

        init {
            imgBtnCopy.setOnClickListener {
                val position = adapterPosition
                val textToCopy = tvLink.text

                val clipboardManager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Copied Text", textToCopy)
                clipboardManager.setPrimaryClip(clipData)

                Toast.makeText(itemView.context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
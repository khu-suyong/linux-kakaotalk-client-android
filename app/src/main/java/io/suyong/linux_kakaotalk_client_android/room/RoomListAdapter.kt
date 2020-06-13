package io.suyong.linux_kakaotalk_client_android.room

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.suyong.linux_kakaotalk_client_android.R
import kotlinx.android.synthetic.main.room_item.view.*

class RoomListAdapter(val context: Context) : RecyclerView.Adapter<RoomListViewHolder>() {
    var list = mutableListOf<Room>()

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomListViewHolder {
        val ctx = parent.context
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.room_item, parent, false)

        return RoomListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RoomListViewHolder, position: Int) {
        val it = list[position]

        holder.title.text = it.title
        holder.caption.text = it.caption
        holder.badge.text = it.notRead.toString()

        try {
            Glide.with(context).load(Uri.parse(it.url)).into(holder.image)
        } catch (err: Exception) { }
    }
}

class RoomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.room_item_image
    val title: TextView = itemView.room_item_title
    val caption: TextView = itemView.room_item_caption
    val badge: TextView = itemView.room_item_badge
}
package io.suyong.linux_kakaotalk_client_android.room

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.suyong.linux_kakaotalk_client_android.ChatActivity
import io.suyong.linux_kakaotalk_client_android.R
import io.suyong.linux_kakaotalk_client_android.RoomActivity
import kotlinx.android.synthetic.main.room_item.view.*

class RoomListAdapter(val activity: Activity) : RecyclerView.Adapter<RoomListViewHolder>() {
    var list = mutableListOf<Room>()

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomListViewHolder {
        val ctx = parent.context
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.room_item, parent, false)

        view.setOnClickListener {
            ChatActivity.room = it.room_item_title.text.toString()

            val intent = Intent(activity, ChatActivity::class.java)
            activity.startActivity(intent)
        }

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
            Glide.with(activity).load(Uri.parse(it.url)).into(holder.image)
        } catch (err: Exception) { }
    }
}

class RoomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.room_item_image
    val title: TextView = itemView.room_item_title
    val caption: TextView = itemView.room_item_caption
    val badge: TextView = itemView.room_item_badge
}
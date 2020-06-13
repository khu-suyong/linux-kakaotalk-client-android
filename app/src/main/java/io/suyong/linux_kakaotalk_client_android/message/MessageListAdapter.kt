package io.suyong.linux_kakaotalk_client_android.message

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
import kotlinx.android.synthetic.main.chat_mine_item.view.*
import kotlinx.android.synthetic.main.chat_other_item.view.*
import kotlinx.android.synthetic.main.room_item.view.*

class MessageListAdapter(val context: Context) : RecyclerView.Adapter<MessageListViewHolder>() {
    var list = mutableListOf<Message>()

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListViewHolder {
        val ctx = parent.context
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.room_item, parent, false)

        return MessageListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageListViewHolder, position: Int) {
        val it = list[position]

        holder.title.text = it.title
        holder.caption.text = it.caption
        holder.badge.text = it.notRead.toString()

        try {
            Glide.with(context).load(Uri.parse(it.url)).into(holder.image)
        } catch (err: Exception) { }
    }

    enum class Type {
        MINE,
        OTHER
    }
}

class MessageListViewHolder(itemView: View, type: MessageListAdapter.Type) : RecyclerView.ViewHolder(itemView) {
    val sender: TextView = if (type == MessageListAdapter.Type.MINE) itemView.chat_mine_item_sender else itemView.chat_other_item_sender
    val content: TextView = if (type == MessageListAdapter.Type.MINE) itemView.chat_mine_item_content else itemView.chat_other_item_content
}
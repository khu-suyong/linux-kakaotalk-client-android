package io.suyong.linux_kakaotalk_client_android.message

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.suyong.linux_kakaotalk_client_android.ChatActivity
import io.suyong.linux_kakaotalk_client_android.R
import kotlinx.android.synthetic.main.chat_mine_item.view.*
import kotlinx.android.synthetic.main.chat_other_item.view.*


class MessageListAdapter(val context: Context) : RecyclerView.Adapter<MessageListViewHolder>() {
    companion object {
        val TYPE_MINE = 0
        val TYPE_OTHER = 1
        val TYPE_FOOTER = 2
    }

    var list = mutableListOf<Message>()

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListViewHolder {
        val ctx = parent.context
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = when(viewType) {
            TYPE_MINE -> inflater.inflate(R.layout.chat_mine_item, parent, false)
            TYPE_OTHER -> inflater.inflate(R.layout.chat_other_item, parent, false)
            else -> {
                val view = View(context)
                view.layoutParams = ViewGroup.LayoutParams(
                    10,
                    (56 * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
                )

                view
            }
        }

        return MessageListViewHolder(view, viewType)
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun onBindViewHolder(holder: MessageListViewHolder, position: Int) {
        if (position != list.size) {
            val it = list[position]

            holder.sender?.text = it.sender
            holder.content?.text = it.text
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == list.size) return TYPE_FOOTER
        else return when(list[position].sender) {
            ChatActivity.sender -> TYPE_MINE
            else -> TYPE_OTHER
        }
    }
}

class MessageListViewHolder(itemView: View, type: Int) : RecyclerView.ViewHolder(itemView) {
    var sender: TextView? = null
    var content: TextView? = null

    init {
        if (type != MessageListAdapter.TYPE_FOOTER) {
            sender = if (type == MessageListAdapter.TYPE_MINE) itemView.chat_mine_item_sender else itemView.chat_other_item_sender
            content = if (type == MessageListAdapter.TYPE_MINE) itemView.chat_mine_item_content else itemView.chat_other_item_content
        }
    }
}
package io.suyong.linux_kakaotalk_client_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.suyong.linux_kakaotalk_client_android.message.Message
import io.suyong.linux_kakaotalk_client_android.message.MessageListAdapter
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_ROOM = 0
        var sender = "HI"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val adapter = MessageListAdapter(this)
        chat_list.adapter = adapter
        chat_list.layoutManager = LinearLayoutManager(this)

        chat_send.setOnClickListener {
            adapter.list.add(Message(if (Math.random() > 0.5) Math.random().toString() else "HI", "Test Text", Date()))

            adapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when(resultCode) {
            REQUEST_ROOM -> {
                intent?.let {
                    supportActionBar?.title = it.extras?.get("room") as String
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }
}
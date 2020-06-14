package io.suyong.linux_kakaotalk_client_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.suyong.linux_kakaotalk_client_android.message.Message
import io.suyong.linux_kakaotalk_client_android.message.MessageListAdapter
import io.suyong.linux_kakaotalk_client_android.network.NetworkManager
import io.suyong.linux_kakaotalk_client_android.room.Room
import kotlinx.android.synthetic.main.activity_chat.*
import java.time.LocalDate
import java.util.*

class ChatActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_ROOM = 0
        var sender = "HI"
    }

    var room = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val adapter = MessageListAdapter(this)
        chat_list.adapter = adapter
        chat_list.layoutManager = LinearLayoutManager(this)

        chat_send.setOnClickListener {
            NetworkManager.emit(
                "send",
                mapOf(
                    "room" to room,
                    "text" to chat_edit.text.toString()
                )
            )
        }

        NetworkManager.on("message") {
            val sender = it.get("sender").toString()
            val text = it.get("sender").toString()
            val time = it.get("date").toString()

            runOnUiThread {
                adapter.list.add(Message(sender, text, time))
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when(resultCode) {
            REQUEST_ROOM -> {
                intent?.let {
                    room = it.extras?.get("room") as String
                    supportActionBar?.title = room
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }
}
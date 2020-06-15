package io.suyong.linux_kakaotalk_client_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.text.set
import androidx.recyclerview.widget.LinearLayoutManager
import io.suyong.linux_kakaotalk_client_android.message.Message
import io.suyong.linux_kakaotalk_client_android.message.MessageListAdapter
import io.suyong.linux_kakaotalk_client_android.network.NetworkManager
import io.suyong.linux_kakaotalk_client_android.room.Room
import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.util.*

class ChatActivity : AppCompatActivity() {
    companion object {
        var room = ""
        var sender = "HI"
    }

    private var adapter = MessageListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.title = room

        Log.d("change", "plz")

        val json = JSONObject(FileManager.read("${RoomActivity.target_uuid}-messages.json"))
        try {
            val array = json.get(room) as JSONArray

            for (i in 0 until array.length()) {
                val element = array.get(i) as JSONObject

                adapter.list.add(Message(element.get("sender").toString(), element.get("text").toString(), ""))
            }
        } catch (error: Exception) {}

        chat_list.adapter = adapter
        chat_list.layoutManager = LinearLayoutManager(this)

        chat_send.setOnClickListener {
            val text = chat_edit.text.toString()

            NetworkManager.emit(
                "send",
                mapOf(
                    "room" to room,
                    "text" to text
                )
            )

            val params = JSONObject()
            params.put("sender", sender)
            params.put("text", text)

            FileManager.save("${RoomActivity.target_uuid}-messages.json", room, params)
            FileManager.save("${RoomActivity.target_uuid}-rooms.json", room, text, true)

            adapter.list.add(Message(sender, text, ""))
            chat_edit.setText("")

            adapter.notifyDataSetChanged()
        }

        adapter.notifyDataSetChanged()

        NetworkManager.on("message") {
            val sender = it.get("sender").toString()
            val text = it.get("text").toString()
            val time = it.get("date").toString()
            val room = it.get("room").toString()

            runOnUiThread {
                adapter.list.add(Message(sender, text, time))
                adapter.notifyDataSetChanged()

                val params = JSONObject()
                params.put("sender", sender)
                params.put("text", text)

                FileManager.save("${RoomActivity.target_uuid}-messages.json", room, params)
            }
        }
    }
}
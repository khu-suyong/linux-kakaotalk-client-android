package io.suyong.linux_kakaotalk_client_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.suyong.linux_kakaotalk_client_android.network.NetworkManager
import io.suyong.linux_kakaotalk_client_android.room.Room
import io.suyong.linux_kakaotalk_client_android.room.RoomListAdapter
import kotlinx.android.synthetic.main.activity_room.*
import org.json.JSONObject

class RoomActivity : AppCompatActivity() {
    companion object {
        var target_uuid: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val adapter = RoomListAdapter(this)
        room_list.adapter = adapter
        room_list.layoutManager = LinearLayoutManager(this)
        room_list.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        try {
            val json = JSONObject(FileManager.read("$target_uuid-rooms.json"))
            json.keys().forEach {
                adapter.list.add(Room("nothing", it.toString(), json.get(it.toString()).toString(), 0))
            }

            adapter.notifyDataSetChanged()
        } catch (error: Exception) {
            FileManager.create("$target_uuid-rooms.json")
        }

        fab_sync.setOnClickListener {
            NetworkManager.emit("room")
            adapter.list.clear()

            try {
                val json = JSONObject(FileManager.read("$target_uuid-rooms.json"))
                Log.d("sync", "json $json")
                json.keys().forEach {
                    adapter.list.add(Room("nothing", it.toString(), json.get(it.toString()).toString(), 0))
                    Log.d("sync", it.toString() + " " + json.get(it.toString()).toString())
                }

                adapter.notifyDataSetChanged()
            } catch (error: Exception) {
                FileManager.create("$target_uuid-rooms.json")
            }
        }

        NetworkManager.emit("room")
        NetworkManager.on("room") {
            val title = it.get("title").toString()
            val caption = it.get("caption").toString()
            Log.d("room", "change")

            runOnUiThread {
                if (FileManager.read("$target_uuid-rooms.json", title).isEmpty()) {
                    adapter.list.add(Room("nothing", title, caption, 0))
                    adapter.notifyDataSetChanged()

                    FileManager.save("$target_uuid-rooms.json", title, caption, true)
                }
            }
        }

        NetworkManager.on("message") {
            Log.d("message", "change")
            runOnUiThread {
                val room = it.get("room").toString()
                val text = it.get("text").toString()

                adapter.list.forEachIndexed {i, r ->
                    if (r.title == room) {
                        adapter.list[i].caption = text

                        FileManager.save("$target_uuid-rooms.json", room, text, true)
                    }
                }

                adapter.notifyDataSetChanged()
            }
        }
    }
}
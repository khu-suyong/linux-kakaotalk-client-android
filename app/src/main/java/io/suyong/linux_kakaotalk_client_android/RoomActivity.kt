package io.suyong.linux_kakaotalk_client_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            val json = JSONObject(FileManager.read("room.json"))
            json.keys().forEach {
                adapter.list.add(Room("nothing", it.toString(), json.get(it.toString()).toString(), 0))
            }

            adapter.notifyDataSetChanged()
        } catch (error: Exception) {
            FileManager.create("room.json")
        }

        NetworkManager.on("room") {
            runOnUiThread {
                adapter.list.add(Room("nothing", it.get("title").toString(), it.get("caption").toString(), 0))
                adapter.notifyDataSetChanged()
            }
        }

        NetworkManager.on("message") {
            runOnUiThread {
                val room = it.get("room").toString()
                val text = it.get("text").toString()

                adapter.list.forEachIndexed {i, r ->
                    if (r.title == room) {
                        adapter.list[i].caption = text

                        FileManager.save("room.json", room, text, true)
                    }
                }

                adapter.notifyDataSetChanged()
            }
        }
    }
}
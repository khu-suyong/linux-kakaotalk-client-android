package io.suyong.linux_kakaotalk_client_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.suyong.linux_kakaotalk_client_android.network.NetworkManager
import io.suyong.linux_kakaotalk_client_android.room.Room
import io.suyong.linux_kakaotalk_client_android.room.RoomListAdapter
import kotlinx.android.synthetic.main.activity_room.*

class RoomActivity : AppCompatActivity() {
    companion object {
        var target_uuid: String = ""
    }
    private val adapter = RoomListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        room_list.adapter = adapter
        room_list.layoutManager = LinearLayoutManager(this)
        room_list.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        NetworkManager.on("room") {
            runOnUiThread {
                adapter.list.add(Room("nothing", it.get("title").toString(), "", 0))
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
                    }
                }

                adapter.notifyDataSetChanged()
            }
        }
    }
}
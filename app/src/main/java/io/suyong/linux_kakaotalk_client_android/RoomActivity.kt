package io.suyong.linux_kakaotalk_client_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.suyong.linux_kakaotalk_client_android.room.Room
import io.suyong.linux_kakaotalk_client_android.room.RoomListAdapter
import kotlinx.android.synthetic.main.activity_room.*

class RoomActivity : AppCompatActivity() {
    private val adapter = RoomListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        room_list.adapter = adapter
        room_list.layoutManager = LinearLayoutManager(this)
        room_list.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        fab_connect.setOnClickListener {
            adapter.list.add(Room("nothing", "test-title ${(Math.random() * 100).toInt()}", "test caption \n${Math.random()}", (Math.random() * 500).toInt()))
            adapter.notifyDataSetChanged()
        }
    }
}
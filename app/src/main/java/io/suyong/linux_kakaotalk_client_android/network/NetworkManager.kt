package io.suyong.linux_kakaotalk_client_android.network

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.suyong.linux_kakaotalk_client_android.MainActivity
import io.suyong.linux_kakaotalk_client_android.RoomActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

object NetworkManager {
    var url: String = ""

    private var socket: Socket? = null
    private var map = HashMap<String, MutableList<(JSONObject) -> Unit>>()

    fun connect(url: String? = null) {
        this.url = url ?: this.url

        try {
            socket = IO.socket(this.url)
            init()
        } catch (error: Exception) {
            Log.e("test", error.toString())
        }
    }

    fun emit(event: String, obj: Map<String, String>? = null) {
        if (obj != null) {
            val json = JSONObject()

            json.put("uuid", MainActivity.uuid)
            for (element in obj) {
                json.put(element.key, element.value)
            }

            socket?.emit(event, json)
        } else {
            socket?.emit(event)
        }
    }

    fun on(event: String, func: (JSONObject) -> Unit) {
        map.getOrPut(event) { mutableListOf() }.add(func)
    }

    fun disconnect() {
        emit(
            "register-client",
            mapOf(
                "type" to "user",
                "target" to RoomActivity.target_uuid
            )
        )

        socket?.disconnect()
    }

    private fun init() {
        socket?.let {
            it.on(Socket.EVENT_CONNECT) {
                emit(
                    "register-client",
                    mapOf(
                        "type" to "user",
                        "target" to RoomActivity.target_uuid
                    )
                )
            }

            it.on("room") { data ->
                val json = JSONObject(data[0].toString())

                map["room"]?.forEach {
                    it.invoke(json)
                }
            }

            it.on(Socket.EVENT_MESSAGE) { data ->
                val json = JSONObject(data[0].toString())

                map[Socket.EVENT_MESSAGE]?.forEach {
                    it.invoke(json)
                }
            }

            it.on(Socket.EVENT_DISCONNECT) {

            }

            it.connect()
        }

        emit("ping")
    }
}
package io.suyong.linux_kakaotalk_client_android.network

import io.socket.client.IO
import io.socket.client.Socket

object NetworkManager {
    var url: String = ""

    private var socket: Socket? = null
    private var map = HashMap<String, MutableList<(Any) -> Any>>()

    fun connect(url: String? = null) {
        try {
            socket = IO.socket(url ?: this.url)
            url?.let { this.url = it }

            init()
            socket?.connect()
        } catch (error: Exception) {}
    }

    fun emit(event: String) {
        socket?.emit(event)
    }

    fun on(event: String, func: (Any) -> Any) {
        map.getOrPut(event) { mutableListOf() }.add(func)
    }

    fun disconnect() {
        socket?.disconnect()
    }

    private fun init() {
        socket?.let {
            it.on(Socket.EVENT_CONNECT) {

            }

            it.on(Socket.EVENT_DISCONNECT) {

            }
        }
    }
}
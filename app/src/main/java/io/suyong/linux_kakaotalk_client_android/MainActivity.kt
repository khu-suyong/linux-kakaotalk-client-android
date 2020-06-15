package io.suyong.linux_kakaotalk_client_android

import android.Manifest
import android.content.Intent
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.preference.PreferenceManager
import io.suyong.linux_kakaotalk_client_android.network.NetworkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        var uuid: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FileManager.init(this)

        val preference = PreferenceManager.getDefaultSharedPreferences(this)
        uuid = preference.getString("uuid", "") ?: ""

        if (uuid == "") {
            val editor = preference.edit()
            editor.putString("uuid", Util.Base62.encoding(Date().time))
            editor.apply()

            uuid = preference.getString("uuid", "") ?: ""
        }

        val url = preference.getString("url", "") ?: ""
        val target = preference.getString("target", "") ?: ""
        val nickname = preference.getString("nickname", "") ?: ""

        textinput_server.setText(url)
        textinput_bridge.setText(target)
        textinput_nickname.setText(nickname)

        textinput_server.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                try {
                    URL(textinput_server.text.toString())

                    textinput_server.error = null
                    main_connect.isEnabled = true
                } catch (err: MalformedURLException) {
                    textinput_server.error = getString(R.string.invalid_format_url)
                    main_connect.isEnabled = false
                }
            }
        })

        main_connect.setOnClickListener {
            RoomActivity.target_uuid = textinput_bridge.text.toString()
            ChatActivity.sender = textinput_nickname.text.toString()

            val editor = preference.edit()
            editor.putString("url", textinput_server.text.toString())
            editor.putString("target", textinput_bridge.text.toString())
            editor.putString("nickname", textinput_nickname.text.toString())
            editor.apply()

            NetworkManager.disconnect()
            NetworkManager.connect(textinput_server.text.toString())
            Log.d("connect", "call")

            startActivity(Intent(this, RoomActivity::class.java))
        }
    }
}
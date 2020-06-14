package io.suyong.linux_kakaotalk_client_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import io.suyong.linux_kakaotalk_client_android.network.NetworkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            NetworkManager.connect(textinput_server.text.toString())

            startActivity(Intent(this, RoomActivity::class.java))
        }
    }
}
package io.suyong.linux_kakaotalk_client_android

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat.getDataDir
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*


object FileManager {
    private var ROOT_PATH = ""

    fun init(context: Context) {
        ROOT_PATH = context.filesDir?.absolutePath.toString()
    }

    fun create(path: String, force: Boolean = false) {
        val file = File("$ROOT_PATH/$path")

        if (force && file.exists()) {
            file.delete()
        }

        if (!file.exists()) {
            try {
                val writer = BufferedWriter(FileWriter(file))
                writer.write("{}")
                writer.close()
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }

    fun save(path: String, key: String, params: Any, force: Boolean = false) {
        create(path)

        try {
            val reader = BufferedReader(FileReader(File("$ROOT_PATH/$path")))

            val text = reader.readText()
            val json = JSONObject(text)

            if (!force) {
                var data = JSONArray()

                try {
                    data = json.get(key) as JSONArray
                } catch (error: Exception) { }

                data.put(params)

                json.put(key, data)
            } else {
                json.put(key, params)
            }


            val writer = BufferedWriter(FileWriter(File("$ROOT_PATH/$path")))
            writer.write(json.toString())

            reader.close()
            writer.close()
        } catch (error: Exception) {
            error.printStackTrace()

            create(path, true)
        }
    }

    fun remove(path: String, key: String) {
        create(path)

        try {
            val reader = BufferedReader(FileReader(File("$ROOT_PATH/$path")))

            val text = reader.readText()
            val json = JSONObject(text)

            json.remove(key)

            val writer = BufferedWriter(FileWriter(File("$ROOT_PATH/$path")))
            writer.write(json.toString())

            reader.close()
            writer.close()
        } catch (error: Exception) {
            error.printStackTrace()

            create(path, true)
        }
    }

    fun read(path: String, key: String = ""): String {
        create(path)

        var result = ""

        try {
            val reader = BufferedReader(FileReader(File("$ROOT_PATH/$path")))

            val text = reader.readText()
            val json = JSONObject(text)

            if (key == "") {
                result = json.toString()
            } else {
                result = json.get(key).toString()
            }

            reader.close()
        } catch (error: Exception) {
            error.printStackTrace()

            create(path, true)
        }

        return result
    }
}
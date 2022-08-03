package de.thm.sbwl47.rush_b_vanilla.view.helper

import android.content.Context
import java.io.IOException
import java.io.InputStream

object JSONLoader {
    fun loadJSONFromAsset(context: Context): String {
        var json: String? = "null"
        json = try {
            val `is`: InputStream = context.assets.open("levels.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }
}
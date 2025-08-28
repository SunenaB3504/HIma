package com.example.hima.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader

class AssetLoader(private val context: Context) {

    fun loadJsonFromAssets(fileName: String): JSONObject? {
        return try {
            context.assets.open(fileName).use { input ->
                val reader = BufferedReader(input.reader(Charsets.UTF_8))
                val text = reader.readText()
                JSONObject(text)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getLetterChar(letterJson: JSONObject): String {
        return letterJson.optString("char", "")
    }

    fun getLetterAudioPath(letterJson: JSONObject): String? {
        return letterJson.optString("audio", null)
    }

    fun getLetterExamples(letterJson: JSONObject): List<JSONObject> {
        val list = mutableListOf<JSONObject>()
        val arr: JSONArray? = letterJson.optJSONArray("examples")
        if (arr != null) {
            for (i in 0 until arr.length()) {
                list.add(arr.getJSONObject(i))
            }
        }
        return list
    }

    fun getStoryText(storyJson: JSONObject): String {
        return storyJson.optString("text", "")
    }

    fun getStoryNarrationPath(storyJson: JSONObject): String? {
        return storyJson.optString("narration", null)
    }
}

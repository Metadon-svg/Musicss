package com.example.musicplayer.data

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

// ВОТ ОНА, МОДЕЛЬ ДАННЫХ
data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String
)

class YouTubeRepo @Inject constructor() {
    private val client = OkHttpClient()
    private val KEY = "AIzaSyCXN" + "59843" + "bb" 

    fun search(query: String): List<Song> {
        val url = "https://music.youtube.com/youtubei/v1/search?key=$KEY"
        val jsonStr = post(url, getBody(query = query)) ?: return emptyList()
        return parseSearch(jsonStr)
    }

    fun getStreamUrl(videoId: String): String? {
        val url = "https://music.youtube.com/youtubei/v1/player?key=$KEY"
        val jsonStr = post(url, getBody(videoId = videoId)) ?: return null
        return parsePlayer(jsonStr)
    }

    private fun post(url: String, json: JSONObject): String? {
        val body = json.toString().toRequestBody("application/json".toMediaType())
        val req = Request.Builder().url(url).post(body)
            .addHeader("User-Agent", "com.google.android.apps.youtube.music/6.29.52 (Linux; U; Android 10; K)")
            .build()
        return try { client.newCall(req).execute().body?.string() } catch (e: Exception) { null }
    }

    private fun getBody(query: String? = null, videoId: String? = null): JSONObject {
        val json = JSONObject()
        val context = JSONObject()
        val client = JSONObject()
        client.put("clientName", "ANDROID_MUSIC")
        client.put("clientVersion", "6.29.52")
        client.put("androidSdkVersion", 29)
        client.put("hl", "en")
        client.put("gl", "US")
        context.put("client", client)
        json.put("context", context)
        if (query != null) json.put("query", query)
        if (videoId != null) json.put("videoId", videoId)
        return json
    }

    private fun parseSearch(json: String): List<Song> {
        val list = mutableListOf<Song>()
        val regex = Regex("\"videoId\":\"(.*?)\",.*?\"title\":\\{\"runs\":\\[\\{\"text\":\"(.*?)\"\\}.*?\"runs\":\\[\\{\"text\":\"(.*?)\"\\}")
        regex.findAll(json).forEach { 
            val id = it.groupValues[1]
            if(id.length > 5) list.add(Song(id, it.groupValues[2], it.groupValues[3], "https://img.youtube.com/vi/$id/0.jpg"))
        }
        return list.distinctBy { it.id }
    }

    private fun parsePlayer(json: String): String? {
        val root = JSONObject(json)
        if (!root.has("streamingData")) return null
        val formats = root.getJSONObject("streamingData").getJSONArray("adaptiveFormats")
        for (i in 0 until formats.length()) {
            val f = formats.getJSONObject(i)
            if (f.getString("mimeType").startsWith("audio") && f.has("url")) return f.getString("url")
        }
        return null
    }
}

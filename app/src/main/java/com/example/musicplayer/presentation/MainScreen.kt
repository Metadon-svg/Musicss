package com.example.musicplayer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(vm: MusicViewModel = hiltViewModel()) {
    val songs by vm.songs
    val current by vm.current
    val playing by vm.isPlaying
    var text by vm.query

    Scaffold(
        bottomBar = {
            if (current != null) {
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFF282828)).padding(8.dp).clickable { vm.toggle() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(model = current!!.image, contentDescription = null, modifier = Modifier.size(50.dp))
                    Column(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
                        Text(current!!.title, color = Color.White, maxLines = 1)
                        Text(current!!.artist, color = Color.Gray, maxLines = 1)
                    }
                    Text(if (playing) "||" else "▶", color = Color.Green, modifier = Modifier.padding(10.dp))
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(padding)) {
            Row(modifier = Modifier.padding(10.dp)) {
                TextField(value = text, onValueChange = { text = it }, modifier = Modifier.weight(1f), colors = TextFieldDefaults.textFieldColors(containerColor = Color.DarkGray, textColor = Color.White))
                Button(onClick = { vm.search() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green)) { Text("GO") }
            }
            LazyColumn {
                items(songs) { song ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { vm.play(song) }.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(model = song.image, contentDescription = null, modifier = Modifier.size(50.dp).clip(RoundedCornerShape(4.dp)))
                        Column(modifier = Modifier.padding(start = 10.dp)) {
                            Text(song.title, color = Color.White)
                            Text(song.artist, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
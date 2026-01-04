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
fun MainScreen(viewModel: MusicViewModel = hiltViewModel()) {
    // Теперь имена точно совпадают с ViewModel
    val tracks by viewModel.tracks
    val current by viewModel.currentTrack
    val playing by viewModel.isPlaying
    var query by viewModel.searchQuery

    Scaffold(
        bottomBar = {
            if (current != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF202020))
                        .padding(8.dp)
                        .clickable { viewModel.togglePlay() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = current!!.imageUrl, 
                        contentDescription = null, 
                        modifier = Modifier.size(50.dp).clip(RoundedCornerShape(4.dp))
                    )
                    Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                        Text(current!!.title, color = Color.White, maxLines = 1)
                        Text(current!!.artist, color = Color.Gray, maxLines = 1)
                    }
                    Text(
                        text = if (playing) "PAUSE" else "PLAY",
                        color = Color(0xFF1DB954),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(padding)
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                // ИСПРАВЛЕННЫЙ TEXTFIELD
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search songs...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.DarkGray,
                        unfocusedContainerColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.search() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954))
                ) {
                    Text("GO")
                }
            }

            LazyColumn {
                items(tracks) { song ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.play(song) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = song.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(song.title, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                            Text(song.artist, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

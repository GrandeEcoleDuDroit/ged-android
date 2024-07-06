package com.upsaclay.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.core.ui.theme.GedoiseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Test(){
    Column {
        Button(
            onClick = { /* Action */ },
        ) {
            Text("Button")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = { /* Action */ },
        ) {
            Text("Outlined Button")
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            onClick = { /* Action */ },
        ) {
            Text("Text Button")
        }
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = "Input",
            onValueChange = { /* Action */ },
        )

        Spacer(modifier = Modifier.height(10.dp))

        TopAppBar(
            title = { Text("AppBar") },
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Text(text = "My Surface")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.padding(20.dp)
        ) {
            Text("Card Content")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Top AppBar") }
                )
            },
            bottomBar = {
                BottomAppBar(
                    content = { Text("BottomAppBar Content") }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* Action */ },
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            },
            content = { padding ->
                Text(text = "Scaffold Content", modifier = Modifier.padding(top = padding.calculateTopPadding()))
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Snackbar(
            action = {
                TextButton(onClick = { /* Action */ }) {
                    Text("Action")
                }
            },
        ) {
            Text("This is a Snackbar")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Favorite",
        )

    }
}

@Preview
@Composable
fun TestPreview(){
    GedoiseTheme {
        Test()
    }
}
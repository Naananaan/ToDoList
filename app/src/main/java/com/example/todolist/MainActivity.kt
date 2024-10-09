package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.ui.theme.ToDoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListApp()
        }
    }
}

// Action class for the reducer
sealed class Action {
    data class AddTask(val task: String) : Action()
    data class RemoveTask(val task: String) : Action()
}

// Reducer function to manage task state
fun taskReducer(state: MutableList<String>, action: Action) {
    when (action) {
        is Action.AddTask -> state.add(action.task)
        is Action.RemoveTask -> state.remove(action.task)
    }
}

@Composable
fun ToDoListApp() {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    val tasks = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.padding(16.dp)) {
        // Input field for adding a new task
        TextField(
            value = textState,
            onValueChange = { textState = it },
            label = { Text("Add a task") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Save button
        Button(
            onClick = {
                if (textState.text.isNotEmpty()) {
                    taskReducer(tasks, Action.AddTask(textState.text))
                    textState = TextFieldValue("") // Clear input after adding task
                }
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Save")
        }

        // Display the task list using LazyColumn
        LazyColumn {
            items(tasks) { task ->
                Text(
                    text = task,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { taskReducer(tasks, Action.RemoveTask(task)) }
                        .padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoListTheme {
        ToDoListApp()
    }
}

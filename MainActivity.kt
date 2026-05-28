package com.example.week10lab5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class TodoItem(
    val id: Long,
    val text: String,
    val isEditing: Boolean = false,
    val draftText: String = text
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TodoApp()
                }
            }
        }
    }
}

@Composable
fun TodoApp() {
    var inputText by remember { mutableStateOf("") }
    val todos = remember { mutableStateListOf<TodoItem>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Week 10 Lab 5 - TODO App",
            style = MaterialTheme.typography.headlineSmall
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("New task") },
                modifier = Modifier.weight(1f)
            )

            Button(onClick = {
                val newTask = inputText.trim()
                if (newTask.isNotEmpty()) {
                    todos.add(TodoItem(id = System.currentTimeMillis(), text = newTask))
                    inputText = ""
                }
            }) {
                Text("Add")
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(todos, key = { it.id }) { item ->
                TodoRow(
                    item = item,
                    onDelete = { deleteTodo(todos, item.id) },
                    onStartEdit = { startEdit(todos, item.id) },
                    onDraftChange = { draft -> updateDraft(todos, item.id, draft) },
                    onSaveEdit = { saveEdit(todos, item.id) },
                    onCancelEdit = { cancelEdit(todos, item.id) }
                )
            }
        }
    }
}

@Composable
fun TodoRow(
    item: TodoItem,
    onDelete: () -> Unit,
    onStartEdit: () -> Unit,
    onDraftChange: (String) -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit
) {
    if (item.isEditing) {
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = item.draftText,
                onValueChange = onDraftChange,
                label = { Text("Edit task") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onSaveEdit) { Text("Save") }
                Button(onClick = onCancelEdit) { Text("Cancel") }
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.text,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onStartEdit) { Text("Edit") }
                Button(onClick = onDelete) { Text("Delete") }
            }
        }
    }
}

private fun deleteTodo(todos: SnapshotStateList<TodoItem>, id: Long) {
    val index = todos.indexOfFirst { it.id == id }
    if (index >= 0) todos.removeAt(index)
}

private fun startEdit(todos: SnapshotStateList<TodoItem>, id: Long) {
    val index = todos.indexOfFirst { it.id == id }
    if (index >= 0) {
        val item = todos[index]
        todos[index] = item.copy(isEditing = true, draftText = item.text)
    }
}

private fun updateDraft(todos: SnapshotStateList<TodoItem>, id: Long, draft: String) {
    val index = todos.indexOfFirst { it.id == id }
    if (index >= 0) {
        val item = todos[index]
        todos[index] = item.copy(draftText = draft)
    }
}

private fun saveEdit(todos: SnapshotStateList<TodoItem>, id: Long) {
    val index = todos.indexOfFirst { it.id == id }
    if (index >= 0) {
        val item = todos[index]
        val updatedText = item.draftText.trim()
        if (updatedText.isNotEmpty()) {
            todos[index] = item.copy(
                text = updatedText,
                isEditing = false,
                draftText = updatedText
            )
        } else {
            todos.removeAt(index)
        }
    }
}

private fun cancelEdit(todos: SnapshotStateList<TodoItem>, id: Long) {
    val index = todos.indexOfFirst { it.id == id }
    if (index >= 0) {
        val item = todos[index]
        todos[index] = item.copy(isEditing = false, draftText = item.text)
    }
}
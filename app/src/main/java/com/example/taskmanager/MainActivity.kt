package com.example.taskmanager

//add these based on what code needs (Atl+Ent)
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.taskmanager.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomePage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


//how and where user added info is being held or stored (separate from remember)
data class Task(
    val name: String,
    val initialComplete: Boolean = false
) {
    var isComplete by mutableStateOf (initialComplete)
}
@Composable
fun HomePage(modifier: Modifier){
    //per instructions added so it can remember/track the adding and deleting of tasks
    val taskList = remember {mutableStateListOf<Task>()}

    Column(modifier = modifier.padding(16.dp)){
        Text(
            text = "Task Manager",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TaskInputField(taskList)
        TaskList(taskList)
    }
}
@Composable
fun TaskList (taskList: SnapshotStateList<Task>){
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        items (taskList)
        {
                task -> TaskItem (task, taskList)
        }
    }
}

@Composable
fun TaskInputField (taskList: SnapshotStateList<Task>){
    var text by remember {mutableStateOf("")}
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy (8.dp)

    )
    {
        TextField(
            value = text,
            onValueChange = { newText -> text = newText},
            label = {Text("Enter Task")},
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {
                if (text.isNotEmpty()){
                    taskList.add (Task(name = text))
                    text = ""
                }
            },
            //pink button color (searched kotlin hex for pink)
            colors = ButtonDefaults.buttonColors(
                containerColor = Color (0xFFFFC0CB)
            )
        )
        {
            Text (text = "Add Task")
        }

    }
}

@Composable
fun TaskItem (task: Task, taskList: SnapshotStateList<Task>){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

    )
    {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = task.isComplete,
                onCheckedChange = { task.isComplete = it }
            )
            Text(
                text = task.name,
                modifier = Modifier.padding(start = 8.dp),
                textDecoration = if (task.isComplete) {
                    //strick through feature
                    TextDecoration.LineThrough
                } else {
                    null
                }
            )
        }
        IconButton(onClick = { taskList.remove(task) }) {
            //searched trash can icon numbers
            Text(text = "\uD83D\uDDD1")
        }
    }
}
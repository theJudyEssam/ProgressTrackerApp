package com.example.progress_tracker
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.progress_tracker.ui.theme.ProgressTrackerTheme
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.progress_tracker.ui.theme.Pen


@Composable
fun ProgressApp(modifier: Modifier = Modifier){ //Main Screen
    Column(
        modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
            .padding(top = 60.dp)
    ){
        NewTaskSection()
        TasksList()
    }}


@SuppressLint("SuspiciousIndentation")  // there was an error that i didnt understand but this fixed it
@Composable
fun NewTaskSection(gameModel: ProgressViewModel = viewModel()){

    // states from ViewModel
    val userInput = gameModel.taskInput.collectAsState()
    val showDialog = gameModel.showTaskDialog.collectAsState()  // I read that it is better to keep UI related state in the
    //UI, but i put showDialog and editShowDialog in the ViewModel as a way to practice ViewModels.


        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Icon(
                imageVector = Pen, contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(20.dp),
            )

            Text(
                text = "What's on your mind today?",
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp))

            Button(
                onClick = { gameModel.showTaskDialogTrue() },
                modifier = Modifier.fillMaxWidth()
            ){Text("Add new task")}


            // trigger for the dialog prompting new task
            if(showDialog.value){
                NewTaskDialog(
                    onDismissRequest = {gameModel.showTaskDialogFalse()},
                    onConfirmation = { // adds the task, clears the field, and closes dialog
                        gameModel.addToDo(userInput.value)
                        gameModel.cleartaskInput()
                        gameModel.showTaskDialogFalse()},
                    userValue = userInput.value,
                    OnValueChange = { newValue -> gameModel.setTaskInput(newValue)}
                )
            }
        }
}


@Composable
fun NewTaskDialog(  //Alert for new task
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    userValue:String,
    OnValueChange: (String) -> Unit,
    label:String = "Enter your todo",
    confirmButton:String = "Add Task",
    dismissButton:String = "Nevermind!"
) {
    AlertDialog(
        title = { Text(label) },
        text = {
            TextField (
                value = userValue,
                onValueChange = OnValueChange
            )
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation() }
            ) {
                Text(confirmButton)
              }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(dismissButton)
              }
        }
    )
}



@Composable
fun TasksList(gameModel: ProgressViewModel = viewModel()){ // this part will contain the tasks, a LazyColumn of TaskItem Composables
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)){
        val TaskList = gameModel.taskMap
        items(TaskList.values.toList()){ todo ->
            TaskItem(
                task = todo,
            )
        }
    }
}


@Composable
fun TaskItem(modifier: Modifier = Modifier,
             task: TaskState = TaskState(),
             gameModel: ProgressViewModel = viewModel(),
){
    // UI-related states and non-UI-related states
    var visible by rememberSaveable {mutableStateOf(false)}
    var animatedBackgroundColor by rememberSaveable {mutableStateOf(false)}
    val editDialog = gameModel.showEditDialog.collectAsState()
    val userInput = gameModel.taskInput.collectAsState()


    val animatedColor by animateColorAsState(
        if (animatedBackgroundColor) task.TaskStatus.color else Status.No_status.color, label = "color")

    
    if(editDialog.value){
        NewTaskDialog(
            onDismissRequest = {gameModel.showEditDialogFalse()},
            onConfirmation = { // close dialog, edit task, and clear the input field
                gameModel.showEditDialogFalse()
                gameModel.editTask(userInput.value, task)
                gameModel.cleartaskInput()},
            userValue = userInput.value,
            OnValueChange = {newValue -> gameModel.setTaskInput(newValue)},
            label = "Edit your task",
            confirmButton = "Save"
        )
    }


    Card(){
        Column(modifier = Modifier
            .drawBehind { drawRect(animatedColor) }
            .padding(12.dp))
        {
            Row(){
                Text(
                    text = task.TaskName,
                    modifier = Modifier
                        .weight(1f),
                    style = MaterialTheme.typography.labelLarge)


                Row(verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = DynamicLabel(task),
                        style = MaterialTheme.typography.labelSmall
                    )

                    Icon(imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { visible = !visible })

                }}


            AnimatedVisibility(visible == true){

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        IconButton(
                            onClick = {
                                gameModel.showEditDialogTrue()
                            },
                            modifier = Modifier
                                .size(15.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp))

                        IconButton(
                            onClick = {gameModel.deleteTask(task)},
                            modifier = Modifier
                                .size(15.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null
                            )
                        }


                        Spacer(modifier = Modifier.weight(1f))
                        DropDown(modifier = Modifier.weight(1f), task = task, onAnimate = {animatedBackgroundColor = true}) }


                    StatusSettings(
                        status = task.TaskStatus.Name,
                        task = task,
                        onStreakChange = {newval ->  gameModel.updateStreak(task, newval)},
                        onProgressChange = {newval ->  gameModel.updateProgress(task, newval)}
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ProgressPreview() {
    ProgressTrackerTheme {
        Scaffold(
            topBar = { NavBar() },
            contentWindowInsets = androidx.compose.foundation.layout.WindowInsets.systemBars,
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues())
        ) { innerPadding ->
            ProgressApp( modifier = Modifier.padding((innerPadding)))
        }
    }
}








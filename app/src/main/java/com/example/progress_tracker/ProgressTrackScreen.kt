package com.example.progress_tracker
import android.R.attr.text
import android.R.id.content
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.DialogProperties
import com.example.progress_tracker.ui.theme.ProgressTrackerTheme
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.example.progress_tracker.ui.theme.Pen


@Composable
fun ProgressApp(modifier: Modifier = Modifier){ //Main Screen
    Column(
        modifier = Modifier.padding(20.dp)
    ){
        NewTaskSection()
        TasksList() }
}


@SuppressLint("SuspiciousIndentation")  // there was an error that i didnt understand but this fixed it
@Composable
fun NewTaskSection(gameModel: ProgressViewModel = viewModel()){

    // states from ViewModel
    val userInput = gameModel.taskInput.collectAsState()
    val showDialog = gameModel.showDialog.collectAsState()


        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Icon(
                imageVector = Pen, contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
            )

            Text(
                text = "What's on your mind today?",
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp))

            Button(
                onClick = { gameModel.DialogTrue() },
                modifier = Modifier.fillMaxWidth()
            ){Text("Add new task")}


            // trigger for the dialog prompting new task
            if(showDialog.value){
                NewTaskDialog(
                    onDismissRequest = {gameModel.DialogFalse()},
                    onConfirmation = {
                        gameModel.addToDo(userInput.value)
                        gameModel.cleartaskInput()
                        gameModel.DialogFalse()},
                    userValue = userInput.value,
                    OnValueChange = { newValue -> gameModel.setTaskInput(newValue)}
                )
            }
        }
}

//Alert for new task
@Composable
fun NewTaskDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    userValue:String,
    OnValueChange: (String) -> Unit,
    label:String = "Enter your todo",
    confirmButton:String = "Add Task",
    dismissButton:String = "Nevermind"
) {
    AlertDialog(
        title = { Text(label) },
        text = {
            //! Major Bug Alert: for some reason the textfield doesnt write
            TextField(
                value = userValue,
                onValueChange = OnValueChange
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()

                }
            ) {
                Text(confirmButton)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(dismissButton)
            }
        }
    )
}


// this part will contain the tasks, a LazyColumn of ListItems
@Composable
fun TasksList(gameModel: ProgressViewModel = viewModel()){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        val TaskList = gameModel.taskList
        items(TaskList){ todo ->
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




    var visible by remember {mutableStateOf(false)} // will change later
    val editDialog = gameModel.editDialog.collectAsState()
    val userInput = gameModel.taskInput.collectAsState()

    
    if(editDialog.value){
        NewTaskDialog(
            onDismissRequest = {gameModel.EditDialogFalse()},
            onConfirmation = {
                gameModel.EditDialogFalse()
                gameModel.editTask(userInput.value, task)
                gameModel.cleartaskInput()},
            userValue = userInput.value,
            OnValueChange = {newValue -> gameModel.setTaskInput(newValue)},
            label = "Edit your task",
            confirmButton = "Save",
            dismissButton = "Nevermind"
        )
    }


    Card(){
        Column(modifier = Modifier.padding(12.dp))
        {
            Row(){
                Text(
                    text = task.TaskName,
                    modifier = Modifier
                        .weight(1f),
                    style = MaterialTheme.typography.labelLarge
                )


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
                                gameModel.EditDialogTrue()
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
                        DropDown(modifier = Modifier.weight(1f), task = task)
                    }


                    //Status settings if it exists

//                    val updatedTask = gameModel.taskList.find { it.TaskId == task.TaskId } ?: task

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
        ProgressApp()
    }
}








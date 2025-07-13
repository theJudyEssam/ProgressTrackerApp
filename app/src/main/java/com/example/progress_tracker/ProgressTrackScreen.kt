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




@Composable
fun ProgressApp(modifier: Modifier = Modifier){
    Column(
        modifier = Modifier.padding(20.dp)
    ){
        NewTaskSection()
        TasksList()
    }
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
    OnValueChange: (String) -> Unit
) {
    AlertDialog(
        title = { Text("Enter your todo") },
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
                Text("Add Task")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Nevermind!")
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
                task = todo
            )
        }


    }
}


@Composable
fun TaskItem(modifier: Modifier = Modifier,
             task: TaskState = TaskState(),
             gameModel: ProgressViewModel = viewModel()
){



    var visible by remember {mutableStateOf(false)} // will change later
    //val currentStatus = gameModel.status.collectAsState()


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
                            onClick = {},
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
                            onClick = {},
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


// Components

@Composable
fun DropDown(modifier: Modifier = Modifier,
             gameModel: ProgressViewModel = viewModel(),
             task: TaskState = TaskState()
){

    var mExpanded by remember { mutableStateOf(false) }
   // var Status = listOf("No Status", "Completed", "In Progress", "Streaked")  //UI-purposes, will refactor later
   //var SelectedStatus by remember { mutableStateOf("No Status") }


    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(
        Modifier.padding(5.dp)
    ){
        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
            Row(
                modifier = Modifier
                    .clickable { mExpanded = !mExpanded }
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .defaultMinSize(minHeight = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.TaskStatus.Name,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }


            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = Modifier

            ) {
                Status.entries.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(text = status.Name, style = MaterialTheme.typography.labelMedium) },
                        onClick = {
                            gameModel.setStatus(status, task)
                            mExpanded = false
                        }
                    )
                }
            }


        }

    }

}


@Composable
fun StatusSettings(modifier: Modifier = Modifier,
                   status:String = "No Status",
                   task: TaskState = TaskState(),
                    onProgressChange: (Float) -> Unit,
                    onStreakChange: (Int) -> Unit
){

    AnimatedVisibility(visible = status == "In Progress") {
        Row(
            modifier = Modifier, 
            verticalAlignment = Alignment.CenterVertically
        )
        {
            
            Text(text = "Progress: ${task.progress.toInt()}%", style = MaterialTheme.typography.labelMedium)


            Spacer(modifier = Modifier.weight(1f))
            Slider(

                modifier = Modifier
                    .width(150.dp)
                    .height(40.dp),

                value = task.progress,
                onValueChange = {onProgressChange(it)},
                valueRange = 0f..100f,

                colors = SliderDefaults.colors(
                    thumbColor = Color.Black,
                    activeTrackColor = Color.Black,
                    inactiveTrackColor = Color.Gray)
            )
        }

    }

    AnimatedVisibility(visible = status == "Streak") {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "${task.streak} Day Streak", style = MaterialTheme.typography.labelMedium)

            Spacer(modifier = Modifier.weight(1f))

            NumericCounter(
                value =task.streak,
                onValueChange = {newVal -> onStreakChange(newVal)}
            )
        }


    }
}


@Composable
fun NumericCounter(
    value: Int,
    onValueChange: (Int) -> Unit,
    min: Int = 0,
    max: Int = 100
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
    ) {
        IconButton(
            onClick = { if (value > min) onValueChange(value - 1) },
            enabled = value > min
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
        }

        Text(
            text = value.toString(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = { if (value < max) onValueChange(value + 1) },
            enabled = value < max
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Decrease")
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



fun DynamicLabel(task: TaskState): String{
    var labelName = when(task.TaskStatus.Name){
        "In Progress" -> "${task.progress.toInt()}% done"
        "Streak" -> "${task.streak} 🔥"
        "Completed" -> "✅"
        else -> "No Status"
    }

    return labelName
}







val Pen: ImageVector
    get() {
        if (_Pen != null) return _Pen!!

        _Pen = ImageVector.Builder(
            name = "Pen",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveToRelative(13.498f, 0.795f)
                lineToRelative(0.149f, -0.149f)
                arcToRelative(1.207f, 1.207f, 0f, true, true, 1.707f, 1.708f)
                lineToRelative(-0.149f, 0.148f)
                arcToRelative(1.5f, 1.5f, 0f, false, true, -0.059f, 2.059f)
                lineTo(4.854f, 14.854f)
                arcToRelative(0.5f, 0.5f, 0f, false, true, -0.233f, 0.131f)
                lineToRelative(-4f, 1f)
                arcToRelative(0.5f, 0.5f, 0f, false, true, -0.606f, -0.606f)
                lineToRelative(1f, -4f)
                arcToRelative(0.5f, 0.5f, 0f, false, true, 0.131f, -0.232f)
                lineToRelative(9.642f, -9.642f)
                arcToRelative(0.5f, 0.5f, 0f, false, false, -0.642f, 0.056f)
                lineTo(6.854f, 4.854f)
                arcToRelative(0.5f, 0.5f, 0f, true, true, -0.708f, -0.708f)
                lineTo(9.44f, 0.854f)
                arcTo(1.5f, 1.5f, 0f, false, true, 11.5f, 0.796f)
                arcToRelative(1.5f, 1.5f, 0f, false, true, 1.998f, -0.001f)
                moveToRelative(-0.644f, 0.766f)
                arcToRelative(0.5f, 0.5f, 0f, false, false, -0.707f, 0f)
                lineTo(1.95f, 11.756f)
                lineToRelative(-0.764f, 3.057f)
                lineToRelative(3.057f, -0.764f)
                lineTo(14.44f, 3.854f)
                arcToRelative(0.5f, 0.5f, 0f, false, false, 0f, -0.708f)
                close()
            }
        }.build()

        return _Pen!!
    }

private var _Pen: ImageVector? = null


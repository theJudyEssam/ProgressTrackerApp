package com.example.progress_tracker
import android.R.attr.text
import android.R.id.content
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




// this will represent the main screen for the app
@Composable
fun ProgressApp(modifier: Modifier = Modifier){


    Column(
        modifier = Modifier.padding(20.dp)
    ){
        NewTaskSection()
        TasksList()
    }
}


@Composable
fun NewTaskSection(gameModel: ProgressViewModel = viewModel()){
    var userInput by remember { mutableStateOf("") }
    val showDialog = gameModel.showDialog.collectAsState()

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            Text(
                text = "What's on your mind today?",
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
            )

            Button(
                onClick = {
                    gameModel.DialogTrue()
//                    Log.d("Dialog", "True")
                },
                modifier = Modifier.fillMaxWidth()

            ){
                Text(
                    text = "Add new task"
                )
            }

            if(showDialog.value){
                NewTaskDialog(
                    onDismissRequest = {gameModel.DialogFalse()},
                    onConfirmation = {gameModel.DialogFalse()},
                    userValue = userInput,
                    OnValueChange = {userInput = it}
                )
            }
        }
}

//Alert for new section
@Composable
fun NewTaskDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    userValue:String,
    OnValueChange: (String) -> Unit
) {
    AlertDialog(
        title = {
            Text(text = "Enter your todo")
        },
        text = {
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
                Text("Neverind!")
            }
        }
    )
}


// this part will contain the tasks, a LazyColumn of ListItems
@Composable
fun TasksList(){
Column(
    verticalArrangement = Arrangement.spacedBy(10.dp)
){
    TaskItem(taskName = "Task 1 ")
    TaskItem(taskName = "Task 1 ")
    TaskItem(taskName = "Task 1 ")
    TaskItem(taskName = "Task 1 ")
}
}


@Composable
fun TaskItem(modifier: Modifier = Modifier,
             taskName:String = "Task Item",
             gameModel: ProgressViewModel = viewModel()
){

    var visible by remember {mutableStateOf(false)}

    Card(){
        Column( modifier = Modifier
            .padding(12.dp)){

            Row(){
                Text(
                    text = taskName,
                    modifier = Modifier
                        .weight(1f),
                    style = MaterialTheme.typography.labelLarge
                )


                Row(verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = "Status",
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
                        DropDown(modifier = Modifier.weight(1f))
                    }


                    //Status settings of it exists
                    StatusSettings(status = "Streak")

                }



            }




        }
    }
}


// Components

@Composable
fun DropDown(modifier: Modifier = Modifier,
             ) {

    var mExpanded by remember { mutableStateOf(false) }
    var Status = listOf("No Status", "Completed", "In Progress", "Streaked")  //UI-purposes, will refactor later
    var SelectedStatus by remember { mutableStateOf("No Status") }

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
                val selectedStatus = null
                Text(
                    text = SelectedStatus,
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
                Status.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(text = label, style = MaterialTheme.typography.labelMedium)},
                        onClick = {
                            SelectedStatus = label
                            mExpanded = false
                        }
                    )


                }
            }


        }

    }

}


@Composable
fun StatusSettings(modifier: Modifier = Modifier, status:String = "No Status"){
    var sliderValue by remember {mutableStateOf(50.0f)}
    var Numericvalue by remember{mutableStateOf(0)}


    AnimatedVisibility(visible = status == "In Progress") {
        Row(
            modifier = Modifier, 
            verticalAlignment = Alignment.CenterVertically
        )
        {
            
            Text(text = "Progress: ${sliderValue.toInt()}%", style = MaterialTheme.typography.labelMedium)


            Spacer(modifier = Modifier.weight(1f))
            Slider(

                modifier = Modifier
                    .width(150.dp)
                    .height(40.dp),

                value = sliderValue,
                onValueChange = { sliderValue = it },
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
            Text(text = "$Numericvalue Day Streak", style = MaterialTheme.typography.labelMedium)

            Spacer(modifier = Modifier.weight(1f))

            NumericCounter(
                value = Numericvalue,
                onValueChange = {Numericvalue = it}
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






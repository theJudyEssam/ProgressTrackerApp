package com.example.progress_tracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun DropDown(modifier: Modifier = Modifier,
             gameModel: ProgressViewModel = viewModel(),
             task: TaskState = TaskState(),
             onAnimate : () -> Unit
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
                            onAnimate()
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



fun DynamicLabel(task: TaskState): String{
    var labelName = when(task.TaskStatus.Name){
        "In Progress" -> "${task.progress.toInt()}% done"
        "Streak" -> "${task.streak} 🔥"
        "Completed" -> "✅"
        else -> "No Status"
    }

    return labelName
}
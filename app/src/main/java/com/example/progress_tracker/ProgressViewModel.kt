package com.example.progress_tracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Status(var Name:String)
{
    No_status("No Status"),
    Completed("Completed"),
    In_Progress("In Progress"),
    Streak("Streak")
}


data class TaskState
    (
    var TaskId: Int =  0,
    var TaskName:String = "",
    var TaskDescription:String = "",
    var TaskStatus: Status = Status.No_status
    )




// Mutables:
    // Visible --> for animatedVisibility
    // SelectedStatus --> the Status that the user chooses
    // showDialog --> for when the "Add Task" is selected
    //




class ProgressViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TaskState())
    val uiState: StateFlow<TaskState> = _uiState.asStateFlow()


    var _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean>  = _showDialog.asStateFlow()
    fun DialogTrue(){
        _showDialog.value = true
    }
    fun DialogFalse(){
        _showDialog.value = false
    }


    var _visible = MutableStateFlow(false)
    val visible: StateFlow<Boolean> = _visible.asStateFlow()

    fun ToggleVisibility(){
        _visible.value = !_visible.value
    }
}


// This will be for the Tasks Logic
class TaskViewModel: ViewModel(){

}

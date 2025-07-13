package com.example.progress_tracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



/*
* smth i have learned:
* dont put values that arent global to all instances in the enum
* cuz an enum is basically like a singleton
* */
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
    var TaskStatus: Status = Status.No_status,
    var progress:Float = 0.0f,
    var streak:Int = 1
    )

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


    var _editDialog = MutableStateFlow(false)
    val editDialog: StateFlow<Boolean> = _editDialog.asStateFlow()

    fun EditDialogTrue(){
        _editDialog.value = true
    }
    fun EditDialogFalse(){
        _editDialog.value = false
    }



    private var _taskInput = MutableStateFlow("")
    val taskInput: StateFlow<String> = _taskInput.asStateFlow()
    fun setTaskInput(name:String){
        _taskInput.value = name
    }

    fun cleartaskInput(){
        _taskInput.value = ""
    }




    private val _taskList = mutableStateListOf<TaskState>()
    val taskList : List<TaskState> get() = _taskList

    fun addToDo(taskname:String){
        var newTask = TaskState(
            TaskId = _taskList.size + 1,
            TaskName = taskname
        )
        _taskList.add(newTask)
    }


    // this shall hold our current status
    private var _Status = MutableStateFlow(Status.No_status)
    val status: StateFlow<Status> = _Status.asStateFlow()

    fun setStatus(status: Status, task: TaskState){
            val index = _taskList.indexOf(task)
            if(index >= 0){
                _taskList[index] = task.copy(TaskStatus = status)
            }
    }

    fun updateStreak(task: TaskState, streak:Int){
        val index = _taskList.indexOf(task)

        if(index >= 0){
            _taskList[index] = task.copy(streak = streak)
        }
    }

    fun updateProgress(task:TaskState, progress:Float){
        val index = _taskList.indexOf(task)

        if(index >= 0){
            _taskList[index] = task.copy(progress = progress)
        }
    }

    fun editTask(newName: String, task:TaskState){
        val index = _taskList.indexOf(task)
        if(index >= 0){
            _taskList[index] = task.copy(TaskName =  newName)
        }
    }

    fun deleteTask(task:TaskState){
        _taskList.remove(task)
    }

}


// This will be for the Tasks Logic
class TaskViewModel: ViewModel(){

}

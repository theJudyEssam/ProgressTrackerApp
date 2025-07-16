package com.example.progress_tracker

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.ui.graphics.Color




/*
* smth i have learned:
* dont put values that arent global to all instances in the enum
* cuz an enum is basically like a singleton
* */
enum class Status(val Name:String, val color: Color)
{
    No_status("No Status", Color(0xFFF1F6F5)),
    Completed("Completed", Color(0xFFD3ECCD)),
    In_Progress("In Progress", Color(0xFFC5D3E8)),
    Streak("Streak", Color(0xFFFFC107))
}


data class TaskState  // Holds state of each task
    (
    var TaskId: Int =  0,
    var TaskName:String = "",
    var TaskDescription:String = "",
    var TaskStatus: Status = Status.No_status,
    var progress:Float = 0.0f,
    var streak:Int = 1
    )

class ProgressViewModel : ViewModel() {

    // State of dialog
    // its generally recommended to keep UI-related states like animation/visibility within composables
    // but I wanted to be more familiar with Viewmodels;
    var _showTaskDialog = MutableStateFlow(false)
    val showTaskDialog: StateFlow<Boolean>  = _showTaskDialog.asStateFlow()
    fun showTaskDialogTrue(){
        _showTaskDialog.value = true
    }
    fun showTaskDialogFalse(){
        _showTaskDialog.value = false
    }


    var _showEditDialog = MutableStateFlow(false)
    val showEditDialog: StateFlow<Boolean> = _showEditDialog.asStateFlow()

    fun showEditDialogTrue(){
        _showEditDialog.value = true
    }
    fun showEditDialogFalse(){
        _showEditDialog.value = false
    }



    private var _taskInput = MutableStateFlow("")
    val taskInput: StateFlow<String> = _taskInput.asStateFlow()
    fun setTaskInput(name:String){
        _taskInput.value = name
    }
    fun cleartaskInput(){
        _taskInput.value = ""
    }


    private val _taskMap = mutableStateMapOf<Int, TaskState>()
    val taskMap : Map<Int,TaskState> get() = _taskMap

    fun addToDo(taskname:String){
        val newTask = TaskState(
            TaskId = _taskMap.size + 1,
            TaskName = taskname
        )
        _taskMap[newTask.TaskId] = newTask
    }

    fun setStatus(status: Status, task: TaskState){
          if(task.TaskId >= 0){
                _taskMap[task.TaskId] = task.copy(TaskStatus = status)
            }
    }

    fun updateStreak(task: TaskState, streak:Int){

        if(task.TaskId >= 0){
            _taskMap[task.TaskId] = task.copy(streak = streak)
        }
    }

    fun updateProgress(task:TaskState, progress:Float){

        if(task.TaskId >= 0){
            _taskMap[task.TaskId] = task.copy(progress = progress)
        }
    }
    fun editTask(newName: String, task:TaskState){
        if(task.TaskId >= 0){
            _taskMap[task.TaskId] = task.copy(TaskName =  newName)
        }
    }
    fun deleteTask(task:TaskState){
        _taskMap.remove(task.TaskId)
    }

}



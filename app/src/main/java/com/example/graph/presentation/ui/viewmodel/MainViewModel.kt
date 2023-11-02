package com.example.graph.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.graph.domain.repository.IRepository
import com.example.graph.domain.repository.Repository
import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    //Has to provides via di
    val repository : IRepository = Repository()

    val graphDataState : StateFlow<GraphData?> = repository.graphData
}
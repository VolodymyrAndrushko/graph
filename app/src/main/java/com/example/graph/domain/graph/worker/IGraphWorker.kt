package com.example.graph.domain.graph.worker

import com.example.graph.presentation.ui.glucose_graph.model.DateType
import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import kotlinx.coroutines.flow.StateFlow

interface IGraphWorker {
    val graphDataState : StateFlow<GraphData?>

    fun updateGraphData()
}
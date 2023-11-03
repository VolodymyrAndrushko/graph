package com.example.graph.domain.graph

import com.example.graph.presentation.ui.glucose_graph.model.DateType
import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import kotlinx.coroutines.flow.StateFlow

interface IGraphCore {
    fun updateGraphData()

    val graphData: StateFlow<GraphData?>
    fun updateType(type: DateType)
}
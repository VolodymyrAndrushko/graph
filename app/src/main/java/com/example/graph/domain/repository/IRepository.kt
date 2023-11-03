package com.example.graph.domain.repository

import com.example.graph.presentation.ui.glucose_graph.model.DateType
import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import kotlinx.coroutines.flow.StateFlow

interface IRepository {
    fun updateGraph()
    fun updateType(type: DateType)

    val graphData: StateFlow<GraphData?>


}
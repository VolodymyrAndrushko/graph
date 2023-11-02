package com.example.graph.domain.repository

import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import kotlinx.coroutines.flow.StateFlow

interface IRepository {

    val graphData: StateFlow<GraphData?>


}
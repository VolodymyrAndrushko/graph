package com.example.graph.domain.graph

import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import kotlinx.coroutines.flow.StateFlow

interface IGraphCore {

    val graphData: StateFlow<GraphData?>
}
package com.example.graph.domain.repository

import com.example.graph.domain.graph.GraphCore
import com.example.graph.domain.graph.IGraphCore
import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import kotlinx.coroutines.flow.StateFlow

class Repository : IRepository {
    //Has to provides via di
    private val graphCore : IGraphCore = GraphCore()

    override val graphData: StateFlow<GraphData?>
        get() = graphCore.graphData

    override fun updateGraph() {
        graphCore.updateGraph()    }
}
package com.example.graph.domain.graph

import com.example.graph.domain.graph.worker.GraphWorker
import com.example.graph.domain.graph.worker.IGraphWorker
import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import kotlinx.coroutines.flow.StateFlow

class GraphCore : IGraphCore{
    //Has to provides via di
    private val graphWorker: IGraphWorker = GraphWorker()

    override val graphData: StateFlow<GraphData?> = graphWorker.graphDataState

}
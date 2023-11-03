package com.example.graph.domain.graph

import android.os.Handler
import android.os.Looper
import com.example.graph.presentation.ui.glucose_graph.model.DateType
import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class GraphCore : IGraphCore{
    //Has to provides via di
    private val _graphDataState = MutableStateFlow<GraphData?>(null)
    override val graphData : StateFlow<GraphData?> = _graphDataState

    private var type : DateType = DateType.Last8

    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 5000
    private val runnable = object : Runnable {
        override fun run() {
            updateGraphData()
            handler.postDelayed(this, delayMillis)

        }
    }

    init {
        updateGraphData()
        handler.postDelayed(runnable, delayMillis)
    }

    override fun updateGraphData() {
        _graphDataState.value = GraphData(
            glucoseValue = (40..250 step Random.nextInt(10,50)).toList().shuffled(),
            glucoseRange = (40..250 step 30).toList(),
            dateRange = getDataRange(type),
            aboveNormal = 190,
            belowNormal = 70,
            dateType = type
        )
    }

    override fun updateType(type : DateType){
        _graphDataState.update { it?.copy(dateType = type, dateRange = getDataRange(type)) }
    }
    private fun getDataRange(dateType: DateType): List<String> {
        return when(dateType){
            DateType.Last8 -> {
                listOf(
                    "12:00",
                    "13:00",
                    "14:00",
                    "15:00",
                    "16:00",
                    "17:00",
                    "18:00",
                    "19:00",
                )
            }
            DateType.Last24 -> {
                listOf(
                    "12 am",
                    "6 am",
                    "12 pm",
                    "6 pm",
                    "12 pm",
                )
            }
            DateType.LastWeek -> {
                listOf(
                    "Sat",
                    "Sum",
                    "Mon",
                    "Tue",
                    "Wed",
                    "Thu",
                    "Fri",
                )
            }
        }
    }
}
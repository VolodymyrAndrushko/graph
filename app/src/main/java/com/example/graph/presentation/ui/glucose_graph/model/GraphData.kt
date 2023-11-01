package com.example.graph.presentation.ui.glucose_graph.model

data class GraphData(
    val glucoseValue : List<Int>,
    val glucoseRange : List<Int>,
    val dateRange : List<String>,
    val aboveNormal : Int,
    val belowNormal : Int,
    val dateType: DateType
)

enum class DateType{
    Last8,
    Last24,
    LastWeek
}

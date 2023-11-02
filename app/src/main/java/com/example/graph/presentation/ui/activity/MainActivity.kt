package com.example.graph.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.graph.databinding.ActivityMainBinding
import com.example.graph.presentation.ui.glucose_graph.model.DateType
import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGraphValue()

        binding.randBtn.setOnClickListener {
            setGraphValue()
        }

    }

    private fun setGraphValue() {
        binding.graph.graphData = GraphData(
            glucoseValue = (40..250 step Random.nextInt(10,50)).toList().shuffled(),
//            glucoseValue = listOf(70, 190,70, 190,70, 190,70, 190).shuffled(),
            glucoseRange = (40..250 step 30).toList(),
//            dateRange = listOf(
//                "12 am",
//                "6 am",
//                "12 pm",
//                "6 pm",
//                "12 pm",
//            ),
                dateRange = listOf(
                    "Sat",
                    "Sum",
                    "Mon",
                    "Tue",
                    "Wed",
                    "Thu",
                    "Fri",
                    ),
            aboveNormal = 190,
            belowNormal = 70,
            dateType = DateType.Last24
        )
    }
}
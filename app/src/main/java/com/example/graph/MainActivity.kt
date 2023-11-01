package com.example.graph

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graph.databinding.ActivityMainBinding
import com.example.graph.presentation.ui.glucose_graph.model.DateType
import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import java.util.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.graph.graphData = GraphData(
            glucoseValue = listOf(
                55,
                100,
                122,
                210,
                144,
                190,
                40,
                133,
                80,
                120,
                90,
                99,
                70,
                40,
                230,
                250,
                100,
            ).shuffled(Random(15)),
            glucoseRange = (40..250 step 30).toList(),
            dateRange = listOf(
                "12 am",
                "6 am",
                "12 pm",
                "6 pm",
                "12 pm",
            ),
//                dateRange = listOf(
//                    "Sat",
//                    "Sum",
//                    "Mon",
//                    "Tue",
//                    "Wed",
//                    "Thu",
//                    "Fri",
//                    ),
            aboveNormal = 190,
            belowNormal = 70,
            dateType = DateType.Last24
        )
    }
}
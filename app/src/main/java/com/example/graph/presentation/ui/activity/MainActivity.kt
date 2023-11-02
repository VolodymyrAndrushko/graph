package com.example.graph.presentation.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.graph.databinding.ActivityMainBinding
import com.example.graph.presentation.ui.glucose_graph.model.DateType
import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import com.example.graph.presentation.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        subscribe()
    }

    private fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.graphDataState.collectLatest {graphData ->
                    graphData?.let {safeGraphData->
                        binding.graph.graphData = graphData
                    }
                }
            }
        }
    }


//    private fun setGraphValue() {
//        binding.graph.graphData = GraphData(
//            glucoseValue = (40..250 step Random.nextInt(10,50)).toList().shuffled(),
////            glucoseValue = listOf(70, 190,70, 190,70, 190,70, 190).shuffled(),
//            glucoseRange = (40..250 step 40).toList(),
////            glucoseRange = (40..250 step 30).toList(),
//            dateRange = listOf(
//                "12 am",
//                "6 am",
//                "12 pm",
//                "6 pm",
//                "12 pm",
//            ),
////                dateRange = listOf(
////                    "Sat",
////                    "Sum",
////                    "Mon",
////                    "Tue",
////                    "Wed",
////                    "Thu",
////                    "Fri",
////                    ),
//            aboveNormal = 190,
//            belowNormal = 70,
//            dateType = DateType.Last24
//        )
//    }
}
package com.example.graph.presentation.ui.activity

import android.os.Bundle
import android.util.Log
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
        setListeners()
    }

    private fun setListeners() {
        binding.randBtn.setOnClickListener {
            viewModel.updateGraph()
        }
        binding.last8.setOnClickListener {
            viewModel.updateType(DateType.Last8)
        }
        binding.last24.setOnClickListener {
            viewModel.updateType(DateType.Last24)
        }
        binding.lastWeek.setOnClickListener {
            viewModel.updateType(DateType.LastWeek)
        }
    }

    private fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.graphDataState.collectLatest {graphData ->
                    graphData?.let {safeGraphData->
                        binding.graph.graphData = safeGraphData
                    }
                }
            }
        }
    }
}
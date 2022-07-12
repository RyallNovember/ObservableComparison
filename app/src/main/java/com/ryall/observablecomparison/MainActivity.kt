package com.ryall.observablecomparison

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.ryall.observablecomparison.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLiveData.setOnClickListener {
            viewModel.triggerLiveData()
        }
        binding.btnSharedFlow.setOnClickListener {
            viewModel.triggerSharedFlow()
        }
        binding.btnStateFlow.setOnClickListener {
           viewModel.triggerStateFlow()
        }
        binding.btnFlow.setOnClickListener {
            //here we can use launch because its not a state flow
            lifecycleScope.launch {
                viewModel.triggerFlow().collectLatest {
                    binding.tvFlow.text = it
                }
            }
        }
        subscribeStateFlowObservables()
            subscribeToObservables()
    }

    private fun subscribeStateFlowObservables(){
        lifecycleScope.launchWhenStarted {
            //flow needs to be launched inside a coroutine
            //this is a collector
            viewModel.stateFlow.collectLatest{
                binding.tvStateFlow.text = it
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun subscribeToObservables(){
        //passing viewLifeCycleOwner is only used in fragments
/*        viewModel.liveData.observe(this){
            binding.tvLiveData.text = it
            Snackbar.make(
                binding.root,
                it,
                Snackbar.LENGTH_LONG
            ).show()
        }*/

        //always use launchWhenStarted for stateflow and not just launch
        lifecycleScope.launchWhenStarted {
            viewModel.sharedFlow.collectLatest{
                 binding.tvSharedFlow.text = it
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}
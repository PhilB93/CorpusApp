package com.example.belarusapp.feature_belarusapp.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.belarusapp.R
import com.example.belarusapp.databinding.FragmentMainBinding
import com.example.belarusapp.feature_belarusapp.domain.model.CityItem
import com.example.belarusapp.feature_belarusapp.presentation.main.adapter.CityOnClickListener
import com.example.belarusapp.feature_belarusapp.presentation.main.adapter.ListCitiesAdapter

import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), CityOnClickListener {

    private val gameAdapter by lazy(LazyThreadSafetyMode.NONE) { ListCitiesAdapter(this) }
    private val binding: FragmentMainBinding by viewBinding()
    private val viewModel by viewModels<ListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivNodata.let { Glide.with(requireContext()).load(R.drawable.ic_nodata).into(it) }
        setupRecyclerView()
        handleEvent()
        collectData()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            binding.recycler,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun handleEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                Log.i("123", event.toString())
                when (event) {
                    is ListViewModel.UIEvent.ShowSnackbar ->
                        showSnackbar(event.message)
                }
            }
        }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest {
                binding.pbGames.isVisible = it.isLoading
                gameAdapter.submitList(it.games)
                showAdCheck(it.games)
            }
        }
    }

    private fun showAdCheck(list: List<CityItem>) {
        binding.ivNodata.isVisible = list.isEmpty()
    }

    private fun setupRecyclerView() = binding.recycler.apply {
        adapter = gameAdapter
    }

    override fun onClick(cityItem: CityItem) {
        Log.i("123", "GIVE ${cityItem.id}")
     findNavController().navigate(MainFragmentDirections.
     actionMainFragmentToDetailsFragment(cityItem.id))
    }
}

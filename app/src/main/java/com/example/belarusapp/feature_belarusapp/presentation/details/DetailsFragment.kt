package com.example.belarusapp.feature_belarusapp.presentation.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.belarusapp.R
import com.example.belarusapp.databinding.FragmentDetailsBinding
import com.example.belarusapp.feature_belarusapp.domain.model.CityDetail
import com.example.belarusapp.feature_belarusapp.presentation.details.adapter.CityAdapter
import com.example.belarusapp.feature_belarusapp.presentation.details.adapter.PointOnClickListener
import com.google.android.material.snackbar.Snackbar

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details), PointOnClickListener {
    private val args: DetailsFragmentArgs by navArgs()
    private val binding: FragmentDetailsBinding by viewBinding()
    private val viewModel by viewModels<DetailViewModel>()
    private val gameAdapter by lazy(LazyThreadSafetyMode.NONE) { CityAdapter(this) }


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
                when (event) {
                    is DetailViewModel.UIEvent.ShowSnackbar ->
                        showSnackbar(event.message)
                }
            }
        }
    }

    private fun collectData() {
            viewModel.onSearch(args.id)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest {
                binding.pbGames.isVisible = it.isLoading
                gameAdapter.submitList(it.games)
                showAdCheck(it.games)

            }
        }
    }

    private fun showAdCheck(list: List<CityDetail>) {
        binding.ivNodata.isVisible = list.isEmpty()
    }

    private fun setupRecyclerView() = binding.recycler.apply {
        adapter = gameAdapter
    }

    override fun onClick(cityItem: CityDetail) {
        Log.i("123", cityItem.toString())
        findNavController().navigate(DetailsFragmentDirections
            .actionDetailsFragmentToPointFragment(cityItem))
    }
}
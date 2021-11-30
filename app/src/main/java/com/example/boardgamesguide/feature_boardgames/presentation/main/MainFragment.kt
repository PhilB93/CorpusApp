package com.example.boardgamesguide.feature_boardgames.presentation.main


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.boardgamesguide.R
import com.example.boardgamesguide.databinding.FragmentMainBinding
import com.example.boardgamesguide.feature_boardgames.domain.model.Game
import com.example.boardgamesguide.feature_boardgames.presentation.main.adapter.BoardGameOnClickListener
import com.example.boardgamesguide.feature_boardgames.presentation.main.adapter.SearchGamesAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), BoardGameOnClickListener {

    private val gameAdapter by lazy(LazyThreadSafetyMode.NONE) { SearchGamesAdapter(this) }
    private val binding: FragmentMainBinding by viewBinding()
    private val viewModel by viewModels<BoardGamesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeUiMode()
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        handleEvent()
        collectData()
        fetchGames()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            binding.recycler,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun handleEvent() {
        lifecycleScope.launchWhenCreated {
            viewModel.eventFlow.collectLatest { event ->
                Log.i("123", event.toString())
                when (event) {
                    is BoardGamesViewModel.UIEvent.ShowSnackbar ->
                        showSnackbar(event.message)

                }
            }
        }
    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collectLatest {
                binding.pbGames.isVisible = it.isLoading
                gameAdapter.submitList(it.games)
            }
        }
    }

    private fun observeUiMode() {
        lifecycleScope.launchWhenCreated {
            viewModel.darkThemeEnabled.collectLatest {
                val defaultMode = if (it) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(defaultMode)
            }
        }
    }

    private fun fetchGames() {
        binding.svGames.apply {
            onActionViewExpanded()
            clearFocus()
            queryHint = "Enter 'Catan' 'Bang' 'Thing' etc"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(name: String): Boolean {
                    if (name.isNotEmpty() && name.isNotBlank() && name.length in 2..20) {
                        binding.recycler.scrollToPosition(0)
                        viewModel.onSearch(name)
                        binding.svGames.clearFocus()
                    } else
                        showSnackbar("Name must contain 3-20 symbols :)")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
    }

    private fun setupRecyclerView() = binding.recycler.apply {
        adapter = gameAdapter
    }

    override fun onClick(game: Game) {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment(game))
    }


}
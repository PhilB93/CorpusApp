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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.job

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
//        handleSearchGames()
//        fetchGames()

        lifecycleScope.launchWhenCreated {
            viewModel.state.collectLatest {
                binding.pbGames.isVisible = it.isLoading
                gameAdapter.submitList(it.games)
            }
        }
        fetchGames()


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
            queryHint = "Enter at least 2 symbols"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(name: String?): Boolean {
                    if (name != null && name.isNotBlank()  && name.length > 1) {
                     //bang gameAdapter.submitList(emptyList())
                        binding.recycler.scrollToPosition(0)
                        viewModel.onSearch(name)
                        binding.svGames.clearFocus()
                    } else
                        Snackbar.make(
                            requireContext(),
                            binding.recycler,
                            "Name must contain at least 2 letters :)",
                            Snackbar.LENGTH_LONG
                        ).show()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("123", "Destroyed")
    }

//    private fun handleSearchGames() {
//        hideProgressBar()
//        binding.apply {
//            lifecycleScope.launchWhenCreated {
//                viewModel.searchGames.collectLatest {
//                    Log.i("123", it.toString())
//                    when (it) {
//                        is NetworkResult.LoadingState -> {
//                            showProgressBar()
//                        }
//                        is NetworkResult.DataState -> {
//                            hideProgressBar()
//                            if (it.data.isNotEmpty())
//                                gameAdapter.submitList(it.data)
//                            else {
//                                Snackbar.make(
//                                    requireContext(),
//                                    binding.recycler,
//                                    "No data",
//                                    Snackbar.LENGTH_LONG
//                                ).show()
//                            }
//                        }
//                        is NetworkResult.ErrorState -> {
//                            hideProgressBar()
//                            Snackbar.make(
//                                requireContext(),
//                                binding.recycler,
//                                it.message,
//                                Snackbar.LENGTH_LONG
//                            ).show()
//                        }
//                        is NetworkResult.EmptyState -> {
//                            hideProgressBar()
//                        }
//                    }
//
//                }
//            }
//        }
//    }

//    private fun fetchGames() {
//        binding.svGames.apply {
//            onActionViewExpanded()
//            clearFocus()
//            queryHint = "Enter at least 2 symbols"
//            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(name: String?): Boolean {
//                    if (name != null && name.length > 1) {
//                        gameAdapter.submitList(emptyList())
//                        binding.recycler.scrollToPosition(0)
//                        viewModel.searchGames(name)
//                        binding.svGames.clearFocus()
//                    } else
//                        Snackbar.make(
//                            requireContext(),
//                            binding.recycler,
//                            "Name must contain at least 2 letters :)",
//                            Snackbar.LENGTH_LONG
//                        ).show()
//                    return true
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    return true
//                }
//            })
//        }
//    }

    private fun setupRecyclerView() = binding.recycler.apply {
        adapter = gameAdapter
    }

    private fun hideProgressBar() {
        binding.pbGames.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.pbGames.visibility = View.VISIBLE
    }

    override fun onClick(game: Game) {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment(game))
    }


}
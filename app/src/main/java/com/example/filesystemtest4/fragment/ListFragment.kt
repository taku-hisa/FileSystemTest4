package com.example.filesystemtest4.fragment

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filesystemtest4.R
import com.example.filesystemtest4.UI.ItemDataAdapter
import com.example.filesystemtest4.UI.MainViewModel
import com.example.filesystemtest4.databinding.FragmentListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val category = arrayOf("A","B","C","D","E") //カテゴリを定義

class ListFragment : Fragment() {

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!
    private val args: ListFragmentArgs by navArgs()
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val Adapter = ItemDataAdapter(requireContext())

        lifecycleScope.launch {
            viewModel.getStream(args.category).collectLatest {
                Adapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            Adapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible =
                    (it.refresh is LoadState.Loading) or (it.append is LoadState.Loading)
            }
        }

        binding.recyclerView.apply {
            layoutManager =
                when {
                    resources.configuration.orientation
                            == Configuration.ORIENTATION_PORTRAIT
                    -> GridLayoutManager(context, 2)
                    else
                    -> GridLayoutManager(context, 4)
                }
            adapter = Adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
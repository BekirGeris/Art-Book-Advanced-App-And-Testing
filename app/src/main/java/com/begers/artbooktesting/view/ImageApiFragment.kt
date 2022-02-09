package com.begers.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.begers.artbooktesting.R
import com.begers.artbooktesting.adapter.ImageRecyclerAdapter
import com.begers.artbooktesting.databinding.FragmentImageApiBinding
import com.begers.artbooktesting.util.Status
import com.begers.artbooktesting.viewmodel.ArtViewModel
import com.bumptech.glide.RequestManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageApiFragment @Inject constructor(
    private val imageRecyclerAdapter: ImageRecyclerAdapter
) : Fragment(R.layout.fragment_image_api) {

    lateinit var viewModel: ArtViewModel

    private var fragmentBinding: FragmentImageApiBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ArtViewModel::class.java]

        val binding = FragmentImageApiBinding.bind(view)
        fragmentBinding = binding

        binding.imageRecyclerView.adapter = imageRecyclerAdapter
        binding.imageRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        subscribeToObservers()

        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)
        }

        var job: Job? = null

        binding.searchTxt.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if (it.toString().isNotEmpty()){
                        viewModel.searchImage(it.toString())
                    }
                }
            }
        }

    }

    fun subscribeToObservers() {
        viewModel.imageList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    val urls = it.data?.hits?.map { imageResult ->
                        imageResult.previewURL
                    }
                    imageRecyclerAdapter.images = urls ?: listOf()
                    fragmentBinding?.apiProBar?.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG).show()
                    fragmentBinding?.apiProBar?.visibility = View.GONE
                }
                Status.LOADING -> {
                    fragmentBinding?.apiProBar?.visibility = View.VISIBLE
                }
            }
        })
    }
}
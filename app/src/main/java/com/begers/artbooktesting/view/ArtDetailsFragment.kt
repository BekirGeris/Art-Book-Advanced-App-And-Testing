package com.begers.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.begers.artbooktesting.R
import com.begers.artbooktesting.databinding.FragmentArtDetailsBinding
import com.begers.artbooktesting.util.Status
import com.begers.artbooktesting.viewmodel.ArtViewModel
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_art_details) {

    private var fragmentBinding: FragmentArtDetailsBinding? = null
    lateinit var viewModel: ArtViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ArtViewModel::class.java]

        val binding = FragmentArtDetailsBinding.bind(view)
        fragmentBinding = binding

        binding.artDetailImageView.setOnClickListener {
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.detailSaveBtn.setOnClickListener {
            viewModel.makeArt(
                binding.artDetailNameTxt.text.toString(),
                binding.artDetailArtistNameTxt.text.toString(),
                binding.artDetailYearTxt.text.toString()
            )
        }

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer { url ->
            fragmentBinding?.let { binding ->
                glide.load(url).into(binding.imageView)
            }
        })

        viewModel.insertArtMsg.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    viewModel.resetArtMessage()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG).show()

                }
                Status.LOADING -> {

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentBinding = null
    }
}
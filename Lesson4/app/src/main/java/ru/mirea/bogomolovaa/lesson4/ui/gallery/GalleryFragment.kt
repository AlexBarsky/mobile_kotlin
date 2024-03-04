package ru.mirea.bogomolovaa.lesson4.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.mirea.bogomolovaa.lesson4.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment() {

    private var binding: FragmentGalleryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root = binding!!.root
        val textView = binding!!.textGallery

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
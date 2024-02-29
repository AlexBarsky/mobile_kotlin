package ru.mirea.bogomolovaa.mireaproject.ui.web

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {

    private var _binding: FragmentWebViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val webViewViewModel =
            ViewModelProvider(this)[WebViewViewModel::class.java]

        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val webView: WebView = binding.webView
        webViewViewModel.webView.observe(viewLifecycleOwner) {
            webView.loadUrl(it)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
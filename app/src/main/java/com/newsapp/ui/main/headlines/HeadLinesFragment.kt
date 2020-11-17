package com.newsapp.ui.main.headlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.newsapp.R
import com.newsapp.databinding.FragmentHeadLinesBinding

class HeadLinesFragment : Fragment() {

    private lateinit var binding : FragmentHeadLinesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeadLinesBinding.inflate(inflater, container, false)

        binding.btnFav.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.navigate_to_favorities)
        }

        return binding.root
    }
}
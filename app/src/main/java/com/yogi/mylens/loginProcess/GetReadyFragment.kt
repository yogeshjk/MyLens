package com.yogi.mylens.loginProcess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yogi.mylens.R
import com.yogi.mylens.databinding.FragmentGetReadyBinding


class GetReadyFragment : Fragment() {
    private lateinit var binding: FragmentGetReadyBinding
    private lateinit var navController: NavController


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        binding.saveButton.setOnClickListener{
                navController.navigate(R.id.action_getReadyFragment_to_loginFragment)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {
        binding = FragmentGetReadyBinding.inflate(inflater, container, false)
        return binding.root
    }
}
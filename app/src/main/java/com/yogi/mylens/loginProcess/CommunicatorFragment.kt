package com.yogi.mylens.loginProcess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yogi.mylens.R
import com.yogi.mylens.databinding.FragmentCommunicatorBinding
import com.yogi.mylens.databinding.FragmentSplasScreenBinding
import com.yogi.mylens.loginProcess.SharedConst
import com.yogi.mylens.loginProcess.SharedPref


class CommunicatorFragment : Fragment() {
    private lateinit var binding: FragmentCommunicatorBinding
    private lateinit var navController: NavController


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)


        SharedPref.init(requireContext())

        val isUserLoggedIn = SharedPref.getBoolean(SharedConst.IS_USER_LOGGED_IN)

        if (isUserLoggedIn) {
            navController.navigate(R.id.action_communicatorFragment_to_home)
        } else {
            navController.navigate(R.id.action_communicatorFragment_to_getReadyFragment)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {
        binding = FragmentCommunicatorBinding.inflate(inflater, container, false)
        return binding.root
    }
}



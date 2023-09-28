package com.yogi.mylens.fragment

import android.os.Bundle
import android.text.Editable
import android.text.Editable.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.yogi.mylens.R
import com.yogi.mylens.databinding.FragmentProfileBinding
import com.yogi.mylens.databinding.FragmentUpdateProfileBinding
import com.yogi.mylens.loginProcess.SharedConst
import com.yogi.mylens.loginProcess.SharedPref

class UpdateProfile : Fragment() {
    private lateinit var binding: FragmentUpdateProfileBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener{
            findNavController().popBackStack()
        }

        setUserDetails()

        binding.saveButton.setOnClickListener {
            val updatedName = binding.nameUpdate.text.toString()
            val updatedEmail = binding.emailUpdate.text.toString()

            updateUserProfile(updatedName, updatedEmail)
        }




    }

    private fun setUserDetails() {
            val a = SharedPref.getData(SharedConst.USER_NAME)
            val b = SharedPref.getData(SharedConst.USER_EMAIL)
            val c = SharedPref.getData(SharedConst.PHN_NO)

            val editableName = Factory.getInstance().newEditable(a)
            val editableEmail = Factory.getInstance().newEditable(b)

            binding.nameUpdate.text = editableName
            binding.emailUpdate.text = editableEmail
            binding.phoneNo.text = c
    }

    private fun updateUserProfile(updatedName: String, updatedEmail: String) {
        val db = FirebaseFirestore.getInstance()
        val userId = SharedPref.getData(SharedConst.PHN_NO)
        Log.d("fire", "UserID: $userId")

        if (userId != null) {
                val updatedUserData = hashMapOf(
                "name" to updatedName,
                "email" to updatedEmail
            )

            db.collection("users").document(userId).update(updatedUserData as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"Successfully Updated",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("fire", "Error updating user profile", e)
                    Toast.makeText(requireContext(), "Error Updating: " + e.message, Toast.LENGTH_SHORT).show()
                }
        }
    }





}
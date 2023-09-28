package com.yogi.mylens.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yogi.mylens.R
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yogi.mylens.activity.PhotographyActivity
import com.yogi.mylens.databinding.FragmentUserInfoBinding
import com.yogi.mylens.loginProcess.SharedPref

class UserInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var continueInfo: MaterialCardView
    private lateinit var mNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setHint()

        val arguments = arguments
        if (arguments != null) {
            mNumber = arguments.getString("mNumber").toString()
        }

        continueInfo = binding.infoContinue

        continueInfo.isEnabled = false
        continueInfo.alpha = 0.5F

        binding.name.addTextChangedListener(textWatcher)
        binding.email.addTextChangedListener(textWatcher)

        continueInfo.setOnClickListener {
            if (!email.contains("@") && !email.contains(".")) {
                Toast.makeText(requireContext(), "Invalid email!!", Toast.LENGTH_SHORT).show()
            } else {
                continueInfo.isEnabled = false
                continueInfo.alpha = 0.5F

                binding.infoProgressBar.visibility = View.VISIBLE
                binding.continueText.visibility = View.INVISIBLE

                storeUserData()
            }
        }
    }

    private fun storeUserData() {
        val db = FirebaseFirestore.getInstance()

        SharedPref.putData("username", name)
        SharedPref.putData("user_email", email)

        val user = hashMapOf(
            "name" to name,
            "email" to email
        )
        val mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.uid
        val userPhoneNumber = mNumber

        if (userId != null) {
            db.collection("users")
                .document(userPhoneNumber) // Use userPhoneNumber as the document ID
                .set(user)
                .addOnSuccessListener {

                    continueInfo.isEnabled = true
                    continueInfo.alpha = 1F

                    binding.infoProgressBar.visibility = View.INVISIBLE
                    binding.continueText.visibility = View.VISIBLE

                    val intent = Intent(requireActivity(), PhotographyActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error: $e", Toast.LENGTH_SHORT).show()
                }
        }
    }



    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            name = binding.name.text.toString()
            email = binding.email.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                continueInfo.alpha = 1F
                continueInfo.isEnabled = true
            }
        }
    }
}

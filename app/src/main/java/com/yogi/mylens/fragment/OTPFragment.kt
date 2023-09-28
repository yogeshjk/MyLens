package com.yogi.mylens.fragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.yogi.mylens.R
import com.yogi.mylens.activity.PhotographyActivity
import com.yogi.mylens.databinding.FragmentOTPBinding
import com.yogi.mylens.loginProcess.SharedConst
import com.yogi.mylens.loginProcess.SharedPref
import java.util.concurrent.TimeUnit

class OTPFragment : Fragment() {

    private lateinit var binding: FragmentOTPBinding
    private lateinit var resent: TextView
    private lateinit var otp: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mNumber: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var typeOtp: String
    private lateinit var continueOtp: MaterialCardView
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        mAuth = FirebaseAuth.getInstance()
        val arguments = arguments
        if (arguments != null) {
            otp = arguments.getString("OTP").toString()
            resendToken = arguments.getParcelable<PhoneAuthProvider.ForceResendingToken>("resendToken")!!
            mNumber = arguments.getString("mNumber").toString()
        }

       // Log.d("fire", "no. $mNumber")
        binding.sendSms.text = "Send via SMS to  $mNumber"


        continueOtp = binding.otpContinue
        resent = binding.resentOtpButton

        continueOtp.isEnabled = false
        continueOtp.alpha = 0.5F

        setTextWatcher()

        resent.visibility = View.INVISIBLE

        resent.setOnClickListener {
            resendOtp()
            resent.visibility = View.INVISIBLE
        }
        binding.anotherNumber.setOnClickListener {
            navController.popBackStack()
        }
        setTimer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOTPBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun setTextWatcher() {
        binding.otp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                typeOtp = binding.otp.text.toString()

                if (typeOtp.length == 6) {
                    continueOtp.alpha = 1F
                    continueOtp.isEnabled = true
                } else {
                    continueOtp.alpha = 0.5F
                    continueOtp.isEnabled = false
                }

                continueOtp.setOnClickListener {
                    continueOtp.isEnabled = false
                    continueOtp.alpha = 0.5F

                    binding.otpProgressBar.visibility = View.VISIBLE
                    binding.continueText.visibility = View.INVISIBLE

                    if (typeOtp.length != 6) {
                        // Handle the case of an invalid OTP
                    }
                    verifyOtp()
                    //setTimer()
                    resent.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun resendOtp() {

        resent.visibility = View.INVISIBLE

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(mNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                // Handle invalid credentials
            } else if (e is FirebaseTooManyRequestsException) {
                // Handle too many requests
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            otp = verificationId
            resendToken = token
        }
    }

    private fun verifyOtp() {
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
            otp, typeOtp
        )
        signInWithPhoneAuthCredential(credential)
    }

    private fun toMain() {
        val intent = Intent(requireActivity(), PhotographyActivity::class.java)
        startActivity(intent)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.additionalUserInfo
                    if (user != null) {
                        val bundle = Bundle()
                        bundle.putString("mNumber", mNumber)
                        SharedPref.putData(SharedConst.PHN_NO, mNumber)
                        if (user.isNewUser) {
                            SharedPref.putBoolean(SharedConst.IS_USER_LOGGED_IN, true)
                            findNavController().navigate(R.id.action_OTPFragment2_to_userInfoFragment2,bundle)

                        } else {
                                continueOtp.isEnabled = true
                                continueOtp.alpha = 1F

                                binding.otpProgressBar.visibility = View.INVISIBLE
                                binding.continueText.visibility = View.VISIBLE

                                Toast.makeText(requireContext(), "authSuccess", Toast.LENGTH_SHORT).show()
                                SharedPref.putBoolean(SharedConst.IS_USER_LOGGED_IN, true)
                                toMain()
                                }

                        }
                }

                else {
                    // Sign in failed, display a message and update the UI
                    Log.d("tag", "signInWithPhoneAuthCredential ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }

    private fun setTimer() {
        val totalTimeMillis = 60000L // 60 seconds
        val intervalMillis = 1000L // 1 second

        val timer = object : CountDownTimer(totalTimeMillis, intervalMillis) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.otpTimer.text = "00:$secondsRemaining"
            }

            override fun onFinish() {
                resent.visibility = View.VISIBLE
            }
        }

        timer.start()
    }
}


import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.yogi.mylens.R
import com.yogi.mylens.databinding.FragmentLoginBinding
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mNumber: String
    private lateinit var mobileNumber: EditText
    private lateinit var navController: NavController



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)

        mobileNumber = binding.mobileNumber
        mAuth = FirebaseAuth.getInstance()
        binding.loginContinue.isEnabled = false
        binding.loginContinue.alpha = 0.5F

        setHintColor()
        setTextWatcher()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setTextWatcher() {
        mobileNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                mNumber = mobileNumber.text.toString().trim()

                if (mNumber.length == 10) {
                    binding.loginContinue.alpha = 1F
                    binding.loginContinue.isEnabled = true
                } else {
                    binding.loginContinue.alpha = 0.5F
                    binding.loginContinue.isEnabled = false

                    binding.continueText.visibility = View.VISIBLE
                    binding.loginProgressBar.visibility = View.INVISIBLE
                }

                binding.loginContinue.setOnClickListener {
                    if (mNumber.length != 10) {
                        Toast.makeText(
                            requireContext(),
                            "Invalid number!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    mNumber = "+91$mNumber"

                    binding.loginContinue.isEnabled = false
                    binding.loginContinue.alpha = 0.5F

                    binding.continueText.visibility = View.INVISIBLE
                    binding.loginProgressBar.visibility = View.VISIBLE

                    sendOTP()
                }
            }
        })
    }

    private fun setHintColor() {
        val pinkColor = ContextCompat.getColor(requireContext(), R.color.deep_orange)
        val colorSpan = ForegroundColorSpan(pinkColor)

        val desc = "By continuing, I agree to the Term Use &\n" + "Privacy Policy "
        val spannable2 = SpannableString(desc)

        spannable2.setSpan(colorSpan, 30, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val colorSpan2 = ForegroundColorSpan(pinkColor)
        spannable2.setSpan(colorSpan2, 41, 55, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.termsCondition.text = spannable2
    }

    private fun sendOTP() {
        mAuth.signOut()

        val options =
            PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(mNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity()) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(
                    requireContext(),
                    "ON verification failed $e",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (e is FirebaseTooManyRequestsException) {
                Toast.makeText(
                    requireContext(),
                    "ON verification failed $e",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            val bundle = Bundle()
            bundle.putString("OTP", verificationId)
            bundle.putParcelable("resendToken", token)
            bundle.putString("mNumber", mNumber)
            navController.navigate(R.id.action_loginFragment_to_OTPFragment, bundle)


            binding.loginContinue.isEnabled = true
            binding.loginContinue.alpha = 1F

            binding.loginProgressBar.visibility = View.INVISIBLE
            binding.continueText.visibility = View.VISIBLE

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "authSuccess", Toast.LENGTH_SHORT).show()
                // Navigate to the next screen or perform any necessary actions.
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                }
            }
        }
    }
}

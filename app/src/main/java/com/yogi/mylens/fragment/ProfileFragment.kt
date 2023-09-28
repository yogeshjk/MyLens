import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yogi.mylens.R
import com.yogi.mylens.databinding.FragmentProfileBinding
import com.yogi.mylens.activity.GetStartActivity
import com.yogi.mylens.loginProcess.SharedConst
import com.yogi.mylens.loginProcess.SharedPref

class ProfileFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()

        setUserDetails()

        setLogout()

        binding.manageProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updateProfile)
        }

        binding.profileBack.setOnClickListener {
           findNavController().popBackStack()

        }

        return binding.root
    }

    private fun setLogout() {
        binding.logoutButton.setOnClickListener {
            mAuth.signOut()
            SharedPref.putBoolean(SharedConst.IS_USER_LOGGED_IN, false)
            SharedPref.putData(SharedConst.USER_NAME, "null")
            SharedPref.putData(SharedConst.USER_EMAIL, "null")

            startActivity(Intent(requireContext(), GetStartActivity::class.java))
            activity?.finish()
        }
    }

    private fun setUserDetails() {
        val n = SharedPref.getData(SharedConst.USER_NAME)
        if (n == "null") {
            fetchData()
        } else {
            binding.userName.text = SharedPref.getData(SharedConst.USER_NAME)
            binding.email.text = SharedPref.getData(SharedConst.USER_EMAIL)
        }
    }

    private fun fetchData() {
        val db = FirebaseFirestore.getInstance()
        val documentID = mAuth.currentUser?.uid

        if (documentID != null) {
            db.collection("users").document(documentID).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val x = it.getString("name")
                        val y = it.getString("email")
                        binding.userName.text = x
                        binding.email.text = y
                        x?.let { it1 -> SharedPref.putData(SharedConst.USER_NAME, it1) }
                        y?.let { it1 -> SharedPref.putData(SharedConst.USER_EMAIL, it1) }
                    }
                }
        }
    }
}

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.yogi.mylens.R
import com.yogi.mylens.dataClass.CategoryData
import com.yogi.mylens.dataClass.TopPhotoData
import com.yogi.mylens.databinding.FragmentHomeFragmnetBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yogi.mylens.activity.PhotographyActivity
import com.yogi.mylens.adapter.IdeaAdapter
import com.yogi.mylens.adapter.TopPhotoAdapter
import com.yogi.mylens.loginProcess.SharedConst
import com.yogi.mylens.loginProcess.SharedPref


class Home : Fragment() {
        private lateinit var binging: FragmentHomeFragmnetBinding
        private lateinit var categoryList: MutableList<CategoryData>
        private lateinit var topPhotographerList: MutableList<TopPhotoData>
        private lateinit var mAuth: FirebaseAuth
        private lateinit var navController: NavController
        private lateinit var district: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binging = FragmentHomeFragmnetBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance() // Initialize mAuth
        return binging.root
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Exit the app when the back button is pressed
                    requireActivity().finish()
                }
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

            navController=Navigation.findNavController(view)

            setCity()

            setProfileImage()

            topPhotographerList = mutableListOf()

//            if (topPhotographerList.isEmpty()) {
//                fetchTopPhotographer()
//            } else {
//                setTopPhotographerAdapter()
//            }

            setIdeaList()

            binging.citySelect.setOnClickListener {
               navController.navigate(R.id.action_home_to_selectCity)
            }

            binging.profile.setOnClickListener {
                navController.navigate(R.id.action_home_to_profileFragment)
            }


//            binging.seeAllTop.setOnClickListener {
//                val bundle = Bundle()
//                bundle.putString("key", "Top Photographers")
//                findNavController().navigate(R.id.action_home_to_photographers, bundle)
//            }

        }


        private fun setIdeaList() {

            var ideaList: MutableList<CategoryData> = mutableListOf()
            ideaList.add(CategoryData(R.drawable.ss1, "Bridal portrait"))
            ideaList.add(CategoryData(R.drawable.ss5, "Wedding style"))
            ideaList.add(CategoryData(R.drawable.ss3, "Romantic couple shot"))
            ideaList.add(CategoryData(R.drawable.ss4, "Mehandi"))

            val ideaRecycler: RecyclerView = binging.ideaRec

            ideaRecycler.apply {
                layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
                adapter = IdeaAdapter() {

                }
            }
            (ideaRecycler.adapter as IdeaAdapter).epList = ideaList
        }


        private fun setCity() {

            district= SharedPref.getData(SharedConst.DISTRICT).toString()
            binging.city.text=district
            if(district=="null"){
                binging.city.text=getString(R.string.delhi)
            }
        }

        private fun setProfileImage() {
            if(SharedPref.getData(SharedConst.USER_NAME)=="null"){
                val db = FirebaseFirestore.getInstance()
                val documentID = mAuth.currentUser?.uid

                if (documentID != null) {
                    db.collection("users").document(documentID).get()
                        .addOnSuccessListener {
                            if (it.exists()) {
                                val x = it.getString("name")?.capitalize()
                                val firstAlphabet: Char? = x?.get(0)
                                val y = firstAlphabet.toString()
                                SharedPref.putData(SharedConst.USER_NAME,y)
                            }
                        }
                }

            }
              val x = SharedPref.getData(SharedConst.USER_NAME)
              val firstAlphabet: Char? = x?.get(0)
              binging.userFirstWord.text = firstAlphabet.toString()
        }

//        private fun fetchTopPhotographer() {
//
//            val db = FirebaseFirestore.getInstance()
//
//            val fieldName = "rating"
//
//            db.collection("photographerInfo")
//                .orderBy(fieldName, Query.Direction.DESCENDING)
//                .limit(5)
//                .get()
//                .addOnSuccessListener {
//
//                    topPhotographerList.clear()
//                    for (document in it) {
//                        val data = document.data
//                        topPhotographerList.add(
//                            TopPhotoData(
//                                data["image"].toString(), data["name"].toString(),
//                                data["rating"].toString(), data["price"].toString()
//                            )
//                        )
//                    }
//
//                    setTopPhotographerAdapter()
//
//                }
//
//        }

//        private fun setTopPhotographerAdapter() {
//            val topPhotoRecycler: RecyclerView = binging.topPhotoRec
//
//            topPhotoRecycler.apply {
//                layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
//                adapter = TopPhotoAdapter {
//
//                    val bundle=Bundle()
//                    bundle.putString("key",it.PhotographerName)
//                    findNavController().navigate(R.id.action_home_to_details,bundle)
//
//                }
//            }
//            (topPhotoRecycler.adapter as TopPhotoAdapter).epList = topPhotographerList
//
//            // Toast.makeText(requireContext(), "fetch data successfully", Toast.LENGTH_SHORT).show()
//        }






    }
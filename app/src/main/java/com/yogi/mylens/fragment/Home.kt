import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.yogi.mylens.R
import com.yogi.mylens.dataClass.CategoryData
import com.yogi.mylens.dataClass.TopPhotoData
import com.yogi.mylens.databinding.FragmentHomeFragmnetBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.yogi.mylens.activity.ProfileActivity
import com.yogi.mylens.loginProcess.SharedConst
import com.yogi.mylens.loginProcess.SharedPref


class Home : Fragment() {

        private lateinit var binging: FragmentHomeFragmnetBinding
        private lateinit var categoryList: MutableList<CategoryData>
        private lateinit var topPhotographerList: MutableList<TopPhotoData>
        private lateinit var mAuth: FirebaseAuth
        private lateinit var navController: NavController
        private lateinit var city: String


    companion object {
            const val REQUEST_CODE = 123 // You can choose any unique request code
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                activity?.finish() // Finish the activity to exit the app
//            }
//        })
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            navController=Navigation.findNavController(view)


            setCity()

            setProfileImage()


            //setCategoryList()

            topPhotographerList = mutableListOf()

//            if (topPhotographerList.isEmpty()) {
//                fetchTopPhotographer()
//            } else {
//                setTopPhotographerAdapter()
//            }
//
//            setIdeaList()

            binging.citySelect.setOnClickListener {
               navController.navigate(R.id.action_home_to_selectCity)
            }

            binging.profile.setOnClickListener {
                val intent = Intent(requireContext(), ProfileActivity::class.java)
                startActivity(intent)
            }


//            binging.seeAllTop.setOnClickListener {
//                val bundle = Bundle()
//                bundle.putString("key", "Top Photographers")
//                findNavController().navigate(R.id.action_home_to_photographers, bundle)
//            }

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?




    ): View {

        binging = FragmentHomeFragmnetBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance() // Initialize mAuth
        return binging.root
    }





//        private fun setIdeaList() {
//
//            var ideaList: MutableList<CategoryData> = mutableListOf()
//            ideaList.add(CategoryData(R.drawable._2dd76f0e1ea89675802c343ff4ee261, "Bridal portrait"))
//            ideaList.add(CategoryData(R.drawable._c42b830b8533e6fff4b5caf9e9894fd, "Wedding style"))
//            ideaList.add(
//                CategoryData(
//                    R.drawable.ccad32362b224d1351d2063ee1fa224d,
//                    "Romantic couple shot"
//                )
//            )
//            ideaList.add(CategoryData(R.drawable.c0a48af4fd27fbeefc9448a397ef7a10, "Mehandi"))
//
//            val ideaRecycler: RecyclerView = binging.ideaRec
//
//            ideaRecycler.apply {
//                layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
//                adapter = IdeaAdapter() {
//
//                }
//            }
//            (ideaRecycler.adapter as IdeaAdapter).epList = ideaList
//        }

//        private fun setCategoryList() {
//
//            categoryList = mutableListOf()
//            categoryList.add(CategoryData(R.drawable.wedding_rings, "Wedding"))
//            categoryList.add(CategoryData(R.drawable.traveler, "Travel"))
//            categoryList.add(CategoryData(R.drawable.residential, "Commercial"))
//            categoryList.add(CategoryData(R.drawable.food, "Food"))
//            categoryList.add(CategoryData(R.drawable.wedding_rings, "College"))
//
//            val categoryRecycler: RecyclerView = binging.categoryRec
//
//            categoryRecycler.apply {
//                layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
//                adapter = CategoryAdapter() {
//
//                    val bundle=Bundle()
//                    bundle.putString("key",it.categoryName)
//
//                    findNavController().navigate(R.id.action_home_to_photographers,bundle)
//
//                }
//            }
//            (categoryRecycler.adapter as CategoryAdapter).epList = categoryList
//        }

        private fun setCity() {
            if(SharedPref.getData(SharedConst.DISTRICT)=="null"){
                val db = FirebaseFirestore.getInstance()
                val documentID = mAuth.currentUser?.uid

                if (documentID != null) {
                    db.collection("users").document(documentID).get()
                        .addOnSuccessListener {
                            if (it.exists()) {
                                val x = it.getString("city")
                                if (x != null) {
                                    SharedPref.putData(SharedConst.DISTRICT,x)
                                }
                            }
                            else{
                                SharedPref.putData(SharedConst.DISTRICT,"Delhi")
                            }
                        }
                }
            }
            city= SharedPref.getData(SharedConst.DISTRICT).toString()
            binging.city.text=city
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
                                binging.userFirstWord.text = firstAlphabet.toString()
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

//        private fun navigateToDestinationFragment() {
//            val photographerFragment = PhotographersFragment()
//            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//
//            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
//
//            // Replace the current fragment with the destination fragment
//            transaction.replace(R.id.container, photographerFragment)
//
//            // Optional: Add the transaction to the back stack (for back navigation)
//            transaction.addToBackStack(null)
//
//            // Commit the transaction
//            transaction.commit()
//        }




    }
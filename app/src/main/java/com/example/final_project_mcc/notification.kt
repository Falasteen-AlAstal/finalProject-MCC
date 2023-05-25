package com.example.final_project_mcc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class notification : Fragment() {
    lateinit var data: ArrayList<TopicMoodle>
    var db: FirebaseFirestore? = null
    var adapter: NotifecationAdapter? = null
    var recview: RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var  va = inflater.inflate(R.layout.fragment_notification, container, false)
        recview = va.findViewById<View>(R.id.recyclerViewn) as RecyclerView
        // recview!!.layoutManager = LinearLayoutManager(context)
        db = Firebase.firestore
        data= arrayListOf<TopicMoodle>()
        adapter = NotifecationAdapter(context, data)
        //recview!!.adapter = adapter
        getSubscribeCategory()

        return va
    }
    fun getSubscribeCategory() {
        db!!.collection("topic")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val id = document.id
                    val name = document.getString("name")
                    val notifications = TopicMoodle(
                        id,
                        name!!,
                    )
                    data.add(notifications)

                    // }
                }
                var notificationsUserAdapter = NotifecationAdapter(requireActivity(), data)
                recview!!.layoutManager = LinearLayoutManager(context)
                recview!!.adapter = adapter
                // hideDialog()
            }
            .addOnFailureListener {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                // hideDialog()
            }
    }


//    private fun getData() {
//        db!!.collection("users").whereEqualTo("role","Patient").get()
//            .addOnSuccessListener { querySnapshot ->
//                txt!!.setText(querySnapshot.documents!!.get(0).get("name").toString())
//            }
//    }

}
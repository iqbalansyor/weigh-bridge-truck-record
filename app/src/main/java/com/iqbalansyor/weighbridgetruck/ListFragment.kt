package com.iqbalansyor.weighbridgetruck

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iqbalansyor.weighbridgetruck.databinding.FragmentListBinding
import java.text.SimpleDateFormat
import java.util.*

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("/list")

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//
//            val newData = WeighBridgeTruck(date = "2024-26-03 04:47:09", driver = "roy")
//
//            myRef.push().setValue(newData)
//                .addOnSuccaqwessListener {
//                    Log.d(TAG, "Data saved successfully")
//                }
//                .addOnFailureListener { e ->
//                    Log.w(TAG, "Error writing data", e)
//                }
//        }

        // Add ValueEventListener to retrieve data
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //val value = dataSnapshot.getValue(WeighBridgeTruck::class.java)

                val dataArray = mutableListOf<WeighBridgeTruck>()

                for (childSnapshot in dataSnapshot.children) {
                    val data = childSnapshot.getValue(WeighBridgeTruck::class.java)
                    data?.let {
                        dataArray.add(it)
                        Log.d(TAG, "Value is: $it")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getCurrentUtcDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date())
    }

    companion object {
        val TAG = "First Fragment"
    }
}

data class WeighBridgeTruck(
    val date: String = "",
    val driver: String = ""
)
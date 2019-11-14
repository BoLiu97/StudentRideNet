package com.example.studentridenet.ui.passenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.studentridenet.R

class PassengerFragment : Fragment() {

    private lateinit var passengerViewModel: PassengerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        passengerViewModel =
            ViewModelProviders.of(this).get(PassengerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_passenger, container, false)

        return root
    }
}
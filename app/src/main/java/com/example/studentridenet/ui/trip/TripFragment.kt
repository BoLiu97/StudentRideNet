package com.example.studentridenet.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.studentridenet.R

class TripFragment : Fragment() {

    private lateinit var tripViewModel: TripViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tripViewModel =
            ViewModelProviders.of(this).get(TripViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_trip, container, false)
        return root
    }
}
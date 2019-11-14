package com.example.studentridenet.ui.safety

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.studentridenet.R

class SafetyFragment : Fragment() {

    companion object {
        fun newInstance() = SafetyFragment()
    }

    private lateinit var viewModel: SafetyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.safety_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SafetyViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

package com.example.studentridenet


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
 * A simple [Fragment] subclass.
 */
class msgIVFragment : Fragment() {

    lateinit var viewModel:ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ViewModel::class.java)
        } ?: throw Exception("bad activity")
        //viewModel.listener = this
        return inflater.inflate(R.layout.fragment_msg_iv, container, false)
    }

}

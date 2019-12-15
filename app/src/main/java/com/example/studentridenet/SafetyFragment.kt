package com.example.studentridenet

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.safety_fragment.*

class SafetyFragment : Fragment() {

    companion object {
        fun newInstance() = SafetyFragment()
    }


    private lateinit var viewModel: shareViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(shareViewModel::class.java)
        } ?: throw Exception("bad activity")
        //viewModel.listener = this
        return inflater.inflate(R.layout.safety_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_Call.setOnClickListener{
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + "911")
            startActivity(callIntent)
        }
    }


}

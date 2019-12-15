package com.example.studentridenet


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.fragment_welcome.*

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkLoginStatus()

        account_login.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_nav_setting)}


        account_return.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_nav_home)
        }

    }
    private fun checkLoginStatus() {

        var currentAccessToken = AccessToken.getCurrentAccessToken()

        if (currentAccessToken == null) { // did not login yet
            account_login.visibility = View.VISIBLE
            account_return.visibility = View.INVISIBLE
        }
        else { // have logged
            account_login.visibility = View.INVISIBLE
            account_return.visibility = View.VISIBLE
        }
    }



}

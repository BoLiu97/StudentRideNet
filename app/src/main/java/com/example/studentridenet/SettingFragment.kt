package com.example.studentridenet


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

import kotlinx.android.synthetic.main.setting_fragment.*
import org.json.JSONArray
import org.json.JSONObject

import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : Fragment() {

    lateinit var callbackManager: CallbackManager
    lateinit var viewModel: shareViewModel


     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(shareViewModel::class.java)
        } ?: throw Exception("bad activity")
        //viewModel.listener = this
        return inflater.inflate(R.layout.setting_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        callbackManager = CallbackManager.Factory.create()

        checkLoginStatus()

        profile_facebooklogin.setOnClickListener {
            val cb = MyFBCallback()
            LoginManager.getInstance()
                .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager, cb)
            checkLoginStatus()

        }





        profile_facebooklogout.setOnClickListener{
            LoginManager.getInstance().logOut()
            findNavController().navigate(R.id.action_nav_setting_self)
        }

        profile_logpage.setOnClickListener {
            findNavController().navigate(R.id.action_nav_setting_to_welcomeFragment)
        }

        profile_home_button.setOnClickListener {
            findNavController().navigate(R.id.action_nav_setting_to_nav_home)


        }
    }


    inner class MyFBCallback : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
//            profile_status.text = "FBLoginSuccess"
            if (result != null) {
                useLoginInformation(result.accessToken)
            }
            Toast.makeText(context, "log success", Toast.LENGTH_LONG).show()
            profile_facebooklogin.visibility = View.INVISIBLE
        }

        override fun onCancel() {
//            profile_status.text = "Login Canceled"
        }

        override fun onError(error: FacebookException?) {
//            profile_status.text = "Login Error"
        }

    }
    lateinit var listener: OnFragmentInteractionListener

    interface OnFragmentInteractionListener {
        fun updatepic()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
            listener = context
    }

    private fun useLoginInformation(accessToken: AccessToken) {


        val request = GraphRequest.newMeRequest(
            accessToken,
            object : GraphRequest.GraphJSONArrayCallback, GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(objects: JSONArray?, response: GraphResponse?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onCompleted(jobject: JSONObject?, response: GraphResponse?) {

                    if (jobject != null) {
                        Log.d("object1", jobject.toString())
                        profile_email.text = jobject.getString("email")
                        profile_name.text = jobject.getString("name")
                        profile_id.text = jobject.getString("id")
                        profile_facebooklogout.visibility = View.VISIBLE
                        profile_home_button.visibility = View.VISIBLE

                        val id: String = jobject.getString("id")

                        val image_url = "https://graph.facebook.com/$id/picture?type=normal"

                        viewModel.facebookname.value = jobject.getString("name")
                        viewModel.facebookid.value = jobject.getString("id")
                        viewModel.facebookemail.value = jobject.getString("email")

                        Glide.with(this@SettingFragment).load(image_url).into(profile_pic)

                        listener.updatepic()
                    }
                }
            }
        )


        val parameters = Bundle()
        parameters.putString("fields", "id , name  , email")
        request.parameters = parameters
        request.executeAsync()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkLoginStatus() {

        var currentAccessToken = AccessToken.getCurrentAccessToken()

        if (currentAccessToken == null) {
            profile_name.setText("")
            profile_email.setText("")
            profile_id.setText("")
            profile_facebooklogout.visibility = View.INVISIBLE
            profile_facebooklogin.visibility = View.VISIBLE
            profile_home_button.visibility = View.INVISIBLE
        }
        else {
            useLoginInformation(currentAccessToken)
            viewModel.facebookname.observe(this, androidx.lifecycle.Observer {
                profile_name.setText(it)
            })
            profile_facebooklogin.visibility = View.INVISIBLE
            profile_facebooklogout.visibility = View.VISIBLE
            profile_home_button.visibility = View.VISIBLE
        }
        viewModel.facebookname.value = profile_name.text.toString()

    }
}

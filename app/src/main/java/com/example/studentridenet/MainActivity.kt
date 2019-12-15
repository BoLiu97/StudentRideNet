package com.example.studentridenet

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.studentridenet.di.user
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header_main.*
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity(), SettingFragment.OnFragmentInteractionListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: shareViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewModel = ViewModelProviders.of(this).get(shareViewModel::class.java)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_help,
                R.id.nav_help, R.id.nav_driver, R.id.nav_passenger,
                R.id.nav_request,R.id.nav_search
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        checkLoginStatus()

        println("this is token in main activity fragment " + AccessToken.getCurrentAccessToken())
    }


    private fun checkLoginStatus() {

        var currentAccessToken = AccessToken.getCurrentAccessToken()

        if (currentAccessToken != null) {
            checklogininformation(currentAccessToken)
        }
    }

    private fun checklogininformation(accessToken: AccessToken) {


        val request = GraphRequest.newMeRequest(
            accessToken,
            object : GraphRequest.GraphJSONArrayCallback, GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(objects: JSONArray?, response: GraphResponse?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onCompleted(jobject: JSONObject?, response: GraphResponse?) {

                    if (jobject != null) {
                        Log.d("object1", jobject.toString())

                        val email = jobject.getString("email")

                        view_email.text = email

                        val name = jobject.getString("name")

                        view_name.text = name

                        val id: String = jobject.getString("id")

                        viewModel.curOOR.value!!.user_id = id
                        viewModel.curOOR.value!!.user_name = name
                        viewModel.curOOR.value!!.email = email


                        val image_url = "https://graph.facebook.com/$id/picture?type=normal"

                        Glide.with(this@MainActivity).load(image_url).into(view_pic)



                        viewModel.curUser.value = user(
                            name = name,
                            email = email,
                            user_id = id
                        )
                        viewModel.regeisterUser()

                    }
                }
            }
        )
        val parameters = Bundle()
        parameters.putString("fields", "id , name  , email")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



    override fun updatepic() {
        view_name.text = viewModel.facebookname.value

        view_email.text = viewModel.facebookemail.value


        Glide.with(this).load("https://graph.facebook.com/${viewModel.facebookid.value}/picture?type=normal").into(view_pic)

        viewModel.curUser.value = user(
            name = viewModel.facebookname.value.toString(),
            email = viewModel.facebookemail.value.toString(),
            user_id = viewModel.facebookid.value.toString()
        )
        viewModel.regeisterUser()

    }
}

package com.internshala.echo.activities

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.internshala.echo.R
import com.internshala.echo.activities.MainActivity.Statisfied.drawerLayout
import com.internshala.echo.adapters.NavigationDrawerAdapter
import com.internshala.echo.fragments.MainScreenFragment
import com.internshala.echo.fragments.SongPlayingFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    object Statisfied{
        var drawerLayout: DrawerLayout?=null
        var notificationManager: NotificationManager?=null
    }

    var trackNotificationBuilder: Notification?=null
    var navigationDrawerIconList: ArrayList<String> = arrayListOf()
    var imagesForNavDrawer = intArrayOf(
            R.drawable.navigation_allsongs,
            R.drawable.navigation_favorites,
            R.drawable.navigation_settings,
            R.drawable.navigation_aboutus
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar= findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        MainActivity.Statisfied.drawerLayout= findViewById(R.id.drawer_layout)

        navigationDrawerIconList.add("All Songs")
        navigationDrawerIconList.add("Favorites")
        navigationDrawerIconList.add("Settings")
        navigationDrawerIconList.add("About  Us")
        val toggle= ActionBarDrawerToggle(this@MainActivity,MainActivity.Statisfied.drawerLayout, toolbar ,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        MainActivity.Statisfied.drawerLayout?.setDrawerListener(toggle)

        val mainScreenFragment = MainScreenFragment()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.details_fragment, mainScreenFragment,"MainScreenFragment")
                .commit()

        val navigationAdapter= NavigationDrawerAdapter(navigationDrawerIconList,imagesForNavDrawer,this)
        navigationAdapter.notifyDataSetChanged()
        val navigationRecyclerView = findViewById<RecyclerView>(R.id.navigation_recycler_view)
        navigationRecyclerView.layoutManager = LinearLayoutManager(this)
        navigationRecyclerView.itemAnimator= DefaultItemAnimator()
        navigationRecyclerView.adapter = navigationAdapter
        navigationRecyclerView.setHasFixedSize(true)

        val intent = Intent(this@MainActivity, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this@MainActivity, System.currentTimeMillis().toInt(),
                intent, 0)
        trackNotificationBuilder= Notification.Builder(this)
                .setContentText("A track is playing in background")
                .setSmallIcon(R.drawable.echo_logo)
                .setContentIntent(pIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .build()
        Statisfied.notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStart() {
        super.onStart()
        try {
            Statisfied.notificationManager?.cancel(1978)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            if(SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                Statisfied.notificationManager?.notify(1978, trackNotificationBuilder)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            Statisfied.notificationManager?.cancel(1978)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}
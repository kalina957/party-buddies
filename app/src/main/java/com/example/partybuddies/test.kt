package com.example.partybuddies

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.kalina.membersdemo.dataObj.DataLv
import com.example.partybuddies.Fragments.FindBuddy_fragment
import com.example.partybuddies.adapters.AdapterLv
import com.example.partybuddies.adapters.CardStackAdapter
import com.example.partybuddies.adapters.UserListAdapter
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_party_info.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.find_parties_fragment.*
import kotlinx.android.synthetic.main.party_info.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class test : AppCompatActivity() {

    lateinit var lv: ListView
    lateinit var adapter: AdapterLv
    lateinit var allUsers:ArrayList<User>
    lateinit var partyUsers:ArrayList<DataLv>
    private var db = FirebaseFirestore.getInstance()
    lateinit var selectedParty: Party
    lateinit var nameView: TextView
    lateinit var dateView: TextView
    lateinit var locationView: TextView
    lateinit var timeView: TextView
    lateinit var descriptionView: TextView
    lateinit var img: ImageView
    var alreadyInParty: Boolean = false
    private lateinit var mAdapter: UserListAdapter
    private lateinit var mRecyclerView: RecyclerView

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10

    /*lateinit internal var pokerViewPager: ViewPager
    private var pagerAdapter: PokerPagerAdapter? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        //this.refreshList()
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (this.checkPermissionForLocation(this)) {
            this.startLocationUpdates()
        }
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps()
        }


        this.recyclerUsers.visibility = View.VISIBLE
        var mFragment: FindBuddy_fragment

        this.switchTinderMode.setOnCheckedChangeListener{
            _, isChecked ->
            if (isChecked){
                this.recyclerUsers.visibility = View.GONE
                val frame: FrameLayout = findViewById(R.id.frameLayoutTinderMode)
                mFragment = FindBuddy_fragment(this.selectedParty)
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                ft.add(frame.id, mFragment).commit()
                this.frameLayoutTinderMode.visibility = View.VISIBLE
                this.appBarPartyInfo.setExpanded(false)
//                FragmentManager fragmentManager = getFragmentManager()
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//                MyFragment fragment = new MyFragment();
//                fragmentTransaction.add(R.id.my_parent_layout, fragment);
//                fragmentTransaction.commit();
            }
            else{
                this.frameLayoutTinderMode.visibility = View.GONE
                this.recyclerUsers.visibility = View.VISIBLE
                this.appBarPartyInfo.setExpanded(true)
            }
        }

       /* allUsers = ArrayList()
        this.nameView = findViewById(R.id.textViewPartyName)
        this.descriptionView = findViewById(R.id.textViewDescription)
        this.timeView = findViewById(R.id.textViewTime)
        this.img = findViewById(R.id.imageView)
        nameView.text = selectedParty.name
        descriptionView.text = selectedParty.description
        timeView.text = selectedParty.startTime + " - " + selectedParty.endTime
        Glide.with(img).load(selectedParty.imgRef).into(img);
        this.db.collection("parties").addSnapshotListener{
                snapshot, ex ->
            if (ex !=null){
                Log.e("Firebase", "Exception: $ex.message")
                return@addSnapshotListener
            }


            this.refreshList()
            Log.d("Firebase", " it's there!")


        }*/
        this.refreshList()
        /*pokerViewPager = findViewById(R.id.pokerViewPager)
        pagerAdapter = PokerPagerAdapter(this)
        pokerViewPager.setAdapter(pagerAdapter)

        val cardFlipPageTransformer = CardFlipPageTransformer()
        cardFlipPageTransformer.isScalable = false
        cardFlipPageTransformer.flipOrientation = CardFlipPageTransformer.VERTICAL
        pokerViewPager.setPageTransformer(true, cardFlipPageTransformer)*/
    }
    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
                true
            }else{
                // Show the permission request
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have to add startlocationUpdate() method later instead of Toast
                Toast.makeText(this,"Permission granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun buildAlertMessageNoGps(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS is off, do you want to enabled it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_PERMISSION_LOCATION)
            }
            .setNegativeButton("No"){dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined
        mLastLocation = location
        val date: Date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("hh:mm:ss a")
        this.textViewPartyLocation2.text = "Latitude: " + mLastLocation.latitude + "Longitude: " + mLastLocation.longitude
        /*tvwTime.text = "Updated at: " + sdf.format(date)
        tvwLatitude.text = "Latitude: " + mLastLocation.latitude
        tvwLongitude.text = "Longitude: " + mLastLocation.longitude*/
    }

    protected fun startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.interval = INTERVAL
        mLocationRequest!!.fastestInterval = FASTEST_INTERVAL

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback,
            Looper.myLooper())
    }


    private fun stoplocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }

    fun refreshList(){
        allUsers = ArrayList()
        var user: User = User()
        this.db.collection("users").get()
            .addOnSuccessListener { result ->
                for (document in result){
                    if (document.toObject(User::class.java).id == "5lKMPWEGgqOKAK6WOo2k"){
                        user = document.toObject(User::class.java)
                    }
//                    this.data.add(document.toObject(DataLv::class.java))
                        this.allUsers.add(User(document.get("BandsRef") as ArrayList<String>,document.get("Music Genres") as ArrayList<String>,document.getString("id")!!,document.getString("name")!!,document.getString("city")!!,document.getString("imgRef")!!, coordinate = document.get("coordinate") as Coordinate))
                    Log.d("FirebaseHelper", "${document.id} => ${document.data}")
                    Log.d("FirebaseHelper", "Succes")
                    this.mRecyclerView = this.recyclerUsers
                    this.mAdapter = UserListAdapter(this.allUsers, user)
                    this.mRecyclerView.adapter = this.mAdapter
                    this.mRecyclerView.layoutManager = LinearLayoutManager(this.mRecyclerView.context)
                }
            }
            .addOnFailureListener {ex ->
                Log.d("FirebaseHelper", ex.message)
            }
    }
    /*private fun refreshList(){
        this.parties = ArrayList()
        this.db.collection("parties").get()
            .addOnSuccessListener { result ->
                for (document in result){
                    this.parties.add(document.toObject(Party::class.java))
                    this.mRecyclerView = this.tr
                    this.mAdapter = PartyListAdapter(this.parties)
                    this.mRecyclerView.adapter = this.mAdapter
                    this.mRecyclerView.layoutManager = LinearLayoutManager(this.tr.context)
                }
                Log.d("FirebaseHelper", "Succes")
            }
            .addOnFailureListener {ex ->
                Log.d("FirebaseHelper", ex.message)
            }
    }*/

    /*inner class PokerPagerAdapter(internal var context: Context) : PagerAdapter() {

        internal var mLayoutInflater: LayoutInflater
        lateinit internal var pages: ArrayList<Any>


        override fun getCount(): Int {
            return pages.size
        }

        init {
            mLayoutInflater = LayoutInflater.from(context)

            pages.add(Any())
            pages.add(Any())
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        // This method should create the page for the given position passed to it as an argument.
        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val rootView = mLayoutInflater.inflate(R.layout.card_image_layout, container, false)
            val imgCardSide = rootView.findViewById(R.id.imgCardSide)
            imgCardSide.setOnClickListener(View.OnClickListener {
                if (position == 0) {
                    pokerViewPager.setCurrentItem(1, true)
                } else {
                    pokerViewPager.setCurrentItem(0, true)
                }
            })
            val sides = intArrayOf(R.drawable.poker_card_front, R.drawable.poker_card_back)
            imgCardSide.setImageResource(sides[position])
            container.addView(rootView)
            return rootView

        }

        // Removes the page from the container for the given position.
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }*/
}

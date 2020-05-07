package com.example.partybuddies

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.kalina.membersdemo.dataObj.DataLv
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.party_info.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.location.Location
import android.location.LocationManager
import android.opengl.Visibility
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.graphics.toColor
import androidx.core.view.GravityCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.partybuddies.Fragments.FindBuddy_fragment
import com.example.partybuddies.adapters.*
import com.google.android.gms.location.*
import com.google.android.material.chip.Chip
import com.google.android.material.navigation.NavigationView
import com.instabug.library.Instabug
import com.instabug.library.Instabug.Builder
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_add_party.*
import kotlinx.android.synthetic.main.activity_party_info.*
import kotlinx.android.synthetic.main.activity_party_info_c.*
import kotlinx.android.synthetic.main.activity_party_info_c.view.*
import kotlinx.android.synthetic.main.find_parties_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class PartyInfoActivity : AppCompatActivity() {

    lateinit var lv: ListView
    lateinit var adapter: AdapterLv
    lateinit var adapterBands: ImageAdapter
    lateinit var allUsers: ArrayList<User>
    lateinit var partyUsers: ArrayList<DataLv>
    private var db = FirebaseFirestore.getInstance()
    lateinit var selectedParty: Party
    lateinit var nameView: TextView
    lateinit var dateView: TextView
    lateinit var locationView: TextView
    lateinit var timeView: TextView
    lateinit var descriptionView: TextView
    lateinit var date: TextView
    lateinit var location: TextView
    lateinit var img: ImageView
    var alreadyInParty: Boolean = false
    private lateinit var mAdapter: UserListAdapter
    private lateinit var mRecyclerView: RecyclerView
    private var users: ArrayList<User> = ArrayList()
    private val mImageUrls = java.util.ArrayList<String>()
    var ButtonsMusicGenres = java.util.ArrayList<Chip>()
    var selectedChips: ArrayList<String> = ArrayList<String>()
    lateinit var gridBands: GridView
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10
//    var chipMusic:Chip = findViewById(R.id.chipMusic)
//    var chipRock:Chip = findViewById(R.id.chipRock)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_info_c)
        this.addChips()
        if (checkPermissionForLocation(this)){
            startLocationUpdates()
        }

    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        buildAlertMessageNoGps()
    }

        this.selectedParty = intent.getSerializableExtra("party") as Party
        if (this.selectedParty.userRef == "5lKMPWEGgqOKAK6WOo2k") {
            this.btnJoin.visibility = View.INVISIBLE
            this.btnJoin.isEnabled = false
        } else {
            if (!this.selectedParty.usersGoingRef.isNullOrEmpty()) {
                for (u in this.selectedParty.usersGoingRef) {
                    if (u == "5lKMPWEGgqOKAK6WOo2k") {
                        this.btnJoin.text = "Leave"
                        this.btnJoin.background = getDrawable(R.drawable.btn_round_red)
                        this.alreadyInParty = true
                    }
                }
            }
        }
        allUsers = ArrayList()
        this.nameView = findViewById(R.id.textViewPartyName)
        this.descriptionView = findViewById(R.id.textViewDescription)
        this.timeView = findViewById(R.id.textViewTime)
        this.img = findViewById(R.id.imageView)
        this.date = findViewById(R.id.textViewDate)
        this.location = findViewById(R.id.textViewPartyLocation2)
        this.date.text = selectedParty.date
        this.location.text = selectedParty.location
        nameView.text = selectedParty.name
        descriptionView.text = selectedParty.description
        timeView.text = selectedParty.startTime + " - " + selectedParty.endTime
        Glide.with(img).load(selectedParty.imgRef).into(img);
        this.db.collection("parties").addSnapshotListener { _, ex ->
            if (ex != null) {
                Log.e("Firebase", "Exception: $ex.message")
                return@addSnapshotListener
            }
            this.refreshList()
            Log.d("Firebase", " it's there!")
        }
//        data.add(DataLv("Eindhoven","Steve","https://picsum.photos/seed/picsum/200/300"))
//        data.add(DataLv("Ema","Utrecht","https://picsum.photos/seed/picsum/200/300"))
//        data.add(DataLv("Steve","Amsterdam","https://picsum.photos/seed/picsum/200/300"))
//        data.add(DataLv("Noah","Eindhoven","https://picsum.photos/seed/picsum/200/300"))
//        data.add(DataLv("Steve","Den Bosch","https://picsum.photos/seed/picsum/200/300"))
//        data.add(DataLv("Ellen","Eindhoven","https://picsum.photos/seed/picsum/200/300"))


       // val imageView = findViewById(R.id.imageView) as ImageView

        this.recyclerUsers.visibility = View.VISIBLE
        frameLayoutTinderMode.visibility = View.GONE
        var mFragment: FindBuddy_fragment
        val scrollBar: HorizontalScrollView = findViewById(R.id.scrollViewBands)
        this.switchTinderMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                scrollBar.visibility = View.GONE
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
            } else {
                scrollBar.visibility = View.VISIBLE
                this.frameLayoutTinderMode.visibility = View.GONE
                this.recyclerUsers.visibility = View.VISIBLE
                this.appBarPartyInfo.setExpanded(true)
            }
        }
    }

    fun message(view: View){
        val intent = Intent(view.context, ChatLogActivity::class.java)
        startActivity(intent)
    }

    fun refreshList() {
        allUsers = ArrayList()
        var user: User = User()
        this.db.collection("users").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.toObject(User::class.java).id == "5lKMPWEGgqOKAK6WOo2k"){
                        user = document.toObject(User::class.java)
                    }
//                    this.data.add(document.toObject(DataLv::class.java))
                    if (this.selectedParty.usersGoingRef.contains(document.id)) {
                        Log.d("Firebase", "${document.toObject(User::class.java).name} ${document.toObject(User::class.java).musicGenres[0]}")
                        //Log.d("FirebaseT", "Music ${ClickChip(document.toObject(User::class.java).musicGenres)}")

                        if(selectedChips.count()==0 || selectedChips.contains("All")){
                            this.allUsers.add(document.toObject(User::class.java))
                        }
                        else
                        {
                        for (chip in this.selectedChips){

                            if(document.toObject(User::class.java).musicGenres.contains(chip)) {
                                this.allUsers.add(document.toObject(User::class.java))
                                break
                            }
                        }
                        }
//                        this.allUsers.add(document.toObject(User::class.java))
                    }


                    Log.d("FirebaseHelper", "${document.id} => ${document.data}")
                }
                Log.d("FirebaseHelper", "Succes")
                this.mRecyclerView = this.recyclerUsers
                this.mAdapter = UserListAdapter(this.allUsers, user)
                this.mRecyclerView.adapter = this.mAdapter
                this.mRecyclerView.layoutManager = LinearLayoutManager(this.mRecyclerView.context)

            }
            .addOnFailureListener { ex ->
                Log.d("FirebaseHelper", ex.message.toString())
            }
    }


    fun joinParty(view: View) {
        if (!this.alreadyInParty) {
            this.selectedParty.usersGoingRef.add("5lKMPWEGgqOKAK6WOo2k")
            this.db.collection("parties").document(this.selectedParty.id).set(selectedParty)
            this.alreadyInParty = true
            this.btnJoin.text = "Leave"
            this.btnJoin.setBackgroundResource(R.drawable.btn_round_red)
        } else {
            this.selectedParty.usersGoingRef.remove("5lKMPWEGgqOKAK6WOo2k")
            this.db.collection("parties").document(this.selectedParty.id).set(selectedParty)
            this.alreadyInParty = false
            this.btnJoin.text = "Join"
            this.btnJoin.setBackgroundResource(R.drawable.round_btn)
        }
    }

    fun findBuddy(view: View) {
        val intent = Intent(this, FindBuddy::class.java)
        startActivity(intent)
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

    fun onLocationChanged(location1: Location) {
        // New location has now been determined
        mLastLocation = location1
        val date: Date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("hh:mm:ss a")
        //this.location.text = "Latitude: ${mLastLocation.latitude} Longitude: ${mLastLocation.longitude}"
        this.addCooridate(mLastLocation.latitude, mLastLocation.longitude)
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

    private fun addCooridate(latitude: Double, longitude: Double){
        val coordinate = hashMapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )

        this.db.collection("users").document("5lKMPWEGgqOKAK6WOo2k")
            .update("coordinate",coordinate as Map<String, Any>)
            .addOnSuccessListener() { it ->
                //this.refreshList(true)
                Log.d("Firebase", "Coordinate has been updated")

                //db.collection("parties").document("one")
                /*.update(updates)
                .addOnSuccessListener { Log.d("FirebaseHelper", "DocumentSnapshot succesfully") }
                .addOnFailureListener { e -> Log.d("FirebaseHelper", e.message)}*/
                //this.finish()
            }
            .addOnFailureListener(){e ->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun updateCoordinate(coordinateId: String, latitude: String, longitude: String){
        this.db.collection("coordinate").document(coordinateId)
            .update("latitude", latitude, "longitude", longitude)
    }
    private fun addChips(){
        this.ButtonsMusicGenres.add(findViewById(R.id.chipMusic))
        this.ButtonsMusicGenres.add(findViewById(R.id.chipRock))
        this.ButtonsMusicGenres.add(findViewById(R.id.chipBlues))
        this.ButtonsMusicGenres.add(findViewById(R.id.chipElectronic))
        this.ButtonsMusicGenres.add(findViewById(R.id.chipHouse))
        this.ButtonsMusicGenres.add(findViewById(R.id.chipIndie))
        this.ButtonsMusicGenres.add(findViewById(R.id.chipJazz))
        this.ButtonsMusicGenres.add(findViewById(R.id.chipPop))
        this.ButtonsMusicGenres.add(findViewById(R.id.chipTechno))

        for (chip in ButtonsMusicGenres){
            chip.setOnClickListener{
                if (this.selectedChips.contains(chip.text.toString())){
                    Log.d("test", this.selectedChips.contains(chip.text.toString()).toString())
                    this.selectedChips.remove(chip.text.toString())
                    //Log.d("test", this.selectedChips.contains(chip.text.toString()).toString())
                }
                else{
                    this.selectedChips.add(chip.text.toString())
                }
                this.refreshList()
            }
        }
    }
/*private fun ClickChip(genres:ArrayList<String>):Boolean{
    addChips()

    var isSelected = false
    for(chip in ButtonsMusicGenres){
        chip.setOnClickListener(){v ->
            Log.d("Firebase", "${chip.text} ${genres[0]}")
            Log.d("Firebase", "${((chip.text).toString())==genres[0]}")
            if(genres.contains((chip.text).toString()))
            {
                Log.d("Result", "${isSelected}")
                isSelected = true
            }
        }
    }
    Log.d("EndResult", "${isSelected}")
return isSelected
}*/


    override fun onStop() {
        super.onStop()
        this.stoplocationUpdates()
    }

    override fun onStart() {
        super.onStart()
        this.startLocationUpdates()
    }

}

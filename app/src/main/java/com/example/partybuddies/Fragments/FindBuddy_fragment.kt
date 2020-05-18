package com.example.partybuddies.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.example.partybuddies.Callbacks.SpotDiffCallback
import com.example.partybuddies.Models.Coordinate
import com.example.partybuddies.Models.Party
import com.example.partybuddies.Models.User
import com.example.partybuddies.R
import com.example.partybuddies.Adapters.CardStackAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.find_buddy_fragment.*

class FindBuddy_fragment(selectedParty: Party): Fragment(), CardStackListener {

    private var db = FirebaseFirestore.getInstance()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var cardStackView: CardStackView
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter
    lateinit var btnMessage: FloatingActionButton
    var selectedParty: Party = selectedParty
    private val spots = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //this.refreshList()
        return inflater.inflate(R.layout.find_buddy_fragment, container, false)
        //this.createSpots()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /*view.fbtnMessage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(requireContext(), "test", Toast.LENGTH_LONG).show()
                Log.d("test", "testtes")
            }
        })*/
        this.drawerLayout = drawer_layout1
        this.cardStackView = this.card_stack_view1
        this.manager = CardStackLayoutManager(this.requireContext(), this)
//        this.btnMessage = view.findViewById(R.id.fbtnMessage)
//        this.fbtnMessage.setOnClickListener{
//
//                val intent = Intent(view.context, ChatLogActivity::class.java)
//                startActivity(intent)
//
//        }
        this.createSpots()
    }


    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if (manager.topPosition == adapter.itemCount) {
            paginate()
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
        Log.d("CardStackView", "onCardAppeared: ($position) ${textView.text}")
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
    }

    private fun setupNavigation() {
        // Toolbar
        //val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(toolbar)

        // DrawerLayout
        /* val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
         actionBarDrawerToggle.syncState()
         drawerLayout.addDrawerListener(actionBarDrawerToggle)*/

        // NavigationView
       /* val navigationView = this.navigation_view1
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.reload -> reload()
                R.id.add_spot_to_first -> addFirst(1)
                R.id.add_spot_to_last -> addLast(1)
                R.id.remove_spot_from_first -> removeFirst(1)
                R.id.remove_spot_from_last -> removeLast(1)
                R.id.replace_first_spot -> replace()
                R.id.swap_first_for_last -> swap()
            }
            drawerLayout.closeDrawers()
            true
        }*/
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun setupButton() {
        /*val skip = this.skip_button
        skip.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }

        val rewind = this.rewind_button
        rewind.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            cardStackView.rewind()
        }*/

        //val like = this.fbtnMessage
        /*like.setOnClickListener {
            /*val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()*/
        }*/
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun paginate() {
        val old = adapter.getSpots()
        val new = old.plus(createSpots())
        val callback =
            SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun reload() {
        val old = adapter.getSpots()
        val new = createSpots()
        val callback =
            SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addFirst(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<User>().apply {
            addAll(old)
            for (i in 0 until size) {
                add(manager.topPosition, createSpot())
            }
        }
        val callback =
            SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addLast(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<User>().apply {
            addAll(old)
            addAll(List(size) { createSpot() })
        }
        val callback =
            SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeFirst(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<User>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(manager.topPosition)
            }
        }
        val callback =
            SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeLast(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<User>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(this.size - 1)
            }
        }
        val callback =
            SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)

    }

    private fun replace() {
        val old = adapter.getSpots()
        val new = mutableListOf<User>().apply {
            addAll(old)
            removeAt(manager.topPosition)
            add(manager.topPosition, createSpot())
        }
        adapter.setSpots(new)
        adapter.notifyItemChanged(manager.topPosition)
    }

    private fun swap() {
        val old = adapter.getSpots()
        val new = mutableListOf<User>().apply {
            addAll(old)
            val first = removeAt(manager.topPosition)
            val last = removeAt(this.size - 1)
            add(manager.topPosition, last)
            add(first)
        }
        val callback =
            SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun createSpot(): User {
        return User(
            BandsRef = arrayListOf(
                "https://firebasestorage.googleapis.com/v0/b/party-buddies.appspot.com/o/aerosmith.jpg?alt=media&token=13ad7d17-58be-4e82-abfc-13631043da42",
                "https://firebasestorage.googleapis.com/v0/b/party-buddies.appspot.com/o/sabbath.png?alt=media&token=2813ed10-df7d-4df7-9476-455178d39ebd"
            ),
            userMusicGenres = arrayListOf("Rock", "Metal", "Grunge"),
            id = "5lKMPWEGgqOKAK6WOo2k",
            city = "Eindhoven",
            name = "Kalina",
            imgRef = "https://firebasestorage.googleapis.com/v0/b/party-buddies.appspot.com/o/girl3.jpg?alt=media&token=806a6ab5-04ee-48e3-8278-73807760fe50",
            coordinate = Coordinate(0.0, 0.0)
        )
    }

    private fun createSpots(): List<User> {

        /*spots.add(User(name = "Yasaka Shrine", city = "Kyoto", imgRef = "https://firebasestorage.googleapis.com/v0/b/party-buddies.appspot.com/o/boi1.jpeg?alt=media&token=92032a51-ec88-4042-ae8c-c8ea31d9764b"))
         spots.add(User(name = "Fushimi Inari Shrine", city = "Kyoto", imgRef = "https://firebasestorage.googleapis.com/v0/b/party-buddies.appspot.com/o/boi2.jpeg?alt=media&token=8f06172a-4563-4e47-9098-0e6f3c476612"))
         spots.add(User(name = "Bamboo Forest", city = "Kyoto", imgRef = "https://firebasestorage.googleapis.com/v0/b/party-buddies.appspot.com/o/boi3.jpeg?alt=media&token=3c4a63f3-5ca2-4fb2-9e0d-b91e6d39e448"))
         spots.add(User(name = "Brooklyn Bridge", city = "New York", imgRef = "https://firebasestorage.googleapis.com/v0/b/party-buddies.appspot.com/o/girl1.jpg?alt=media&token=d5a8634e-40d2-499d-a347-6666c9438f8e"))
         spots.add(User(name = "Empire State Building", city = "New York", imgRef = "https://firebasestorage.googleapis.com/v0/b/party-buddies.appspot.com/o/girl2.jpg?alt=media&token=f31a6cfe-9178-4fdc-a46f-3c5e470855a8"))
         spots.add(User(name = "The statue of Liberty", city = "New York", imgRef = "https://firebasestorage.googleapis.com/v0/b/party-buddies.appspot.com/o/girl3.jpg?alt=media&token=806a6ab5-04ee-48e3-8278-73807760fe50"))
         spots.add(User(name = "Louvre Museum", city = "Paris", imgRef = "https://source.unsplash.com/LrMWHKqilUw/600x800"))
         spots.add(User(name = "Eiffel Tower", city = "Paris", imgRef = "https://source.unsplash.com/HN-5Z6AmxrM/600x800"))
         spots.add(User(name = "Big Ben", city = "London", imgRef = "https://source.unsplash.com/CdVAUADdqEc/600x800"))
         spots.add(User(name = "Great Wall of China", city = "China", imgRef = "https://source.unsplash.com/AWh9C-QjhE4/600x800"))*/
        //this.users = ArrayList()
        /*this.db.collection("users").get()
            .addOnSuccessListener { result ->
                for (document in result){
                    spots.add(User(userMusicGenres = document.get("Music Genres") as ArrayList<String>,city = document.getString("city").toString(),name = document.getString("name").toString(),imgRef = document.getString("imgRef").toString()))
                }
                this.adapter = CardStackAdapter(spots)
                this.setupNavigation()
                this.setupCardStackView()
                this.setupButton()
            }
            .addOnFailureListener {ex ->
                Log.d("FirebaseHelper", ex.message.toString())
            }*/
        this.db.collection("users").get()
            .addOnSuccessListener { result ->
                for (document in result) {
//                    this.data.add(document.toObject(DataLv::class.java))
                    if (this.selectedParty.usersGoingRef.contains(document.id)){
                        spots.add(document.toObject(User::class.java))
                    }
                       /* this.spots.add(
                            User(
                                document.getString("id")!!,
                                document.getString("name")!!,
                                document.getString("city")!!,
                                document.getString("imgRef")!!
                            )
                        )*/
            }
                this.adapter = CardStackAdapter(spots)
                this.setupNavigation()
                this.setupCardStackView()
                this.setupButton()
            }
            .addOnFailureListener { ex ->
                Log.d("FirebaseHelper", ex.message.toString())
            }
        return spots
    }

}
package com.example.partybuddies.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPageAdapter: FragmentPagerAdapter{

    private val fragments: ArrayList<Fragment> = ArrayList()
    private val fragmentsTitles: ArrayList<String> = ArrayList()

    constructor(fm: FragmentManager?) : super(fm)


    override fun getItem(position: Int): Fragment {
        return this.fragments.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return this.fragmentsTitles.get(position)
    }


    override fun getCount(): Int {
        return this.fragments.size
    }

    fun addFragment(fragment: Fragment, title: String){
        this.fragments.add(fragment)
        this.fragmentsTitles.add(title)
    }



}
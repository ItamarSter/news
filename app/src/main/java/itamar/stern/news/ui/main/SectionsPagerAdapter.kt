package itamar.stern.news.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import itamar.stern.news.ui.categories.business.BusinessFragment
import itamar.stern.news.ui.categories.entertainment.EntertainmentFragment
import itamar.stern.news.ui.categories.favorites.FavoritesFragment
import itamar.stern.news.ui.categories.general.GeneralFragment
import itamar.stern.news.ui.categories.health.HealthFragment
import itamar.stern.news.ui.categories.science.ScienceFragment
import itamar.stern.news.ui.categories.sports.SportsFragment
import itamar.stern.news.ui.categories.technology.TechnologyFragment

class SectionsPagerAdapter(fa: FragmentActivity) :
    FragmentStateAdapter(fa) {

    override fun getItemCount() = 8

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                GeneralFragment()
            }
            1 -> {
                BusinessFragment()
            }
            2 -> {
                EntertainmentFragment()
            }
            3 -> {
                HealthFragment()
            }
            4 -> {
                ScienceFragment()
            }
            5 -> {
                SportsFragment()
            }
            6 -> {
                TechnologyFragment()
            }
            7 -> {
                FavoritesFragment()
            }

            else -> throw IllegalArgumentException("No such fragment")
        }
    }
}
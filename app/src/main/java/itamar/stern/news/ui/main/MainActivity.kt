package itamar.stern.news.ui.main


import android.graphics.Color
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import itamar.stern.news.R
import itamar.stern.news.databinding.ActivityMainBinding
import itamar.stern.news.NewsApplication

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        val tabColors = arrayOf(
            Color.BLUE,
            Color.YELLOW,
            Color.RED,
            Color.GREEN,
            Color.CYAN,
            Color.GRAY,
            Color.MAGENTA,
            Color.CYAN
        )
        val tabsNames = arrayOf(
            applicationContext.resources.getString(R.string.general_fragment),
            applicationContext.resources.getString(R.string.business_fragment),
            applicationContext.resources.getString(R.string.entertainment_fragment),
            applicationContext.resources.getString(R.string.health_fragment),
            applicationContext.resources.getString(R.string.science_fragment),
            applicationContext.resources.getString(R.string.sports_fragment),
            applicationContext.resources.getString(R.string.technology_fragment),
            applicationContext.resources.getString(R.string.favorites_fragment)
        )
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = tabsNames[position]
            //tab.view.setBackgroundColor(tabColors[position])
        }.attach()

        viewPager.currentItem = NewsApplication.whereToGoFromWelcome

    }



}
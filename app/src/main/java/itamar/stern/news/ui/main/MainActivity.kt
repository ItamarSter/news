package itamar.stern.news.ui.main



import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import itamar.stern.news.R
import itamar.stern.news.databinding.ActivityMainBinding
import itamar.stern.news.NewsApplication
import itamar.stern.news.models.Category



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        //livedata for handle logged-out user trying to mark favorites - send him to login:
        //(observe in onCreate)
        val goToLogin = MutableLiveData(false)
    }
    val tabsColors = arrayOf(
        R.drawable.layer_bg_selected_tab_general,
        R.drawable.layer_bg_selected_tab_business,
        R.drawable.layer_bg_selected_tab_entertainment,
        R.drawable.layer_bg_selected_tab_health,
        R.drawable.layer_bg_selected_tab_science,
        R.drawable.layer_bg_selected_tab_sports,
        R.drawable.layer_bg_selected_tab_technology,
        R.drawable.layer_bg_selected_tab_favorites
    )
    val tabsBacks = arrayOf(
        R.color.tab_general,
        R.color.tab_business,
        R.color.tab_entertainment,
        R.color.tab_health,
        R.color.tab_science,
        R.color.tab_sports,
        R.color.tab_technology,
        R.color.tab_favorites
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
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
            tab.view.setBackgroundResource(tabsBacks[position])
        }.attach()

        //Show the page which the user clicked on it in the welcome screen:
        viewPager.currentItem = NewsApplication.whereToGoFromWelcome

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.view.setBackgroundResource(tabsColors[tab.position])
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.view.setBackgroundResource(tabsBacks[tab.position])
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        //When logged-out user trying to mark favorites - send him to login:
        goToLogin.observe(this){
            if(it){
                tabs.getTabAt(7)?.view?.setBackgroundResource(tabsColors[7])
                viewPager.currentItem = Category.FAVORITES.second
            }
        }




    }



}
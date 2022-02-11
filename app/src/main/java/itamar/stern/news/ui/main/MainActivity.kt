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
            tab.view.setBackgroundResource(Category.tabsBacks[position])
        }.attach()

        //Show the page which the user clicked on it in the welcome screen:
        viewPager.currentItem = NewsApplication.whereToGoFromWelcome

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.view.setBackgroundResource(Category.tabsColors[tab.position])
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.view.setBackgroundResource(Category.tabsBacks[tab.position])
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        //When logged-out user trying to mark favorites - send him to login:
        goToLogin.observe(this){
            if(it){
                tabs.getTabAt(7)?.view?.setBackgroundResource(Category.tabsColors[7])
                viewPager.currentItem = Category.FAVORITES.second
            }
        }
    }
}
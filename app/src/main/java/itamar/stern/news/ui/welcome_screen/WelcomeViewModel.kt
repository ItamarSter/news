package itamar.stern.news.ui.welcome_screen;
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import itamar.stern.news.NewsApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WelcomeViewModel : ViewModel(){

    var welcomeNews = NewsApplication.repository.welcomeNewsList

    fun loadNews(
        callbackStartDownloading: () -> Unit,
        callbackFinishedDownloading: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                callbackStartDownloading()
            }
            NewsApplication.repository.fetchNewsForWelcome {
                welcomeNews.postValue(NewsApplication.repository.welcomeNewsList.value)
            }

            launch(Dispatchers.Main) {
                callbackFinishedDownloading()
            }
        }
    }
}
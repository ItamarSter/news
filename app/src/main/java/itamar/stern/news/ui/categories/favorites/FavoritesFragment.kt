package itamar.stern.news.ui.categories.favorites

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import itamar.stern.news.adapters.NewsAdapter
import itamar.stern.news.databinding.FavoritesFragmentBinding
import itamar.stern.news.view_model.ViewModel
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import itamar.stern.news.NewsApplication
import itamar.stern.news.R
import itamar.stern.news.ui.main.MainActivity


class FavoritesFragment : Fragment() {

    private lateinit var binding: FavoritesFragmentBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        binding = FavoritesFragmentBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //If we came here from logged-out user trying to mark favorites - return the livedata to false:
        MainActivity.goToLogin.postValue(false)

        if (NewsApplication.account == null) {
            showLoginViews()
        } else {
            showFavoritesViews()
        }

        binding.buttonSignIn.setOnClickListener {
            signInResult.launch(NewsApplication.mGoogleSignInClient.signInIntent)
        }
        binding.fabSignOut.setOnClickListener {
            NewsApplication.mGoogleSignInClient.signOut()
            showLoginViews()
            hideFavoritesViews()
        }

        //show the favorites:
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        viewModel.favorites.observe(viewLifecycleOwner) {
            binding.recyclerViewFavorites.adapter = NewsAdapter(it) { /*onClickNews callback*/ news ->
                viewModel.openNewsDialog(
                    requireContext(),
                    news,
                    binding.recyclerViewFavorites.height,
                    binding.recyclerViewFavorites.width
                )
            }
        }

    }

    private val signInResult = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        GoogleSignIn.getSignedInAccountFromIntent(result.data)
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "${requireContext().resources.getString(R.string.welcome)} ${it.displayName}",
                    Toast.LENGTH_SHORT
                ).show()
                hideLoginViews()
                showFavoritesViews()
            }
    }

    private fun showFavoritesViews() {
        binding.recyclerViewFavorites.visibility = View.VISIBLE
        binding.fabSignOut.visibility = View.VISIBLE
    }

    private fun hideFavoritesViews() {
        binding.recyclerViewFavorites.visibility = View.INVISIBLE
        binding.fabSignOut.visibility = View.INVISIBLE
    }

    private fun showLoginViews() {
        binding.buttonSignIn.visibility = View.VISIBLE
        binding.textViewSignIn.visibility = View.VISIBLE
    }

    private fun hideLoginViews() {
        binding.buttonSignIn.visibility = View.INVISIBLE
        binding.textViewSignIn.visibility = View.INVISIBLE
    }
}
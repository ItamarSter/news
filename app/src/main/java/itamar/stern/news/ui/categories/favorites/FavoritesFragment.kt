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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import itamar.stern.news.R


class FavoritesFragment : Fragment() {

    companion object {
        val userName = MutableLiveData("no one")
    }
    private lateinit var binding: FavoritesFragmentBinding
    private lateinit var viewModel: ViewModel
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        binding = FavoritesFragmentBinding.inflate(layoutInflater)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestProfile()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account == null) {
            showLoginViews()
        } else {
            userName.postValue(account.displayName)
            showFavoritesViews()
        }

        binding.buttonSignIn.setOnClickListener {
            signInResult.launch(mGoogleSignInClient.signInIntent)
        }
        binding.fabSignOut.setOnClickListener {
            mGoogleSignInClient.signOut()
            showLoginViews()
            hideFavoritesViews()
        }

        //show the favorites:
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        viewModel.favorites.observe(viewLifecycleOwner) {
            binding.recyclerViewFavorites.adapter = NewsAdapter(it) { news ->
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
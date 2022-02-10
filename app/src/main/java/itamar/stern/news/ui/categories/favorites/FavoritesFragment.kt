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
import itamar.stern.news.ui.view_model.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import android.app.Activity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.google.android.gms.tasks.Task
import itamar.stern.news.R


class FavoritesFragment : Fragment() {

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
            binding.buttonSignIn.visibility = View.VISIBLE
            binding.textViewSignIn.visibility = View.VISIBLE

            binding.buttonSignIn.setOnClickListener {
                exampleActivityResult.launch(mGoogleSignInClient.signInIntent)
            }
        } else {
            binding.recyclerViewFavorites.visibility = View.VISIBLE
        }

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

    private val exampleActivityResult = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        val task: Task<GoogleSignInAccount> =
            GoogleSignIn.getSignedInAccountFromIntent(result.data)
        task.addOnSuccessListener {
            Toast.makeText(requireContext(), "${requireContext().resources.getString(R.string.welcome)} ${it.displayName}", Toast.LENGTH_SHORT).show()
            binding.buttonSignIn.visibility = View.INVISIBLE
            binding.textViewSignIn.visibility = View.INVISIBLE
            binding.recyclerViewFavorites.visibility = View.VISIBLE
        }
    }
}
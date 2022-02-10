package itamar.stern.news.ui.categories.favorites

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import itamar.stern.news.adapters.NewsAdapter
import itamar.stern.news.databinding.FavoritesFragmentBinding
import itamar.stern.news.ui.view_model.ViewModel

class FavoritesFragment : Fragment() {

    private lateinit var binding:FavoritesFragmentBinding
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

        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        viewModel.favorites.observe(viewLifecycleOwner){
            binding.recyclerViewFavorites.adapter = NewsAdapter(it){ news->
                viewModel.openNewsDialog(requireContext(), news, binding.recyclerViewFavorites.height, binding.recyclerViewFavorites.width)
            }
        }
    }

}
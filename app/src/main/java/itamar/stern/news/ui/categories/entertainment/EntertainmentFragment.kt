package itamar.stern.news.ui.categories.entertainment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import itamar.stern.news.adapters.NewsAdapter
import itamar.stern.news.databinding.EntertainmentFragmentBinding
import itamar.stern.news.models.Category
import itamar.stern.news.utils.dp
import itamar.stern.news.view_model.ViewModel

class EntertainmentFragment : Fragment() {

    private lateinit var binding: EntertainmentFragmentBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        binding = EntertainmentFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //First loading:
        viewModel.loadNews("0", Category.ENTERTAINMENT.first, {}, {
            binding.progressBarEntertainment2.visibility = View.INVISIBLE
        })

        binding.recyclerViewEntertainment.layoutManager = LinearLayoutManager(requireContext())
        viewModel.allLoadedNewsLists[Category.ENTERTAINMENT.first]?.observe(viewLifecycleOwner){
            binding.recyclerViewEntertainment.adapter = NewsAdapter(it){ news->
                viewModel.openNewsDialog(requireContext(), news, binding.recyclerViewEntertainment.height, binding.recyclerViewEntertainment.width)
            }
        }

        viewModel.listenToScrollAndLoadMoreNews(binding.recyclerViewEntertainment, Category.ENTERTAINMENT.first,{
            //Show progressBar when downloading old news:
            binding.progressBarEntertainment.visibility = View.VISIBLE
        }){ position ->
            //Hide progressBar when finished download:
            binding.progressBarEntertainment.visibility = View.GONE
            //Scroll to position where we were before the downloading:
            val offset = binding.recyclerViewEntertainment.height
            (binding.recyclerViewEntertainment.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position-100, (offset+48.dp()).toInt())
        }
    }

}
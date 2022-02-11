package itamar.stern.news.ui.categories.technology

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import itamar.stern.news.adapters.NewsAdapter
import itamar.stern.news.databinding.TechnologyFragmentBinding
import itamar.stern.news.models.Category
import itamar.stern.news.utils.dp
import itamar.stern.news.view_model.ViewModel

class TechnologyFragment : Fragment() {

    private lateinit var binding: TechnologyFragmentBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TechnologyFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //First loading:
        viewModel.loadNews("0", Category.TECHNOLOGY.first, {}, {
            binding.progressBarTechnology2.visibility = View.INVISIBLE
        })

        binding.recyclerViewTechnology.layoutManager = LinearLayoutManager(requireContext())
        viewModel.allLoadedNewsLists[Category.TECHNOLOGY.first]?.observe(viewLifecycleOwner){
            binding.recyclerViewTechnology.adapter = NewsAdapter(it){ news->
                viewModel.openNewsDialog(requireContext(), news, binding.recyclerViewTechnology.height, binding.recyclerViewTechnology.width)
            }
        }

        viewModel.listenToScrollAndLoadMoreNews(binding.recyclerViewTechnology, Category.TECHNOLOGY.first,{
            //Show progressBar when downloading old news:
            binding.progressBarTechnology.visibility = View.VISIBLE
        }){ position ->
            //Hide progressBar when finished download:
            binding.progressBarTechnology.visibility = View.GONE
            //Scroll to position where we were before the downloading:
            val offset = binding.recyclerViewTechnology.height
            (binding.recyclerViewTechnology.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position-100, (offset+48.dp()).toInt())
        }
    }

}
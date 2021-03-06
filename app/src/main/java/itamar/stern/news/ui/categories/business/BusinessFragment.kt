package itamar.stern.news.ui.categories.business

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import itamar.stern.news.adapters.NewsAdapter
import itamar.stern.news.databinding.BusinessFragmentBinding
import itamar.stern.news.models.Category
import itamar.stern.news.utils.dp
import itamar.stern.news.view_model.ViewModel

class BusinessFragment : Fragment() {
    private lateinit var binding: BusinessFragmentBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        binding = BusinessFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //First loading:
        viewModel.loadNews("0", Category.BUSINESS.first, {}, {
            binding.progressBarBusiness.visibility = View.INVISIBLE
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.allLoadedNewsLists[Category.BUSINESS.first]?.observe(viewLifecycleOwner){
            binding.recyclerView.adapter = NewsAdapter(it){ news->
                viewModel.openNewsDialog(requireContext(), news, binding.recyclerView.height, binding.recyclerView.width)
            }
        }

        viewModel.listenToScrollAndLoadMoreNews(binding.recyclerView, Category.BUSINESS.first,{
            //Show progressBar when downloading old news:
            binding.progressBarBusinessMore.visibility = View.VISIBLE
        }){ position->
            //Hide progressBar when finished download:
            binding.progressBarBusinessMore.visibility = View.GONE
            //Scroll to position where we were before the downloading:
            val offset = binding.recyclerView.height
            (binding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position-100, (offset+48.dp()).toInt())
        }

    }

}
package itamar.stern.news.ui.categories.general

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import itamar.stern.news.adapters.NewsAdapter
import itamar.stern.news.databinding.GeneralFragmentBinding
import itamar.stern.news.models.Category
import itamar.stern.news.view_model.ViewModel
import itamar.stern.news.utils.dp

class GeneralFragment : Fragment() {
    private lateinit var binding: GeneralFragmentBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        binding = GeneralFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //First loading:
        viewModel.loadNews("0", Category.GENERAL.first, {}, {
            binding.progressBarGeneral2.visibility = View.INVISIBLE
        })

        binding.recyclerViewGeneral.layoutManager = LinearLayoutManager(requireContext())
        viewModel.allLoadedNewsLists[Category.GENERAL.first]?.observe(viewLifecycleOwner){
            binding.recyclerViewGeneral.adapter = NewsAdapter(it){ news->
                viewModel.openNewsDialog(requireContext(), news, binding.recyclerViewGeneral.height, binding.recyclerViewGeneral.width)
            }
        }

        viewModel.listenToScrollAndLoadMoreNews(binding.recyclerViewGeneral, Category.GENERAL.first,{
            //Show progressBar when downloading old news:
            binding.progressBarGeneral.visibility = View.VISIBLE
        }){ position ->
            //Hide progressBar when finished download:
            binding.progressBarGeneral.visibility = View.GONE
            //Scroll to position where we were before the downloading:
            val offset = binding.recyclerViewGeneral.height
            //48.dp = size of progressBar
            (binding.recyclerViewGeneral.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position-100, (offset+48.dp()).toInt())
        }
    }




}
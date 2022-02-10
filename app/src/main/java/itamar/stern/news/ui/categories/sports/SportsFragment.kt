package itamar.stern.news.ui.categories.sports

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import itamar.stern.news.adapters.NewsAdapter
import itamar.stern.news.databinding.SportsFragmentBinding
import itamar.stern.news.view_model.ViewModel

class SportsFragment : Fragment() {

    private lateinit var binding: SportsFragmentBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SportsFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //First loading:
        viewModel.loadNews("0", "sports", {}, {
            binding.progressBarSports2.visibility = View.INVISIBLE
        })

        binding.recyclerViewSports.layoutManager = LinearLayoutManager(requireContext())
        viewModel.allSportsNews.observe(viewLifecycleOwner){
            binding.recyclerViewSports.adapter = NewsAdapter(it){ news->
                viewModel.openNewsDialog(requireContext(), news, binding.recyclerViewSports.height, binding.recyclerViewSports.width)
            }
        }

        viewModel.listenToScrollAndLoadMoreNews(binding.recyclerViewSports, "sports",{
            //Show progressBar when downloading old news:
            binding.progressBarSports.visibility = View.VISIBLE
        }){ position ->
            //Hide progressBar when finished download:
            binding.progressBarSports.visibility = View.GONE
            //Scroll to position where we were before the downloading:
            val offset = binding.recyclerViewSports.height
            (binding.recyclerViewSports.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position-100, offset)
        }
    }

}
package itamar.stern.news.ui.categories.science

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import itamar.stern.news.adapters.NewsAdapter
import itamar.stern.news.databinding.ScienceFragmentBinding
import itamar.stern.news.view_model.ViewModel

class ScienceFragment : Fragment() {

    private lateinit var binding: ScienceFragmentBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScienceFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //First loading:
        viewModel.loadNews("0", "science", {}, {
            binding.progressBarScience2.visibility = View.INVISIBLE
        })

        binding.recyclerViewScience.layoutManager = LinearLayoutManager(requireContext())
        viewModel.allScienceNews.observe(viewLifecycleOwner){
            binding.recyclerViewScience.adapter = NewsAdapter(it){ news->
                viewModel.openNewsDialog(requireContext(), news, binding.recyclerViewScience.height, binding.recyclerViewScience.width)
            }
        }

        viewModel.listenToScrollAndLoadMoreNews(binding.recyclerViewScience, "science",{
            //Show progressBar when downloading old news:
            binding.progressBarScience.visibility = View.VISIBLE
        }){ position ->
            //Hide progressBar when finished download:
            binding.progressBarScience.visibility = View.GONE
            //Scroll to position where we were before the downloading:
            val offset = binding.recyclerViewScience.height
            (binding.recyclerViewScience.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position-100, offset)
        }
    }
}
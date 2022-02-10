package itamar.stern.news.ui.categories.health

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import itamar.stern.news.adapters.NewsAdapter
import itamar.stern.news.databinding.HealthFragmentBinding
import itamar.stern.news.ui.view_model.ViewModel

class HealthFragment : Fragment() {

    private lateinit var binding: HealthFragmentBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HealthFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //First loading:
        viewModel.loadNews("0", "health", {}, {
            binding.progressBarHealth2.visibility = View.INVISIBLE
        })

        binding.recyclerViewHealth.layoutManager = LinearLayoutManager(requireContext())
        viewModel.allHealthNews.observe(viewLifecycleOwner){
            binding.recyclerViewHealth.adapter = NewsAdapter(it){ news->
                viewModel.openNewsDialog(requireContext(), news, binding.recyclerViewHealth.height, binding.recyclerViewHealth.width)
            }
        }

        viewModel.listenToScrollAndLoadMoreNews(binding.recyclerViewHealth, "health",{
            //Show progressBar when downloading old news:
            binding.progressBarHealth.visibility = View.VISIBLE
        }){ position ->
            //Hide progressBar when finished download:
            binding.progressBarHealth.visibility = View.GONE
            //Scroll to position where we were before the downloading:
            val offset = binding.recyclerViewHealth.height
            (binding.recyclerViewHealth.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position-100, offset)
        }
    }

}
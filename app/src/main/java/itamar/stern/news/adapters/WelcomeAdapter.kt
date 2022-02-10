package itamar.stern.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import itamar.stern.news.R
import itamar.stern.news.databinding.WelcomeNewsItemBinding
import itamar.stern.news.models.News
import itamar.stern.news.utils.createDateString

class WelcomeAdapter(val news: List<News>, val callbackClickOnCategory:(String)->Unit) : RecyclerView.Adapter<WelcomeAdapter.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            WelcomeNewsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        with(holder.binding) {
            textViewTitle.text = news[position].title
            textViewSource.text = news[position].source
            textViewCategory.text = news[position].category.uppercase()
            textViewDateTime.text = createDateString(news[position].published_at)
            if (news[position].image != null) {
                Glide
                    .with(holder.binding.root.context)
                    .load(news[position].image)
                    .into(imageView)
                imageView.visibility = View.VISIBLE
                imageView.layoutParams.width = root.width / 3
            } else {
                //Don't show recycled images:
                imageView.visibility = View.GONE
            }
            root.setOnClickListener {
                it.setBackgroundColor(ContextCompat.getColor(root.context, R.color.gray))
                callbackClickOnCategory(news[position].category)
            }
        }
    }

    override fun getItemCount() = news.size

    class VH(val binding: WelcomeNewsItemBinding) : RecyclerView.ViewHolder(binding.root)

}
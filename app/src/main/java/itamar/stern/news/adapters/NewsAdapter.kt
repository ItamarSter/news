package itamar.stern.news.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import itamar.stern.news.databinding.NewsItemBinding
import itamar.stern.news.models.News
import itamar.stern.news.ui.NewsApplication
import itamar.stern.news.utils.createDateString

class NewsAdapter(val news: List<News>, val callbackClickOnNews: (news:News)->Unit) : RecyclerView.Adapter<NewsAdapter.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            NewsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        with(holder.binding) {
            textViewTitle.text = news[position].title
            textViewSource.text = news[position].source
            textViewDateTime.text = createDateString(news[position].published_at)
            if (news[position].image != null) {
                Glide
                    .with(holder.binding.root.context)
                    .asBitmap()
                    .load(news[position].image)
                    .thumbnail(0.05f)
                    .apply(
                        RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL)
                    )
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            imageView.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            imageView.visibility = View.VISIBLE
                            NewsApplication.bitmapImages[news[position].image!!] = resource
                            return false
                        }
                    })
                    .into(imageView)
                imageView.visibility = View.VISIBLE
                imageView.layoutParams.width = root.width / 3
            } else {
                //Don't show recycled images:
                imageView.visibility = View.GONE
            }
            root.setOnClickListener {
                callbackClickOnNews(news[position])
            }
        }
    }

    override fun getItemCount() = news.size

    class VH(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root)

}
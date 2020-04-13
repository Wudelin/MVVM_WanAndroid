package com.wdl.wanandroid.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wdl.wanandroid.bean.BannerData
import com.youth.banner.adapter.BannerAdapter

/**
 * Create by: wdl at 2020/4/13 17:13
 */
class ImgBannerAdapter(val context: Context, val data: List<BannerData>) :
    BannerAdapter<BannerData, ImgBannerAdapter.BannerVH>(data) {

    class BannerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView as ImageView
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerVH {
        val imageView = ImageView(parent!!.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                //注意，必须设置为match_parent，这个是viewpager2强制要求的
                //注意，必须设置为match_parent，这个是viewpager2强制要求的
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return BannerVH(imageView)
    }

    override fun onBindView(holder: BannerVH?, data: BannerData?, position: Int, size: Int) {
        Glide.with(context).load(data?.imagePath).apply(RequestOptions.centerCropTransform())
            .into(holder!!.imageView)
    }
}
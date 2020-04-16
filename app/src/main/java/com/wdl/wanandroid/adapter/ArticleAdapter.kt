package com.wdl.wanandroid.adapter


import androidx.recyclerview.widget.DiffUtil
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BasePagedListAdapter
import com.wdl.wanandroid.base.BaseViewHolder
import com.wdl.wanandroid.databinding.ArticleItemLayoutBinding
import com.wdl.wanandroid.db.bean.HomeArticleDetail

/**
 * Create by: wdl at 2020/4/15 15:15
 */
class ArticleAdapter :
    BasePagedListAdapter<HomeArticleDetail, ArticleItemLayoutBinding>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<HomeArticleDetail> =
            object : DiffUtil.ItemCallback<HomeArticleDetail>() {
                override fun areItemsTheSame(
                    oldItem: HomeArticleDetail,
                    newItem: HomeArticleDetail
                ): Boolean = oldItem.aId == newItem.aId

                override fun areContentsTheSame(
                    oldItem: HomeArticleDetail,
                    newItem: HomeArticleDetail
                ): Boolean = oldItem == newItem

            }
    }

    override fun getLayoutId(viewType: Int): Int = R.layout.article_item_layout

    override fun bindData(
        data: HomeArticleDetail,
        holder: BaseViewHolder<ArticleItemLayoutBinding>,
        position: Int
    ) {
        holder.binding.model = data
    }

}


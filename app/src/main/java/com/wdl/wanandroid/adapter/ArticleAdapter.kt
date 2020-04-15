package com.wdl.wanandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseViewHolder
import com.wdl.wanandroid.databinding.ArticleItemLayoutBinding
import com.wdl.wanandroid.db.bean.HomeArticleDetail

/**
 * Create by: wdl at 2020/4/15 15:15
 */
class ArticleAdapter :
    PagedListAdapter<HomeArticleDetail, HomePageVH>(DIFF_CALLBACK) {

    private var mDiffer: AsyncPagedListDiffer<HomeArticleDetail>? = null

    init {
        mDiffer = AsyncPagedListDiffer(this, DIFF_CALLBACK)
        setHasStableIds(true)
    }

    private fun getItemData(position: Int): HomeArticleDetail? = getItem(position)
//
//    override fun getItemCount(): Int = mDiffer?.itemCount ?: 0
//
//    override fun getItemId(position: Int): Long = generateItemId(mDiffer, position)

//    override fun submitList(pagedList: PagedList<HomeArticleDetail>?) {
//        pagedList?.addWeakCallback(pagedList.snapshot(), object : BasePagedListCallback() {
//            override fun onInserted(position: Int, count: Int) {
//                mDiffer?.submitList(pagedList)
//            }
//        })
//    }

    private fun generateItemId(
        differ: AsyncPagedListDiffer<HomeArticleDetail>?,
        position: Int
    ): Long =
        differ?.getItem(position)?.id?.toLong() ?: 0L

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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomePageVH {
        return HomePageVH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.article_item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomePageVH, position: Int) {
        val data = getItemData(position) ?: return
        holder.binding.executePendingBindings()
        (holder.binding as ArticleItemLayoutBinding).model = data
    }
}

class HomePageVH(bing: ViewDataBinding) :
    BaseViewHolder<ArticleItemLayoutBinding>(bing)


abstract class BasePagedListCallback : PagedList.Callback() {
    override fun onChanged(position: Int, count: Int) {

    }

    override fun onInserted(position: Int, count: Int) {

    }

    override fun onRemoved(position: Int, count: Int) {

    }
}
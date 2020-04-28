package com.wdl.wanandroid.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wdl.wanandroid.R
import com.wdl.wanandroid.db.bean.PopUrlBean
import java.util.*

/**
 * Create by: wdl at 2020/4/28 14:59
 * 热门网址 适配器
 */
class UrlAdapter : BaseQuickAdapter<PopUrlBean, BaseViewHolder>(R.layout.flow_layout) {
    override fun convert(holder: BaseViewHolder, item: PopUrlBean) {
        holder.setText(R.id.tv, item.name)
        holder.setTextColor(R.id.tv, randomColor())
    }

    /**
     * 获取随机rgb颜色值
     */
    private fun randomColor(): Int {
        Random().run {
            //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
            val red = nextInt(190)
            val green = nextInt(190)
            val blue = nextInt(190)
            //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
            return Color.rgb(red, green, blue)
        }
    }
}
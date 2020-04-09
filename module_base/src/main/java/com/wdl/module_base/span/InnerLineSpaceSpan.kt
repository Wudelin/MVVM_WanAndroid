package com.wdl.module_base.span

import android.graphics.Paint
import android.text.style.LineHeightSpan
import kotlin.math.roundToInt

/**
 * Create by: wdl at 2020/4/9 11:48
 * 百度技术： TextView行间距适配
 * https://mp.weixin.qq.com/s/xQ5I0omWC8-6W4RAYl0hkA
 */
class InnerLineSpaceSpan(private val mHeight: Int) : LineHeightSpan {
    override fun chooseHeight(
        text: CharSequence?,
        start: Int,
        end: Int,
        spanstartv: Int,
        lineHeight: Int,
        fm: Paint.FontMetricsInt?
    ) {

        // 原始行高
        val originHeight: Int? = fm?.let {
            it.descent - it.ascent
        }
        if (originHeight!! <= 0) {
            return
        }
        // 计算比例值
        val ratio = mHeight * 1.0F / originHeight
        // 根据最新行高，修改descent
        fm.descent = (fm.descent * ratio).roundToInt()
        // 根据最新行高，修改ascent
        fm.ascent = fm.descent - mHeight
    }
}
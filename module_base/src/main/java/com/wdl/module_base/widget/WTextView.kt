package com.wdl.module_base.widget

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.AttributeSet
import com.wdl.module_base.span.InnerLineSpaceSpan


/**
 * Create by: wdl at 2020/4/9 11:58
 * 百度技术： TextView行间距适配
 * https://mp.weixin.qq.com/s/xQ5I0omWC8-6W4RAYl0hkA
 */
class WTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    /**
     * 排除每行文字间的padding
     *
     * @param text
     */
    public fun setCustomText(text: CharSequence?) {
        if (text == null) {
            return
        }

        // 获得视觉定义的每行文字的行高
        val lineHeight = textSize.toInt()
        val ssb: SpannableStringBuilder
        if (text is SpannableStringBuilder) {
            ssb = text
            // 设置LineHeightSpan
            ssb.setSpan(
                InnerLineSpaceSpan(lineHeight),
                0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            ssb = SpannableStringBuilder(text)
            // 设置LineHeightSpan
            ssb.setSpan(
                InnerLineSpaceSpan(lineHeight),
                0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // 调用系统setText()方法
        setText(ssb)
    }
}
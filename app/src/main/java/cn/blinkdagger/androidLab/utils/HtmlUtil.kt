package cn.blinkdagger.androidLab.utils

import android.text.Html
import android.text.Spanned
import android.text.TextUtils

/**
 * @Author ls
 * @Date 2018/10/30
 * @Description
 * @Version
 */
object HtmlUtil {
    /**
     * 过滤空格
     *
     * @param body
     * @return
     */
    fun fillBR(body: String?): String {
        var body = body
        if (body != null) {
            body = body.trim { it <= ' ' }
            body = body.replace("\n".toRegex(), "")
            body = body.replace("\r".toRegex(), "")
            body = body.replace("\t".toRegex(), "")
            body = body.replace("(<br />){2,}".toRegex(), "<br />")
            body = body.replace("(<div>&nbsp;</div>){2,}".toRegex(), "<div>&nbsp;</div>")
            body = body.replace("<p><br/></p>".toRegex(), "")
            body = body.replace("<p ><br/></p>".toRegex(), "")
            body = body.replace("<p ><span ><br/></span></p>".toRegex(), "")
            body = body.replace("(&nbsp; ){2,}".toRegex(), "&nbsp;")
        } else {
            body = ""
        }
        return body
    }

    /**
     * 清除多余的换行
     *
     * @param body
     * @return
     */
    fun fliterBR(body: String?): String {
        var body = body
        if (body != null) {
            body = body.replace("\n{2,}".toRegex(), "\n")
            body = body.replace("<br />\n<br />", "<br />")
            body = body.replace("<br /><br />", "<br />")
            body = body.replace("<br />\n\r<br />", "<br />")
            body = body.replace("<br />\r<br />", "<br />")
            body = body.replace("\r<br />", "<br />")
            body = body.trim { it <= ' ' }
            if (body.endsWith("<br />")) {
                body = body.substring(0, body.lastIndexOf("<br />"))
            }
        } else {
            body = ""
        }
        return body
    }

    /**
     * 过滤图片
     *
     * @param html
     * @return
     */
    fun fliterImageFromhtml(html: String): Spanned? {
        return if (TextUtils.isEmpty(html)) {
            null
        } else Html.fromHtml(html.replace("(\uFFFC|\\n)".toRegex(), ""))
    }
}
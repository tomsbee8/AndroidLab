package cn.blinkdagger.androidLab.Utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * @Author ls
 * @Date 2018/10/30
 * @Description
 * @Version
 */
public class HtmlUtil {

    /**
     * 过滤空格
     *
     * @param body
     * @return
     */
    public static String fillBR(String body) {
        if (body != null) {
            body = body.trim();
            body = body.replaceAll("\n", "");
            body = body.replaceAll("\r", "");
            body = body.replaceAll("\t", "");
            body = body.replaceAll("(<br />){2,}", "<br />");
            body = body.replaceAll("(<div>&nbsp;</div>){2,}", "<div>&nbsp;</div>");
            body = body.replaceAll("<p><br/></p>", "");
            body = body.replaceAll("<p ><br/></p>", "");
            body = body.replaceAll("<p ><span ><br/></span></p>", "");
            body = body.replaceAll("(&nbsp; ){2,}", "&nbsp;");
        } else {
            body = "";
        }
        return body;
    }

    /**
     * 清除多余的换行
     *
     * @param body
     * @return
     */
    public static String fliterBR(String body) {
        if (body != null) {
            body = body.replaceAll("\n{2,}", "\n");
            body = body.replace("<br />\n<br />", "<br />");
            body = body.replace("<br /><br />", "<br />");
            body = body.replace("<br />\n\r<br />", "<br />");
            body = body.replace("<br />\r<br />", "<br />");
            body = body.replace("\r<br />", "<br />");
            body = body.trim();
            if (body.endsWith("<br />")) {
                body = body.substring(0, body.lastIndexOf("<br />"));
            }
        } else {
            body = "";
        }
        return body;
    }

    /**
     * 过滤图片
     *
     * @param html
     * @return
     */
    public static Spanned fliterImageFromhtml(String html) {
        if (TextUtils.isEmpty(html)) {
            return null;
        }
        Spanned result = Html.fromHtml(html.replaceAll("(\uFFFC|\\n)", ""));
        return result;
    }
}

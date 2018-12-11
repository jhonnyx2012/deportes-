package com.core.util;

import android.graphics.Paint;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by juan.bustamante on 6/03/2017.
 */

public class StringUtils {

    private static final String EMPTY_STRING = "";
    public static final String STRING_CODIGN_UTF = "UTF-8";
    public static final String STRING_CODING_ISO8859 = "ISO-8859-1";


    public static String capitalize(String text, String separator, String appendText) {
        String temp;
        String[] words = text.split(separator);
        if (words.length == 0) {
            words = new String[]{text};
        }

        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            temp = words[0].trim();

            if (temp.length() > 0) {
                sb.append(Character.toUpperCase(temp.charAt(0)));
                if (temp.length() > 1) {
                    sb.append(temp.subSequence(1, temp.length()).toString().toLowerCase());
                }
            }

            for (int i = 1; i < words.length; i++) {
                sb.append(appendText);
                temp = words[i].trim();
                if (temp.length() > 0) {
                    sb.append(Character.toUpperCase(temp.charAt(0)));
                    if (temp.length() > 1)
                        sb.append(temp.subSequence(1, temp.length()).toString().toLowerCase());
                }
            }
        }
        return sb.toString();
    }

    public static String decodeIsoString(String unicodeText) {
        String name = EMPTY_STRING;
        try {
            name = new String(unicodeText.getBytes(STRING_CODING_ISO8859), STRING_CODIGN_UTF);
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        return Html.fromHtml(name).toString();
    }

    public static void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            //span = new URLSpanNoUnderline(span.getURL());
            //s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    public static String breakLongText(String mAddress, int maxLengthToBreak) {
        Paint paint = new Paint();
        float width = paint.measureText(mAddress);
        if (width > maxLengthToBreak) {
            List<String> arrayList;
            String[] array = (mAddress.split("\\s"));
            arrayList = Arrays.asList(array);
            int fiftyPercent = (int) (Math.round(arrayList.size() * 0.50));
            String linebreak = arrayList.get(fiftyPercent) + "\n";
            arrayList.set(fiftyPercent, linebreak);
            mAddress = TextUtils.join(" ", arrayList);
            mAddress = mAddress.replace("\n ", "\n");
        }

        return mAddress;
    }

    public static String normalize(String string){
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        string = string.replaceAll("[^\\p{ASCII}]", "");
        return string;
    }

}

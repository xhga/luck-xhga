package org.luck.xhga.common.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FormatUtil {

    /**
     * 百分比
     * @param ratio
     * @return
     */
    public static String pct(Object ratio) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(ratio);
    }

    /**
     * 千位符
     * @param ratio
     * @return
     */
    public static String ths(Object ratio) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
        return df.format(ratio);
    }
}

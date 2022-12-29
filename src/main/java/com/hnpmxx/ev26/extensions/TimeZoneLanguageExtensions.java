package com.hnpmxx.ev26.extensions;

import java.text.DecimalFormat;

public class TimeZoneLanguageExtensions {
    /**
     * 转化时区
     * @param tzl tzl
     * @return String
     */
    public static String FormatTimeZoneLanguageTime(float tzl){
        DecimalFormat df = new DecimalFormat("0.00");
        String formatString = df.format(tzl);
        String[] nums = formatString.split("\\.");
        if(nums.length >= 2){
            return String.format("%s:%s", nums[0], (nums[1].length() >= 2 ? nums[1] : "0" + nums[1]));
        }
        return String.format("%s:%s",
                formatString.substring(0, formatString.length() - 2),
                formatString.substring(formatString.length() - 2));
    }

    /**
     * 进一步划分时区, 如: 东八区又1/4时区
     * @param tzl tzl
     * @return String
     */
    public static String FormatTimeZoneLanguageZone(int tzl){
        String formatString = String.valueOf(tzl);
        if(formatString.length() <= 2){
            return String.format("%s:00", tzl);
        }
        return String.format("%s:%s",
                formatString.substring(0, formatString.length() - 2),
                formatString.substring(formatString.length() - 2));
    }
}

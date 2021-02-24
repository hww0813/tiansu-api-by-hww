package com.yuanqing.common.utils;

import java.text.DecimalFormat;

public  class FlowUtil {
    public static String setFlow(Long flow) {
        DecimalFormat df = new DecimalFormat("#.00");
        String flowStr = "";
        Double num = 1024.00; //byte
        if (flow == 0L) {
            flowStr = "0";
        } else if (flow < num) {
            flowStr = flow + "B";
        } else if (flow < Math.pow(num, 2)) {
            flowStr = df.format(flow / num) + "K";
        } else if (flow < Math.pow(num, 3)) {
            flowStr = df.format(flow / Math.pow(num, 2)) + "M";
        } else if (flow < Math.pow(num, 4)) {
            flowStr = df.format(flow / Math.pow(num, 3)) + "G";
        } else {
            flowStr = df.format(flow / Math.pow(num, 4)) + "T";
        }
        return flowStr;
    }
}

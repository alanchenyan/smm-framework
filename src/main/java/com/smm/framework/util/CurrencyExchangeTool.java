package com.smm.framework.util;

/**
 * @author Alan Chen
 * @description 多币种转换
 * @date 2021/4/21
 */
public class CurrencyExchangeTool {

    private CurrencyExchangeTool(){}

    /**
     * MOP-HKD
     * @param mop
     * @param hkdToMopRate
     * @return
     */
    public static long mopToHkd(long mop,double hkdToMopRate){
        Double hkd = NumberTool.div(mop,hkdToMopRate,0);
        return hkd.longValue();
    }

    /**
     * MOP-RMB : MOP-HKD-RMB
     * @param mop
     * @param hkdToMopRate
     * @return
     */
    public static long mopToRmb(long mop,double hkdToMopRate,double rmbToHkdRate){
        Double hkd = NumberTool.div(mop,hkdToMopRate);
        Double rmb = NumberTool.div(hkd,rmbToHkdRate);

        Double roundNum = NumberTool.round(rmb,0);
        return roundNum.longValue();
    }

    /**
     * HKD-MOP
     * @param hkd
     * @param hkdToMopRate
     * @return
     */
    public static long hkdToMop(long hkd,double hkdToMopRate){
        Double mop = NumberTool.mul(hkd,hkdToMopRate,0);
        return mop.longValue();
    }

    /**
     * HKD-RMB
     * @param hkd
     * @param rmbToHkdRate
     * @return
     */
    public static long hkdToRmb(long hkd,double rmbToHkdRate){
        Double rmb = NumberTool.div(hkd,rmbToHkdRate,0);
        return rmb.longValue();
    }

    /**
     * RMB-MOP : RMB-HKD-MOP
     * @param rmb
     * @param rmbToHkdRate
     * @param hkdToMopRate
     * @return
     */
    public static long rmbToMop(long rmb,double rmbToHkdRate,double hkdToMopRate){
        Double hkd = NumberTool.mul(rmb,rmbToHkdRate);
        Double mop = NumberTool.mul(hkd,hkdToMopRate);

        Double roundNum = NumberTool.round(mop,0);
        return roundNum.longValue();
    }

    /**
     * RMB-HKD
     * @param rmb
     * @param rmbToHkdRate
     * @return
     */
    public static long rmbToHkd(long rmb,double rmbToHkdRate){
        Double hkd = NumberTool.mul(rmb,rmbToHkdRate,0);
        return hkd.longValue();
    }
}

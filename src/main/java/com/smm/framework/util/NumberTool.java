package com.smm.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author Alan Chen
 * @description 数字工具类
 * @date 2020-03-14
 */
public class NumberTool {

    private NumberTool(){}

    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    public static String numberFormat(String num){
        DecimalFormat df = new DecimalFormat("#0.00");
        if(StringUtils.isBlank(num)){
            return df.format(0.0);
        }
        return df.format(Double.valueOf(num));
    }

    /**
     * 格式化成字符串，默认保留两位小数
     * @param num
     * @return
     */
    public static String numberFormat(double num){
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(num);
    }

    /**
     * 提供精确的加法运算
     * @param v1
     * @param v2
     * @return
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的加法运算(小数进行四舍五入)
     * @param v1
     * @param v2
     * @return
     */
    public static double add(double v1,double v2,int scale){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return round(b1.add(b2).doubleValue(),scale);
    }

    /**
     * 提供精确的减法运算
     * @param v1
     * @param v2
     * @return
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算(小数进行四舍五入)
     * @param v1
     * @param v2
     * @return
     */
    public static double sub(double v1,double v2,int scale){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return round(b1.subtract(b2).doubleValue(),scale);
    }

    /**
     * 提供精确的乘法运算
     * @param v1
     * @param v2
     * @return
     */
    public static double mul(double v1,int v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算(小数进行四舍五入)
     * @param v1
     * @param v2
     * @return
     */
    public static double mul(double v1,int v2,int scale){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return round(b1.multiply(b2).doubleValue(),scale);
    }

    /**
     * 提供精确的乘法运算
     * @param v1
     * @param v2
     * @return
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算(小数进行四舍五入)
     * @param v1
     * @param v2
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return
     */
    public static double mul(double v1,double v2,int scale){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return round(b1.multiply(b2).doubleValue(),scale);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1,double v2){
        return div(v1,v2,DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1,double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}

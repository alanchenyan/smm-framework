package com.smm.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author Alan Chen
 * @description 钱单位转换工具
 * @date 2020-04-03
 */
public class MoneyTool {

    final static DecimalFormat df = new DecimalFormat("#0.00");

    private MoneyTool(){

    }

    public static Long yuan2Fen(String yuan){
        if(StringUtils.isNotBlank(yuan)){
            return new BigDecimal(yuan).multiply(new BigDecimal(100)).longValue();
        }else{
            return 0L;
        }
    }

    public static String fen2Yuan(String fen){
        if(StringUtils.isNotBlank(fen)){
            BigDecimal yuan = BigDecimal.valueOf(Long.valueOf(fen)).divide(new BigDecimal(100));
            return df.format(yuan);
        }else{
            return "0.00";
        }
    }

    public static String fen2Yuan(Long fen){
        if(fen!=null){
            BigDecimal yuan = BigDecimal.valueOf(Long.valueOf(fen)).divide(new BigDecimal(100));
            return df.format(yuan);
        }else{
            return "0.00";
        }
    }

    public static double fen2YuanDouble(Long fen){
        if(fen!=null){
            return BigDecimal.valueOf(Long.valueOf(fen)).divide(new BigDecimal(100)).doubleValue();
        }
        return 0.0;
    }
}

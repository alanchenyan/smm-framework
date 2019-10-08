package com.smm.framework.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
*
*@author  feizaishui
*@since 处理时间的填充
*/
public class GlobalMetaObjectHandler implements MetaObjectHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalMetaObjectHandler.class);

    String createTimeFieldName = "createTime";
    String updateTimeFieldName = "updateTime";
    String deletedFieldName = "deleted";

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            Object createTime = getFieldValByName(createTimeFieldName, metaObject);
            Object updateTime = getFieldValByName(updateTimeFieldName, metaObject);
            Object delTag = getFieldValByName(deletedFieldName, metaObject);

            Date date = new Date();
            if (null == createTime) {
                setFieldValByName(createTimeFieldName, date, metaObject);
            }
            if (null == updateTime) {
                setFieldValByName(updateTimeFieldName, date, metaObject);
            }
            if (null == delTag) {
                setFieldValByName(deletedFieldName, 0, metaObject);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            setFieldValByName(updateTimeFieldName, new Date(), metaObject);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}

package com.whj.blog.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;


public class MyBeanUtils {



    public static String[] getNullpropertyName(Object source) {
        //使用BeanWrapper封装传入的类
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        //获取bean类所有的属性定义
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();

        List<String> nullPropertyNames = new ArrayList<>();

        for(PropertyDescriptor pd : pds) {
            //获取属性值
            String propertyName = pd.getName();
            if(beanWrapper.getPropertyValue(propertyName) == null)
                nullPropertyNames.add(propertyName);
        }

        return nullPropertyNames.toArray(new String[nullPropertyNames.size()]);
    }


}

package com.mytlx.compiler.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author TLX
 * @date 2019.5.22
 * @time 11:32
 */
public class ToStringUtils implements Serializable {

    private static final long serialVersionUID = 402154372800404254L;

    /*ReflectionToStringBuilder类是用来实现类中的toString()方法的类，
    它采用Java反射机制（Reflection），通过reflection包中的AccessibleObject类
    绕过访问控制而直接存取对象的私有成员。因此在使用该类时，要注意运行环境的安全策略。*/

    //这个方法会在使用打印的时候  输出toString方法    子类不用复写 ToString()方法
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}


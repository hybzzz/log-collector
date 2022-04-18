package com.hybzzz.log.collector.agent;


import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;
import jdk.internal.org.objectweb.asm.commons.Method;


/**
 * AdviceAdapter: 子类
 * 对methodVisitor进行了扩展， 能让我们更加轻松的进行方法分析
 */
public class MethodAdapterVisitor extends AdviceAdapter {

    protected MethodAdapterVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }



    @Override
    protected void onMethodEnter() {
        // 加载参数
        loadArg(0);
        //(Lch/qos/logback/classic/spi/LoggingEvent;)Lch/qos/logback/classic/spi/LoggingEvent;
        invokeStatic(Type.getType("Lcom/hybzzz/log/collector/agent/LogFilter;"),
                new Method("doFilter", "(Lch/qos/logback/classic/spi/LoggingEvent;)Lch/qos/logback/classic/spi/LoggingEvent;"));
    }

}
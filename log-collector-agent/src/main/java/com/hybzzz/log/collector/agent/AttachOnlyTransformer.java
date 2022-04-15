package com.hybzzz.log.collector.agent;

import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.util.List;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

public class AttachOnlyTransformer implements MyTransformer {
    @Override
    public String getHookClassName() {
        return "ch/qos/logback/core/spi/AppenderAttachableImpl";
    }

    public AttachOnlyTransformer( ) {
    }


    @Override
    public boolean javaagentMode() {

        return true;
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {

        ClassReader reader = new ClassReader(classBytes);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        reader.accept(new ClassVisitor(ASM5,writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                             String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature,  exceptions);
                if("appendLoopOnAppenders".equals(name)){
                    if(methodVisitor!=null){
                        return new MethodAdapterVisitor (api,methodVisitor, access, name, desc);
                    }else{
                        return null;
                    }
                }
                return  methodVisitor;
            }
        }, ClassReader.EXPAND_FRAMES);

        return writer.toByteArray();

    }
}

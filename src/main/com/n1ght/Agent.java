package com.n1ght;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.AccessibleObject;
public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
//        JarFileHelper.addJarToBootstrap(inst);
        inst.addTransformer(new NightTransformer(), true);
        inst.retransformClasses(new Class[] { AccessibleObject.class });
    }
}
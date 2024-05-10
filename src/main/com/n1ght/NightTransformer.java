package com.n1ght;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class NightTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {


        if(className.equals("java/io/ObjectOutputStream$BlockDataOutputStream")){
            System.out.println("true2");

            ClassPool classPool = ClassPool.getDefault();
            try {
                CtClass ctClass = classPool.get("java.io.ObjectOutputStream$BlockDataOutputStream");
                classPool.importPackage(IOException.class.getName());
                CtMethod writeUTF = ctClass.getMethod("writeUTF","(Ljava/lang/String;J)V");
                ctClass.removeMethod(writeUTF);
                CtMethod make1 = CtNewMethod.make("void writeUTF(String s, long utflen) throws IOException {\n" +
                        "            writeShort((int) utflen*3);\n" +
                        "            writeUTFBody(s);\n" +
                        "        }", ctClass);
                ctClass.addMethod(make1);


                CtMethod method = ctClass.getDeclaredMethod("writeUTFBody");
                ctClass.removeMethod(method);
                CtMethod make = CtNewMethod.make("    private void writeUTFBody(String s) throws IOException {\n" +
                        "        int limit = MAX_BLOCK_SIZE - 3;\n" +
                        "        int len = s.length();\n" +
                        "        for (int off = 0; off < len; ) {\n" +
                        "            int csize = Math.min(len - off, CHAR_BUF_SIZE);\n" +
                        "            s.getChars(off, off + csize, cbuf, 0);\n" +
                        "            for (int cpos = 0; cpos < csize; cpos++) {\n" +
                        "                char c = cbuf[cpos];\n" +
                        "                if (pos <= limit) {\n" +
                        "//                    if (c <= 0x007F && c != 0) {\n" +
                        "//                        buf[pos++] = (byte) c;\n" +
                        "//                    } else if (c > 0x07FF) {\n" +
                        "                buf[pos + 2] = (byte) (0x80 | ((c >> 0) & 0x3F));\n" +
                        "                    buf[pos + 1] = (byte) (0x80 | ((c >> 6) & 0x3F));\n" +
                        "                  buf[pos + 0] = (byte) (0xE0 | ((c >> 12) & 0x0F));\n" +
                        "                    pos += 3;\n" +
                        "//                    } else {\n" +
                        "//                        buf[pos + 1] = (byte) (0x80 | ((c >> 0) & 0x3F));\n" +
                        "//                        buf[pos + 0] = (byte) (0xC0 | ((c >> 6) & 0x1F));\n" +
                        "//                        pos += 2;\n" +
                        "//                    }\n" +
                        "                } else {    // write one byte at a time to normalize block\n" +
                        "                    if (c <= 0x007F && c != 0) {\n" +
                        "                        write(c);\n" +
                        "                    } else if (c > 0x07FF) {\n" +
                        "                        write(0xE0 | ((c >> 12) & 0x0F));\n" +
                        "                        write(0x80 | ((c >> 6) & 0x3F));\n" +
                        "                        write(0x80 | ((c >> 0) & 0x3F));\n" +
                        "                    } else {\n" +
                        "                        write(0xC0 | ((c >> 6) & 0x1F));\n" +
                        "                        write(0x80 | ((c >> 0) & 0x3F));\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            }\n" +
                        "            off += csize;\n" +
                        "        }\n" +
                        "    }\n" +
                        "}", ctClass);
                ctClass.addMethod(make);
                ctClass.detach();
                return ctClass.toBytecode();
            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }
        return classfileBuffer;
    }


}
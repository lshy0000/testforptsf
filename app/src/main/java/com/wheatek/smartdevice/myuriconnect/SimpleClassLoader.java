package com.wheatek.smartdevice.myuriconnect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLClassLoader;
import java.security.SecureClassLoader;

/**
 * 类找不到时使用Object 替换。
 */
public class SimpleClassLoader extends SecureClassLoader {
    private File mLibPath = new File("E:\\android\\workplace\\WheatekSmartDevice\\app\\build\\intermediates\\javac\\debug\\classes");

    public SimpleClassLoader() {
        super();
    }

    protected Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        Class cls = null;
        cls = findClass(name);
        if (cls == null) {
            cls = Object.class;
        }
        return cls;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String fileName = getFileName(name);
            File file = new File(mLibPath, fileName);
            FileInputStream is = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = bos.toByteArray();
            is.close();
            bos.close();
            return defineClass(name, data, 0, data.length);
        } catch (Throwable e) {
        }
        return super.findClass(name);
    }

    //将包名转换为实际路径
    private String getFileName(String name) {
        name = name.replaceAll("\\.", "/");
        return name + ".class";
    }

}

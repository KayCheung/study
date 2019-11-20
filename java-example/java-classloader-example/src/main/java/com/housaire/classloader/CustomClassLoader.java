package com.housaire.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/4/12 19:36
 * @see
 * @since 1.0.0
 */
public class CustomClassLoader extends ClassLoader
{

    private String path;

    public CustomClassLoader(String path)
    {
        super();
        if (!path.endsWith(File.separator))
        {
            path = path + File.separator;
        }
        this.path = path;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        getParent();
        if (name.startsWith("java."))
        {
            return super.loadClass(name, resolve);
        }
        synchronized (getClassLoadingLock(name))
        {
            Class<?> c = findLoadedClass(name);
            if (c == null)
            {
                c = findClass(name);
            }
            if (resolve)
            {
                resolveClass(c);
            }
            return c;
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException
    {
        String path = this.path + name.replace(".", File.separator) + ".class";
        byte[] data = getData(path);

        if (null != data)
        {
            return defineClass(name, data, 0, data.length);
        }
        else
        {
            return null;
        }
    }

    private byte[] getData(String path)
    {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try
        {
            in = new FileInputStream(new File(path));
            out = new ByteArrayOutputStream();
            byte[] bt = new byte[1024];

            int len = 0;
            while ((len = in.read(bt)) > -1)
            {
                out.write(bt, 0, len);
            }
            return out.toByteArray();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

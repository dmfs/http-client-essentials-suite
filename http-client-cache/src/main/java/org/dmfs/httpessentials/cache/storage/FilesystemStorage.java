/*
 * Copyright 2016 Marten Gajda <marten@dmfs.org>
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dmfs.httpessentials.cache.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;


/**
 * A {@link Storage} implementation that stores objects in a directory structure.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class FilesystemStorage implements Storage
{
    private final File mBaseDir;
    private int mMaxDepth;


    /**
     * Creates a {@link FilesystemStorage} with the given base directory and maximum directory depth.
     *
     * @param baseDir
     *         The directory to store all slots in.
     * @param maxDepth
     *         The maximum depth of the directory structure, pass <code>0</code> to create a flat storage without any directories.
     */
    public FilesystemStorage(File baseDir, int maxDepth)
    {
        mBaseDir = baseDir;
        mMaxDepth = maxDepth;
    }


    @Override
    public boolean isEmpty(String name) throws IOException
    {
        File file = encodedFile(name);
        return !file.isFile() || file.length() == 0;
    }


    @Override
    public InputStream inputStream(String name) throws IOException
    {
        File file = encodedFile(name);
        if (!file.isFile())
        {
            return new EmptyInputStream();
        }
        return new FileInputStream(file);
    }


    @Override
    public OutputStream outputStream(String name) throws IOException
    {
        File file = encodedFile(name);
        file.getParentFile().mkdirs();
        return new FileOutputStream(file, false);
    }


    @Override
    public void wipe(String name) throws IOException
    {
        File file = encodedFile(name);
        if (file.isFile())
        {
            file.delete();
        }
    }


    @Override
    public long size(String name) throws IOException
    {
        File file = encodedFile(name);
        return file.isFile() ? file.length() : 0;
    }


    private File encodedFile(String name) throws IOException
    {
        return file(mBaseDir, URLEncoder.encode(name, "UTF-8"), mMaxDepth);
    }


    private File file(File base, String name, int level)
    {
        if (level == 0 || name.length() < 3)
        {
            return new File(base, name);
        }
        else
        {
            return file(new File(base, name.substring(0, 2)), name.substring(2), level - 1);
        }
    }
}

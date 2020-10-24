package me.jayoevans.storage.reader;

import me.jayoevans.storage.common.FileUtil;

import java.io.File;

public class Setup
{
    public static void main(String[] args) throws Exception
    {
        File source = new File("server");
        FileUtil.zip(source, new File("dist"), "server");
    }
}

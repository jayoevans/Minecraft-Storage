package me.jayoevans.storage.writer;

import me.jayoevans.storage.common.FileUtil;
import me.jayoevans.storage.common.StorageManager;

import java.io.File;
import java.util.UUID;

public class StorageWriter
{
    public static void main(String[] args) throws Exception
    {
        String serverDirectory = args.length > 0 ? args[0] : ".";
        String serverId = args.length > 1 ? args[1] : UUID.randomUUID().toString();

        new StorageWriter(serverDirectory, serverId);
    }

    private static final String SERVER_DIRECTORY = "server";

    private final StorageManager storageManager = new StorageManager();

    private final File parentDirectory;
    private final File serverDirectory;
    private final String serverId;

    public StorageWriter(String directory, String serverId) throws Exception
    {
        this.parentDirectory = new File(directory);
        this.serverDirectory = new File(this.parentDirectory, SERVER_DIRECTORY);
        this.serverId = serverId;

        if (!this.parentDirectory.exists())
        {
            this.parentDirectory.mkdirs();
        }

        File world = new File(this.serverDirectory, "world");
        File sourceFile = FileUtil.zip(world, this.serverDirectory, "world");

        String key = this.storageManager.getKey(this.serverId);
        this.storageManager.putObject(key, sourceFile);

//        sourceFile.delete();
        System.out.println("Done!");
    }
}

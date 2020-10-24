package me.jayoevans.storage.reader;

import me.jayoevans.storage.common.Constants;
import me.jayoevans.storage.common.FileUtil;
import me.jayoevans.storage.common.StorageManager;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

public class StorageReader
{
    public static void main(String[] args) throws Exception
    {
        String serverDirectory = args.length > 0 ? args[0] : ".";
        String serverId = args.length > 1 ? args[1] : UUID.randomUUID().toString();

        new StorageReader(serverDirectory, serverId);
    }

    private static final String SERVER_DIRECTORY = "server";

    private final StorageManager storageManager = new StorageManager();

    private final File parentDirectory;
    private final File serverDirectory;
    private final String serverId;

    public StorageReader(String directory, String serverId) throws Exception
    {
        this.parentDirectory = new File(directory);
        this.serverDirectory = new File(this.parentDirectory, SERVER_DIRECTORY);
        this.serverId = serverId;

        if (!this.parentDirectory.exists())
        {
            this.parentDirectory.mkdirs();
        }

        System.out.println("Creating server...");
        this.createServer();
        System.out.println("Done!");

        System.out.println("Setting up config...");
        this.setupConfig();
        System.out.println("Done!");

        System.out.println("Loading world...");
        this.loadWorld();
        System.out.println("Done!");
    }

    private void createServer() throws Exception
    {
        File source = new File("server.tar.gz");

        if (!source.exists())
        {
            throw new Exception("server.tar.gz does not exist");
        }

        FileUtil.unzip(source, this.parentDirectory);
    }

    private void setupConfig() throws Exception
    {
        File config = new File(this.serverDirectory, Constants.CONFIG_FILE);

        Files.write(config.toPath(), this.serverId.getBytes());
    }

    private void loadWorld() throws Exception
    {
        String key = this.storageManager.getKey(this.serverId);

        try
        {
            File sourceFile = new File(this.serverDirectory, "world.tar.gz");
            this.storageManager.getObject(key, sourceFile);

            FileUtil.unzip(sourceFile, this.serverDirectory);
//            sourceFile.delete();
        }
        catch (Exception e)
        {
            System.out.println("No world found!");

            e.printStackTrace();
            // World does not exist
        }
    }
}

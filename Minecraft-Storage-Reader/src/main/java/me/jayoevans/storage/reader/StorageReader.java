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

    private final StorageManager storageManager = new StorageManager();

    private final File serverDirectory;
    private final String serverId;

    public StorageReader(String serverDirectory, String serverId) throws Exception
    {
        this.serverDirectory = new File(serverDirectory);
        this.serverId = serverId;

        if (!this.serverDirectory.exists())
        {
            this.serverDirectory.mkdirs();
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

        FileUtil.unzip(source, this.serverDirectory);
    }

    private void setupConfig() throws Exception
    {
        File config = new File(this.serverDirectory, Constants.CONFIG_FILE);

        Files.write(config.toPath(), this.serverId.getBytes());
    }

    private void loadWorld() throws Exception
    {
        String key = this.storageManager.getKey(this.serverId);

        this.storageManager.getObject(key, this.serverDirectory);
        // TODO world object from bucket
        // Unzip into server directory
    }
}

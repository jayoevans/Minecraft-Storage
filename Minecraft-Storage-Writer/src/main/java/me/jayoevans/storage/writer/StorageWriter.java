package me.jayoevans.storage.writer;

import me.jayoevans.storage.common.Constants;
import me.jayoevans.storage.common.FileUtil;
import me.jayoevans.storage.common.StorageManager;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StorageWriter extends JavaPlugin
{
    private final StorageManager storageManager = new StorageManager();

    private String serverId;

    @Override
    public void onDisable()
    {
        World world = this.getServer().getWorld("world");
        this.getServer().unloadWorld(world, true);

        try
        {
            File worldFile = FileUtil.zip(world.getWorldFolder(), "world");

            String key = this.storageManager.getKey(this.getServerId());
            this.storageManager.putObject(key, worldFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String getServerId() throws IOException
    {
        if (this.serverId != null)
        {
            return this.serverId;
        }

        File config = new File(Constants.CONFIG_FILE);

        return this.serverId = Files.readAllLines(config.toPath()).get(0);
    }
}

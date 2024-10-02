package me.lukasabbe.bookshelfInspectorBridge;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.PacketDataSerializer;
import org.bukkit.block.Block;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;


import java.util.logging.Level;

public final class BookshelfInspectorBridge extends JavaPlugin implements Listener, PluginMessageListener {

    public PluginLogger logger = new PluginLogger(this);

    @Override
    public void onEnable() {
        //register packets
        getServer().getMessenger().registerOutgoingPluginChannel(this, "bookshelfinspector:mod_check");
        getServer().getMessenger().registerOutgoingPluginChannel(this,"bookshelfinspector:book_shelf_inventory");
        getServer().getMessenger().registerIncomingPluginChannel(this,"bookshelfinspector:book_shelf_inventory_request",this);

        //register event
        getServer().getPluginManager().registerEvents(this,this);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent){
        getServer().getScheduler().runTaskLater(this, ()->{
            logger.log(Level.INFO,"Sent packet");
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeBoolean(true);
            joinEvent.getPlayer().sendPluginMessage(this,"bookshelfinspector:mod_check", new byte[0]);
        },20L);
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if(!s.equals("bookshelfinspector:book_shelf_inventory_request")) return;
        PacketDataSerializer inData = new PacketDataSerializer(Unpooled.wrappedBuffer(bytes));

        BlockPosition pos = inData.e();
        final Block blockAt = player.getWorld().getBlockAt(BlockPosition.a(pos.a()),BlockPosition.b(pos.a()),BlockPosition.c(pos.a()));
        if(!(blockAt.getState() instanceof ChiseledBookshelf chiseledBookshelf)) return;
        ItemStack stack = chiseledBookshelf.getInventory().getItem(inData.getInt(8));
        if(stack == null) return;
        inData.clear();
    }
}

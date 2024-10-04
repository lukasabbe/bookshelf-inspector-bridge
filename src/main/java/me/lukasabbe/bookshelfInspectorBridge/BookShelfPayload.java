package me.lukasabbe.bookshelfInspectorBridge;

import net.minecraft.core.BlockPosition;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.ItemStack;

public record BookShelfPayload (ItemStack itemStack, BlockPosition pos, int slotNum) implements CustomPacketPayload {
    public static final CustomPacketPayload.b<BookShelfPayload> ID = new CustomPacketPayload.b<>(MinecraftKey.a("bookshelfinspector", "book_shelf_inventory"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BookShelfPayload> CODEC = StreamCodec.a(
            ItemStack.h,BookShelfPayload::itemStack,
            BlockPosition.b, BookShelfPayload::pos,
            ByteBufCodecs.f, BookShelfPayload::slotNum,
            BookShelfPayload::new);
    @Override
    public b<? extends CustomPacketPayload> a() {
        return ID;
    }
}

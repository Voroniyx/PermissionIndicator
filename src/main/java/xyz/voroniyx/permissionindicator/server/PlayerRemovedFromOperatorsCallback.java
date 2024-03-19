package xyz.voroniyx.permissionindicator.server;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PlayerRemovedFromOperatorsCallback {
    Event<PlayerRemovedFromOperatorsCallback> EVENT = EventFactory.createArrayBacked(PlayerRemovedFromOperatorsCallback.class, (listeners) -> (gameProfile, callbackInfo, server) -> {
        for (PlayerRemovedFromOperatorsCallback listener : listeners) {
            listener.onRemoveFromOperators(gameProfile, callbackInfo, server);
        }
    });

    void onRemoveFromOperators(GameProfile gameProfile, CallbackInfo callbackInfo, MinecraftServer server);
}
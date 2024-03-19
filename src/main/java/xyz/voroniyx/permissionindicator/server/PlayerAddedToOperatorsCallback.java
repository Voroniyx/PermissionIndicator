package xyz.voroniyx.permissionindicator.server;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PlayerAddedToOperatorsCallback {
    Event<PlayerAddedToOperatorsCallback> EVENT = EventFactory.createArrayBacked(PlayerAddedToOperatorsCallback.class, (listeners) -> (profile, ci, server) -> {
        for (PlayerAddedToOperatorsCallback listener : listeners) {
            listener.onAddToOperators(profile, ci, server);
        }
    });

    void onAddToOperators(GameProfile profile, CallbackInfo ci, MinecraftServer server);
}
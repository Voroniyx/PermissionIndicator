package xyz.voroniyx.permissionindicator.handlers;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface Permissions {
    public void onAddToOperators(GameProfile profile, CallbackInfo ci, MinecraftServer server);
    public void joinServerForFirstTime(ServerPlayerEntity player, MinecraftServer server);
    public boolean joinServer(ServerPlayerEntity player, MinecraftServer server);
    public void onRemoveFromOperators(GameProfile gameProfile, CallbackInfo callbackInfo, MinecraftServer server);
    public void doServerStartAction(MinecraftServer Server);
}

package xyz.voroniyx.permissionindicator.mixin;


import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.voroniyx.permissionindicator.server.PlayerAddedToOperatorsCallback;
import xyz.voroniyx.permissionindicator.server.PlayerFirstJoinCallback;
import xyz.voroniyx.permissionindicator.server.PlayerJoinCallback;
import xyz.voroniyx.permissionindicator.server.PlayerRemovedFromOperatorsCallback;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(at = @At(value = "TAIL"), method = "onPlayerConnect")
    private void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        PlayerJoinCallback.EVENT.invoker().joinServer(player, player.getServer());
        if (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.LEAVE_GAME)) < 1) {
            PlayerFirstJoinCallback.EVENT.invoker().joinServerForFirstTime(player, player.getServer());
        }
    }

    @Inject(at = @At(value = "TAIL"), method = "removeFromOperators")
    private void onRemoveFromOperators(GameProfile profile, CallbackInfo ci) {
        PlayerRemovedFromOperatorsCallback.EVENT.invoker().onRemoveFromOperators(profile, ci, this.server);
    }

    @Inject(at = @At(value = "TAIL"), method = "addToOperators")
    private void onAddFromOperators(GameProfile profile, CallbackInfo ci) {
        PlayerAddedToOperatorsCallback.EVENT.invoker().onAddToOperators(profile, ci, this.server);
    }
}
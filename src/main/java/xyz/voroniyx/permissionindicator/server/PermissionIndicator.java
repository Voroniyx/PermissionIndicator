package xyz.voroniyx.permissionindicator.server;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.voroniyx.permissionindicator.handlers.Op;
import xyz.voroniyx.permissionindicator.handlers.Three;
import xyz.voroniyx.permissionindicator.handlers.Two;

public class PermissionIndicator implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public Op opHandler = new Op();
    public Three threeHandler = new Three();
    public Two twoHandler = new Two();

    @Override
    public void onInitializeServer() {
        PlayerJoinCallback.EVENT.register(this::doJoinAction);
        ServerLifecycleEvents.SERVER_STARTED.register(this::doServerStartAction);
        PlayerRemovedFromOperatorsCallback.EVENT.register(this::onOperatorRemoved);
        PlayerAddedToOperatorsCallback.EVENT.register(this::onOperatorAdded);
        LOGGER.info("Started Plugin");
    }

    private void doServerStartAction(MinecraftServer minecraftServer) {
        LOGGER.info("Checking Teams...");
        ServerScoreboard scoreboard = minecraftServer.getScoreboard();

        scoreboard.getTeams().forEach(scoreboard::removeTeam);

        opHandler.doServerStartAction(minecraftServer);
        threeHandler.doServerStartAction(minecraftServer);
        twoHandler.doServerStartAction(minecraftServer);

        LOGGER.info("All teams available...");
    }


    private void doJoinAction(ServerPlayerEntity player, MinecraftServer server) {
        LOGGER.info(player.getName().getString() + " joined. Checking Permission Level");

        boolean OpJoinResult = opHandler.joinServer(player, server);
        if(!OpJoinResult) {
            boolean ThirdJoinResult = threeHandler.joinServer(player, server);
            if(!ThirdJoinResult) {
                twoHandler.joinServer(player, server);
            }
        }
    }

    private void onOperatorRemoved(GameProfile gameProfile, CallbackInfo callbackInfo, MinecraftServer server) {
        opHandler.onRemoveFromOperators(gameProfile, callbackInfo, server);
    }

    private void onOperatorAdded(GameProfile gameProfile, CallbackInfo callbackInfo, MinecraftServer server) {
        opHandler.onAddToOperators(gameProfile, callbackInfo, server);
    }
}

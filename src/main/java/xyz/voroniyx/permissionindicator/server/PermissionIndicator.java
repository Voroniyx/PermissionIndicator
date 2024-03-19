package xyz.voroniyx.permissionindicator.server;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class PermissionIndicator implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

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

        scoreboard.getTeams().forEach(team -> {
            scoreboard.removeTeam(team);
        });

        Team adminTeam = scoreboard.getTeam("admin_team");
        if(adminTeam == null) {
            LOGGER.info("Creating Admin Team...");
            Team newAdminTeam = scoreboard.addTeam("admin_team");
            newAdminTeam.setDisplayName(Text.literal("Admin"));
            //newAdminTeam.setPrefix(Text.literal("[Admin] "));
            newAdminTeam.setColor(Formatting.GOLD);
        }

        LOGGER.info("All teams available...");
    }


    private void doJoinAction(ServerPlayerEntity player, MinecraftServer server) {
        LOGGER.info(player.getName().getString() + " joined. Checking Permission Level");

        if(player.hasPermissionLevel(4)) {
            Team adminTeam = server.getScoreboard().getTeam("admin_team");
            if(adminTeam != null) {
                CommandManager commandManager = server.getCommandManager();
                ServerCommandSource commandSource = server.getCommandSource();
                commandManager.executeWithPrefix(commandSource, "/team join admin_team " + player.getName().getString());
                player.sendMessage(Text.of("Added you to Admin Team"));

                return;
            }
        }
    }

    private void onOperatorRemoved(GameProfile gameProfile, CallbackInfo callbackInfo, MinecraftServer server) {
        String name = gameProfile.getName();
        CommandManager commandManager = server.getCommandManager();
        ServerCommandSource commandSource = server.getCommandSource();
        commandManager.executeWithPrefix(commandSource, "/team leave " + name);
    }

    private void onOperatorAdded(GameProfile gameProfile, CallbackInfo callbackInfo, MinecraftServer server) {
        String name = gameProfile.getName();
        CommandManager commandManager = server.getCommandManager();
        ServerCommandSource commandSource = server.getCommandSource();
        commandManager.executeWithPrefix(commandSource, "/team join admin_team " + name);
    }
}

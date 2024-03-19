package xyz.voroniyx.permissionindicator.handlers;

import com.mojang.authlib.GameProfile;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class Op implements Permissions {
    @Override
    public void onAddToOperators(GameProfile profile, CallbackInfo ci, MinecraftServer server) {
        String name = profile.getName();
        CommandManager commandManager = server.getCommandManager();
        ServerCommandSource commandSource = server.getCommandSource();
        commandManager.executeWithPrefix(commandSource, "/team join admin_team " + name);
    }

    @Override
    public void joinServerForFirstTime(ServerPlayerEntity player, MinecraftServer server) {
        return;
    }

    @Override
    public boolean joinServer(ServerPlayerEntity player, MinecraftServer server) {
        if(player.hasPermissionLevel(4)) {
            Team adminTeam = server.getScoreboard().getTeam("admin_team");
            if(adminTeam != null) {
                CommandManager commandManager = server.getCommandManager();
                ServerCommandSource commandSource = server.getCommandSource();
                commandManager.executeWithPrefix(commandSource, "/team join admin_team " + player.getName().getString());
                player.sendMessage(Text.of("Added you to Admin Team"));

                return true;
            }
        }

        return false;
    }

    @Override
    public void onRemoveFromOperators(GameProfile gameProfile, CallbackInfo callbackInfo, MinecraftServer server) {
        String name = gameProfile.getName();
        CommandManager commandManager = server.getCommandManager();
        ServerCommandSource commandSource = server.getCommandSource();
        commandManager.executeWithPrefix(commandSource, "/team leave " + name);
    }

    @Override
    public void doServerStartAction(MinecraftServer server) {
        ServerScoreboard scoreboard = server.getScoreboard();

        Team adminTeam = scoreboard.getTeam("admin_team");
        if(adminTeam == null) {
            Team newAdminTeam = scoreboard.addTeam("admin_team");
            newAdminTeam.setDisplayName(Text.literal("Admin"));
            //newAdminTeam.setPrefix(Text.literal("[Admin] "));
            newAdminTeam.setColor(Formatting.GOLD);
        }
    }
}

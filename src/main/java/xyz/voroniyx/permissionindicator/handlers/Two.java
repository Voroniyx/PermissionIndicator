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

public class Two implements Permissions{
    @Override
    public void onAddToOperators(GameProfile profile, CallbackInfo ci, MinecraftServer server) {
        return;
    }

    @Override
    public void joinServerForFirstTime(ServerPlayerEntity player, MinecraftServer server) {
        return;
    }

    @Override
    public boolean joinServer(ServerPlayerEntity player, MinecraftServer server) {
        if(player.hasPermissionLevel(2)) {
            Team secondLevelTeam = server.getScoreboard().getTeam("permission_two_team");
            if(secondLevelTeam != null) {
                CommandManager commandManager = server.getCommandManager();
                ServerCommandSource commandSource = server.getCommandSource();
                commandManager.executeWithPrefix(commandSource, "/team join permission_two_team " + player.getName().getString());
                player.sendMessage(Text.of("Added you to Second Level Team "));

                return true;
            }
        }

        return false;
    }

    @Override
    public void onRemoveFromOperators(GameProfile gameProfile, CallbackInfo callbackInfo, MinecraftServer server) {
        return;
    }

    @Override
    public void doServerStartAction(MinecraftServer server) {
        ServerScoreboard scoreboard = server.getScoreboard();

        Team levelThreeTeam = scoreboard.getTeam("permission_two_team");
        if(levelThreeTeam == null) {
            Team newAdminTeam = scoreboard.addTeam("permission_two_team");
            newAdminTeam.setDisplayName(Text.literal("Level2"));
            //newAdminTeam.setPrefix(Text.literal("[Level2] "));
            newAdminTeam.setColor(Formatting.AQUA);
        }
    }
}

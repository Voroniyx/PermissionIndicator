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

public class PermissionLevelThree implements IPermissionsLevelHandler {
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
        if(player.hasPermissionLevel(3)) {
            Team adminTeam = server.getScoreboard().getTeam("permission_three_team");
            if(adminTeam != null) {
                CommandManager commandManager = server.getCommandManager();
                ServerCommandSource commandSource = server.getCommandSource();
                commandManager.executeWithPrefix(commandSource, "/team join permission_three_team " + player.getName().getString());
                player.sendMessage(Text.of("Added you to Third Level Team "));

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

        Team levelThreeTeam = scoreboard.getTeam("permission_three_team");
        if(levelThreeTeam == null) {
            Team newAdminTeam = scoreboard.addTeam("permission_three_team");
            newAdminTeam.setDisplayName(Text.literal("Level3"));
            //newAdminTeam.setPrefix(Text.literal("[Level3] "));
            newAdminTeam.setColor(Formatting.YELLOW);
        }
    }
}

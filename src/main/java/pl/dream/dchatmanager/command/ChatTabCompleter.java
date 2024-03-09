package pl.dream.dchatmanager.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ChatTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if(args.length==1){
            return Arrays.asList("blockedWords", "clear", "off", "on");
        }
        else if(args.length==2){
            if(args[0].equalsIgnoreCase("blockedWords")){
                return Arrays.asList("remove", "add");
            }
        }

        return null;
    }
}

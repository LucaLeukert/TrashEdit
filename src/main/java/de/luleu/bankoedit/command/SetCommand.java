package de.luleu.bankoedit.command;

import de.luleu.bankoedit.Main;
import de.luleu.bankoedit.operation.SetOperation;
import de.luleu.bankoedit.selector.RegionSelector;
import de.luleu.bankoedit.sessions.SessionManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SetCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cUm diesen Befehl benutzen zu können musst du ein Spieler sein!");
            return false;
        }

        final Player player = (Player) sender;
        final Material material = Material.getMaterial(args[0].toUpperCase(Locale.ROOT));

        if (material == null) {
            player.sendMessage(Component.text("§cDu must ein valides Material angeben!"));
            return false;
        }

        if (!Main.sessionManager.contains(player)) {
            player.sendMessage(Component.text("§cDu hast keine Session, erstelle eine Session mit §8\"§3/session§8\""));
            return false;
        }

        SessionManager.SessionHolder holder = Main.sessionManager.getIfPresent(player);

        if (!holder.getSession().getRegionSelector().getRegion().isSelected()) {
            player.sendMessage(Component.text("§cDu musst erst 2 Positionen markieren!"));
            return false;
        }

        holder.getSession().executeOperation(new SetOperation(holder, material));

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> strings = new ArrayList<>();

        if (args.length == 1) {
            for (Material material : Material.values()) {

                if (material.toString().startsWith(args[0].toUpperCase()) && material.isBlock() )
                    strings.add(material.toString());
            }
        }

        return strings;
    }
}

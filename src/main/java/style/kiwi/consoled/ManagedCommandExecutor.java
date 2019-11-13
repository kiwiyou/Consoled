package style.kiwi.consoled;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;
import style.kiwi.consoled.tree.CommandNode;
import style.kiwi.consoled.tree.ParsedCommand;

import java.util.Arrays;
import java.util.logging.Level;

class ManagedCommandExecutor implements CommandExecutor {
    private CommandNode root;
    private ExecutorContext context;

    ManagedCommandExecutor(CommandNode root, ExecutorContext context) {
        this.root = root;
        this.context = context;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var current = root;
        ParsedCommand feasibleCommand = null;
        CommandNode unmatchedNode = null;
        CommandNode deepestNode = null;
        for (var i = 0; i <= args.length && current != null; ++i) {
            var currentCommand = current.getCommand();
            var hasPermission = current.getPermission() == null || sender.hasPermission(current.getPermission());
            if (currentCommand != null) {
                if (hasPermission) {
                    deepestNode = current;
                }
                var parseArgs = Arrays.copyOfRange(args, i, args.length);
                var parsed = currentCommand.tryParse(parseArgs);
                if (parsed != null) {
                    if (hasPermission) {
                        feasibleCommand = parsed;
                    }
                    unmatchedNode = current;
                }
            }
            if (i < args.length) {
                current = current.findDescendant(args[i]);
            } else {
                break;
            }
        }
        if (feasibleCommand != null) {
            var context = new CommandContext(command, sender, args);
            try {
                if (feasibleCommand.accept(context, this.context)) {
                    return true;
                }
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING, "Exception occurred while executing command", e);
                return true;
            }
        } else if (unmatchedNode != null) {
            var permissionMessage = findNearestPermissionMessage(unmatchedNode);
            if (permissionMessage == null) {
                return false;
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', permissionMessage));
                return true;
            }
        }
        var usage = findNearestUsage(deepestNode, sender);
        if (usage == null) {
            return false;
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
            return true;
        }
    }

    private String findNearestUsage(CommandNode from, Permissible toCheck) {
        String usageText;
        if (from != null) {
            usageText = from.getUsage();
        } else {
            usageText = (from = root).getUsage();
        }
        var cursor = from.getParent();
        while (cursor != null && usageText == null) {
            if (from.getPermission() == null || toCheck.hasPermission(from.getPermission())) {
                usageText = cursor.getUsage();
            }
            cursor = cursor.getParent();
        }
        return usageText;
    }

    private String findNearestPermissionMessage(CommandNode from) {
        String permissionMessage;
        if (from != null) {
            permissionMessage = from.getPermissionMessage();
        } else {
            permissionMessage = (from = root).getPermissionMessage();
        }
        var cursor = from.getParent();
        while (cursor != null && permissionMessage == null) {
            permissionMessage = cursor.getPermissionMessage();
            cursor = cursor.getParent();
        }
        return permissionMessage;
    }
}

package style.kiwi.consoled;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import style.kiwi.consoled.tree.CommandNode;

import java.util.Collections;
import java.util.List;

class ManagedTabCompleter implements TabCompleter {
    private CommandNode root;

    ManagedTabCompleter(CommandNode root) {
        this.root = root;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        CommandNode current = root;
        for (int i = 0; i < args.length; ++i) {
            if (args.length - 1 == i) {
                return current.findPartial(args[i], sender);
            } else {
                current = current.findDescendant(args[i]);
                if (current == null) {
                    break;
                }
            }
        }
        return Collections.emptyList();
    }
}

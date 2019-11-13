package style.kiwi.consoled.tree;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;

public class CommandTreeParser {
    public static Map<String, CommandNode> parse(YamlConfiguration configuration) {
        Map<String, CommandNode> trees = new HashMap<>();
        for (var key : configuration.getKeys(false)) {
            var tree = new CommandNode();
            parseSection(configuration.getConfigurationSection(key), tree);
            trees.put(key, tree);
        }
        return trees;
    }

    private static void parseSection(ConfigurationSection section, CommandNode tree) {
        for (var key : section.getKeys(false)) {
            switch (key) {
                case "_usage":
                    tree.setUsage(section.getString(key));
                    break;
                case "_perm":
                    tree.setPermission(section.getString(key));
                    break;
                case "_permMessage":
                    tree.setPermissionMessage(section.getString(key));
                    break;
                default:
                    var newNode = tree.findOrCreate(key);
                    parseSection(section.getConfigurationSection(key), newNode);
                    break;
            }
        }
    }
}

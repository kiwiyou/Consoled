package style.kiwi.consoled;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import style.kiwi.consoled.tree.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Consoled extends JavaPlugin implements CommandService {
    private ExecutorContext context = new ExecutorContext();
    private Map<NamespacedKey, CommandNode> registry = new HashMap<>();

    public void onEnable() {
        getServer().getServicesManager().register(CommandService.class, this, this, ServicePriority.Normal);
    }

    @Override
    public void register(JavaPlugin plugin) {
        var settingStream = plugin.getResource("commands.yml");
        if (settingStream == null) {
            getLogger().severe("Can't find commands.yml of plugin " + plugin.getName());
            return;
        }
        var available = plugin.getDescription().getCommands().keySet();
        for (var label : available) {
            registry.computeIfAbsent(new NamespacedKey(plugin, label), k -> new CommandNode());
        }
        try (var reader = new InputStreamReader(settingStream)) {
            var configuration = YamlConfiguration.loadConfiguration(reader);
            var registry = CommandTreeParser.parse(configuration);
            for (var entry : registry.entrySet()) {
                var node = this.registry.get(new NamespacedKey(plugin, entry.getKey()));
                if (node != null) {
                    node.merge(entry.getValue());
                }
            }
            for (var entry : this.registry.entrySet()) {
                var command = plugin.getCommand(entry.getKey().getKey());
                command.setExecutor(new ManagedCommandExecutor(entry.getValue(), context));
                command.setTabCompleter(new ManagedTabCompleter(entry.getValue()));
            }
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Exception occurred while closing commands.yml", e);
        }
    }

    @Override
    public <T> void registerExecutor(JavaPlugin plugin, T executor) {
        for (var method : executor.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(CommandFor.class)) continue;
            var annotation = method.getAnnotation(CommandFor.class);
            var targetCommand = annotation.value().split("\\.");
            if (targetCommand.length < 1) continue;
            var node = registry.computeIfAbsent(new NamespacedKey(plugin, targetCommand[0]), key -> new CommandNode());
            for (var i = 1; i < targetCommand.length; ++i) {
                node = node.findOrCreate(targetCommand[i]);
            }
            var command = node.getCommand();
            if (command == null) {
                command = new Command();
                node.setCommand(command);
            }
            command.addExecutor(method);
        }
        context.putInstance(executor);
    }
}

package style.kiwi.consoled;

import org.bukkit.plugin.java.JavaPlugin;

public interface CommandService {
    /**
     * Register commands specified in commands.yml of Plugin `plugin`.
     * @param plugin Plugin to register commands of
     */
    void register(JavaPlugin plugin);

    /**
     * Register executors used in commands.
     * @param plugin Plugin to register the executor of
     * @param executor Executor specified in YAML, `[command]._executor` section
     * @param <T> Arbitrary type for executor
     */
    <T> void registerExecutor(JavaPlugin plugin, T executor);
}

package style.kiwi.consoled.tree;


import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandNode {
    private Map<String, CommandNode> descendants = new HashMap<>();
    private CommandNode parent = null;
    private Command command = null;
    private String usage = null;
    private String permission = null;
    private String permissionMessage = null;

    public CommandNode findDescendant(String label) {
        if (this.descendants != null) {
            return this.descendants.get(label);
        } else {
            return null;
        }
    }

    public List<String> findPartial(String partial, Permissible sender) {
        return descendants.entrySet().stream().filter(entry -> {
            var permission = entry.getValue().getPermission();
            return entry.getKey().startsWith(partial) && (permission == null || sender.isPermissionSet(permission));
        }).map(Map.Entry::getKey).collect(Collectors.toUnmodifiableList());
    }

    public CommandNode findOrCreate(String label) {
        return this.descendants.computeIfAbsent(label, key -> {
            var newNode = new CommandNode();
            newNode.setParent(this);
            return newNode;
        });
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public void merge(CommandNode other) {
        for (var descendant : other.descendants.entrySet()) {
            descendants.compute(descendant.getKey(), (key, myDescendant) -> {
                if (myDescendant == null) {
                    return descendant.getValue();
                } else {
                    myDescendant.merge(descendant.getValue());
                    return myDescendant;
                }
            });
        }
        if (other.command != null) {
            this.command = other.command;
        }
        if (other.usage != null) {
            this.usage = other.usage;
        }
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }

    public void setPermissionMessage(String permissionMessage) {
        this.permissionMessage = permissionMessage;
    }

    public CommandNode getParent() {
        return parent;
    }

    private void setParent(CommandNode parent) {
        this.parent = parent;
    }
}

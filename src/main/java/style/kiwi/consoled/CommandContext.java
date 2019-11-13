package style.kiwi.consoled;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandContext {
    private Command command;
    private CommandSender sender;
    private String[] rawArgs;

    public CommandContext(Command command, CommandSender sender, String[] args) {
        this.command = command;
        this.sender = sender;
        this.rawArgs = args;
    }

    public Command getCommand() {
        return command;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String[] getRawArgs() {
        return rawArgs;
    }
}

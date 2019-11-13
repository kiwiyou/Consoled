package style.kiwi.consoled.tree;

import style.kiwi.consoled.CommandContext;
import style.kiwi.consoled.ExecutorContext;

import java.lang.reflect.Method;

public class ParsedCommand {
    private Method method;
    private Object[] args;

    ParsedCommand(Method method, Object[] parsed) {
        this.method = method;
        args = new Object[parsed.length + 1];
        System.arraycopy(parsed, 0, args, 1, parsed.length);
    }

    public boolean accept(CommandContext context, ExecutorContext executorContext) throws Exception {
        args[0] = context;
        return (boolean) method.invoke(executorContext.getInstance(method.getDeclaringClass()), args);
    }
}

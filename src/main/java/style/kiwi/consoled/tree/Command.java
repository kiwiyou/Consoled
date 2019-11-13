package style.kiwi.consoled.tree;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Command {
    private List<MethodMatcher> executors = new ArrayList<>();

    public void addExecutor(Method method) {
        var matcher = MethodMatcher.create(method);
        if (matcher != null) {
            executors.add(matcher);
        }
    }

    public ParsedCommand tryParse(String[] args) {
        for (MethodMatcher matcher : executors) {
            var parsed = matcher.parse(args);
            if (parsed != null) {
                return new ParsedCommand(matcher.getMethod(), parsed);
            }
        }
        return null;
    }
}

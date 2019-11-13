package style.kiwi.consoled.tree;

import style.kiwi.consoled.CommandContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

class MethodMatcher {
    private Method method;
    private ArgumentAcceptor acceptor;

    static MethodMatcher create(Method method) {
        var parameters = method.getParameters();
        var i = parameters.length;
        if (i < 1 || !CommandContext.class.equals(parameters[0].getType())) return null;
        ArgumentAcceptor prev = null;
        while (i > 1) {
            var index = i - 1;
            ArgumentAcceptor acceptor = null;
            var parameter = parameters[index];
            var type = parameter.getType();
            if (type.isArray()) {
                if (!(i == parameters.length && method.isVarArgs())) {
                    return null;
                } else {
                    type = type.getComponentType();
                }
            }
            if (int.class.equals(type) || Integer.class.equals(type)) {
                acceptor = new SimpleAcceptor(arg -> {
                    try {
                        return Integer.parseInt(arg);
                    } catch (NumberFormatException nfe) {
                        return null;
                    }
                });
            } else if (double.class.equals(type) || Double.class.equals(type)) {
                acceptor = new SimpleAcceptor(arg -> {
                    try {
                        return Double.parseDouble(arg);
                    } catch (NumberFormatException nfe) {
                        return null;
                    }
                });
            } else if (long.class.equals(type) || Long.class.equals(type)) {
                acceptor = new SimpleAcceptor(arg -> {
                    try {
                        return Long.parseLong(arg);
                    } catch (NumberFormatException nfe) {
                        return null;
                    }
                });
            } else if (float.class.equals(type) || Float.class.equals(type)) {
                acceptor = new SimpleAcceptor(arg -> {
                    try {
                        return Float.parseFloat(arg);
                    } catch (NumberFormatException nfe) {
                        return null;
                    }
                });
            } else if (short.class.equals(type) || Short.class.equals(type)) {
                acceptor = new SimpleAcceptor(arg -> {
                    try {
                        return Short.parseShort(arg);
                    } catch (NumberFormatException nfe) {
                        return null;
                    }
                });
            } else if (boolean.class.equals(type) || Boolean.class.equals(type)) {
                acceptor = new SimpleAcceptor(arg -> {
                    if (arg.equalsIgnoreCase("true") || arg.equals("1")) {
                        return true;
                    } else if (arg.equalsIgnoreCase("false") || arg.equals("0")) {
                        return false;
                    } else {
                        return null;
                    }
                });
            } else if (byte.class.equals(type) || Byte.class.equals(type)) {
                acceptor = new SimpleAcceptor(arg -> {
                    try {
                        return Byte.parseByte(arg);
                    } catch (NumberFormatException nfe) {
                        return null;
                    }
                });
            } else if (String.class.equals(type)) {
                acceptor = new SimpleAcceptor(arg -> arg);
            } else {
                return null;
            }
            if (i == parameters.length && method.isVarArgs()) {
                prev = new VarargAcceptor(acceptor, type);
            } else if (parameter.isAnnotationPresent(Optional.class)) {
                prev = new OptionalAcceptor(acceptor, prev);
            } else {
                prev = new ThenAcceptor(acceptor, prev);
            }
            i -= 1;
        }
        var result = new MethodMatcher();
        result.method = method;
        result.acceptor = prev;
        return result;
    }

    Object[] parse(String[] args) {
        if (acceptor != null) {
            var visitor = acceptor.accept(Arrays.stream(args).collect(Collectors.toCollection(LinkedList::new)), new AcceptVisitor());
            if (visitor != null) {
                return visitor.yield();
            } else {
                return null;
            }
        } else {
            return new Object[0];
        }
    }

    Method getMethod() {
        return method;
    }
}


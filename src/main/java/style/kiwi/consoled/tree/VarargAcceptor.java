package style.kiwi.consoled.tree;

import java.lang.reflect.Array;
import java.util.List;

public class VarargAcceptor implements ArgumentAcceptor {
    private ArgumentAcceptor unit;
    private Class<?> clazz;

    VarargAcceptor(ArgumentAcceptor single, Class<?> clazz) {
        this.unit = single;
        this.clazz = clazz;
    }

    @Override
    public AcceptVisitor accept(List<String> arg, AcceptVisitor visitor) {
        var saved = new AcceptVisitor();
        while (!arg.isEmpty()) {
            var result = unit.accept(arg, saved);
            if (result == null) {
                break;
            }
            saved = result;
        }
        var result = saved.yield();
        var array = Array.newInstance(clazz, result.length);
        System.arraycopy(result, 0, array, 0, result.length);
        visitor.add(array);
        return visitor;
    }
}

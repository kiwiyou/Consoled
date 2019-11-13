package style.kiwi.consoled.tree;

import java.util.List;
import java.util.function.Function;

public class SimpleAcceptor implements ArgumentAcceptor {
    private Function<String, Object> mapper;

    SimpleAcceptor(Function<String, Object> mapper) {
        this.mapper = mapper;
    }

    @Override
    public AcceptVisitor accept(List<String> args, AcceptVisitor visitor) {
        if (args.isEmpty()) return null;
        var result = mapper.apply(args.remove(0));
        if (result != null) {
            return visitor.add(result);
        } else {
            return null;
        }
    }
}

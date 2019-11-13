package style.kiwi.consoled.tree;

import java.util.LinkedList;
import java.util.List;

public class OptionalAcceptor implements ArgumentAcceptor {
    private ArgumentAcceptor inner;
    private ArgumentAcceptor next;

    OptionalAcceptor(ArgumentAcceptor inner, ArgumentAcceptor next) {
        this.inner = new ThenAcceptor(inner, next);
        this.next = next;
    }

    @Override
    public AcceptVisitor accept(List<String> args, AcceptVisitor visitor) {
        var copiedArgs = new LinkedList<>(args);
        var newVisitor = inner.accept(copiedArgs, visitor.copy());
        if (newVisitor != null) {
            return newVisitor;
        } else if (next != null) {
            return next.accept(args, visitor.add(null));
        } else {
            return visitor;
        }
    }
}
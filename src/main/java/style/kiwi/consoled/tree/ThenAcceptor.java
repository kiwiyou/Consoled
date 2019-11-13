package style.kiwi.consoled.tree;

import java.util.List;

public class ThenAcceptor implements ArgumentAcceptor {
    private ArgumentAcceptor prev;
    private ArgumentAcceptor next;

    ThenAcceptor(ArgumentAcceptor prev, ArgumentAcceptor next) {
        this.prev = prev;
        this.next = next;
    }

    @Override
    public AcceptVisitor accept(List<String> args, AcceptVisitor visitor) {
        var prevResult = prev.accept(args, visitor);
        if (prevResult != null) {
            if (next != null) {
                return next.accept(args, prevResult);
            } else {
                return prevResult;
            }
        } else {
            return null;
        }
    }
}

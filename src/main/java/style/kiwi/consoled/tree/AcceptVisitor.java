package style.kiwi.consoled.tree;

import java.util.Stack;

class AcceptVisitor {
    private Stack<Object> accepted;

    AcceptVisitor() {
        accepted = new Stack<>();
    }

    AcceptVisitor copy() {
        var copied = new AcceptVisitor();
        copied.accepted = (Stack<Object>) accepted.clone();
        return copied;
    }

    AcceptVisitor add(Object object) {
        accepted.push(object);
        return this;
    }

    Object[] yield() {
        return accepted.toArray();
    }
}

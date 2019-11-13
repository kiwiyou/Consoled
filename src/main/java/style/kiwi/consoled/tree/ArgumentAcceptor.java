package style.kiwi.consoled.tree;

import java.util.List;

interface ArgumentAcceptor {
    AcceptVisitor accept(List<String> args, AcceptVisitor visitor);
}

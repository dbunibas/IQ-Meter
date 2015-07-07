package it.unibas.spicybenchmark.operators.generators.ext.simpack.tree;

import simpack.api.ITreeNode;
import simpack.api.ITreeNodeComparator;
import simpack.util.tree.comparator.NamedTreeNodeComparator;
import simpack.util.tree.comparator.TypedTreeNodeComparator;

public class DebugTreeNodeComparator implements ITreeNodeComparator<ITreeNode> {
    
    private static org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(DebugTreeNodeComparator.class.getName());

    public DebugTreeNodeComparator() {
    }

    public int compare(ITreeNode o1, ITreeNode o2) {
        logger.debug("-------------");
        logger.debug("Comparing " + o1 + " with " + o2);
        NamedTreeNodeComparator c1 = new NamedTreeNodeComparator();
        logger.debug("NamedTreeNodeComparator " + c1.compare(o1, o2));
        TypedTreeNodeComparator c2 = new TypedTreeNodeComparator();
        logger.debug("TypedTreeNodeComparator " + c2.compare(o1, o2));
        logger.debug("-------------");
        return c1.compare(o1, o2);
    }

}

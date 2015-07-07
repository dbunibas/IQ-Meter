/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view;

import it.unibas.iqmeter.model.MappingTool;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Antonio Genovese
 */
public class ModelTreeExecutions implements TreeModel{
    
    String nodeRoot;
    MappingTool tool;

    public ModelTreeExecutions(MappingTool tool) {
        this.nodeRoot = "Executions";
        this.tool = tool;
    }
    
    

    public String getRoot() {
       return this.nodeRoot;
    }

    public Object getChild(Object parent, int index) {
        return this.tool.getMappingTask(index);
    }

    public int getChildCount(Object parent) {
        return this.tool.getExecutionsList().size();
        
    }

    public boolean isLeaf(Object node) {
       return !node.toString().equals(this.nodeRoot.toString());
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
       
    }

    public int getIndexOfChild(Object parent, Object child) {
       return 0;

    }

    public void addTreeModelListener(TreeModelListener l) {
       
    }

    public void removeTreeModelListener(TreeModelListener l) {
        
    }
    
    
    
    
}

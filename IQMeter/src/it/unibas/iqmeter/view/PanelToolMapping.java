/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view;

import it.unibas.iqmeter.controller.ActionShowEditScript;
import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.controller.ActionDeleteExecution;
import it.unibas.iqmeter.controller.ActionExplainQuality;
import it.unibas.iqmeter.controller.ActionOpenFolder;
import it.unibas.iqmeter.controller.ActionReloadProject;
import it.unibas.iqmeter.controller.ActionSelectExecution;
import it.unibas.iqmeter.controller.ActionShowAddTool;
import it.unibas.iqmeter.controller.ActionShowEffortGraph;
import it.unibas.iqmeter.controller.ListenerExcelAdapter;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.ping.framework.Applicazione;
import it.unibas.ping.framework.PannelloPing;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class PanelToolMapping extends PannelloPing {

    /**
     * Creates new form PanelToolMapping
     */
    private Log logger = LogFactory.getLog(this.getClass());

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelExecutions = new javax.swing.JPanel();
        scroolPaneTree = new javax.swing.JScrollPane();
        treeMappingExecutions = new javax.swing.JTree();
        panelDetails = new javax.swing.JPanel();
        scroolPaneTable = new javax.swing.JScrollPane();
        tableDetails = new javax.swing.JTable();

        setFont(getFont());
        setPreferredSize(new java.awt.Dimension(300, 250));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        treeMappingExecutions.setFont(treeMappingExecutions.getFont());
        treeMappingExecutions.setName("treeMappingExecutions"); // NOI18N
        treeMappingExecutions.setRowHeight(27);
        scroolPaneTree.setViewportView(treeMappingExecutions);

        javax.swing.GroupLayout panelExecutionsLayout = new javax.swing.GroupLayout(panelExecutions);
        panelExecutions.setLayout(panelExecutionsLayout);
        panelExecutionsLayout.setHorizontalGroup(
            panelExecutionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroolPaneTree, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        panelExecutionsLayout.setVerticalGroup(
            panelExecutionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelExecutionsLayout.createSequentialGroup()
                .addComponent(scroolPaneTree, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(panelExecutions);

        panelDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Execution Details"));

        tableDetails.setAutoCreateRowSorter(true);
        tableDetails.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableDetails.setFont(tableDetails.getFont());
        tableDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableDetails.setGridColor(new java.awt.Color(255, 255, 255));
        tableDetails.setRowHeight(20);
        tableDetails.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tableDetails.setShowHorizontalLines(false);
        tableDetails.setShowVerticalLines(false);
        tableDetails.setUpdateSelectionOnSort(false);
        scroolPaneTable.setViewportView(tableDetails);

        javax.swing.GroupLayout panelDetailsLayout = new javax.swing.GroupLayout(panelDetails);
        panelDetails.setLayout(panelDetailsLayout);
        panelDetailsLayout.setHorizontalGroup(
            panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroolPaneTable, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
        );
        panelDetailsLayout.setVerticalGroup(
            panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDetailsLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(scroolPaneTable, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addGap(13, 13, 13))
        );

        add(panelDetails);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelDetails;
    private javax.swing.JPanel panelExecutions;
    private javax.swing.JScrollPane scroolPaneTable;
    private javax.swing.JScrollPane scroolPaneTree;
    private javax.swing.JTable tableDetails;
    private javax.swing.JTree treeMappingExecutions;
    // End of variables declaration//GEN-END:variables

    @Override
    public void inizializza() {
        initComponents();
        postInit();

    }

    private void postInit() {
        configTree();
        //IOsservatoreCollezione ossTree = new ObserverTree(this.treeMappingExecutions, MappingTool.class.getName() + "_" + this.getName(), MappingToolPM.class, Constant.EXECUTIONS_LIST);
        //logger.debug("Add osservatoreCollezione --> " + MappingTool.class.getName() + "_" + this.getName() + " --> " + ossTree);
        this.tableDetails.setModel(new ModelTableDetails(this.getName(), null));

        setTreeModel();
        configTable();
    }

    private void setTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Executions", true);
        root.setParent(null);
        DefaultTreeModel model = new DefaultTreeModel(root, false);
        this.treeMappingExecutions.setModel(model);
    }

    public void buildTableDetails() {
        MappingExecution execution = (MappingExecution) Applicazione.getInstance().getModello().getBean(Constant.EXECUTION_SELECTED);
        logger.debug("Execution build Table: " + execution.getNumberLabel());
        ModelTableDetails tableModel = new ModelTableDetails(this.getName(), execution);
        this.tableDetails.setModel(tableModel);
        configTable();
        this.scroolPaneTable.getVerticalScrollBar().setValue(0);
    }

    public void cleanTableDetails() {
        this.tableDetails.setModel(new ModelTableDetails(this.getName(), null));
        configTable();
    }

    private void configTree() {
        this.treeMappingExecutions.setCellRenderer(new RenderTreeExecutions(this.getName()));
        this.treeMappingExecutions.addTreeSelectionListener(new TreeSelectionListnerGeneric(ActionSelectExecution.class.getName()));
        this.treeMappingExecutions.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.treeMappingExecutions.setName(this.getName() + Constant.TREE_EXECUTIONS);
        this.treeMappingExecutions.addMouseListener(new MouseListenerTree());
        this.treeMappingExecutions.addKeyListener(new KeyboardKeyListenerTree());
        this.createPopupMenu();
    }

    private void configTable() {
        this.tableDetails.getColumnModel().getColumn(0).setPreferredWidth(250);
        this.tableDetails.getColumnModel().getColumn(0).setMaxWidth(300);
        this.tableDetails.setRowHeight(25);
        this.tableDetails.setBorder(null);
        this.tableDetails.setDefaultRenderer(Object.class, new RenderTableDetails());
        ListenerExcelAdapter myAd = new ListenerExcelAdapter(this.tableDetails);
    }

    //only 4-5 executions before the crash
//    public void addNodeExecution() {
//        DefaultTreeModel model = (DefaultTreeModel) this.treeMappingExecutions.getModel();
//        MappingTool tool = (MappingTool) Applicazione.getInstance().getModello().getBean(MappingTool.class.getName() + "_" + this.getName());
//        DefaultMutableTreeNode child = new DefaultMutableTreeNode(tool.getMappingTask(tool.getNumberExecutions() - 1));
//        //model.insertNodeInto(child, (MutableTreeNode) model.getRoot(), model.getChildCount(model.getRoot()));
//        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
//        root.add(child);
//        //treeMappingExecutions.expandPath(new TreePath(model.getPathToRoot(child.getParent())));
//        //model.nodesWereInserted((TreeNode)root, new int[]{model.getChildCount(model.getRoot()) - 1});
//        //expanse the tree path 
//        model.reload((TreeNode) model.getRoot());
//        //Applicazione.getInstance().getModello().notificaModificaCollezione(tool, "executionsList", tool.getNumberExecutions() - 1, Modello.AGGIUNTA);
//    }
    //version with the tree model --> only 2-3 executions before the crash
//    public void addNodeExecution() {
//        MappingTool tool = (MappingTool) Applicazione.getInstance().getModello().getBean(MappingTool.class.getName() + "_" + this.getName());
//        ModelTreeExecutions model = new ModelTreeExecutions(tool);
//        this.treeMappingExecutions.setModel(model);
//    }
    public void addNodeExecution() {
        DefaultTreeModel model = (DefaultTreeModel) this.treeMappingExecutions.getModel();
        MappingTool tool = (MappingTool) Applicazione.getInstance().getModello().getBean(MappingTool.class.getName() + "_" + this.getName());
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(tool.getMappingTask(tool.getNumberExecutions() - 1));
        model.insertNodeInto(child, (MutableTreeNode) model.getRoot(), model.getChildCount(model.getRoot()));
        model.reload((TreeNode) model.getRoot());
    }

    public void deleteNodeExecution(int index) {
        DefaultTreeModel model = (DefaultTreeModel) this.treeMappingExecutions.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.remove(index);
        model.reload((TreeNode) model.getRoot());
    }

    public void recreateTree() {
        DefaultTreeModel model = (DefaultTreeModel) this.treeMappingExecutions.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        MappingTool tool = (MappingTool) Applicazione.getInstance().getModello().getBean(MappingTool.class.getName() + "_" + this.getName());
        if (tool == null) {
            throw new IllegalStateException("Unable to load tool " + MappingTool.class.getName() + "_" + this.getName());
        }
        for (int i = 0; i < tool.getNumberExecutions(); i++) {
            MappingExecution execution = tool.getMappingTask(i);
            logger.trace("Add execution: " + execution.getNumberLabel());
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(execution);
            model.insertNodeInto(child, (MutableTreeNode) model.getRoot(), model.getChildCount(model.getRoot()));
        }
        model.reload((TreeNode) model.getRoot());
    }

    private void createPopupItems(JPopupMenu popup) {
        JMenuItem itemDelete = new JMenuItem();
        itemDelete
                .setAction(Applicazione.getInstance().getControllo().getAzioneSwing(ActionDeleteExecution.class
                                .getName()));
        itemDelete.setIcon(Utils.createIcon(Constant.ICON_DELETE));

        JMenuItem itemGraph = new JMenuItem();

        itemGraph.setAction(Applicazione.getInstance().getControllo().getAzioneSwing(ActionShowEffortGraph.class
                .getName()));
        itemGraph.setIcon(Utils.createIcon(Constant.ICON_GRAPH));

        JMenuItem itemQuality = new JMenuItem();

        itemQuality.setAction(Applicazione.getInstance().getControllo().getAzioneSwing(ActionExplainQuality.class
                .getName()));
        itemQuality.setIcon(Utils.createIcon(Constant.ICON_EXPLAIN_QUALITY_SMALL));

        JMenuItem itemScript = new JMenuItem();

        itemScript.setAction(Applicazione.getInstance().getControllo().getAzioneSwing(ActionShowEditScript.class
                .getName()));
        itemScript.setIcon(Utils.createIcon(Constant.ICON_ANNOTATION_SCRIPT_SMALL));

        JMenuItem itemAdd = new JMenuItem();

        itemAdd.setAction(Applicazione.getInstance().getControllo().getAzioneSwing(ActionShowAddTool.class
                .getName()));
        itemAdd.setIcon(Utils.createIcon(Constant.ICON_ADD_TOOL_SMALL));

        JMenuItem itemReload = new JMenuItem();

        itemReload.setAction(Applicazione.getInstance().getControllo().getAzioneSwing(ActionReloadProject.class
                .getName()));
        itemReload.setIcon(Utils.createIcon(Constant.ICON_RELOAD_SMALL));

        JMenuItem itemOpenFolder = new JMenuItem();

        itemOpenFolder.setAction(Applicazione.getInstance().getControllo().getAzioneSwing(ActionOpenFolder.class
                .getName()));
        itemOpenFolder.setIcon(Utils.createIcon(Constant.ICON_OPEN_FOLDER_SMALL));
        itemOpenFolder.setText(
                "Open Output Folder");

        popup.add(itemDelete);
        popup.add(itemGraph);
        popup.add(itemScript);
        popup.add(itemQuality);
        popup.add(itemAdd);
        popup.add(itemReload);
        popup.add(itemOpenFolder);
    }

    private void createPopupMenu() {
        final JPopupMenu popup = new JPopupMenu();
        createPopupItems(popup);

        this.treeMappingExecutions.add(popup);
        this.treeMappingExecutions.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    logger.trace("Mapping execution popup Active");
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private class TreeSelectionListnerGeneric implements TreeSelectionListener {

        private String idAction;

        public TreeSelectionListnerGeneric(String name) {
            this.idAction = name;
        }

        public void valueChanged(TreeSelectionEvent e) {
            Applicazione.getInstance().getControllo().eseguiAzione(idAction, e);
        }
    }

    private class MouseListenerTree implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 || e.getButton() == MouseEvent.BUTTON2) {
                TreePath path = treeMappingExecutions.getPathForLocation(e.getX(), e.getY());
                if (path != null) {
                    Applicazione.getInstance().getControllo().eseguiAzione(ActionShowEffortGraph.class.getName(), e);
                }

            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    private class KeyboardKeyListenerTree implements KeyListener {

        public void keyTyped(KeyEvent e) {

        }

        public void keyPressed(KeyEvent e) {

        }

        public void keyReleased(KeyEvent e) {
            logger.debug("Key Released: " + e.getKeyCode());
            if (e.getKeyCode() == KeyEvent.VK_DELETE && Applicazione.getInstance().getModello().getBean(Constant.EXECUTION_SELECTED) != null) {
                Applicazione.getInstance().getControllo().eseguiAzione(ActionDeleteExecution.class.getName(), e);
            }
        }

    }
}
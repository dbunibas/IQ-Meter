/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view.effortgraph;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;
import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.ping.framework.Applicazione;
import it.unibas.ping.framework.FramePing;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class DialogEffortGraph extends FramePing {

    private mxGraph graph;
    private Log logger = LogFactory.getLog(this.getClass());

    static {
        setDefaultLookAndFeelDecorated(true);
        setlookAndFeel();
    }

    private static void setlookAndFeel() {
        try {
            String lookAndFeelname = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeelname);
        } catch (Exception ex) {
            System.out.println("Error loading look and feel");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCentral = new javax.swing.JPanel();
        panelGraph = new javax.swing.JPanel();
        labelIcon = new javax.swing.JLabel();
        labelMessage = new javax.swing.JLabel();
        buttonClose = new javax.swing.JButton();
        buttonExportImg = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        panelCentral.setPreferredSize(new java.awt.Dimension(100, 100));

        panelGraph.setFont(panelGraph.getFont());
        panelGraph.setPreferredSize(new java.awt.Dimension(70, 70));
        panelGraph.setLayout(new javax.swing.BoxLayout(panelGraph, javax.swing.BoxLayout.LINE_AXIS));

        labelIcon.setPreferredSize(new java.awt.Dimension(16, 20));

        labelMessage.setFont(labelMessage.getFont());

        buttonClose.setFont(buttonClose.getFont());
        buttonClose.setText("Close");
        buttonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCloseActionPerformed(evt);
            }
        });

        buttonExportImg.setFont(buttonExportImg.getFont());
        buttonExportImg.setText("Export");
        buttonExportImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExportImgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCentralLayout = new javax.swing.GroupLayout(panelCentral);
        panelCentral.setLayout(panelCentralLayout);
        panelCentralLayout.setHorizontalGroup(
            panelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCentralLayout.createSequentialGroup()
                .addGroup(panelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCentralLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 593, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addComponent(buttonExportImg)
                        .addGap(18, 18, 18)
                        .addComponent(buttonClose)
                        .addGap(8, 8, 8))
                    .addGroup(panelCentralLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(panelGraph, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)))
                .addGap(30, 30, 30))
        );
        panelCentralLayout.setVerticalGroup(
            panelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCentralLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(panelGraph, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buttonClose)
                        .addComponent(buttonExportImg))
                    .addComponent(labelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(panelCentral);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonCloseActionPerformed

    private void buttonExportImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExportImgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonExportImgActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClose;
    private javax.swing.JButton buttonExportImg;
    private javax.swing.JLabel labelIcon;
    private javax.swing.JLabel labelMessage;
    private javax.swing.JPanel panelCentral;
    private javax.swing.JPanel panelGraph;
    // End of variables declaration//GEN-END:variables

    @Override
    public void inizializza() {
        initComponents();
        Image img = Utils.createIcon("graph.png").getImage();
        this.setIconImage(img);
        this.labelIcon.setVisible(false);
        this.labelMessage.setVisible(false);
        postInit();
        JScrollPane pane = new JScrollPane(this.panelCentral);
        this.getContentPane().add(pane);
        this.setLocationRelativeTo(this.vista.getFramePrincipale());
        this.pack();
    }

    private void close() {
        this.labelIcon.setVisible(false);
        this.labelMessage.setVisible(false);
        this.setVisible(false);
    }

    private String directoryExecution() {
        return this.getName().replace(this.getClass().getName(), "");
    }

    private void setStatus(boolean success) {
        if (success) {
            this.labelIcon.setIcon(Utils.createIcon(Constant.ICON_OK));
            this.labelMessage.setText(Constant.MESSAGE_SUCCESS_EXPORT_GRAPH);
        } else {
            this.labelIcon.setIcon(Utils.createIcon(Constant.ICON_ERROR));
            this.labelMessage.setText(Constant.MESSAGE_ERROR_EXPORT_GRAPH);
        }
        this.labelIcon.setVisible(true);
        this.labelMessage.setVisible(true);
    }

    public void buildGraph() {
        this.labelIcon.setVisible(false);
        this.labelMessage.setVisible(false);
        this.panelGraph.removeAll();
        MappingExecution task = (MappingExecution) Applicazione.getInstance().getModello().getBean(Constant.EXECUTION_SELECTED);
        JPanel toolPanel = (JPanel) Applicazione.getInstance().getModello().getBean(Constant.TAB_SELECTED);
        mxGraph mxgraph = (mxGraph) Applicazione.getInstance().getModello().getBean(task.getDirectory() + "_mxGraph");

        mxGraphComponent graphComponent = new mxGraphComponent(mxgraph);
        //for add edges
        graphComponent.setConnectable(false);
        graphComponent.setName("effortGraph");
        graphComponent.getViewport().setBackground(Color.white);
        //prevents the child to break away from his father
        graphComponent.getGraphHandler().setRemoveCellsFromParent(false);
        graphComponent.getGraphHandler().setCenterPreview(true);
        this.panelGraph.add(graphComponent, BorderLayout.CENTER);
        this.setName(this.getClass().getName() + task.getDirectory());
        this.panelGraph.setSize(graphComponent.getSize());
        this.setTitle("Effort Graph for the Execution #" + task.getNumberLabel() + " of " + toolPanel.getName());
        this.setSize(1000, 600);
        this.graph = mxgraph;
        mxgraph.refresh();
        this.setLocationRelativeTo(this.vista.getFramePrincipale());
    }

    private void postInit() {
        this.buttonClose.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        this.buttonClose.setText("Close");
        this.buttonClose.setMnemonic(new Integer(KeyEvent.VK_C));
        this.buttonClose.setToolTipText("Close Effort Graph");
        this.buttonExportImg.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                BufferedImage image = new BufferedImage(450, 450, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = (Graphics2D) image.getGraphics();
                BufferedImage image2 = mxCellRenderer.createBufferedImage(graph, null, 1.0, null, true, null, new mxGraphics2DCanvas(g2));
                MappingExecution task = (MappingExecution) Applicazione.getInstance().getModello().getBean(Constant.EXECUTION_SELECTED);
                JPanel toolPanel = (JPanel) Applicazione.getInstance().getModello().getBean(Constant.TAB_SELECTED);
                Scenario scenario = (Scenario) Applicazione.getInstance().getModello().getBean(Scenario.class.getName());
                File outputfile = new File(directoryExecution() + "/" + scenario.getName() + "_" + toolPanel.getName() + "_effortGraph" + task.getQuality().getFmeasure() + ".png");
                try {
                    ImageIO.write(image2, "png", outputfile);
                    setStatus(true);
                } catch (Exception ex) {
                    logger.error(ex);
                    vista.finestraErrore(Constant.MESSAGE_ERROR_EXPORT_GRAPH + "\n" + ex);
                    setStatus(false);
                }
            }
        });
        this.buttonExportImg.setText("Export");
        this.buttonExportImg.setMnemonic(new Integer(KeyEvent.VK_E));
        this.buttonExportImg.setToolTipText("Export Effort Graph Image in the Mapping Execution folder");
    }
}
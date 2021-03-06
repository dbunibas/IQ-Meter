/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.controller.ActionAddTool;
import it.unibas.iqmeter.controller.ActionChooseFile;
import it.unibas.iqmeter.controller.ActionSelectTool;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.view.presentationmodel.MappingToolPM;
import it.unibas.ping.binding.Form;
import it.unibas.ping.binding.IForm;
import it.unibas.ping.binding.collegatori.CollegatoreComboBox;
import it.unibas.ping.binding.collegatori.CollegatoreTextField;
import it.unibas.ping.binding.collegatori.ICollegatore;
import it.unibas.ping.binding.osservatori.IOsservatore;
import it.unibas.ping.binding.osservatori.OsservatoreTextField;
import it.unibas.ping.convalida.ConvalidatoreValoreAstratto;
import it.unibas.ping.convalida.IConvalidatoreValore;
import it.unibas.ping.framework.FinestraDiDialogoPing;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;

/**
 *
 * @author antonio
 */
public class DialogAddTool extends FinestraDiDialogoPing {

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

        panelTop = new javax.swing.JPanel();
        panelStep = new javax.swing.JPanel();
        labelImg = new javax.swing.JLabel();
        labelIcon = new javax.swing.JLabel();
        panelData = new javax.swing.JPanel();
        labelTool = new javax.swing.JLabel();
        comboTools = new javax.swing.JComboBox();
        labelTranslated = new javax.swing.JLabel();
        fieldTranslated = new javax.swing.JTextField();
        buttonFileTranslated = new javax.swing.JButton();
        labelNote = new javax.swing.JLabel();
        labelMapping = new javax.swing.JLabel();
        fieldMapping = new javax.swing.JTextField();
        buttonFileMapping = new javax.swing.JButton();
        fieldScript = new javax.swing.JTextField();
        labelScript = new javax.swing.JLabel();
        buttonFileScript = new javax.swing.JButton();
        panelBottom = new javax.swing.JPanel();
        separatorBottom = new javax.swing.JSeparator();
        buttonUndo = new javax.swing.JButton();
        buttonAdd = new javax.swing.JButton();

        panelTop.setAlignmentX(0.0F);
        panelTop.setPreferredSize(new java.awt.Dimension(600, 370));

        panelStep.setAlignmentX(0.0F);
        panelStep.setAlignmentY(0.0F);
        panelStep.setFont(panelStep.getFont());
        panelStep.setPreferredSize(new java.awt.Dimension(280, 370));

        labelImg.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelImg.setAlignmentY(0.0F);

        labelIcon.setIconTextGap(0);
        labelIcon.setPreferredSize(new java.awt.Dimension(200, 200));

        javax.swing.GroupLayout panelStepLayout = new javax.swing.GroupLayout(panelStep);
        panelStep.setLayout(panelStepLayout);
        panelStepLayout.setHorizontalGroup(
            panelStepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStepLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(labelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
            .addGroup(panelStepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelStepLayout.createSequentialGroup()
                    .addComponent(labelImg)
                    .addGap(0, 200, Short.MAX_VALUE)))
        );
        panelStepLayout.setVerticalGroup(
            panelStepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStepLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(labelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelStepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelStepLayout.createSequentialGroup()
                    .addComponent(labelImg, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        panelData.setMinimumSize(new java.awt.Dimension(320, 370));
        panelData.setPreferredSize(new java.awt.Dimension(320, 370));

        labelTool.setFont(labelTool.getFont());
        labelTool.setText("Mapping Tool:");

        comboTools.setFont(comboTools.getFont());
        comboTools.setName("comboTools"); // NOI18N

        labelTranslated.setFont(labelTranslated.getFont());
        labelTranslated.setText("Generated Instance:");

        fieldTranslated.setFont(fieldTranslated.getFont());
        fieldTranslated.setMinimumSize(new java.awt.Dimension(6, 25));
        fieldTranslated.setName("fieldTranslated"); // NOI18N
        fieldTranslated.setPreferredSize(new java.awt.Dimension(6, 25));

        buttonFileTranslated.setFont(buttonFileTranslated.getFont());
        buttonFileTranslated.setText("...");
        buttonFileTranslated.setMaximumSize(new java.awt.Dimension(71, 25));
        buttonFileTranslated.setMinimumSize(new java.awt.Dimension(71, 25));
        buttonFileTranslated.setName("buttonFileTranslated"); // NOI18N
        buttonFileTranslated.setPreferredSize(new java.awt.Dimension(71, 25));

        labelNote.setFont(labelNote.getFont().deriveFont(labelNote.getFont().getSize()-2f));
        labelNote.setText("<html><P ALIGN=\"justify\">Note: <br>For quality measure we compare Generated Instance wrt Expected Target Instance.</P>");
        labelNote.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        labelMapping.setFont(labelMapping.getFont());
        labelMapping.setText("Mapping File:");

        fieldMapping.setFont(fieldMapping.getFont());
        fieldMapping.setMinimumSize(new java.awt.Dimension(6, 25));
        fieldMapping.setName("fieldMapping"); // NOI18N
        fieldMapping.setPreferredSize(new java.awt.Dimension(6, 25));

        buttonFileMapping.setFont(buttonFileMapping.getFont());
        buttonFileMapping.setText("...");
        buttonFileMapping.setMaximumSize(new java.awt.Dimension(71, 25));
        buttonFileMapping.setMinimumSize(new java.awt.Dimension(71, 25));
        buttonFileMapping.setName("buttonFileMapping"); // NOI18N
        buttonFileMapping.setPreferredSize(new java.awt.Dimension(71, 25));

        fieldScript.setFont(fieldScript.getFont());
        fieldScript.setMinimumSize(new java.awt.Dimension(6, 25));
        fieldScript.setName("fieldScript"); // NOI18N
        fieldScript.setPreferredSize(new java.awt.Dimension(6, 25));

        labelScript.setFont(labelScript.getFont());
        labelScript.setText("Script File:");

        buttonFileScript.setFont(buttonFileScript.getFont());
        buttonFileScript.setText("...");
        buttonFileScript.setMaximumSize(new java.awt.Dimension(71, 25));
        buttonFileScript.setMinimumSize(new java.awt.Dimension(71, 25));
        buttonFileScript.setName("buttonFileScript"); // NOI18N
        buttonFileScript.setPreferredSize(new java.awt.Dimension(71, 25));

        javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
        panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addComponent(labelMapping)
                .addContainerGap(276, Short.MAX_VALUE))
            .addGroup(panelDataLayout.createSequentialGroup()
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelNote, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                    .addGroup(panelDataLayout.createSequentialGroup()
                        .addComponent(labelTool)
                        .addGap(18, 18, 18)
                        .addComponent(comboTools, 0, 232, Short.MAX_VALUE))
                    .addGroup(panelDataLayout.createSequentialGroup()
                        .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDataLayout.createSequentialGroup()
                                .addComponent(labelTranslated, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                                .addGap(57, 57, 57))
                            .addGroup(panelDataLayout.createSequentialGroup()
                                .addComponent(labelScript)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 199, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDataLayout.createSequentialGroup()
                                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fieldMapping, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                                    .addComponent(fieldScript, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                                    .addComponent(fieldTranslated, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))
                                .addGap(18, 18, 18)))
                        .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonFileMapping, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonFileScript, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonFileTranslated, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(30, 30, 30))
        );
        panelDataLayout.setVerticalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelTool))
                .addGap(18, 18, 18)
                .addComponent(labelMapping)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldMapping, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonFileMapping, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(labelTranslated)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldTranslated, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonFileTranslated, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(labelScript)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldScript, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonFileScript, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(labelNote, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelTopLayout = new javax.swing.GroupLayout(panelTop);
        panelTop.setLayout(panelTopLayout);
        panelTopLayout.setHorizontalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addComponent(panelStep, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(panelData, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE))
        );
        panelTopLayout.setVerticalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelStep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        panelBottom.setAlignmentX(0.0F);
        panelBottom.setAlignmentY(0.0F);
        panelBottom.setPreferredSize(new java.awt.Dimension(600, 50));

        separatorBottom.setBackground(new java.awt.Color(240, 240, 240));
        separatorBottom.setAlignmentY(0.0F);
        separatorBottom.setPreferredSize(new java.awt.Dimension(600, 6));

        buttonUndo.setFont(buttonUndo.getFont());
        buttonUndo.setText("Cancel");
        buttonUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUndoActionPerformed(evt);
            }
        });

        buttonAdd.setFont(buttonAdd.getFont());
        buttonAdd.setText("Add");
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBottomLayout = new javax.swing.GroupLayout(panelBottom);
        panelBottom.setLayout(panelBottomLayout);
        panelBottomLayout.setHorizontalGroup(
            panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(separatorBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBottomLayout.createSequentialGroup()
                .addGap(409, 409, 409)
                .addComponent(buttonAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(buttonUndo, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBottomLayout.setVerticalGroup(
            panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBottomLayout.createSequentialGroup()
                .addComponent(separatorBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonUndo)
                    .addComponent(buttonAdd))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBottom, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
            .addComponent(panelTop, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelTop, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUndoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonUndoActionPerformed

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonAddActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonFileMapping;
    private javax.swing.JButton buttonFileScript;
    private javax.swing.JButton buttonFileTranslated;
    private javax.swing.JButton buttonUndo;
    private javax.swing.JComboBox comboTools;
    private javax.swing.JTextField fieldMapping;
    private javax.swing.JTextField fieldScript;
    private javax.swing.JTextField fieldTranslated;
    private javax.swing.JLabel labelIcon;
    private javax.swing.JLabel labelImg;
    private javax.swing.JLabel labelMapping;
    private javax.swing.JLabel labelNote;
    private javax.swing.JLabel labelScript;
    private javax.swing.JLabel labelTool;
    private javax.swing.JLabel labelTranslated;
    private javax.swing.JPanel panelBottom;
    private javax.swing.JPanel panelData;
    private javax.swing.JPanel panelStep;
    private javax.swing.JPanel panelTop;
    private javax.swing.JSeparator separatorBottom;
    // End of variables declaration//GEN-END:variables

    @Override
    public void inizializza() {
        initComponents();
        createForm();
        postInit();
        this.setLocationRelativeTo(this.vista.getFramePrincipale());
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setResizable(false);
        this.setModal(true);
        this.setTitle("Add Mapping Tool");
        this.pack();
    }

    @SuppressWarnings("unchecked")
    public void buildCombo() {
        this.comboTools.removeAllItems();
        List<String> tools = (List<String>) this.modello.getBean(Constant.TOOLS_LIST);
        for (String string : tools) {
            comboTools.addItem(string.replace("_", " "));
        }
        postInit();
    }

    private void createForm() {
        IForm form = new Form(this);

        //tool name
        ICollegatore collTool = new CollegatoreComboBox(form, this.comboTools, MappingTool.class.getName(), MappingToolPM.class, "toolName");
        
        //mapping file
        IOsservatore obsMapping = new OsservatoreTextField(Constant.FIELD_FILE_MAPPING, MappingTool.class.getName(), MappingToolPM.class, "mappingPath");
        ICollegatore collMapping = new CollegatoreTextField(form, Constant.FIELD_FILE_MAPPING, MappingTool.class.getName(), MappingToolPM.class, "mappingPath");
        IConvalidatoreValore convMapping = createFieldValidator(Constant.FIELD_FILE_MAPPING, "Mapping Task");
        collMapping.addConvalidatore(convMapping);
        
        //file instances generated
        IOsservatore obsTranslated = new OsservatoreTextField(Constant.FIELD_FILE_TRANSLATED, MappingTool.class.getName(), MappingToolPM.class, "translatedInstance");
        ICollegatore collTranslated = new CollegatoreTextField(form, Constant.FIELD_FILE_TRANSLATED, MappingTool.class.getName(), MappingToolPM.class, "translatedInstance");
        IConvalidatoreValore convTranslated = createFieldValidator(Constant.FIELD_FILE_TRANSLATED, "Generated Instance");
        collTranslated.addConvalidatore(convTranslated);
        
        //file script
        IOsservatore obsScript = new OsservatoreTextField(Constant.FIELD_FILE_SCRIPT, MappingTool.class.getName(), MappingToolPM.class, "scriptFile");
        ICollegatore collScript = new CollegatoreTextField(form, Constant.FIELD_FILE_SCRIPT, MappingTool.class.getName(), MappingToolPM.class, "scriptFile");

        form.setBottoneCommit(this.buttonAdd, ActionAddTool.class.getName());
        form.setBottoneRollback(this.buttonUndo);
    }

    private IConvalidatoreValore createFieldValidator(String field, final String message) {
        return new ConvalidatoreValoreAstratto(field) {
            @Override
            public List convalida(Object o) {
                String name = (String) o;

                List<String> errors = new ArrayList<String>();
                if (name.equals("")) {
                    errors.add("The data value " + message + " can not be empty");
                } else if (message.equals("Mapping Task")) {
                    String tool = (String) modello.getBean(Constant.COMBO_TOOL_SELECTED);
                    if (!Utils.convalidateFormatFile(tool, name)) {
                        errors.add("The File for data value " + message + " is not valid");
                    }
                } else if (!name.contains(File.separator)) {
                    errors.add("Path for data value " + message + " is not valid");
                }
                return errors;
            }

            @Override
            public Object converti(Object o) {
                return o;
            }
        };
    }

    private void postInit() {
        this.buttonFileMapping.setAction(this.controllo.getAzioneSwing(ActionChooseFile.class.getName()));
        this.buttonFileTranslated.setAction(this.controllo.getAzioneSwing(ActionChooseFile.class.getName()));
        this.buttonFileScript.setAction(this.controllo.getAzioneSwing(ActionChooseFile.class.getName()));
        this.comboTools.setAction(this.controllo.getAzioneSwing(ActionSelectTool.class.getName()));
        this.labelImg.setIcon(Utils.createImage(Constant.TOOLS_BACK));
        this.labelIcon.setIcon(Utils.createImage("tools.png"));
        this.comboTools.setToolTipText(Constant.TOOLTIP_TOOL_NAME);
        this.fieldMapping.setToolTipText(Constant.TOOLTIP_TOOL_MAPPING_FILE);
        this.fieldTranslated.setToolTipText(Constant.TOOLTIP_TOOL_INSTANCE_GENERATED);
        this.fieldScript.setToolTipText(Constant.TOOLTIP_TOOL_SCRIPT);

    }
}

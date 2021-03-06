/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view.wizard;

import it.unibas.iqmeter.Constant;
import it.unibas.iqmeter.Utils;
import it.unibas.iqmeter.controller.ActionChooseFile;
import it.unibas.iqmeter.controller.wizard.ActionBackStep2;
import it.unibas.iqmeter.controller.wizard.ActionNextStep2;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.iqmeter.view.presentationmodel.ScenarioPM;
import it.unibas.ping.binding.Form;
import it.unibas.ping.binding.IForm;
import it.unibas.ping.binding.collegatori.CollegatoreTextField;
import it.unibas.ping.binding.collegatori.ICollegatore;
import it.unibas.ping.binding.osservatori.IOsservatore;
import it.unibas.ping.binding.osservatori.OsservatoreTextField;
import it.unibas.ping.convalida.ConvalidatoreValoreAstratto;
import it.unibas.ping.convalida.IConvalidatoreValore;
import it.unibas.ping.framework.FinestraDiDialogoPing;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.UIManager;

/**
 *
 * @author antonio
 */
public class ProjectWizardStep2 extends FinestraDiDialogoPing {

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
        labelSteps = new javax.swing.JLabel();
        separatorTop1 = new javax.swing.JSeparator();
        labelStep1 = new javax.swing.JLabel();
        labelStep2 = new javax.swing.JLabel();
        labelStep3 = new javax.swing.JLabel();
        labelImg = new javax.swing.JLabel();
        labelStep4 = new javax.swing.JLabel();
        panelData = new javax.swing.JPanel();
        separatorTop2 = new javax.swing.JSeparator();
        labelStepName = new javax.swing.JLabel();
        labelSchemaSource = new javax.swing.JLabel();
        fieldSchemaSource = new javax.swing.JTextField();
        buttonSchemaSource = new javax.swing.JButton();
        fieldExpected = new javax.swing.JTextField();
        labelTarget = new javax.swing.JLabel();
        buttonFileExpected = new javax.swing.JButton();
        fieldSchemaTarget = new javax.swing.JTextField();
        labelSchemaTarget = new javax.swing.JLabel();
        buttonSchemaTarget = new javax.swing.JButton();
        checkCopy = new javax.swing.JCheckBox();
        panelBottom = new javax.swing.JPanel();
        separatorBottom = new javax.swing.JSeparator();
        buttonCancel = new javax.swing.JButton();
        buttonNext = new javax.swing.JButton();
        buttonBack = new javax.swing.JButton();
        buttonFinish = new javax.swing.JButton();

        panelTop.setAlignmentX(0.0F);
        panelTop.setPreferredSize(new java.awt.Dimension(600, 370));

        panelStep.setAlignmentX(0.0F);
        panelStep.setAlignmentY(0.0F);
        panelStep.setFont(panelStep.getFont());
        panelStep.setPreferredSize(new java.awt.Dimension(280, 370));

        labelSteps.setFont(labelSteps.getFont().deriveFont(labelSteps.getFont().getStyle() | java.awt.Font.BOLD));
        labelSteps.setText("Steps");

        separatorTop1.setBackground(new java.awt.Color(240, 240, 240));
        separatorTop1.setPreferredSize(new java.awt.Dimension(242, 6));

        labelStep1.setFont(labelStep1.getFont().deriveFont(labelStep1.getFont().getStyle() & ~java.awt.Font.BOLD));
        labelStep1.setText("Step 1- Project Data");

        labelStep2.setFont(labelStep2.getFont().deriveFont(labelStep2.getFont().getStyle() | java.awt.Font.BOLD));
        labelStep2.setText("Step 2 - Mapping Data >>");
        labelStep2.setMaximumSize(new java.awt.Dimension(143, 7));
        labelStep2.setMinimumSize(new java.awt.Dimension(143, 7));
        labelStep2.setPreferredSize(new java.awt.Dimension(228, 7));

        labelStep3.setFont(labelStep3.getFont().deriveFont(labelStep3.getFont().getStyle() & ~java.awt.Font.BOLD));
        labelStep3.setText("Step 3 - Instances Data");
        labelStep3.setMaximumSize(new java.awt.Dimension(143, 7));
        labelStep3.setMinimumSize(new java.awt.Dimension(143, 7));
        labelStep3.setPreferredSize(new java.awt.Dimension(143, 7));

        labelImg.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        labelStep4.setFont(labelStep4.getFont().deriveFont(labelStep4.getFont().getStyle() & ~java.awt.Font.BOLD));
        labelStep4.setText("Step 4 - Add Tool");
        labelStep4.setMaximumSize(new java.awt.Dimension(143, 7));
        labelStep4.setMinimumSize(new java.awt.Dimension(143, 7));
        labelStep4.setPreferredSize(new java.awt.Dimension(143, 7));

        javax.swing.GroupLayout panelStepLayout = new javax.swing.GroupLayout(panelStep);
        panelStep.setLayout(panelStepLayout);
        panelStepLayout.setHorizontalGroup(
            panelStepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStepLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelStepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(separatorTop1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSteps, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelStep1)
                    .addComponent(labelStep3, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelStep2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelStep4, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46))
            .addGroup(panelStepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelStepLayout.createSequentialGroup()
                    .addComponent(labelImg)
                    .addGap(0, 286, Short.MAX_VALUE)))
        );
        panelStepLayout.setVerticalGroup(
            panelStepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStepLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelSteps)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separatorTop1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelStep1)
                .addGap(35, 35, 35)
                .addComponent(labelStep2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(labelStep3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(labelStep4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelStepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelStepLayout.createSequentialGroup()
                    .addComponent(labelImg, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );

        panelData.setMinimumSize(new java.awt.Dimension(320, 370));
        panelData.setPreferredSize(new java.awt.Dimension(320, 370));

        separatorTop2.setBackground(new java.awt.Color(240, 240, 240));
        separatorTop2.setOpaque(true);
        separatorTop2.setPreferredSize(new java.awt.Dimension(300, 6));

        labelStepName.setFont(labelStepName.getFont());
        labelStepName.setText("Step 2 - Mapping Data");

        labelSchemaSource.setFont(labelSchemaSource.getFont());
        labelSchemaSource.setText("Source Schema Mapping:");

        fieldSchemaSource.setFont(fieldSchemaSource.getFont());
        fieldSchemaSource.setMinimumSize(new java.awt.Dimension(6, 25));
        fieldSchemaSource.setName("fieldSchemaSource"); // NOI18N
        fieldSchemaSource.setPreferredSize(new java.awt.Dimension(6, 25));

        buttonSchemaSource.setFont(buttonSchemaSource.getFont());
        buttonSchemaSource.setText("...");
        buttonSchemaSource.setMaximumSize(new java.awt.Dimension(71, 25));
        buttonSchemaSource.setMinimumSize(new java.awt.Dimension(71, 25));
        buttonSchemaSource.setName("buttonFileSchemaSource"); // NOI18N
        buttonSchemaSource.setPreferredSize(new java.awt.Dimension(71, 25));

        fieldExpected.setFont(fieldExpected.getFont());
        fieldExpected.setMinimumSize(new java.awt.Dimension(6, 25));
        fieldExpected.setName("fieldExpected"); // NOI18N
        fieldExpected.setPreferredSize(new java.awt.Dimension(6, 25));

        labelTarget.setFont(labelTarget.getFont());
        labelTarget.setText("Expected Target Instance:");

        buttonFileExpected.setFont(buttonFileExpected.getFont());
        buttonFileExpected.setText("...");
        buttonFileExpected.setMaximumSize(new java.awt.Dimension(71, 25));
        buttonFileExpected.setMinimumSize(new java.awt.Dimension(71, 25));
        buttonFileExpected.setName("buttonFileExpected"); // NOI18N
        buttonFileExpected.setPreferredSize(new java.awt.Dimension(71, 25));

        fieldSchemaTarget.setFont(fieldSchemaTarget.getFont());
        fieldSchemaTarget.setMinimumSize(new java.awt.Dimension(6, 25));
        fieldSchemaTarget.setName("fieldSchemaTarget"); // NOI18N
        fieldSchemaTarget.setPreferredSize(new java.awt.Dimension(6, 25));

        labelSchemaTarget.setFont(labelSchemaTarget.getFont());
        labelSchemaTarget.setText("Target Schema Mapping:");

        buttonSchemaTarget.setFont(buttonSchemaTarget.getFont());
        buttonSchemaTarget.setText("...");
        buttonSchemaTarget.setMaximumSize(new java.awt.Dimension(71, 25));
        buttonSchemaTarget.setMinimumSize(new java.awt.Dimension(71, 25));
        buttonSchemaTarget.setName("buttonFileSchemaTarget"); // NOI18N
        buttonSchemaTarget.setPreferredSize(new java.awt.Dimension(71, 25));

        checkCopy.setFont(checkCopy.getFont());
        checkCopy.setSelected(true);
        checkCopy.setText("Copy Target Instance");
        checkCopy.setToolTipText("Copy Target Instance File in the Project Folder");
        checkCopy.setName("checkCopy"); // NOI18N
        checkCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkCopyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
        panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDataLayout.createSequentialGroup()
                        .addComponent(fieldExpected, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonFileExpected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelDataLayout.createSequentialGroup()
                        .addComponent(fieldSchemaSource, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonSchemaSource, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(labelSchemaSource)
                    .addComponent(labelStepName)
                    .addComponent(labelTarget)
                    .addGroup(panelDataLayout.createSequentialGroup()
                        .addComponent(fieldSchemaTarget, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonSchemaTarget, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(labelSchemaTarget))
                .addContainerGap())
            .addGroup(panelDataLayout.createSequentialGroup()
                .addComponent(separatorTop2, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addGap(20, 20, 20))
            .addGroup(panelDataLayout.createSequentialGroup()
                .addComponent(checkCopy, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelDataLayout.setVerticalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelStepName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separatorTop2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelSchemaSource)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSchemaSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldSchemaSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(labelSchemaTarget)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSchemaTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldSchemaTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(labelTarget)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonFileExpected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldExpected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(checkCopy)
                .addGap(45, 45, 45))
        );

        javax.swing.GroupLayout panelTopLayout = new javax.swing.GroupLayout(panelTop);
        panelTop.setLayout(panelTopLayout);
        panelTopLayout.setHorizontalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addComponent(panelStep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelData, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
        );
        panelTopLayout.setVerticalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelStep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelBottom.setAlignmentX(0.0F);
        panelBottom.setAlignmentY(0.0F);
        panelBottom.setPreferredSize(new java.awt.Dimension(600, 50));

        separatorBottom.setBackground(new java.awt.Color(240, 240, 240));
        separatorBottom.setAlignmentY(0.0F);
        separatorBottom.setPreferredSize(new java.awt.Dimension(600, 6));

        buttonCancel.setFont(buttonCancel.getFont());
        buttonCancel.setText("Cancel");

        buttonNext.setFont(buttonNext.getFont());
        buttonNext.setText("Next >");

        buttonBack.setFont(buttonBack.getFont());
        buttonBack.setText("< Back");

        buttonFinish.setFont(buttonFinish.getFont());
        buttonFinish.setText("Finish");

        javax.swing.GroupLayout panelBottomLayout = new javax.swing.GroupLayout(panelBottom);
        panelBottom.setLayout(panelBottomLayout);
        panelBottomLayout.setHorizontalGroup(
            panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(separatorBottom, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
            .addGroup(panelBottomLayout.createSequentialGroup()
                .addGap(280, 280, 280)
                .addComponent(buttonBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(buttonNext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(buttonFinish, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(buttonCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBottomLayout.setVerticalGroup(
            panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBottomLayout.createSequentialGroup()
                .addComponent(separatorBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonBack)
                    .addGroup(panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buttonNext)
                        .addComponent(buttonFinish))
                    .addComponent(buttonCancel))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTop, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
            .addComponent(panelBottom, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void checkCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkCopyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkCopyActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonBack;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonFileExpected;
    private javax.swing.JButton buttonFinish;
    private javax.swing.JButton buttonNext;
    private javax.swing.JButton buttonSchemaSource;
    private javax.swing.JButton buttonSchemaTarget;
    private javax.swing.JCheckBox checkCopy;
    private javax.swing.JTextField fieldExpected;
    private javax.swing.JTextField fieldSchemaSource;
    private javax.swing.JTextField fieldSchemaTarget;
    private javax.swing.JLabel labelImg;
    private javax.swing.JLabel labelSchemaSource;
    private javax.swing.JLabel labelSchemaTarget;
    private javax.swing.JLabel labelStep1;
    private javax.swing.JLabel labelStep2;
    private javax.swing.JLabel labelStep3;
    private javax.swing.JLabel labelStep4;
    private javax.swing.JLabel labelStepName;
    private javax.swing.JLabel labelSteps;
    private javax.swing.JLabel labelTarget;
    private javax.swing.JPanel panelBottom;
    private javax.swing.JPanel panelData;
    private javax.swing.JPanel panelStep;
    private javax.swing.JPanel panelTop;
    private javax.swing.JSeparator separatorBottom;
    private javax.swing.JSeparator separatorTop1;
    private javax.swing.JSeparator separatorTop2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void inizializza() {
        initComponents();
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        //this.setSize(600, 455);
        this.setResizable(false);
        this.setModal(true);
        this.setTitle("New Project");
        this.createForm();
        this.postInit();
        this.pack();
    }

    private void createForm() {
        IForm formStep2 = new Form(this);
        // instances expected
        //IOsservatore obsExpected = new OsservatoreTextField(Constant.FIELD_FILE_EXPECTED, Constant.SCENARIO_NEW, Scenario.class, "expectedInstance");
        IOsservatore obsExpected = new OsservatoreTextField(Constant.FIELD_FILE_EXPECTED, Constant.SCENARIO_NEW, ScenarioPM.class, "expectedInstance");
        ICollegatore collExpected = new CollegatoreTextField(formStep2, obsExpected);
        IConvalidatoreValore convExpected = createFieldValidator(Constant.FIELD_FILE_EXPECTED, "Expected Target Instance", this);
        collExpected.addConvalidatore(convExpected);
        // source schema
        //IOsservatore obsSchemaSource = new OsservatoreTextField(Constant.FIELD_SCHEMA_SOURCE, Constant.SCENARIO_NEW, Scenario.class, "schemaSource");
        IOsservatore obsSchemaSource = new OsservatoreTextField(Constant.FIELD_SCHEMA_SOURCE, Constant.SCENARIO_NEW, ScenarioPM.class, "schemaSource");
        ICollegatore collSchemaSource = new CollegatoreTextField(formStep2, obsSchemaSource);
        IConvalidatoreValore convSchemaSource = createFieldValidator(Constant.FIELD_SCHEMA_SOURCE, "Schema Source", this);
        collSchemaSource.addConvalidatore(convSchemaSource);

        // target schema
        //IOsservatore obsSchemaTarget = new OsservatoreTextField(Constant.FIELD_SCHEMA_TARGET, Constant.SCENARIO_NEW, Scenario.class, "schemaTarget");
        IOsservatore obsSchemaTarget = new OsservatoreTextField(Constant.FIELD_SCHEMA_TARGET, Constant.SCENARIO_NEW, ScenarioPM.class, "schemaTarget");
        ICollegatore collSchemaTarget = new CollegatoreTextField(formStep2, obsSchemaTarget);
        IConvalidatoreValore convSchemaTarget = createFieldValidator(Constant.FIELD_SCHEMA_TARGET, "Schema Target", this);
        collSchemaTarget.addConvalidatore(convSchemaTarget);

        formStep2.setBottoneIndietro(this.buttonBack, ActionBackStep2.class.getName());
        formStep2.setBottoneRollback(this.buttonCancel);
        formStep2.setBottoneAvanti(this.buttonNext, ActionNextStep2.class.getName());
    }

    private void postInit() {
        this.buttonFileExpected.setAction(this.controllo.getAzioneSwing(ActionChooseFile.class.getName()));
        this.buttonSchemaSource.setAction(this.controllo.getAzioneSwing(ActionChooseFile.class.getName()));
        this.buttonSchemaTarget.setAction(this.controllo.getAzioneSwing(ActionChooseFile.class.getName()));
        this.labelImg.setIcon(Utils.createImage(Constant.WIZARD_IMG));
        this.buttonFinish.setEnabled(false);
        this.fieldExpected.setToolTipText(Constant.TOOLTIP_INSTANCE_EXPECTED);
        this.fieldSchemaSource.setToolTipText(Constant.TOOLTIP_SCHEMA_SOURCE);
        this.fieldSchemaTarget.setToolTipText(Constant.TOOLTIP_SCHEMA_TARGET);

    }

    private IConvalidatoreValore createFieldValidator(final String field, final String message, final ProjectWizardStep2 panel) {
        return new ConvalidatoreValoreAstratto(field) {

            @Override
            public List convalida(Object o) {
                String name = (String) o;
                List<String> errors = new ArrayList<String>();
                if (name.equals("")) {
                    errors.add("The data value " + message + " can not be empty");
                } else if (field.equals(Constant.FIELD_SCHEMA_TARGET) && name.equals(panel.fieldSchemaSource.getText())) {
                    errors.add(Constant.MESSAGE_NOT_VALIDE_SCHEMA);
                } else if (field.equals(Constant.FIELD_SCHEMA_TARGET) && !name.endsWith(".xsd")
                        || field.equals(Constant.FIELD_SCHEMA_SOURCE) && !name.endsWith(".xsd")) {
                    errors.add("The File for data value " + message + " is not valid (.xsd required)");
                } else if (field.equals(Constant.FIELD_FILE_EXPECTED) && !name.endsWith(".xml")) {
                    errors.add("The File for data value " + message + " is not valid (.xml required)");
                } else {
                    if (!new File(name).isFile()) {
                        errors.add("The data value " + message + " is not an existing file");
                    }
                }
                return errors;
            }

            @Override
            public Object converti(Object o) {
                return o;
            }
        };
    }

    @Override
    public void visualizza() {
        FinestraDiDialogoPing dialog1 = (FinestraDiDialogoPing) this.vista.getSottoVista(ProjectWizardStep1.class.getName());
        this.setLocation(dialog1.getLocation());
        super.visualizza();
    }

    public void visualizzaBack() {
        FinestraDiDialogoPing dialog3 = (FinestraDiDialogoPing) this.vista.getSottoVista(ProjectWizardStep3.class.getName());
        this.setLocation(dialog3.getLocation());
        super.visualizza();
    }
}

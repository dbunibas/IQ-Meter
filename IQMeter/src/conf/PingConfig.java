/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package conf;
import it.unibas.iqmeter.view.DialogAddTool;
import it.unibas.iqmeter.view.DialogAnnotationScript;
import it.unibas.iqmeter.view.DialogExplainQuality;
import it.unibas.iqmeter.view.effortgraph.DialogEffortGraph;
import it.unibas.iqmeter.view.PanelChart;
import it.unibas.iqmeter.view.PanelChartClick;
import it.unibas.iqmeter.view.PanelChartClickTemporal;
import it.unibas.iqmeter.view.PanelChartTemporal;
import it.unibas.iqmeter.view.wizard.ProjectWizardStep1;
import it.unibas.iqmeter.view.wizard.ProjectWizardStep2;
import it.unibas.iqmeter.view.View;
import it.unibas.iqmeter.view.wizard.ProjectWizardStep3;
import it.unibas.iqmeter.view.wizard.ProjectWizardStep4;
import it.unibas.ping.configurazione.Configurazione;

/*
 *
 * @author antonio
 *
 *
 */
public class PingConfig extends Configurazione{
    
    public PingConfig(){
        super();
        this.setNomeApplicazione("IQ Meter");
        this.setAutore("Antonio Genovese");
        this.setVistaPrincipale(View.class.getName());
        this.addSottoVista(ProjectWizardStep1.class.getName());
        this.addSottoVista(ProjectWizardStep3.class.getName());
        this.addSottoVista(ProjectWizardStep2.class.getName());
        this.addSottoVista(ProjectWizardStep4.class.getName());
        this.addSottoVista(PanelChart.class.getName());
        this.addSottoVista(PanelChartClick.class.getName());
        this.addSottoVista(PanelChartTemporal.class.getName());
        this.addSottoVista(PanelChartClickTemporal.class.getName());
        this.addSottoVista(DialogEffortGraph.class.getName());
        this.addSottoVista(DialogExplainQuality.class.getName());
        this.addSottoVista(DialogAddTool.class.getName());
        this.addSottoVista(DialogAnnotationScript.class.getName());
    }
}

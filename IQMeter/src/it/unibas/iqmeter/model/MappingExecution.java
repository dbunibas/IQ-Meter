/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author antonio
 */
public class MappingExecution implements Comparable<MappingExecution> {

    private Date mappingTime;
    private String generateIstancePath;
    private String directory;
    private String mappingFilePath;
    private int numberLabel;
    private SolutionQuality quality;
    private EffortGraph effortGraph;
    private EffortRecording effortRecording;
    private Log logger = LogFactory.getLog(this.getClass());

    public EffortGraph getEffortGraph() {
        return effortGraph;
    }

    public void setEffortGraph(EffortGraph effortGraph) {
        this.effortGraph = effortGraph;
    }

    public EffortRecording getEffortRecording() {
        return effortRecording;
    }

    public void setEffortRecording(EffortRecording effortRecording) {
        this.effortRecording = effortRecording;
    }

    public Date getMappingTime() {
        return mappingTime;
    }

    public SolutionQuality getQuality() {
        return quality;
    }

    public void setQuality(SolutionQuality quality) {
        this.quality = quality;
    }

    public void setMappingTime(Date mappingTime) {
        this.mappingTime = mappingTime;
    }

    public String getTimeLabel() {
        DateFormat format = new SimpleDateFormat("MMM-dd 'at' HH.mm.ss ");
        return format.format(this.mappingTime);
    }

    public String getGenerateIstancePath() {
        return generateIstancePath;
    }

//    public String getLabelGenerateIstancePath() {
//        String out = "..";
//        out += generateIstancePath.substring(generateIstancePath.lastIndexOf("/"));
//        return "Generated File: " + out;
//    }
    public void setGenerateIstancePath(String generateIstancePath) {
        this.generateIstancePath = generateIstancePath;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public int getNumberLabel() {
        return numberLabel;
    }

    public void setNumberLabel(int numberLabel) {
        this.numberLabel = numberLabel;
    }

    public String getMappingFilePath() {
        return mappingFilePath;
    }

    public void setMappingFilePath(String mappingFilePath) {
        this.mappingFilePath = mappingFilePath;
    }

    @Override
    public String toString() {
        String stringQuality = ((float) (this.quality.getFmeasure() * 100)) + "";
        String effort = "";
        int recorded = 0;
        if (this.effortGraph != null) {
            effort += this.effortGraph.getBitEffort();
        }
        if (this.effortRecording != null) {
            recorded = this.effortRecording.getTotalInteraction();
        }
        return "Execution #" + this.numberLabel + "(Effort " + effort + " - Recorded "+ recorded +" - Quality " + stringQuality.replaceAll(".0$", "") + "%)";
        //return "Execution # " + this.numberLabel;
    }

    public int compareTo(MappingExecution o) {
        if (o.getQuality().getFmeasure() > this.quality.getFmeasure()) {
            logger.info(this.quality.getFmeasure() + " Before " + o.getQuality().getFmeasure());
            return -1;
        }
        return 1;
    }
    
    public static Comparator<MappingExecution> MappingDateComparator = new Comparator<MappingExecution>() {
        public int compare(MappingExecution exec1, MappingExecution exec2) {
            Date date1 = exec1.getMappingTime();
            Date date2 = exec2.getMappingTime();
            return date1.compareTo(date2);
        }
    };
    
    public static Comparator<MappingExecution> MappingEffortComparator = new Comparator<MappingExecution>() {

        public int compare(MappingExecution o1, MappingExecution o2) {
            int bitEffort1 = o1.getEffortGraph().getBitEffort();
            int bitEffort2 = o2.getEffortGraph().getBitEffort();
            return Integer.compare(bitEffort1, bitEffort2);
        }
    };
    
    public static Comparator<MappingExecution> MappingRecordedComparator = new Comparator<MappingExecution>() {

        public int compare(MappingExecution o1, MappingExecution o2) {
            int operation1 = o1.getEffortRecording().getTotalInteraction();
            int operation2 = o2.getEffortRecording().getTotalInteraction();
            return Integer.compare(operation1, operation2);
        }
    };
}

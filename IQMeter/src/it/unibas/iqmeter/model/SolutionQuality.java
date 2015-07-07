/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.model;

/**
 *
 * @author antonio
 */
public class SolutionQuality {

    private int tuplesExpected;
    private int tuplesTranslated;
    private int nodesTranslated;
    private int nodesExpected;
    private int analyzedFeatures;   
    private double precision;
    private double fmeasure;
    private double recall;


    /**
     * @return the tuplesExpected
     */
    public int getTuplesExpected() {
        return tuplesExpected;
    }

    /**
     * @param tuplesExpected the tuplesExpected to set
     */
    public void setTuplesExpected(int tuplesExpected) {
        this.tuplesExpected = tuplesExpected;
    }

    /**
     * @return the tuplesTranslated
     */
    public int getTuplesTranslated() {
        return tuplesTranslated;
    }

    /**
     * @param tuplesTranslated the tuplesTranslated to set
     */
    public void setTuplesTranslated(int tuplesTranslated) {
        this.tuplesTranslated = tuplesTranslated;
    }

    /**
     * @return the nodesTranslated
     */
    public int getNodesTranslated() {
        return nodesTranslated;
    }

    /**
     * @param nodesTranslated the nodesTranslated to set
     */
    public void setNodesTranslated(int nodesTranslated) {
        this.nodesTranslated = nodesTranslated;
    }

    /**
     * @return the nodesExpected
     */
    public int getNodesExpected() {
        return nodesExpected;
    }

    /**
     * @param nodesExpected the nodesExpected to set
     */
    public void setNodesExpected(int nodesExpected) {
        this.nodesExpected = nodesExpected;
    }

    /**
     * @return the analyzedFeatures
     */
    public int getAnalyzedFeatures() {
        return analyzedFeatures;
    }


    public void setAnalyzedFeatures(int analyzedFeatures) {
        this.analyzedFeatures = analyzedFeatures;
    }

    /**
     * @return the precision
     */
    public double getPrecision() {
        return precision;
    }


    public void setPrecision(double precision) {
        this.precision = precision;
    }

    /**
     * @return the fmeasure
     */
    public double getFmeasure() {
        return fmeasure;
    }


    public void setFmeasure(double fmeasure) {
        this.fmeasure = fmeasure;
    }

    /**
     * @return the recall
     */
    public double getRecall() {
        return recall;
    }

    /**
     * @param recall the recall to set
     */
    public void setRecall(double recall) {
        this.recall = recall;
    }
    
    
}

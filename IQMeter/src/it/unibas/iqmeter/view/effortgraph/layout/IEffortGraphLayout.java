/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view.effortgraph.layout;

import com.mxgraph.view.mxGraph;
import it.unibas.iqmeter.model.EffortGraph;

/**
 *
 * @author Antonio Genovese
 */
public interface IEffortGraphLayout {
    public mxGraph createmxGraph(EffortGraph graphEffort);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter.view;

import it.unibas.iqmeter.model.MappingExecution;
import it.unibas.iqmeter.model.Scenario;
import it.unibas.ping.framework.Applicazione;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Antonio Genovese
 */
public class ModelTableDetails extends AbstractTableModel {

    private String toolName;
    private MappingExecution execution;
    private Log logger = LogFactory.getLog(this.getClass());

    public ModelTableDetails(String toolName, MappingExecution execution) {
        this.toolName = toolName;
        this.execution = execution;
        
    }

    public int getRowCount() {
        if (execution == null) {
            return 0;
        }
        //return 17;
        return 13;
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (this.execution != null) {
            switch (rowIndex) {
                case 0: {
                    if (columnIndex == 0) {
                        return "System ";
                    } else if (columnIndex == 1) {
                        return this.toolName;
                    }
                }
                break;
                case 1: {
                    if (columnIndex == 0) {
                        return "Date ";
                    } else if (columnIndex == 1) {
                        DateFormat format = new SimpleDateFormat(//"yyyy-MMMM-dd' at 'HH:mm:ss.SSS");
                                "yyyy.MM.dd 'at' HH:mm:ss.SSS");

                        return format.format(this.execution.getMappingTime());
                    }
                }
                break;
                case 2: {
                    if (columnIndex == 0) {
                        return "Quality ";
                    } else if (columnIndex == 1) {
                        float mes = (float) (this.execution.getQuality().getFmeasure() * 100);
                        String quality = mes + "%";
                        return quality.replaceAll(".0%$", "%");
                    }

                }
                break;
                case 3: {
                    if (columnIndex == 0) {
                        return "Effort ";
                    } else if (columnIndex == 1) {
                        String effort = "";
                        if (this.execution.getEffortGraph() != null) {
                            effort += this.execution.getEffortGraph().getBitEffort() + " bit";
                        }
                        return effort;
                    }
                }
                break;
                case 4: {
                    if (columnIndex == 0) {
                        return "Effort Graph Nodes ";
                    } else if (columnIndex == 1) {
                        String nodes = "";
                        if (this.execution.getEffortGraph() != null) {
                            nodes += this.execution.getEffortGraph().getNumVertex();
                        }
                        return nodes;
                    }
                }
                break;
                case 5: {
                    if (columnIndex == 0) {
                        return "Effort Graph Edges ";
                    } else if (columnIndex == 1) {
                        String edges = "";
                        if (this.execution.getEffortGraph() != null) {
                            edges += this.execution.getEffortGraph().getNumEdges();
                        }
                        return edges;
                    }
                }
                break;
                case 6: {
                    if (columnIndex == 0) {
                        return "Effort Graph Annotations ";
                    } else if (columnIndex == 1) {
                        String annotation = "";
                        if (this.execution.getEffortGraph() != null) {
                            annotation += this.execution.getEffortGraph().getNumAnnotation();
                        }
                        return annotation;
                    }
                }
                break;
                case 7: {
                    if (columnIndex == 0) {
                        return "Effort Graph Functions ";
                    } else if (columnIndex == 1) {
                        String functions = "";
                        if (this.execution.getEffortGraph() != null) {
                            functions += this.execution.getEffortGraph().getNumFunction();
                        }
                        return functions;
                    }
                }
                break;

                case 8: {
                    if (columnIndex == 0) {
                        return "Effort Graph Boxes ";
                    } else if (columnIndex == 1) {
                        String effort = "";
                        if (this.execution.getEffortGraph() != null) {
                            effort += this.execution.getEffortGraph().getNumBox();
                        }
                        return effort;
                    }
                }
                break;
                    
               case 9: {
                    if (columnIndex == 0) {
                        return "Effort Recorded Total: ";
                    } else if (columnIndex == 1) {
                        String effort = "";
                        if (this.execution.getEffortRecording() != null) {
                            effort += this.execution.getEffortRecording().getTotalInteraction();
                        }
                        return effort;
                    }
                }
                break;
                   
                case 10: {
                    if (columnIndex == 0) {
                        return "Effort Recorded Mouse Operations: ";
                    } else if (columnIndex == 1) {
                        String effort = "";
                        if (this.execution.getEffortRecording() != null) {
                            effort += this.execution.getEffortRecording().getClickRecorded();
                        }
                        return effort;
                    }
                }
                break;
                    
                case 11: {
                    if (columnIndex == 0) {
                        return "Effort Recorded Keyboard Operations: ";
                    } else if (columnIndex == 1) {
                        String effort = "";
                        if (this.execution.getEffortRecording() != null) {
                            effort += this.execution.getEffortRecording().getKeyboardRecorded();
                        }
                        return effort;
                    }
                }
                break; 

//                case 9: {
//                    if (columnIndex == 0) {
//                        return "Tool cost of Function ";
//                    } else if (columnIndex == 1) {
//                        return this.execution.getEffortGraph().getToolFunctionCost();
//                    }
//                }
//                break;
//
//                case 10: {
//                    if (columnIndex == 0) {
//                        return "Tool cost of Box ";
//                    } else if (columnIndex == 1) {
//                        return this.execution.getEffortGraph().getToolBoxCost();
//                    }
//                }
//                break;
//
//
//                case 11: {
//                    if (columnIndex == 0) {
//                        return "Bits for Node ";
//                    } else if (columnIndex == 1) {
//                        String bit = "";
//                        if (this.execution.getEffortGraph() != null) {
//                            bit += this.execution.getEffortGraph().getBitNodes();
//                        }
//                        return bit;
//                    }
//                }
//                break;
//                case 12: {
//                    if (columnIndex == 0) {
//                        return "Bits for Function ";
//                    } else if (columnIndex == 1) {
//                        String bit = "";
//                        if (this.execution.getEffortGraph() != null) {
//                            bit += this.execution.getEffortGraph().getBitFunction();
//                        }
//                        return bit;
//                    }
//                }
//                break;
//                case 13: {
//                    if (columnIndex == 0) {
//                        return "Bits for Annotation ";
//                    } else if (columnIndex == 1) {
//                        String bit = "";
//                        if (this.execution.getEffortGraph() != null) {
//                            bit += this.execution.getEffortGraph().getBitAnnotation();
//                        }
//                        return bit;
//                    }
//                }
//                break;
//                case 14: {
//                    if (columnIndex == 0) {
//                        return "Bits for Box ";
//                    } else if (columnIndex == 1) {
//                        String bit = "";
//                        if (this.execution.getEffortGraph() != null) {
//                            bit += this.execution.getEffortGraph().getBitBox();
//                        }
//                        return bit;
//                    }
//                }
//                break;
//                case 15: {
//                    if (columnIndex == 0) {
//                        return "Bits for Edge ";
//                    } else if (columnIndex == 1) {
//                        String bit = "";
//                        if (this.execution.getEffortGraph() != null) {
//                            bit += this.execution.getEffortGraph().getBitEdges();
//                        }
//                        return bit;
//                    }
//                }
//                break;
                case 12: {
                    if (columnIndex == 0) {
                        return "Output Folder ";
                    } else if (columnIndex == 1) {
                        File file = new File(this.execution.getDirectory());
                        String folder = "../" + file.getName();
                        return folder;
                    }

                }
                break;
            }
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "Scenario";
        } else if (column == 1) {
            return ((Scenario)Applicazione.getInstance().getModello().getBean(Scenario.class.getName())).getName();
        }
        return null;
    }
}

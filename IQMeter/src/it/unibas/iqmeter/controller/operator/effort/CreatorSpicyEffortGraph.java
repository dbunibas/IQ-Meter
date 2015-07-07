package it.unibas.iqmeter.controller.operator.effort;

import it.unibas.iqmeter.model.EffortGraphNode;
import it.unibas.iqmeter.model.MappingTool;
import it.unibas.iqmeter.persistence.DAOMappingExecution;
import it.unibas.spicy.model.correspondence.ValueCorrespondence;
import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.SelectionCondition;
import it.unibas.spicy.model.datasource.nodes.LeafNode;
import it.unibas.spicy.model.datasource.operators.FindNode;
import it.unibas.spicy.model.expressions.Expression;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicy.model.paths.PathExpression;
import it.unibas.spicy.model.paths.operators.GeneratePathExpression;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.DAOMappingTask;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.nfunk.jep.JEP;

@SuppressWarnings("unchecked")
public class CreatorSpicyEffortGraph implements ICreatorEffortGraph {

    private DAOMappingTask daoMappingTask = new DAOMappingTask();
    private GeneratePathExpression pathGenerator = new GeneratePathExpression();
    private FindNode nodeFinder = new FindNode();
    private Log logger = LogFactory.getLog(this.getClass());
    private UndirectedGraph<EffortGraphNode, DefaultEdge> effortGraph;

    @Override
    public UndirectedGraph<EffortGraphNode, DefaultEdge> getGraph(String scenarioPath, MappingTool tool) {
        effortGraph = new SimpleGraph<EffortGraphNode, DefaultEdge>(DefaultEdge.class);
        try {

            String scriptAnnotation = DAOMappingExecution.loadFileScript(scenarioPath, tool).replaceAll("\n", "");
            if (!scriptAnnotation.equals("")) {
                createAnnotationScriptVertex(scriptAnnotation);
            }
            MappingTask mappingTask = daoMappingTask.loadMappingTask(scenarioPath);
            IDataSourceProxy sourceSchema = mappingTask.getSourceProxy();
            addSchemaNodes(sourceSchema.getIntermediateSchema(), EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA);
            IDataSourceProxy targetSchema = mappingTask.getTargetProxy();
            addSchemaNodes(targetSchema.getIntermediateSchema(), EffortGraphNode.TYPE_NODE_TARGET_SCHEMA);
            addCorrespondences(mappingTask);
            addSelectionConditions(mappingTask);
        } catch (DAOException ex) {
            logger.error("Unable to load scenario: " + ex.getLocalizedMessage());
        }
        return effortGraph;
    }

    //add vertices for each element in the schema
    private void addSchemaNodes(INode schemaNode, String type) {
        if (!(schemaNode instanceof LeafNode)) {
            PathExpression path = pathGenerator.generatePathFromRoot(schemaNode);
            String nodeId = path.toString();
            logger.debug("## Adding vertex for schema node " + nodeId);
            EffortGraphNode effortNode = new EffortGraphNode(type, schemaNode.getLabel(), nodeId);
            effortGraph.addVertex(effortNode);
        }
        if (schemaNode.getChildren() != null) {
            for (INode child : schemaNode.getChildren()) {
                addSchemaNodes(child, type);
            }
        }
    }

    // searching correspondences and add arcs
    // Note: the correspondences can be between the schema nodes or between the functions
    private void addCorrespondences(MappingTask mappingTask) {
        if (mappingTask.getValueCorrespondences().size() > 0) {
            for (int i = 0; i < mappingTask.getValueCorrespondences().size(); i++) {
                ValueCorrespondence valueCorrespondence = mappingTask.getValueCorrespondences().get(i);
                if (valueCorrespondence.getSourceValue() == null) {
                    if (verifyTransformationFunction(valueCorrespondence)) {
                        createFunctionNode(valueCorrespondence, mappingTask);
                    } else {
                        addEdgesBetweenSchemaNodes(valueCorrespondence, mappingTask);   //Standard correspondences
                    }
                } else {
                    createSourceValue(valueCorrespondence, mappingTask);
                }
            }
        }
    }

    //add "classic" edges between two nodes (source and target)
    private void addEdgesBetweenSchemaNodes(ValueCorrespondence valueCorrespondence, MappingTask mappingTask) {
        INode iNodeSource = nodeFinder.findNodeInSchema(valueCorrespondence.getSourcePaths().get(0), mappingTask.getSourceProxy());
        INode iNodeTarget = nodeFinder.findNodeInSchema(valueCorrespondence.getTargetPath(), mappingTask.getTargetProxy());
        EffortGraphNode sourceNode = findSchemaNode(iNodeSource);
        EffortGraphNode targetNode = findSchemaNode(iNodeTarget);
        logger.debug("Adding edge between schema nodes " + sourceNode + " <--> " + targetNode);
        effortGraph.addEdge(sourceNode, targetNode);

    }

    private void createSourceValue(ValueCorrespondence valueCorrespondence, MappingTask mappingTask) {
        EffortGraphNode functionNode = createFunctionVertex(valueCorrespondence);
        IDataSourceProxy target = mappingTask.getTargetProxy();
        INode targetNode = nodeFinder.findNodeInSchema(valueCorrespondence.getTargetPath(), target);
        EffortGraphNode schemaNode = findSchemaNode(targetNode);
        logger.debug("Adding edge between schema nodes " + schemaNode + " and function " + functionNode);
        if (schemaNode.getType().equals(EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA)) {
            effortGraph.addEdge(schemaNode, functionNode);
        } else {
            effortGraph.addEdge(functionNode, schemaNode);
        }

    }

    private boolean verifyTransformationFunction(ValueCorrespondence valueCorrespondence) {
        if (valueCorrespondence.getSourcePaths().size() > 1) {
            return true;
        }
        String transformationFunctionString = valueCorrespondence.getTransformationFunction().toString();
        logger.trace("TrasformationString" + transformationFunctionString);
        String sourcePathString = valueCorrespondence.getSourcePaths().get(0).toString();
        return (!transformationFunctionString.equals(sourcePathString));
    }

    // create node-type function, the node corresponding to a function of spicy
    private void createFunctionNode(ValueCorrespondence valueCorrespondence, MappingTask mappingTask) {
        EffortGraphNode functionNode = createFunctionVertex(valueCorrespondence);
        IDataSourceProxy source = mappingTask.getSourceProxy();
        for (PathExpression pathExpression : valueCorrespondence.getSourcePaths()) {
            INode sourceNode = nodeFinder.findNodeInSchema(pathExpression, source);
            EffortGraphNode schemaNode = findSchemaNode(sourceNode);
            logger.debug("Adding edge between schema nodes " + schemaNode + " and function " + functionNode);
            if (schemaNode.getType().equals(EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA)) {
                effortGraph.addEdge(schemaNode, functionNode);
            } else {
                effortGraph.addEdge(functionNode, schemaNode);
            }

        }
        IDataSourceProxy target = mappingTask.getTargetProxy();
        INode targetNode = nodeFinder.findNodeInSchema(valueCorrespondence.getTargetPath(), target);
        EffortGraphNode schemaNode = findSchemaNode(targetNode);
        logger.debug("Adding edge between schema nodes " + schemaNode + " and function " + functionNode);
        if (schemaNode.getType().equals(EffortGraphNode.TYPE_NODE_SOURCE_SCHEMA)) {
            effortGraph.addEdge(schemaNode, functionNode);
        } else {
            effortGraph.addEdge(functionNode, schemaNode);
        }

    }

    private EffortGraphNode createFunctionVertex(ValueCorrespondence valueCorrespondence) {
        Expression function = valueCorrespondence.getTransformationFunction();
        String functionLabel = function.toString();
        logger.trace("functionLabel" + functionLabel);
        String functionId = valueCorrespondence.toString();
        EffortGraphNode functionNode = new EffortGraphNode(EffortGraphNode.TYPE_NODE_FUNCTION, functionLabel, functionId);
        logger.debug("## Adding vertex for function " + functionNode);
        effortGraph.addVertex(functionNode);
//        addSubfunction(function, functionNode);
        return functionNode;
    }

    //add node function for the nested function NOTE: change ignored
    private void addSubfunction(Expression function, EffortGraphNode functionNode) {
        JEP jepExp = function.getJepExpression();
        String functionString = jepExp.toStringWithSlashes();
        logger.trace("Complete Function String Value from jepEXP:" + functionString);
        String[] functions = functionString.split("fn:");
        for (int i = 1; i < functions.length; i++) {
            String string = functions[i];
            if (!string.isEmpty()) {
                logger.trace("SubFunction String Value:" + string);
                adjustLabelFunction(functionNode, string, functions, i);
            }

        }
    }

    //create subfunction  NOTE: change ignored
    private void adjustLabelFunction(EffortGraphNode functionNode, String label, String[] arrayFunctions, int i) {
        if (i == 1 && arrayFunctions.length > 2) {
            label = label.replaceAll("/", ".");
            label = label.replaceAll("\\(\\),", ",");
            label = label.replaceAll(".text()|\\$|\\($", "");
            //functionNode.setLabel(label.replaceAll("\\.+\\w\\(+\\)+", ""));
            functionNode.setLabel(label);
//            createAnnotationFromFunction(functionNode);
        } else if (i > 1) {
            EffortGraphNode subFunctionNode = new EffortGraphNode(EffortGraphNode.TYPE_NODE_FUNCTION, label, label);
            label = label.replaceAll("/", ".");
            label = label.replaceAll("\\(\\),", ",");
            label = label.replaceAll(".text()|\\$|\\($", "");
            logger.debug("## Adding vertex for function " + subFunctionNode);
            subFunctionNode.setLabel(label);
            effortGraph.addVertex(subFunctionNode);
            // createAnnotationFromFunction(subFunctionNode);
            if (i > 2) {
                EffortGraphNode node = this.findFunctionNode(arrayFunctions[i - 1]);
                if (node != null) {
                    effortGraph.addEdge(node, subFunctionNode);
                }
            } else {
                effortGraph.addEdge(functionNode, subFunctionNode);
            }
//        } else {
//            createAnnotationFromFunction(functionNode);
        }
    }

    //create Annotation from function (TODO: I have not divided the annotations in the functions )
    //NOTE: change ignored
    private void createAnnotationFromFunction(EffortGraphNode function) {
        if (function.getLabel().contains("\"")) {
            logger.trace("function label with annotation " + function.getLabel());
            String labelAnnotation = function.getLabel().replaceAll("\"", "##");
            logger.trace("String annotation from function " + labelAnnotation);
            while (labelAnnotation.contains("##")) {
                String subAnnotation = labelAnnotation.substring(labelAnnotation.indexOf("##") + 2);
                logger.trace("subAnnotation: " + subAnnotation);
                String l = subAnnotation.substring(0, subAnnotation.indexOf("##"));
                if (!l.isEmpty()) {
                    EffortGraphNode annotation = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, l, labelAnnotation);
                    labelAnnotation = labelAnnotation.replaceFirst("##" + l + "##", "");
                    function.setLabel(function.getLabel().replace("\"" + l + "\"", ""));
                    effortGraph.addVertex(annotation);
                    effortGraph.addEdge(function, annotation);
                } else {
                    labelAnnotation = labelAnnotation.replaceFirst("####", "");
                }
            }
            function.setLabel(function.getLabel().replaceAll(" ,|\\(+\\).", ""));
        }
    }

//SELECTION CONDITION (sample type!="primary")
    private void addSelectionConditions(MappingTask mappingTask) {
        IDataSourceProxy source = mappingTask.getSourceProxy();
        IDataSourceProxy target = mappingTask.getTargetProxy();
        for (SelectionCondition selectionCondition : source.getSelectionConditions()) {
            addAnnotationForSelectionCondition(selectionCondition, source);
        }
        for (SelectionCondition selectionCondition : target.getSelectionConditions()) {
            addAnnotationForSelectionCondition(selectionCondition, target);
        }
    }

    private void addAnnotationForSelectionCondition(SelectionCondition selectionCondition, IDataSourceProxy source) {
        for (PathExpression pathExpression : selectionCondition.getSetPaths()) {
            INode iNode = nodeFinder.findNodeInSchema(pathExpression, source);
            EffortGraphNode schemaNode = findSchemaNode(iNode);
            //replace path of node from selection condition
            EffortGraphNode annotationNode = createAnnotationVertex(selectionCondition.getCondition().toString().replaceAll("\\w+\\.", ""), selectionCondition.getCondition().toString());
            logger.debug("Adding edge between schema nodes " + schemaNode + " and annotation " + annotationNode);
            effortGraph.addEdge(schemaNode, annotationNode);

        }
    }

    private EffortGraphNode createAnnotationVertex(String annotation, String annotationId) {
        Pattern patternAnnotation = Pattern.compile("^\\(.*\\)$");
        annotation = annotation.replaceAll("\\s", "");

        if (patternAnnotation.matcher(annotation).matches()) {
            annotation = annotation.replaceAll("^\\(|\\)$", "");
        }
        //clean the condition with characters blank|()
        //EffortGraphNode annotationNode = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, annotation.replaceAll("\\(|\\)|\\s|\"", ""), annotation);
        //EffortGraphNode annotationNode = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, annotation.replaceAll("^\\(|\\)$|\\s", ""), annotationId);
        EffortGraphNode annotationNode = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION, annotation, annotationId);
        logger.debug("## Adding vertex for annotation " + annotationNode);
        effortGraph.addVertex(annotationNode);
        return annotationNode;
    }

    //creates a node of type annotation script to load the script file, when we write code manually
    private EffortGraphNode createAnnotationScriptVertex(String annotation) {
        annotation = annotation.replaceAll("\n|\\s+", "");
        logger.debug("Annotation script string: " + annotation);
        EffortGraphNode annotationNode = new EffortGraphNode(EffortGraphNode.TYPE_NODE_ANNOTATION_SCRIPT, annotation, annotation);
        logger.debug("## Adding vertex for annotation script " + annotationNode);
        effortGraph.addVertex(annotationNode);
        return annotationNode;
    }

    // Utility methods to search the nodes of the graph
    private EffortGraphNode findSchemaNode(INode iNode) {
        PathExpression path = pathGenerator.generatePathFromRoot(iNode);
        for (EffortGraphNode effortGraphNode : effortGraph.vertexSet()) {
            if (effortGraphNode.getNodeId().equals(path.toString())) {
                return effortGraphNode;
            }
        }
        throw new IllegalArgumentException("Unable to find node " + path.toString() + " in graph " + effortGraph.vertexSet());
    }

    //find for ValueCorrespondence
    private EffortGraphNode findFunctionNode(ValueCorrespondence valueCorrespondence) {
        for (EffortGraphNode effortGraphNode : effortGraph.vertexSet()) {
            if (effortGraphNode.getNodeId().equals(valueCorrespondence.toString())) {
                return effortGraphNode;
            }
        }
        throw new IllegalArgumentException("Unable to find function node " + valueCorrespondence.toString() + " in graph " + effortGraph.vertexSet());
    }

    //find for id String
    private EffortGraphNode findFunctionNode(String id) {
        for (EffortGraphNode effortGraphNode : effortGraph.vertexSet()) {
            if (effortGraphNode.getNodeId().equals(id)) {
                return effortGraphNode;
            }
        }
        return null;
    }

    public int getNumSchemaSource() {
        return 0;
    }

    public int getNumSchemaTarget() {
        return 0;
    }

    public void setNumSchemaSource(int i) {
    }

    public void setNumSchemaTarget(int i) {
    }
}

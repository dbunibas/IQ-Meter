/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.iqmeter;

/**
 *
 * @author antonio
 */
public class Constant {

    private Constant() {
    }
    //Bean
    public static final String SCENARIO_NEW = "ScenarioNew";
    public static final String TOOL_NEW = "MappingToolNew";
    public static final String TOOLS_LIST_NEW = "MappingToolListNew";
    public static final String DIRECTORY_LOAD_PROJECT = "Directory";
    public static final String EXECUTION_SELECTED = "ExecutionSelected";
    public static final String EXECUTION_ANALYZED = "ExecutionAnalyzed";
    public static final String TOOLS_LIST = "ListTools";
    public static final String EXECUTIONS_LIST = "executionsList";
    public static final String COMBO_TOOL_SELECTED = "ComboToolSelected";
    public static final String COMBO_TOOL_SELECTED_INDEX = "ComboToolSelectedIndex";
    public static final String TOOL_ANALYZED = "ToolAnalyzed";
    public static final String SCRIPT_FILE_NAME = "AnnotationScript.txt";
    public static final String ANNOTATION_SCRIPT_TEXT = "Annotation_Script";
    public static final String CHART_SERIES = "chartSeries";
    public static final String RECORDING_STATE = "recordingState";
    public static final String CLICK_NUMBER = "clickNumber_";
    public static final String KEYBOARD_NUMBER = "keyboardNumber_";
    //Component
    public static final String PROGRESS_BAR = "progressBar";
    public static final String LABEL_ICON_STATUS = "labelIconStatus";
    public static final String PANEL_SOUTH = "panelSouth";
    public static final String PANEL_CONTAINER_CHART = "panelContainerChart";
    public static final String TREE_EXECUTIONS = "treeMappingExecutions";
    public static final String TAB_PANE = "tabbedPaneTools";
    public static final String TAB_SELECTED = "SelectedPanel";
    public static final String AREA_CHART = "chart_area";
    public static final String AREA_SCRIPT = "textAreaScript";
    public static final String LINE_CHART = "chart_line";
    public static final String QUALITY_TEXT = "qualityText";
    public static final String COMBO_TOOL_STEP = "comboToolsStep";
    //Component Button         
    public static final String RADIO_BUTTON_EXCLUSIONS = "radioButtonExclusions";
    public static final String BUTTON_FOLDER_WIZARD = "buttonFolderWizard";
    public static final String BUTTON_FILE_EXPECTED = "buttonFileExpected";
    public static final String BUTTON_FILE_EXCLUSIONS = "buttonFileExclusions";
    public static final String BUTTON_FILE_MAPPING = "buttonFileMapping";
    public static final String BUTTON_FILE_MAPPING_STEP = "buttonFileMappingStep";
    public static final String BUTTON_FILE_SCRIPT_STEP = "buttonFileScriptStep";
    public static final String BUTTON_FILE_SCRIPT = "buttonFileScript";
    public static final String BUTTON_FILE_TRANSLATED = "buttonFileTranslated";
    public static final String BUTTON_FILE_TRANSLATED_STEP = "buttonFileTranslatedStep";
    public static final String BUTTON_FILE_SCHEMA_SOURCE = "buttonFileSchemaSource";
    public static final String BUTTON_FILE_SCHEMA_TARGET = "buttonFileSchemaTarget";
    public static final String BUTTON_COLLAPSE_CHART = "buttonCollapse";
    public static final String CHECK_COPY = "checkCopy";
    public static final String BUTTON_SAVE_SCRIPT = "buttonScriptSave";
    public static final String BUTTON_DELETE_SCRIPT = "buttonScriptDelete";
    //Component Field
    public static final String FIELD_FILE_EXPECTED = "fieldExpected";
    public static final String FIELD_FILE_EXCLUSIONS = "fieldExclusions";
    public static final String FIELD_FILE_MAPPING = "fieldMapping";
    public static final String FIELD_FILE_MAPPING_STEP = "fieldMappingStep";
    public static final String FIELD_FILE_SCRIPT_STEP = "fieldScriptStep";
    public static final String FIELD_FILE_SCRIPT = "fieldScript";
    public static final String FIELD_FILE_TRANSLATED = "fieldTranslated";
    public static final String FIELD_FILE_TRANSLATED_STEP = "fieldTranslatedStep";
    public static final String FIELD_SCHEMA_SOURCE = "fieldSchemaSource";
    public static final String FIELD_SCHEMA_TARGET = "fieldSchemaTarget";
    public static final String FIELD_PROJECT_NAME = "fieldName";
    public static final String FIELD_OUTPUT_FOLDER = "fieldOutput";
    public static final String TEXT_AREA_FEATURES = "textAreaFeatures";
    public static final String TEXT_AREA_EXCLUSION = "textAreaExclusionsList";
    //Resources
    public static final String IMAGES = "/resources/images/";
    public static final String FOLDER_CONF = "/resources/config/";
    public static final String WIZARD_IMG = "wizard.jpeg";
    public static final String TOOLS_BACK = "tools_background.jpeg";
    public static final String ICONS = "/resources/icons/";
    public static final String ICON_ERROR = "16x16/status_error.png";
    public static final String ICON_OK = "16x16/status_ok.png";
    public static final String ICON_INFO = "16x16/status_info.png";
    public static final String ICON_WARNING = "16x16/status_warning.png";
    public static final String ICON_DELETE = "16x16/delete_small.png";
    public static final String ICON_ANNOTATION_SCRIPT_SMALL = "16x16/script_small.png";
    public static final String ICON_ANNOTATION_SCRIPT = "script.png";
    public static final String ICON_EXPLAIN_QUALITY_SMALL = "16x16/explain_quality.png";
    public static final String ICON_DIALOG_ERROR = "32x32/dialog-error.png";
    public static final String ICON_LINE_CHART_OPEN = "32x32/line_chart_open.png";
    public static final String ICON_LINE_CHART_CLOSE = "32x32/line_chart_close.png";
    public static final String ICON_ADD_TOOL = "add-tool.png";
    public static final String ICON_ADD_TOOL_SMALL = "16x16/add-tool_small.png";
    public static final String ICON_RELOAD = "reload.png";
    public static final String ICON_CHART_IMAGE = "16x16/chart_image.png";
    public static final String ICON_RELOAD_SMALL = "16x16/reload_small.png";
    public static final String ICON_OPEN_FOLDER_SMALL = "16x16/open-file_small.png";
    public static final String FILE_PROPERTIES_TOOLS = "resources/config/tools-conf.properties";
    public static final String FILE_PROPERTIES_CHART = "resources/config/template-chart.properties";
    public static final String ICON_GRAPH = "16x16/graph_small.png";
    //State
    public static final int STATE_SCENARIO_ACTIVE = 1;
    public static final int STATE_NO_SCENARIO = 0;
    public static final int STATE_NO_TOOLS_AVAILABLE = 2;
    //Message 
    public static final String MESSAGE_NEW_SCENARIO = "New Project successfully created";
    public static final String MESSAGE_ERROR_COMPARE = "Error comparing the mapping file";
    public static final String MESSAGE_ERROR_SCENARIO = "Project not created";
    public static final String MESSAGE_LOAD_SCENARIO = "Project successfully loaded";
    public static final String MESSAGE_LOADING_SCENARIO = "Loading Project...";
    public static final String MESSAGE_RELOAD_SCENARIO = "Project successfully reloaded";
    public static final String MESSAGE_PROBLEM_LOAD_SCENARIO = "Project not loaded";
    public static final String MESSAGE_QUALITY = "Running Mapping...";
    public static final String MESSAGE_QUALITY_ERROR = "Mapping run error";
    public static final String MESSAGE_RECORD = "Recording user interaction...";
    public static final String MESSAGE_RECORD_STOP = "Recorded user interaction --> ";
    public static final String MESSAGE_RECORD_MOUSE = "Mouse clicks: ";
    public static final String MESSAGE_RECORD_KEYBOARD = "Keyboard inputs: ";
    public static final String MESSAGE_RECORD_RESET = "Do you want to resume old work?";
    public static final String MESSAGE_QUALITY_SKIP = "Mapping run skipped for files not found";
    public static final String MESSAGE_QUALITY_SUCCESS = "Mapping execution successfully performed";
    public static final String MESSAGE_DIALOG_DELETE_TASK = "Are you sure to want delete the selected Mapping Execution? "
            + "\nIn this case will be deleted all related files.";
    public static final String MESSAGE_DIALOG_DELETE_SCRIPT = "Are you sure to want delete the Annotation Script for selected Mapping Execution? "
            + "\nIn this case " + "the file " + SCRIPT_FILE_NAME + " will be deleted from system.";
    public static final String MESSAGE_DIALOG_EDIT_SCRIPT = "Are you sure to want edit the Annotation Script for selected Mapping Execution? "
             + "\nIn this case " + "the file " + SCRIPT_FILE_NAME + " will be overwritten.";
    public static final String MESSAGE_DELETE_TASK = "Mapping execution successfully deleted";
    
    public static final String MESSAGE_PROBLEM_DELETE_TASK = "Mapping execution delete error, try reload the Project";
    public static final String MESSAGE_EXPORT_ERROR = "File .ods not exported";
    public static final String MESSAGE_EXPORT_SUCCESS = "File .ods successfully exported to the output project folder";
    public static final String MESSAGE_NOT_VALIDE_SCHEMA = "The File Target Schema  can not be the same of Source Schema";
    public static final String MESSAGE_NEW_TOOL = "New Mapping Tool successfully added";
    public static final String MESSAGE_ERROR_ADD_TOOL = "The Mapping Tool has not been added";
    public static final String MESSAGE_ERROR_EFFORT_TOOL = "The effort calculation fo this is not support because for it is not specified a implementation class";
    public static final String MESSAGE_NO_GRAPH = "Effort Graph not available for this mapping Tool";
    public static final String MESSAGE_ERROR_EXPORT_GRAPH = "Error exporting the Effort Graph";
    public static final String MESSAGE_SUCCESS_EXPORT_GRAPH = "The Effort Graph image succesfully exported in the execution folder";
    public static final String MESSAGE_ERROR_LOAD_TOOLS_LIST = "Error loading Tools list from the file tools-conf.properties ";
    public static final String MESSAGE_ERROR_LOAD_QUALITY_FILE = "Error loading the file similarityResult.txt";
    public static final String MESSAGE_ERROR_ACTION_OPEN_DOCUMENT = "Action is not supported on the current platform";
    public static final String MESSAGE_EXCEPTION_CALCULATE_QUALITY = "Error in the calculation of quality, ";
    //Message state
    public static final String MESSAGE_STATE_CHART_EXPORT_SUCCESS = "Chart image successfully exported";
    public static final String MESSAGE_STATE_CHART_EXPORT_ERROR = "Chart image not exported";
    public static final String MESSAGE_STATE_ERROR_OVERWRITE_SCRIPT = "Error saving the Annotation Script file";
    public static final String MESSAGE_STATE_ERROR_DELETE_SCRIPT = "Error deleting the Annotation Script file";
    public static final String MESSAGE_STATE_OPERATION_SCRIPT_SUCCESS = "Annotation Script successfully ";
    
    //Message file properties
    public static final String MESSAGE_FILE_PROPERTIES = "File properties automatically generated by IQ Meter. \n Note: Don't delete it otherwise it will be impossible to load  the ";
    
    
    //Message Tooltip wizard
    public static final String TOOLTIP_NAME = "Specifies the name of the project";
    public static final String TOOLTIP_FOLDER = "Specifies the path to the output folder of the project";
    public static final String TOOLTIP_SCHEMA_SOURCE = "Specifies the source data schema file";
    public static final String TOOLTIP_SCHEMA_TARGET = "Specifies the target data schema file";
    public static final String TOOLTIP_INSTANCE_EXPECTED = "Specifies the expected instance file";
    public static final String TOOLTIP_FEATURES = "Define the features that you want include for the calculation of the quality";
    public static final String TOOLTIP_EXCLUSION = "Define the list of exclusions from the calculation of the quality";
    public static final String TOOLTIP_EXCLUSION_FILE = "Spacifies the exclusion list file";
    public static final String TOOLTIP_TOOL_NAME = "Select the mapping tool to add";
    public static final String TOOLTIP_TOOL_MAPPING_FILE = "Specifies the mapping file of the tool";
    public static final String TOOLTIP_TOOL_INSTANCE_GENERATED = "Specifies the generated instance file from the tool";
    public static final String TOOLTIP_TOOL_SCRIPT = "Specifies the script file of additional annotation for the effort graph";
}

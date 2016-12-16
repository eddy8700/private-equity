
package org.datacontract.schemas._2004._07.cadis;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CADISComponents.ComponentKey.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CADISComponents.ComponentKey">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DataPorter"/>
 *     &lt;enumeration value="DataInspector"/>
 *     &lt;enumeration value="DataMatcherProcess"/>
 *     &lt;enumeration value="DataMatcherInbox"/>
 *     &lt;enumeration value="DataMatcherRealign"/>
 *     &lt;enumeration value="DataConstructor"/>
 *     &lt;enumeration value="DataIllustrator"/>
 *     &lt;enumeration value="DataIllustratorTemplate"/>
 *     &lt;enumeration value="Solution"/>
 *     &lt;enumeration value="CadisForBenchmarks_Obsolete"/>
 *     &lt;enumeration value="DataBlocks"/>
 *     &lt;enumeration value="DataGeneratorFunction"/>
 *     &lt;enumeration value="DataGeneratorInbox"/>
 *     &lt;enumeration value="DataManagerFunction"/>
 *     &lt;enumeration value="DataManagerInbox"/>
 *     &lt;enumeration value="EventWatcher"/>
 *     &lt;enumeration value="ProcessLauncher"/>
 *     &lt;enumeration value="RuleBuilder"/>
 *     &lt;enumeration value="Workflow"/>
 *     &lt;enumeration value="ComponentPromotion"/>
 *     &lt;enumeration value="NotSpecified"/>
 *     &lt;enumeration value="WorkflowPage"/>
 *     &lt;enumeration value="WorkflowElement"/>
 *     &lt;enumeration value="Archive"/>
 *     &lt;enumeration value="Diagram"/>
 *     &lt;enumeration value="ViewBuilder"/>
 *     &lt;enumeration value="DataFlowRule"/>
 *     &lt;enumeration value="DataFlowProcess"/>
 *     &lt;enumeration value="DataFlowSequence"/>
 *     &lt;enumeration value="CubeView"/>
 *     &lt;enumeration value="TableSet"/>
 *     &lt;enumeration value="XmlViewBuilderDeprecated"/>
 *     &lt;enumeration value="DataMatcherIdGenerator"/>
 *     &lt;enumeration value="TableBuilder"/>
 *     &lt;enumeration value="SqlProcedureBuilder"/>
 *     &lt;enumeration value="SqlFunctionBuilder"/>
 *     &lt;enumeration value="SilverlightHost"/>
 *     &lt;enumeration value="ServiceHost"/>
 *     &lt;enumeration value="WebServiceHost"/>
 *     &lt;enumeration value="WebUIHost"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CADISComponents.ComponentKey", namespace = "http://schemas.datacontract.org/2004/07/CADIS.Common")
@XmlEnum
public enum CADISComponentsComponentKey {

    @XmlEnumValue("DataPorter")
    DATA_PORTER("DataPorter"),
    @XmlEnumValue("DataInspector")
    DATA_INSPECTOR("DataInspector"),
    @XmlEnumValue("DataMatcherProcess")
    DATA_MATCHER_PROCESS("DataMatcherProcess"),
    @XmlEnumValue("DataMatcherInbox")
    DATA_MATCHER_INBOX("DataMatcherInbox"),
    @XmlEnumValue("DataMatcherRealign")
    DATA_MATCHER_REALIGN("DataMatcherRealign"),
    @XmlEnumValue("DataConstructor")
    DATA_CONSTRUCTOR("DataConstructor"),
    @XmlEnumValue("DataIllustrator")
    DATA_ILLUSTRATOR("DataIllustrator"),
    @XmlEnumValue("DataIllustratorTemplate")
    DATA_ILLUSTRATOR_TEMPLATE("DataIllustratorTemplate"),
    @XmlEnumValue("Solution")
    SOLUTION("Solution"),
    @XmlEnumValue("CadisForBenchmarks_Obsolete")
    CADIS_FOR_BENCHMARKS_OBSOLETE("CadisForBenchmarks_Obsolete"),
    @XmlEnumValue("DataBlocks")
    DATA_BLOCKS("DataBlocks"),
    @XmlEnumValue("DataGeneratorFunction")
    DATA_GENERATOR_FUNCTION("DataGeneratorFunction"),
    @XmlEnumValue("DataGeneratorInbox")
    DATA_GENERATOR_INBOX("DataGeneratorInbox"),
    @XmlEnumValue("DataManagerFunction")
    DATA_MANAGER_FUNCTION("DataManagerFunction"),
    @XmlEnumValue("DataManagerInbox")
    DATA_MANAGER_INBOX("DataManagerInbox"),
    @XmlEnumValue("EventWatcher")
    EVENT_WATCHER("EventWatcher"),
    @XmlEnumValue("ProcessLauncher")
    PROCESS_LAUNCHER("ProcessLauncher"),
    @XmlEnumValue("RuleBuilder")
    RULE_BUILDER("RuleBuilder"),
    @XmlEnumValue("Workflow")
    WORKFLOW("Workflow"),
    @XmlEnumValue("ComponentPromotion")
    COMPONENT_PROMOTION("ComponentPromotion"),
    @XmlEnumValue("NotSpecified")
    NOT_SPECIFIED("NotSpecified"),
    @XmlEnumValue("WorkflowPage")
    WORKFLOW_PAGE("WorkflowPage"),
    @XmlEnumValue("WorkflowElement")
    WORKFLOW_ELEMENT("WorkflowElement"),
    @XmlEnumValue("Archive")
    ARCHIVE("Archive"),
    @XmlEnumValue("Diagram")
    DIAGRAM("Diagram"),
    @XmlEnumValue("ViewBuilder")
    VIEW_BUILDER("ViewBuilder"),
    @XmlEnumValue("DataFlowRule")
    DATA_FLOW_RULE("DataFlowRule"),
    @XmlEnumValue("DataFlowProcess")
    DATA_FLOW_PROCESS("DataFlowProcess"),
    @XmlEnumValue("DataFlowSequence")
    DATA_FLOW_SEQUENCE("DataFlowSequence"),
    @XmlEnumValue("CubeView")
    CUBE_VIEW("CubeView"),
    @XmlEnumValue("TableSet")
    TABLE_SET("TableSet"),
    @XmlEnumValue("XmlViewBuilderDeprecated")
    XML_VIEW_BUILDER_DEPRECATED("XmlViewBuilderDeprecated"),
    @XmlEnumValue("DataMatcherIdGenerator")
    DATA_MATCHER_ID_GENERATOR("DataMatcherIdGenerator"),
    @XmlEnumValue("TableBuilder")
    TABLE_BUILDER("TableBuilder"),
    @XmlEnumValue("SqlProcedureBuilder")
    SQL_PROCEDURE_BUILDER("SqlProcedureBuilder"),
    @XmlEnumValue("SqlFunctionBuilder")
    SQL_FUNCTION_BUILDER("SqlFunctionBuilder"),
    @XmlEnumValue("SilverlightHost")
    SILVERLIGHT_HOST("SilverlightHost"),
    @XmlEnumValue("ServiceHost")
    SERVICE_HOST("ServiceHost"),
    @XmlEnumValue("WebServiceHost")
    WEB_SERVICE_HOST("WebServiceHost"),
    @XmlEnumValue("WebUIHost")
    WEB_UI_HOST("WebUIHost");
    private final String value;

    CADISComponentsComponentKey(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CADISComponentsComponentKey fromValue(String v) {
        for (CADISComponentsComponentKey c: CADISComponentsComponentKey.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

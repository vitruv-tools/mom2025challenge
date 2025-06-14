package tools.vitruv.methodologisttemplate.vsum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.vitruv.framework.vsum.VirtualModel;
import tools.vitruv.methodologisttemplate.model.Ontology.Component;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.Configuration;

import java.nio.file.Path;
import java.util.List;

public class ScenarioTwoTest extends AbstractVSUMExampleTest {
    @Test
    void test(@TempDir Path tempDir) throws Exception {
        VirtualModel vsum = createDefaultVirtualModel(tempDir);
        var ontologyviewtype = getOntologyViewType(vsum);
        ontologyviewtype.register(vsum,"resources/part_catalogue/ontology.owl", tempDir);
        var systemviewtype = getSystemDecompositionViewType(vsum);
        systemviewtype.register(vsum, "resources/system_decomposition/system_config.json", tempDir);
        var requirementsviewtype = getRequirementsViewType(vsum);
        requirementsviewtype.register(vsum, "resources/requirements_specification/requirements.csv", tempDir);
        var reportviewtype = getReportViewType(vsum);
        reportviewtype.save("target/test/vsumexport/report.md", vsum);
        modificationScenarioOne(vsum);
        reportviewtype.save("target/test/vsumexport/report1.1.md", vsum);

    }
}

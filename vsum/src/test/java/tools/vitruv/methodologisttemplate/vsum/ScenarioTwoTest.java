package tools.vitruv.methodologisttemplate.vsum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.vitruv.framework.vsum.VirtualModel;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScenarioTwoTest extends AbstractVSUMExampleTest {
    private static final int REQUIREMENT_1_LINE = 22;
    private static final int REQUIREMENT_2_LINE = 23;
    private static final int SATISFIED_LINE = 29;

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
        // save the report view for automatic (and potential manual) inspection
        reportviewtype.save(tempDir.toString() + "/report.md", vsum);
        var content = Files.readAllLines(Path.of(tempDir.toString() + "/report.md"));
        
        // lines 22 and 23 should contain |SATISFIED| and line 29 **SATISFIED**
        assertTrue(content.get(REQUIREMENT_1_LINE).contains("|SATISFIED|"));
        assertTrue(content.get(REQUIREMENT_2_LINE).contains("|SATISFIED|"));
        assertTrue(content.get(SATISFIED_LINE).contains("**SATISFIED**"));
       
        modificationScenarioOne(vsum);
        // save the report view for automatic (and potential manual) inspection
        reportviewtype.save(tempDir.toString() + "/report1.1.md", vsum);
        content = Files.readAllLines(Path.of(tempDir.toString() + "/report1.1.md"));
        
        // lines 22 should contain |NOT SATISFIED| and line 23 |SATISFIED| and line 29 **NOT SATISFIED**
        assertTrue(content.get(REQUIREMENT_1_LINE).contains("|NOT SATISFIED|"));
        assertTrue(content.get(REQUIREMENT_2_LINE).contains("|SATISFIED|"));
        assertTrue(content.get(SATISFIED_LINE).contains("**NOT SATISFIED**"));
    }
}

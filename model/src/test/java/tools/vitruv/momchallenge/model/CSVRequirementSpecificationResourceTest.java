package tools.vitruv.momchallenge.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryRegistryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;

import tools.vitruv.methodologisttemplate.model.requirement_specification.Constraint;
import tools.vitruv.methodologisttemplate.model.requirement_specification.Requirement;
import tools.vitruv.methodologisttemplate.model.csv.CSVRequirementSpecificationResourceFactory;

/**
 * Round-trip test for CSVRequirementSpecificationResource
 */
public class CSVRequirementSpecificationResourceTest {

    @BeforeAll
    static void registerFactory() {
        ResourceFactoryRegistryImpl.INSTANCE.getExtensionToFactoryMap()
            .put("csv", new CSVRequirementSpecificationResourceFactory());
    }

    @Test
    void testDeserializationAndSerialization(@TempDir(cleanup = CleanupMode.ON_SUCCESS) Path tmp)
            throws IOException {

        /* Load CSV */
        var rs = new ResourceSetImpl();
        var original = rs.createResource(
                       URI.createFileURI("resources/requirements_specification/requirements.csv"));
        original.load(new HashMap<>());
        checkRequirementsOf(original);

        /* Save the copy & unload */
        String copyPath = tmp + File.separator + "requirements_copy.csv";
        var copy = rs.createResource(URI.createFileURI(copyPath));
        copy.getContents().addAll(original.getContents());
        copy.save(new HashMap<>());
        copy.unload();

        /* Reload the copy & validate */
        var reloaded = rs.createResource(URI.createFileURI(copyPath));
        reloaded.load(new HashMap<>());
        checkRequirementsOf(reloaded);
    }

    /* validation*/

    private void checkRequirementsOf(org.eclipse.emf.ecore.resource.Resource res) {
        assertEquals(2, res.getContents().size());
   
        assertRequirement((Requirement) res.getContents().get(0),
            "REQ001",
            "Total system mass for SatAlphaV1 configuration must be acceptable",
            "calculated_total_mass_kg", "<=", "350", "kg");

        
        assertRequirement((Requirement) res.getContents().get(1),
            "REQ002",
            "Maximum individual component mass",
            "component_mass_max", "<=", "200", "kg");
    }

    private void assertRequirement(Requirement req,
                                   String id, String desc,
                                   String param, String op,
                                   String value, String unit) {

        // requirement 
        assertEquals(id,   req.getRequirementID());
        assertEquals(desc, req.getDescription());

        // constraint
        Constraint c = req.getConstraint();
        assertNotNull(c);
        assertEquals(param,  c.getParameter());
        assertEquals(op,     c.getOperator());
        assertEquals(value,  c.getValue());
        assertEquals(unit,   c.getUnit());

        // bidirectional integrity
        assertSame(req, c.getRequirement());
    }
}


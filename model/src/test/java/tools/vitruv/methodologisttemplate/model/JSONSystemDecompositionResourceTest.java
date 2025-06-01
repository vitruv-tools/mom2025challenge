package tools.vitruv.methodologisttemplate.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import System_Decomposition.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryRegistryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import tools.vitruv.methodologisttemplate.model.serialization.JSONSystemDecompositionResourceFactory;

class JSONSystemDecompositionResourceTest {
    /**
     * Sets up the appropriate ResourceFactory.
     */
    @BeforeAll
    static void setUpSystemDecompositionResourceFactory() {
        ResourceFactoryRegistryImpl.INSTANCE.getExtensionToFactoryMap().put("json", new JSONSystemDecompositionResourceFactory());
    }

    /**
     * Uses a JSONSystemDecompositionResource to deserialize system_config.json.
     */
    @Test
    void testDeserialization() throws IOException {
        // Load the System Configuration.
        var resourceSet = new ResourceSetImpl();
        var resource = resourceSet.createResource(URI.createFileURI("resources/system_decomposition/system_config.json"));
        resource.load(new HashMap<>());

        // Assert one config element.
        assertEquals(1, resource.getContents().size());
        var configuration = (Configuration) resource.getContents().get(0);

        // Check configuration itself.
        assertEquals("SatAlphaV1", configuration.getId());
        assertEquals("Configuration for Satellite Alpha, Version 1", configuration.getDescription());
        assertEquals(340.9d, configuration.getMass_kg());

        // Check components.
        assertEquals(5, configuration.getComponents().size());
        assertComponent(configuration, "T001", "MainThruster001", "ThrusterComponent", 1, 150.5);
        assertComponent(configuration, "SP001A", "SolarPanel_A", "SolarPanelComponent", 1, 75.2);
        assertComponent(configuration, "SP001B", "SolarPanel_B", "SolarPanelComponent", 1, 75.2);
        assertComponent(configuration, "ANT001", "CommAntenna001", "AntennaComponent", 1, 20.0);
        assertComponent(configuration, "ANT002", "CommAntenna002", "AntennaComponent", 1, 20.0);
    }

    /**
     * Tests that config has a Component object with the given parameters.
     * 
     * @param config
     * @param id
     * @param name
     * @param type
     * @param quantity
     * @param massPerUnit
     */
    private void assertComponent(Configuration config, String id, String name, String type, long quantity, double massPerUnit) {
        // Look up the component in the configuration.
        var componentSearchResult = config.getComponents().stream()
            .filter(c -> c.getId().equals(id))
            .findAny();
        assertTrue(componentSearchResult.isPresent());
        var component = componentSearchResult.get();

        // Verify the component.
        assertEquals(id, component.getId());
        assertEquals(name, component.getName());
        assertEquals(type, component.getType());
        assertEquals(quantity, component.getQuantity());
        assertEquals(massPerUnit, component.getMass_kg());
    }
}

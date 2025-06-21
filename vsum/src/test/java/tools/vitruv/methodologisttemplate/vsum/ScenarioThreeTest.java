package tools.vitruv.methodologisttemplate.vsum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tools.vitruv.methodologisttemplate.model.System_Decomposition.Component;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.Configuration;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.System_DecompositionFactory;

class ScenarioThreeTest extends AbstractVSUMExampleTest{
    @Test
    void test(@TempDir Path tempDir) {
        // Open a System Decomposition view.
        var vsum = createDefaultVirtualModel(tempDir);
        var systemviewtype = getSystemDecompositionViewType(vsum);
        systemviewtype.register(vsum, "resources/system_decomposition/system_config.json", tempDir);

        // Retrieve the desired components.
        var systemView = getDefaultView(vsum, List.of(Configuration.class)).withChangeDerivingTrait();
        var components = systemView.getRootObjects(Configuration.class)
            .stream()
            .flatMap(configuration -> configuration.getComponents().stream())
            .filter(component -> component.getType().contains("Antenna"))
            .toList();

        assertEquals(2, components.size());
        findComponent(components, "ANT001");
        findComponent(components, "ANT002");

        // Modify the view: Add a new component.
        modifyView(systemView, it -> {
            var antenna001 = findComponent(components, "ANT001");
            var configuration = antenna001.getConfiguration();

            var antenna003 = System_DecompositionFactory.eINSTANCE.createComponent();
            antenna003.setId("ANT003");
            antenna003.setName("CommAntenna002");
            antenna003.setMass_kg(20.0);
            antenna003.setType("AntennaComponent");
            antenna003.setQuantity(3);
            configuration.getComponents().add(antenna003);
        });

        // Check the updated view.
        var updatedView = getDefaultView(vsum, List.of(Configuration.class)).withChangeDerivingTrait();
        var configurationOpt = updatedView.getRootObjects(Configuration.class).stream().findFirst();
        assertTrue(configurationOpt.isPresent());

        // Six components; mass now is 400.9.
        var configuration = configurationOpt.get();
        assertEquals(6, configuration.getComponents().size());
        assertEquals(400.9, configuration.getMass_kg(), 1e-5);
    }

    private Component findComponent(Collection<Component> components, String id) {
        var component = components.stream().filter(c -> c.getId().equals(id)).findFirst();
        assertTrue(component.isPresent());
        return component.get();
    }
}

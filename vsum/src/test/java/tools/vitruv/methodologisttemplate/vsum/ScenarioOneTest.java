package tools.vitruv.methodologisttemplate.vsum;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tools.vitruv.framework.vsum.VirtualModel;
import tools.vitruv.methodologisttemplate.model.Ontology.Component;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.Configuration;

/**
 * This class provides an example how to define and use a VSUM.
 */
public class ScenarioOneTest extends AbstractVSUMExampleTest{

  // @Test
  void test(@TempDir Path tempDir) throws Exception {
    VirtualModel vsum = createDefaultVirtualModel(tempDir);
    var ontologyviewtype = getOntologyViewType(vsum);
    ontologyviewtype.register(vsum,"resources/part_catalogue/ontology.owl", tempDir);
    var systemviewtype = getSystemDecompositionViewType(vsum);
    systemviewtype.register(vsum, "resources/system_decomposition/system_config.json", tempDir);
  
    // now all the models are successfully integrated and we can do the change we want to do
    // i.e., changing the weight of the component T001 from 150.5 to 165.0
    var view = getDefaultView(vsum, List.of(Component.class)).withChangeDerivingTrait();
    modifyView(view, it -> {
      it.getRootObjects(Component.class).stream().filter(cmp -> cmp.getComponentID().equals("T001")).toList().get(0).setHasMass(165.0);
    });
   
    // and check whether the results conform to our idea
    // the modification directly has taken place
    assertView(getDefaultView(vsum, List.of(Component.class)), it -> it.getRootObjects(Component.class).stream().filter(cmp -> cmp.getComponentID().equals("T001")).toList().get(0).getHasMass() == 165.0);
    // and in addition, the configuration total mass and the component mass have been updated
    assertView(getDefaultView(vsum, List.of(Configuration.class)), it -> {
      var config = it.getRootObjects(Configuration.class).iterator().next();
      var component = config.getComponents().stream().filter(cmp -> cmp.getId().equals("T001")).toList().get(0);

      assertEquals(165.0d, component.getMass_kg(), 0.00001);
      assertEquals(355.4d, config.getMass_kg(), 0.00001);
      return true;
    });
    
    // and save the new view versions for possible manual inspection
    ontologyviewtype.save("target/test/vsumexport/ontology_1.1.owl", vsum);
    systemviewtype.save("target/test/vsumexport/system_config1_1.json", vsum);
  }
}

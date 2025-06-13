package tools.vitruv.methodologisttemplate.vsum;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tools.vitruv.framework.views.CommittableView;
import tools.vitruv.framework.vsum.VirtualModel;
import tools.vitruv.methodologisttemplate.model.Ontology.Component;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.Configuration;
import tools.vitruv.methodologisttemplate.viewtype.adapters.ontology.Owl2Ecore;

/**
 * This class provides an example how to define and use a VSUM.
 */
public class ScenarioOne extends AbstractVSUMExampleTest{

  @Test
  void test(@TempDir Path tempDir) throws Exception {
    VirtualModel vsum = createDefaultVirtualModel(tempDir);

    // load the ontology and its instance
    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File("resources/part_catalogue/ontology.owl"));
    URI metamodelURI = URI.createFileURI(tempDir + "metamodels/ontology.ecore");
    URI modelURI = URI.createFileURI(tempDir + "models/instance.xmi");
    ResourceSet resourceSet = new ResourceSetImpl();
    // resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
    new Owl2Ecore().convert(ontology, resourceSet, metamodelURI, modelURI);
    Resource model = null;
    for (var res : resourceSet.getResources()){
      if (res.getURI().equals(modelURI)) {
        model = res;
      }
      res.save(Collections.emptyMap());
    }
    // load the system decomposition model
    var sd = resourceSet.createResource(URI.createFileURI("resources/system_decomposition/system_config.json"));
    sd.load(Collections.emptyMap());
    CommittableView view = getDefaultView(vsum, List.of()).withChangeDerivingTrait();
    Resource pc = model;
    modifyView(view, (CommittableView v) -> {
      int i = 0;
      while (!pc.getContents().isEmpty()) {
        v.registerRoot(pc.getContents().get(0), URI.createFileURI(tempDir + "pc.xmi#" + i++));
      }
    });
    view = getDefaultView(vsum, List.of()).withChangeDerivingTrait();
    modifyView(view, (CommittableView v) -> {
      int i = 0;
      while (!sd.getContents().isEmpty()) {
        v.registerRoot(sd.getContents().get(0), URI.createFileURI(tempDir + "sd.xmi#" + i++));
      }
    });

    view = getDefaultView(vsum, List.of(Component.class)).withChangeDerivingTrait();
    var saveres = resourceSet.createResource(URI.createFileURI("target/test/vsumexport/pc.xmi"));
    saveres.getContents().addAll(view.getRootObjects());
    saveres.save(Collections.emptyMap());

    view = getDefaultView(vsum, List.of(Configuration.class)).withChangeDerivingTrait();
    saveres = resourceSet.createResource(URI.createFileURI("target/test/vsumexport/sd.xmi"));
    saveres.getContents().addAll(view.getRootObjects());
    saveres.save(Collections.emptyMap());
    // assert that the directly added Component is present
    Assertions.assertEquals(5, getDefaultView(vsum, List.of(Component.class)).getRootObjects().size());
    // as well as the Root that should be created by the Reactions, see templateReactions.reactions#14
    // Assertions.assertEquals(1, getDefaultView(vsum, List.of(Root.class)).getRootObjects().size());

    // now all the models are successfully integrated and we can do the change we want to do
    // i.e., changing the weight of the component T001 from 150.5 to 165.0
    view.close();
    view = getDefaultView(vsum, List.of(Component.class)).withChangeDerivingTrait();
    modifyView(view, it -> {
      it.getRootObjects(Component.class).stream().filter(cmp -> cmp.getComponentID().equals("T001")).toList().get(0).setHasMass(165.0);
    });
    // and check whether the results conform to our idea
    // the modification directly has taken place
    assertView(getDefaultView(vsum, List.of(Component.class)), it -> {
      return it.getRootObjects(Component.class).stream().filter(cmp -> cmp.getComponentID().equals("T001")).toList().get(0).getHasMass() == 165.0;
    });
    // and in addition, the configuration total mass and the component mass have been updated
    assertView(getDefaultView(vsum, List.of(Configuration.class)), it -> {
      var config = it.getRootObjects(Configuration.class).iterator().next();
      var component = config.getComponents().stream().filter(cmp -> cmp.getId().equals("T001")).toList().get(0);
      return component.getMass_kg() - 165.0 < 0.00001 && config.getMass_kg() - 355.4 < 0.00001;
    });
    // and save the new view versions
    view = getDefaultView(vsum, List.of(Configuration.class)).withChangeDerivingTrait();
    saveres = resourceSet.createResource(URI.createFileURI("target/test/vsumexport/sd_1.1.xmi"));
    saveres.getContents().addAll(view.getRootObjects());
    saveres.save(Collections.emptyMap());
    view = getDefaultView(vsum, List.of(Component.class)).withChangeDerivingTrait();
    saveres = resourceSet.createResource(URI.createFileURI("target/test/vsumexport/pc_1.1.xmi"));
    saveres.getContents().addAll(view.getRootObjects());
    saveres.save(Collections.emptyMap());
  }


}

package tools.vitruv.methodologisttemplate.vsum;

import mir.reactions.pc2sd.Pc2sdChangePropagationSpecification;
import mir.reactions.sd2pc.Sd2pcChangePropagationSpecification;
import ontology.Component;
import ontology.OntologyFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryRegistryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import tools.vitruv.framework.vsum.VirtualModelBuilder;
import tools.vitruv.framework.vsum.internal.InternalVirtualModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tools.vitruv.change.propagation.ChangePropagationMode;
import tools.vitruv.change.testutils.TestUserInteraction;
import tools.vitruv.framework.views.CommittableView;
import tools.vitruv.framework.views.View;
import tools.vitruv.framework.views.ViewTypeFactory;
import tools.vitruv.framework.vsum.VirtualModel;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.Configuration;
import tools.vitruv.methodologisttemplate.model.persistence.JSONSystemDecompositionResourceFactory;
import tools.vitruv.momchallenge.Owl2Ecore;
import com.google.gson.ExclusionStrategy;

/**
 * This class provides an example how to define and use a VSUM.
 */
public class VSUMExampleTest {

  @BeforeAll
  static void setup() {
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("json", new JSONSystemDecompositionResourceFactory());
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

  }

  @Test
  void systemInsertionAndPropagationTest(@TempDir Path tempDir) throws Exception {
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

  private InternalVirtualModel createDefaultVirtualModel(Path projectPath) {
    InternalVirtualModel model = new VirtualModelBuilder()
        .withStorageFolder(projectPath)
        .withUserInteractorForResultProvider(new TestUserInteraction.ResultProvider(new TestUserInteraction()))
        .withChangePropagationSpecifications(List.of(new Pc2sdChangePropagationSpecification(), new Sd2pcChangePropagationSpecification()))
        .buildAndInitialize();
    model.setChangePropagationMode(ChangePropagationMode.TRANSITIVE_CYCLIC);
    return model;
  }

  // See https://github.com/vitruv-tools/Vitruv/issues/717 for more information about the rootTypes
  private View getDefaultView(VirtualModel vsum, Collection<Class<?>> rootTypes) {
    var selector = vsum.createSelector(ViewTypeFactory.createIdentityMappingViewType("default"));
    selector.getSelectableElements().stream()
      .filter(element -> rootTypes.stream().anyMatch(it -> it.isInstance(element)))
      .forEach(it -> selector.setSelected(it, true));
    return selector.createView();
  }

  // These functions are only for convience, as they make the code a bit better readable
  private void modifyView(CommittableView view, Consumer<CommittableView> modificationFunction) {
    modificationFunction.accept(view);
    view.commitChanges();
  }

  private boolean assertView(View view, Function<View, Boolean> viewAssertionFunction) {
    return viewAssertionFunction.apply(view);
  }

}

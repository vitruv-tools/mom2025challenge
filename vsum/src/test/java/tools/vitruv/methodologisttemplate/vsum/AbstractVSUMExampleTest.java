package tools.vitruv.methodologisttemplate.vsum;

import mir.reactions.pc2sd.Pc2sdChangePropagationSpecification;
import mir.reactions.sd2pc.Sd2pcChangePropagationSpecification;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.jupiter.api.BeforeAll;
import tools.vitruv.change.propagation.ChangePropagationMode;
import tools.vitruv.change.testutils.TestUserInteraction;
import tools.vitruv.framework.views.CommittableView;
import tools.vitruv.framework.views.View;
import tools.vitruv.framework.views.ViewTypeFactory;
import tools.vitruv.framework.vsum.VirtualModel;
import tools.vitruv.framework.vsum.VirtualModelBuilder;
import tools.vitruv.framework.vsum.internal.InternalVirtualModel;
import tools.vitruv.methodologisttemplate.model.Ontology.Component;
import tools.vitruv.methodologisttemplate.viewtype.OntologyViewType;
import tools.vitruv.methodologisttemplate.viewtype.ReportViewType;
import tools.vitruv.methodologisttemplate.viewtype.RequirementsViewType;
import tools.vitruv.methodologisttemplate.viewtype.SystemDecompositionViewType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractVSUMExampleTest {

    @BeforeAll
    static void setup() {
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
    }

    protected InternalVirtualModel createDefaultVirtualModel(Path projectPath) {
        InternalVirtualModel model = new VirtualModelBuilder()
                .withStorageFolder(projectPath)
                .withUserInteractorForResultProvider(new TestUserInteraction.ResultProvider(new TestUserInteraction()))
                .withChangePropagationSpecifications(List.of(new Pc2sdChangePropagationSpecification(), new Sd2pcChangePropagationSpecification()))
                .withViewType(new OntologyViewType("ontology"))
                .withViewType(new SystemDecompositionViewType("system"))
                .withViewType(new RequirementsViewType("requirements"))
                .withViewType(new ReportViewType("report"))
                .buildAndInitialize();
        model.setChangePropagationMode(ChangePropagationMode.TRANSITIVE_CYCLIC);
        return model;
    }

    // See https://github.com/vitruv-tools/Vitruv/issues/717 for more information about the rootTypes
    protected View getDefaultView(VirtualModel vsum, Collection<Class<?>> rootTypes) {
        var selector = vsum.createSelector(ViewTypeFactory.createIdentityMappingViewType("default"));
        selector.getSelectableElements().stream()
                .filter(element -> rootTypes.stream().anyMatch(it -> it.isInstance(element)))
                .forEach(it -> selector.setSelected(it, true));
        return selector.createView();
    }

    // These functions are only for convience, as they make the code a bit better readable
    protected void modifyView(CommittableView view, Consumer<CommittableView> modificationFunction) {
        modificationFunction.accept(view);
        view.commitChanges();
    }

    protected boolean assertView(View view, Function<View, Boolean> viewAssertionFunction) {
        return viewAssertionFunction.apply(view);
    }

    protected void saveResources(VirtualModel vsum, String exportPath, Class<?> export) {
        ResourceSet resourceSet = new ResourceSetImpl();
        var view = getDefaultView(vsum, List.of(export));
        var saveres = resourceSet.createResource(URI.createFileURI(exportPath));
        saveres.getContents().addAll(view.getRootObjects());
        try {
            saveres.save(Collections.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected OntologyViewType getOntologyViewType(VirtualModel vsum) {
        return (OntologyViewType) vsum.getViewTypes().stream().filter(it -> it.getName().equals("ontology")).findAny().get();
    }

    protected SystemDecompositionViewType getSystemDecompositionViewType(VirtualModel vsum) {
        return (SystemDecompositionViewType) vsum.getViewTypes().stream().filter(it -> it.getName().equals("system")).findAny().get();
    }

    protected RequirementsViewType getRequirementsViewType(VirtualModel vsum) {
        return (RequirementsViewType) vsum.getViewTypes().stream().filter(it -> it.getName().equals("requirements")).findAny().get();
    }

    protected ReportViewType getReportViewType(VirtualModel vsum) {
        return (ReportViewType) vsum.getViewTypes().stream().filter(it -> it.getName().equals("report")).findAny().get();
    }

    protected void modificationScenarioOne(VirtualModel vsum) {
        var view = getDefaultView(vsum, List.of(Component.class)).withChangeDerivingTrait();
        modifyView(view, it -> {
            it.getRootObjects(Component.class).stream().filter(cmp -> cmp.getComponentID().equals("T001")).toList().get(0).setHasMass(165.0);
        });
    }
}

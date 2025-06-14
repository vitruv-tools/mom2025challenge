package tools.vitruv.methodologisttemplate.viewtype;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import tools.vitruv.framework.views.CommittableView;
import tools.vitruv.framework.vsum.VirtualModel;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.Configuration;
import tools.vitruv.methodologisttemplate.model.requirement_specification.Constraint;
import tools.vitruv.methodologisttemplate.model.requirement_specification.Requirement;
import tools.vitruv.methodologisttemplate.viewtype.adapters.csv.CSVRequirementSpecificationResourceFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class RequirementsViewType extends RegistrableViewType {
    private Resource requirements = null;
    public RequirementsViewType(String name) {
        super(name);
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("csv", new CSVRequirementSpecificationResourceFactory());
    }

    @Override
    public void register(VirtualModel vsum, String modelPath, Path tempDir) {
        requirements = loadResource(modelPath);
        var view = getDefaultView(vsum, List.of()).withChangeDerivingTrait();
        modifyView(view, (CommittableView v) -> {
            int i = 0;
            while (!requirements.getContents().isEmpty()) {
                v.registerRoot(requirements.getContents().get(0), URI.createFileURI(tempDir + "requirements.xmi#" + i++));
            }
        });
    }



    @Override
    public void save(String path, VirtualModel vsum) {
        saveResources(vsum, path, List.of(Constraint.class, Requirement.class));
    }
}

package tools.vitruv.methodologisttemplate.viewtype;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import tools.vitruv.methodologisttemplate.model.systemdecomposition.JSONSystemDecompositionResourceFactory;
import tools.vitruv.framework.views.CommittableView;
import tools.vitruv.framework.views.impl.IdentityMappingViewType;
import tools.vitruv.framework.vsum.VirtualModel;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.Configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class SystemDecompositionViewType extends RegistrableViewType {
    private Resource sd = null;
    public SystemDecompositionViewType(String name) {
        super(name);
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("json", new JSONSystemDecompositionResourceFactory());
    }

    public void register(VirtualModel vsum, String modelPath, Path tempDir) {
        var sd = loadResource(modelPath);
        var view = getDefaultView(vsum, List.of()).withChangeDerivingTrait();
        modifyView(view, (CommittableView v) -> {
            int i = 0;
            while (!sd.getContents().isEmpty()) {
                v.registerRoot(sd.getContents().get(0), URI.createFileURI(tempDir + "sd.xmi#" + i++));
            }
        });
    }
    public void save(String path, VirtualModel vsum) {
        saveResources(vsum, path, List.of(Configuration.class));
    }
}

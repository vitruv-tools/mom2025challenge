package tools.vitruv.methodologisttemplate.viewtype;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import tools.vitruv.framework.vsum.VirtualModel;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

public abstract class RegistrableViewType extends SaveableViewType {

    public RegistrableViewType(String name) {
        super(name);
    }

    public abstract void register (VirtualModel vsum, String modelPath, Path tempDir);

    protected Resource loadResource(String modelPath) {
        ResourceSet resourceSet = new ResourceSetImpl();
        // load the system decomposition model
        var resource = resourceSet.createResource(URI.createFileURI(modelPath));
        try {
            resource.load(Collections.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resource;
    }
}

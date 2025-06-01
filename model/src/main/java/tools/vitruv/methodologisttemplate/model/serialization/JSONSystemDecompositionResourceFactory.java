package tools.vitruv.methodologisttemplate.model.serialization;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;

/**
 * A JSONSystemDecompositionResourceFactory produces JSONSystemDecompositionResources.
 */
public class JSONSystemDecompositionResourceFactory implements Factory {
    @Override
    public Resource createResource(URI uri) {
        return new JSONSystemDecompositionResource(uri);
    }    
}

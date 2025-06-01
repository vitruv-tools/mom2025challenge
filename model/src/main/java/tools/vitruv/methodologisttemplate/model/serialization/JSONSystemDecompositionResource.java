package tools.vitruv.methodologisttemplate.model.serialization;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

/**
 * An implementation of ResourceImpl that serializes and deserializes 
 * Configuration objects with Gson.
 */
public class JSONSystemDecompositionResource extends ResourceImpl {
    /**
     * Creates a new JSONSystemDecompositionResource.
     * 
     * @param uri - URI
     */
    public JSONSystemDecompositionResource(URI uri) {
        super(uri);
    }
}

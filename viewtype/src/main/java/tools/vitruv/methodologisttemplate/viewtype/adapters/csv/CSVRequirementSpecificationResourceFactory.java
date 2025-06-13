package tools.vitruv.methodologisttemplate.viewtype.adapters.csv;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;

public class CSVRequirementSpecificationResourceFactory implements Factory {
    @Override public Resource createResource(URI uri) {
        return new CSVRequirementSpecificationResource(uri);
    }
}

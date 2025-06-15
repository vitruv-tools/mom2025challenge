package tools.vitruv.methodologisttemplate.viewtype;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import tools.vitruv.framework.views.CommittableView;
import tools.vitruv.framework.views.View;
import tools.vitruv.framework.views.ViewTypeFactory;
import tools.vitruv.framework.views.impl.IdentityMappingViewType;
import tools.vitruv.framework.vsum.VirtualModel;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public abstract class SaveableViewType extends IdentityMappingViewType {
    public SaveableViewType(String name) {
        super(name);
    }

    public abstract void save(String path, VirtualModel vsum);

    protected void saveResources(VirtualModel vsum, String exportPath, List<Class<?>> export) {
        ResourceSet resourceSet = new ResourceSetImpl();
        var view = getDefaultView(vsum, export);
        var saveres = resourceSet.createResource(URI.createFileURI(exportPath));
        saveres.getContents().addAll(view.getRootObjects());
        try {
            saveres.save(Collections.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected View getDefaultView(VirtualModel vsum, Collection<Class<?>> rootTypes) {
        var selector = vsum.createSelector(ViewTypeFactory.createIdentityMappingViewType("default"));
        selector.getSelectableElements().stream()
                .filter(element -> rootTypes.stream().anyMatch(it -> it.isInstance(element)))
                .forEach(it -> selector.setSelected(it, true));
        return selector.createView();
    }

    protected void modifyView(CommittableView view, Consumer<CommittableView> modificationFunction) {
        modificationFunction.accept(view);
        view.commitChanges();
    }
}

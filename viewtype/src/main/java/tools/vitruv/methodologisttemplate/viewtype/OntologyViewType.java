package tools.vitruv.methodologisttemplate.viewtype;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import tools.vitruv.framework.views.CommittableView;
import tools.vitruv.framework.vsum.VirtualModel;
import tools.vitruv.methodologisttemplate.viewtype.adapters.ontology.Owl2Ecore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class OntologyViewType extends RegistrableViewType {
    private OWLOntology ontology = null;
    private Owl2Ecore owl2Ecore = new Owl2Ecore();

    public OntologyViewType(String name) {
        super(name);
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    }

    public void register(VirtualModel vsum, String modelPath, Path tempDir) {
        // load the ontology and its instance
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            ontology = manager.loadOntologyFromOntologyDocument(new File(modelPath));
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }

        URI metamodelURI = URI.createFileURI(tempDir + "metamodels/ontology.ecore");
        URI modelURI = URI.createFileURI(tempDir + "models/instance.xmi");
        ResourceSet resourceSet = new ResourceSetImpl();
        // resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
        owl2Ecore.convert(ontology, resourceSet, metamodelURI, modelURI);
        Resource model = null;
        for (var res : resourceSet.getResources()){
            if (res.getURI().equals(modelURI)) {
                model = res;
            }
            try {
                res.save(Collections.emptyMap());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        CommittableView view = getDefaultView(vsum, List.of()).withChangeDerivingTrait();
        Resource pc = model;
        modifyView(view, (CommittableView v) -> {
            int i = 0;
            while (!pc.getContents().isEmpty()) {
                v.registerRoot(pc.getContents().get(0), URI.createFileURI(tempDir + "pc.xmi#" + i++));
            }
        });
    }

    public void save(String path, VirtualModel vsum) {
        try {
            System.out.println("Saving ontology at " + IRI.create(new File(path).getAbsoluteFile()));
            owl2Ecore.updateOntology(ontology.getOWLOntologyManager(),ontology);
            ontology.saveOntology(IRI.create(new File(path).getAbsoluteFile()));
        } catch (OWLOntologyStorageException e) {
            throw new RuntimeException(e);
        }
    }


}

package model.ontology;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import tools.vitruv.methodologisttemplate.viewtype.adapters.ontology.Owl2Ecore;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class Owl2EcoreTest {

    @Test
    public void test () throws OWLOntologyCreationException, IOException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File("resources/part_catalogue/ontology.owl"));
        URI metamodelURI = URI.createFileURI("target/test/main/ecore/ontology.ecore");
        URI modelURI = URI.createFileURI("target/test/main/ecore/instance.xmi");
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
        new Owl2Ecore().convert(ontology, resourceSet, metamodelURI, modelURI);
        for (var res : resourceSet.getResources()){
            res.save(Collections.emptyMap());
        }

    }
}

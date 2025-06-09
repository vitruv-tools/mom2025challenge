package tools.vitruv.momchallenge;

import ontology.OntologyFactory;
import org.eclipse.emf.codegen.ecore.genmodel.*;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Owl2Ecore {

    private static final Map<OWLClass, EClass> CLASSES = new HashMap<>();
    private static final Map<OWLDataProperty, EAttribute> ATTRIBUTES = new HashMap<>();

    public static void main(String[] args) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File("model/resources/part_catalogue/ontology.owl"));
            var ePackage = EcoreFactory.eINSTANCE.createEPackage();
            ePackage.setName("ontology");
            ePackage.setNsPrefix("ontology");
            ontology.nestedClassExpressions().forEach(it -> addClass(it.asOWLClass(), ePackage));
            ontology.nestedClassExpressions().forEach(it -> addSuperClass(it.asOWLClass(), ontology));
            ontology.getDataPropertiesInSignature(false).stream().forEach(it -> addProperties(it, ontology));
            // in addition to the properties (which specify the ID), we also have the name, which is used in other
            // models as well, but is not a property in the sense of the data properties
            // therefore, we add a "name" property here to fill it with that name later
            // and as that should only be defined once, we set it to the upmost class
addNameAttribute();
            var resSet = new ResourceSetImpl();
            resSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
            var res = resSet.createResource(URI.createFileURI("model/src/main/ecore/ontology.ecore"));
            res.getContents().add(ePackage);
            res.save(Collections.emptyMap());
            var model = resSet.createResource(URI.createFileURI("model/src/main/ecore/instance.xmi"));
            handleInstances(ontology, model);
            model.save(Collections.emptyMap());
            // not working for now, so manual generation from an eclipse instance
            // generateGenmodel(ePackage, resSet, "model/src/main/ecore/ontology.genmodel");
        } catch (OWLOntologyCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void handleInstances(OWLOntology ontology, Resource model) {
        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {
            // Assumption 1: an individual has one and only one type
            // create the appropriate EObject
            var owltype = EntitySearcher.getTypes(individual, ontology).toList().get(0);
            EObject eobject = OntologyFactory.eINSTANCE.create(CLASSES.get(owltype));
            ontology.getDataPropertyAssertionAxioms(individual).forEach(it -> {
                var attribute = eobject.eClass().getEAllAttributes().stream().filter(its -> its.getName().equals(ATTRIBUTES.get(it.getProperty()).getName())).toList();
                if (!attribute.isEmpty()) {
                    try {
                        eobject.eSet(attribute.get(0), it.getObject().parseDouble());
                    } catch (Exception e) {
                        eobject.eSet(attribute.get(0), it.getObject().getLiteral());
                    }
                }
            });
            // after the "normal" attributes have been set, we have to still set the name attribute, i.e., the
            // one attribute that is still unset
            eobject.eClass().getEAllAttributes().forEach(it -> {if (eobject.eGet(it) == null) {eobject.eSet(it, individual.getIRI().getShortForm());}});
            model.getContents().add(eobject);
        }
    }

    private static void addNameAttribute() {
        for (EClass eClass : CLASSES.values()) {
            // not very generic, as it assumes that there is no diamond inheritance
            if (eClass.getESuperTypes().isEmpty()) {
                var attribute = EcoreFactory.eINSTANCE.createEAttribute();
                attribute.setName("name");
                attribute.setChangeable(true);
                attribute.setEType(EcorePackage.Literals.ESTRING);
                ATTRIBUTES.put(null, attribute);
                eClass.getEStructuralFeatures().add(attribute);
            }
        }
    }

    // not working for now skipped, code manually created with eclipse
    private static void generateGenmodel(EPackage ePackage, ResourceSet resourceSet, String genmodelPath) throws IOException {
        // Create GenModel
        GenModelFactory genModelFactory = GenModelFactory.eINSTANCE;
        GenModel genModel = genModelFactory.createGenModel();
        GenPackage genPackage = genModelFactory.createGenPackage();
        genPackage.setEcorePackage(ePackage);
        genModel.getGenPackages().add(genPackage);

        String outputPath = new File("model/target/generated-sources/ecore").getAbsolutePath();
        new File(outputPath).mkdirs(); // Ensure the directory exists
        genModel.setModelDirectory(outputPath.replace('\\', '/'));
        genModel.setModelName("GeneratorModel");
        genModel.setComplianceLevel(GenJDKLevel.JDK120_LITERAL);
        genModel.setRuntimePlatform(GenRuntimePlatform.IDE);
        genModel.setForceOverwrite(true);
        genModel.getGenPackages().get(0).setBasePackage("tools.vitruv.momchallenge.model");
        genModel.getGenPackages().get(0).setPrefix("ontology");
        genModel.initialize(Collections.singleton(ePackage));
        genModel.reconcile();
        genModel.setCanGenerate(true);
        var genModelResource = resourceSet.createResource(URI.createFileURI(new File(genmodelPath).getAbsolutePath()));
        genModelResource.getContents().add(genModel);
        genModelResource.save(Collections.EMPTY_MAP);
        genModel.generate(new BasicMonitor.Printing(System.out));
    }

    private static void addClass(OWLClass owlClass, EPackage ePackage) {
        EClass eClass = EcoreFactory.eINSTANCE.createEClass();
        if (ePackage.getNsURI() == null || ePackage.getNsURI().isEmpty()) {
            // remove the #
            ePackage.setNsURI(owlClass.getIRI().getNamespace().substring(0, owlClass.getIRI().getNamespace().length() - 1));
        }
        eClass.setName(owlClass.getIRI().getShortForm());
        CLASSES.put(owlClass, eClass);
        ePackage.getEClassifiers().add(eClass);
    }

    private static void addSuperClass(OWLClass owlClass, OWLOntology ontology) {
        ontology.getSubClassAxiomsForSubClass(owlClass).forEach(axiom -> CLASSES.get(owlClass).getESuperTypes().add(CLASSES.get(axiom.getSuperClass())));
    }

    private static void addProperties(OWLDataProperty property, OWLOntology ontology) {
        var eClass = CLASSES.get(ontology.getDataPropertyDomainAxioms(property).iterator().next().getDomain());
        var attribute = EcoreFactory.eINSTANCE.createEAttribute();
        attribute.setName(property.getIRI().getShortForm());
        attribute.setChangeable(true);
        ATTRIBUTES.put(property, attribute);
        var range = ontology.getDataPropertyRangeAxioms(property).iterator().next().getRange().toString();
        if (range.endsWith("string")) {
            attribute.setEType(EcorePackage.Literals.ESTRING);
        } else if (range.endsWith("int") || range.endsWith("integer")) {
            attribute.setEType(EcorePackage.Literals.EINT);
        } else if (range.endsWith("boolean")) {
            attribute.setEType(EcorePackage.Literals.EBOOLEAN);
        } else if (range.endsWith("float")) {
            attribute.setEType(EcorePackage.Literals.EFLOAT);
        } else if (range.endsWith("double") || range.endsWith("decimal")) {
            attribute.setEType(EcorePackage.Literals.EDOUBLE);
        } else if (range.endsWith("long")) {
            attribute.setEType(EcorePackage.Literals.ELONG);
        } else if (range.endsWith("short")) {
            attribute.setEType(EcorePackage.Literals.ESHORT);
        } else if (range.endsWith("byte")) {
            attribute.setEType(EcorePackage.Literals.EBYTE);
        } else if (range.endsWith("dateTime")) {
            attribute.setEType(EcorePackage.Literals.EDATE);
        } else {
            // Default fallback
            attribute.setEType(EcorePackage.Literals.ESTRING);
        }
        eClass.getEStructuralFeatures().add(attribute);
    }
}
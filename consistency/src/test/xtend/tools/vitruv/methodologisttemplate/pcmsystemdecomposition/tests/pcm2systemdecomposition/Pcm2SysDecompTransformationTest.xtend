package tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition

import java.nio.file.Path
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.palladiosimulator.pcm.repository.Repository
import org.palladiosimulator.pcm.repository.RepositoryFactory
import org.palladiosimulator.pcm.system.System
import org.palladiosimulator.pcm.system.SystemFactory
import tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.Pcm2SysDecompChangePropagationSpecification
import tools.vitruv.framework.views.View
import tools.vitruv.framework.testutils.integration.ViewBasedVitruvApplicationTest
import tools.vitruv.applications.util.temporary.java.JavaSetup

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.fail  // ← Für fail() Methode

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull

import org.eclipse.emf.ecore.EObject
import tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.SysDecompQueryUtil

import static extension edu.kit.ipd.sdq.commons.util.java.lang.IterableUtil.claimOne

class Pcm2SysDecompTransformationTest extends ViewBasedVitruvApplicationTest {
    protected var extension Pcm2SysDecompViewFactory viewFactory

    static val PCM_REPOSITORY_FILE_EXTENSION = "repository"
    static val PCM_SYSTEM_FILE_EXTENSION = "system"
    static val PCM_MODEL_FOLDER_NAME = "pcm"

    override protected enableTransitiveCyclicChangePropagation() {
        false
    }

    @BeforeAll
    def static void setupJavaFactories() {
        JavaSetup.prepareFactories()
    }

    @BeforeEach
    def final void setupViewFactory() {
        viewFactory = new Pcm2SysDecompViewFactory(virtualModel)
    }

    protected def Path getProjectModelPath(String modelName, String modelFileExtension) {
        Path.of(PCM_MODEL_FOLDER_NAME).resolve(modelName + "." + modelFileExtension)
    }

    override protected getChangePropagationSpecifications() {
        return #[new Pcm2SysDecompChangePropagationSpecification()]
    }

    // === creators ===
    protected def void createRepository(String repositoryName) {
        changePcmView[
            val repository = RepositoryFactory.eINSTANCE.createRepository()
            repository.entityName = repositoryName
            registerRoot(repository, getProjectModelPath(repository.entityName, PCM_REPOSITORY_FILE_EXTENSION).uri)
        ]
    }

    protected def void createSystem(String systemName) {
        changePcmView[
            val system = SystemFactory.eINSTANCE.createSystem()
            system.entityName = systemName
            registerRoot(system, getProjectModelPath(system.entityName, PCM_SYSTEM_FILE_EXTENSION).uri)
        ]
    }

    // === assertions ===
    protected def void assertConfigurationExists(View view, String expectedId) {
        val configurations = SysDecompQueryUtil.getSysDecompConfigurations(view)
        assertNotNull(configurations.findFirst[
            val idFeature = eClass.getEStructuralFeature("id")
            idFeature !== null && eGet(idFeature).toString == expectedId
        ], "Configuration with id '" + expectedId + "' should exist")
    }

    protected def void assertConfigurationHasComponents(View view, String configId, int expectedComponentCount) {
        val config = SysDecompQueryUtil.claimConfiguration(view, configId)
        val components = SysDecompQueryUtil.getConfigurationComponents(config)
        assertEquals(expectedComponentCount, components.size(), 
                    "Configuration should have " + expectedComponentCount + " components")
    }

    protected def void assertConfigurationMass(View view, String configId, double expectedMass, double delta) {
        val config = SysDecompQueryUtil.claimConfiguration(view, configId)
        val actualMass = SysDecompQueryUtil.getConfigurationMass(config)
        assertEquals(expectedMass, actualMass, delta, 
                    "Configuration mass should be " + expectedMass + " kg")
    }
    
    protected def void assertComponentExists(View view, String configId, String componentName) {
    val config = SysDecompQueryUtil.claimConfiguration(view, configId)
    val components = SysDecompQueryUtil.getConfigurationComponents(config)
    val matchingComponent = components.findFirst[ comp |
        val nameFeature = comp.eClass.getEStructuralFeature("name")
        nameFeature !== null && comp.eGet(nameFeature).toString == componentName
    ]
    assertNotNull(matchingComponent, 
        "Component '" + componentName + "' should exist in configuration '" + configId + "'")
}

protected def void assertComponentType(View view, String configId, String componentName, String expectedType) {
    val config = SysDecompQueryUtil.claimConfiguration(view, configId)
    val components = SysDecompQueryUtil.getConfigurationComponents(config)
    val component = components.findFirst[ comp |
        val nameFeature = comp.eClass.getEStructuralFeature("name")
        nameFeature !== null && comp.eGet(nameFeature).toString == componentName
    ]
    assertNotNull(component, "Component '" + componentName + "' should exist")
    
    val typeFeature = component.eClass.getEStructuralFeature("type")
    if (typeFeature !== null) {
        val actualType = component.eGet(typeFeature).toString
        assertEquals(expectedType, actualType, 
            "Component '" + componentName + "' should have type '" + expectedType + "'")
    } else {
        fail("Component '" + componentName + "' has no 'type' attribute")
    }
}

protected def void assertComponentMass(View view, String configId, String componentName, double expectedMass, double delta) {
    val config = SysDecompQueryUtil.claimConfiguration(view, configId)
    val components = SysDecompQueryUtil.getConfigurationComponents(config)
    val component = components.findFirst[ comp |
        val nameFeature = comp.eClass.getEStructuralFeature("name")
        nameFeature !== null && comp.eGet(nameFeature).toString == componentName
    ]
    assertNotNull(component, "Component '" + componentName + "' should exist")
    
    val massFeature = component.eClass.getEStructuralFeature("mass_kg")
    if (massFeature !== null) {
        val actualMass = (component.eGet(massFeature) as Number).doubleValue
        assertEquals(expectedMass, actualMass, delta, 
            "Component '" + componentName + "' mass should be " + expectedMass + " kg")
    } else {
        fail("Component '" + componentName + "' has no 'mass_kg' attribute")
    }
}
}
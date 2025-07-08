package tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition

import edu.kit.ipd.sdq.activextendannotations.Utility

import org.eclipse.emf.ecore.EObject
import org.palladiosimulator.pcm.core.composition.AssemblyContext
import org.palladiosimulator.pcm.core.composition.CompositionFactory
import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.pcm.repository.CompositeComponent
import org.palladiosimulator.pcm.repository.Repository
import org.palladiosimulator.pcm.repository.RepositoryComponent
import org.palladiosimulator.pcm.repository.RepositoryFactory
import org.palladiosimulator.pcm.system.System
import tools.vitruv.framework.views.View


import static extension edu.kit.ipd.sdq.commons.util.java.lang.IterableUtil.claimOne

@Utility
class Pcm2SysDecompTestUtils {
    // Test constants
    public static val REPOSITORY_NAME = "TestRepository"
    public static val SYSTEM_NAME = "TestSystem"
    public static val BASIC_COMPONENT_NAME = "TestBasicComponent"
    public static val ASSEMBLY_CONTEXT_NAME = "TestAssemblyContext"
    
    // Expected values based on your reactions
    public static val BASE_COMPONENT_MASS = 1.0
    public static val BASE_SYSTEM_MASS = 2.0
    public static val ASSEMBLY_CONTEXT_MASS = 1.5
    
    public static val RENAME_SUFFIX = "Renamed"
    public static val MASS_DELTA = 0.001
}

@Utility
class PcmCreatorsUtil {
    static def BasicComponent createBasicComponent(String name) {
        val component = RepositoryFactory.eINSTANCE.createBasicComponent()
        component.entityName = name
        return component
    }

    static def CompositeComponent createCompositeComponent(String name) {
        val component = RepositoryFactory.eINSTANCE.createCompositeComponent()
        component.entityName = name
        return component
    }

    static def AssemblyContext createAssemblyContext(RepositoryComponent component, String name) {
        val ac = CompositionFactory.eINSTANCE.createAssemblyContext()
        ac.entityName = name
        ac.encapsulatedComponent__AssemblyContext = component
        return ac
    }
}

@Utility
class PcmQueryUtil {
    static def Repository claimSinglePcmRepository(View view) {
        view.getRootObjects(Repository).claimOne
    }

    static def System claimSinglePcmSystem(View view) {
        view.getRootObjects(System).claimOne
    }

    static def RepositoryComponent claimComponent(Repository repository, String componentName) {
        repository.components__Repository.filter[it.entityName == componentName].claimOne
    }
}

@Utility
class SysDecompQueryUtil {
    static def getSysDecompConfigurations(View view) {
        view.rootObjects.filter(EObject).filter[
            eClass.name == "Configuration"
        ]
    }
    
    static def getSysDecompComponents(View view) {
        view.rootObjects.filter(EObject).filter[
            eClass.name == "Component"
        ]
    }
    
    static def claimConfiguration(View view, String configId) {
        view.getSysDecompConfigurations().findFirst[
            val idFeature = eClass.getEStructuralFeature("id")
            idFeature !== null && eGet(idFeature).toString == configId
        ]
    }
    
    static def getConfigurationMass(EObject config) {
        val massFeature = config.eClass.getEStructuralFeature("mass_kg")
        if (massFeature !== null) {
            config.eGet(massFeature) as Double
        } else {
            0.0
        }
    }
    
    static def getConfigurationComponents(EObject config) {
        val componentsFeature = config.eClass.getEStructuralFeature("components")
        if (componentsFeature !== null) {
            config.eGet(componentsFeature) as java.util.List<EObject>
        } else {
            #[]
        }
    }
}
package tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.repository

import tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.Pcm2SysDecompTransformationTest
import tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.Pcm2SysDecompTestUtils



import org.junit.jupiter.api.Test

import static tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.PcmCreatorsUtil.createBasicComponent
import static tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.PcmQueryUtil.claimSinglePcmRepository
import tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.PcmQueryUtil

class ComponentTransformationTest extends Pcm2SysDecompTransformationTest {

    @Test
    def void testCreateBasicComponent() {
        // Arrange
        createRepository(Pcm2SysDecompTestUtils.REPOSITORY_NAME)

        // Act: Add a BasicComponent to the Repository
        changePcmView [
            claimSinglePcmRepository(it) => [
                components__Repository += createBasicComponent(Pcm2SysDecompTestUtils.BASIC_COMPONENT_NAME)
            ]
        ]

        // Assert: Component should be created in SysDecomp
        validateSysDecompView [
            //printAllSysDecompObjects(it) // Debug output
            
            assertConfigurationHasComponents(it, Pcm2SysDecompTestUtils.REPOSITORY_NAME, 1)
            assertComponentExists(it, Pcm2SysDecompTestUtils.REPOSITORY_NAME, Pcm2SysDecompTestUtils.BASIC_COMPONENT_NAME)
            assertComponentType(it, Pcm2SysDecompTestUtils.REPOSITORY_NAME, Pcm2SysDecompTestUtils.BASIC_COMPONENT_NAME, "RepositoryComponent")
            assertComponentMass(it, Pcm2SysDecompTestUtils.REPOSITORY_NAME, Pcm2SysDecompTestUtils.BASIC_COMPONENT_NAME, 
                              Pcm2SysDecompTestUtils.BASE_COMPONENT_MASS, Pcm2SysDecompTestUtils.MASS_DELTA)
        ]
        
        println("✅ BasicComponent creation test passed")
    }
    
    @Test
    def void testComponentRename() {
        // Arrange
        createRepository(Pcm2SysDecompTestUtils.REPOSITORY_NAME)
        changePcmView [
            claimSinglePcmRepository(it) => [
                components__Repository += createBasicComponent(Pcm2SysDecompTestUtils.BASIC_COMPONENT_NAME)
            ]
        ]
        
        // Act: Rename the component
        val newComponentName = Pcm2SysDecompTestUtils.BASIC_COMPONENT_NAME + Pcm2SysDecompTestUtils.RENAME_SUFFIX
        changePcmView [
            val repository = claimSinglePcmRepository(it)
            val component = PcmQueryUtil.claimComponent(repository, Pcm2SysDecompTestUtils.BASIC_COMPONENT_NAME)
            component.entityName = newComponentName
        ]
        
        // Assert: SysDecomp Component should be renamed
        validateSysDecompView [
            //printAllSysDecompObjects(it) // Debug output
            
            assertComponentExists(it, Pcm2SysDecompTestUtils.REPOSITORY_NAME, newComponentName)
            // Component with old name should not exist (depends on your reactions logic)
        ]
        
        println("✅ Component rename test passed")
    }
    
    @Test
    def void testRemoveComponent() {
        // Arrange
        createRepository(Pcm2SysDecompTestUtils.REPOSITORY_NAME)
        changePcmView [
            claimSinglePcmRepository(it) => [
                components__Repository += createBasicComponent(Pcm2SysDecompTestUtils.BASIC_COMPONENT_NAME)
            ]
        ]
        
        // Act: Remove the component
        changePcmView [
            val repository = claimSinglePcmRepository(it)
            val component = PcmQueryUtil.claimComponent(repository, Pcm2SysDecompTestUtils.BASIC_COMPONENT_NAME)
            repository.components__Repository.remove(component)
        ]
        
        // Assert: SysDecomp Component should be removed
        validateSysDecompView [
            //printAllSysDecompObjects(it) // Debug output
            
            assertConfigurationHasComponents(it, Pcm2SysDecompTestUtils.REPOSITORY_NAME, 0)
        ]
        
        println("✅ Component removal test passed")
    }
}
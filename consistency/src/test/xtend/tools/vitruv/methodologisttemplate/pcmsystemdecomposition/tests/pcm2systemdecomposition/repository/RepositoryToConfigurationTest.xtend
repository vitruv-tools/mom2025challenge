package tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.repository

import org.junit.jupiter.api.Test
import tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.Pcm2SysDecompTestUtils
import tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.Pcm2SysDecompTransformationTest
import tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition.PcmQueryUtil

class RepositoryToConfigurationTest extends Pcm2SysDecompTransformationTest {


	@Test
    def void testCreateRepository() {
        // Act: Create a PCM Repository
        createRepository(Pcm2SysDecompTestUtils.REPOSITORY_NAME)

        // Assert: Check that Configuration was created
        validateSysDecompView [
            //printAllSysDecompObjects(it) // Debug output
            
            assertConfigurationExists(it, Pcm2SysDecompTestUtils.REPOSITORY_NAME)
            assertConfigurationMass(it, Pcm2SysDecompTestUtils.REPOSITORY_NAME, 0.0, 
                                  Pcm2SysDecompTestUtils.MASS_DELTA)
            assertConfigurationHasComponents(it, Pcm2SysDecompTestUtils.REPOSITORY_NAME, 0)
        ]
        
        println("✅ Repository creation test passed")
    }
    
    @Test
    def void testRepositoryRename() {
        // Arrange
        createRepository(Pcm2SysDecompTestUtils.REPOSITORY_NAME)
        
        // Act: Rename the repository
        val newName = Pcm2SysDecompTestUtils.REPOSITORY_NAME + Pcm2SysDecompTestUtils.RENAME_SUFFIX
        changePcmView [
            val repository = PcmQueryUtil.claimSinglePcmRepository(it)
            repository.entityName = newName
        ]
        
        // Assert: Configuration should be renamed too
        validateSysDecompView [
            //printAllSysDecompObjects(it) // Debug output
            
            assertConfigurationExists(it, newName)
            // Old configuration should not exist anymore (depends on your reactions logic)
        ]
        
        println("✅ Repository rename test passed")
    }
}
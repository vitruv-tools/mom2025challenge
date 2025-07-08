package tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition

import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.palladiosimulator.pcm.repository.Repository
import org.palladiosimulator.pcm.system.System
import tools.vitruv.framework.views.View
import tools.vitruv.framework.testutils.view.TestViewFactory


import org.eclipse.emf.ecore.EObject
@FinalFieldsConstructor
class Pcm2SysDecompViewFactory extends TestViewFactory {

    private def View createPcmView() {
        createViewOfElements("PCM", #{Repository, System})
    }

    private def View createSysDecompView() {
        // EObject für alle SysDecomp Elemente
        createViewOfElements("SysDecomp", #{EObject})
    }

    def void changePcmView((View)=>void modelModification) {
        changeViewRecordingChanges(createPcmView, modelModification)
    }

    def void validateSysDecompView((View)=>void viewValidation) {
        validateView(createSysDecompView, viewValidation)
    }
}
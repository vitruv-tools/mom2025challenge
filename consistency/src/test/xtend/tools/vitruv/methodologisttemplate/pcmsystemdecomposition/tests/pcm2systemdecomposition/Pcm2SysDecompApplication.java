package tools.vitruv.methodologisttemplate.pcmsystemdecomposition.tests.pcm2systemdecomposition;

import java.util.HashSet;
import java.util.Set;
import tools.vitruv.framework.applications.VitruvApplication;
import tools.vitruv.change.propagation.ChangePropagationSpecification;

public class Pcm2SysDecompApplication implements VitruvApplication {

    @Override
    public Set<ChangePropagationSpecification> getChangePropagationSpecifications() {
        Set<ChangePropagationSpecification> specs = new HashSet<>();
        specs.add(new Pcm2SysDecompChangePropagationSpecification());
        return specs;
    }

    @Override
    public String getName() {
        return "PCM → System Decomposition";
    }
}
package tools.vitruv.methodologisttemplate.viewtype.adapters.systemdecomposition;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import tools.vitruv.methodologisttemplate.model.System_Decomposition.CommonSystemBase;

/**
 * ConfigurationInclusionStrategy decides which fields should be serialized, and which shouldn't
 * for Configuration objects.
 */
class ConfigurationInclusionStrategy implements ExclusionStrategy {
    /**
     * Since all model elements inherit from CommonSystemBase,
     * only serialize attributes and references defined from there.
     */
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        var declaringClass = f.getDeclaringClass();
        return !CommonSystemBase.class.isAssignableFrom(declaringClass);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
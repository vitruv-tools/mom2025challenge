package tools.vitruv.methodologisttemplate.model.systemdecomposition;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import tools.vitruv.methodologisttemplate.model.System_Decomposition.Component;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.Configuration;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.System_DecompositionFactory;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.System_DecompositionPackage;

/**
 * A deserializer/converter of JSON to Configuration objects.
 */
public class ConfigurationTypeAdapter implements JsonDeserializer<Configuration>, JsonSerializer<Configuration> {
    /**
     * Factory object to create model elements.
     */
    private static final System_DecompositionFactory FACTORY = System_DecompositionPackage.eINSTANCE.getSystem_DecompositionFactory();

    private static final String CONFIGURATION_SYSTEM_ID = "system_id";
    private static final String CONFIGURATION_DESCRIPTION = "description";
    private static final String CONFIGURATION_MASS = "calculated_total_mass_kg";
    private static final String COMPONENT_ID = "id";
    private static final String COMPONENT_NAME = "name";
    private static final String COMPONENT_TYPE = "type";
    private static final String COMPONENT_QUANTITY = "quantity";
    private static final String COMPONENT_MASS = "mass_kg_per_unit";

    @Override
    public Configuration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        // Create a new object for the Configuration.
        var object = json.getAsJsonObject();
        var configuration = FACTORY.createConfiguration();

        // Parse basic fields.
        configuration.setId(object.get(ConfigurationTypeAdapter.CONFIGURATION_SYSTEM_ID).getAsString());
        configuration.setDescription(object.get(ConfigurationTypeAdapter.CONFIGURATION_DESCRIPTION).getAsString());
        configuration.setMass_kg(object.get(ConfigurationTypeAdapter.CONFIGURATION_MASS).getAsDouble());

        // Parse component field.
        var componentArray = object.get("components").getAsJsonArray();
        var components = componentArray.asList().stream().map(this::deserializeComponent).toList();
        configuration.getComponents().addAll(components);
        return configuration;
    }

    private Component deserializeComponent(JsonElement json) {
        // Assume we get an object:
        var object = json.getAsJsonObject();
        // Call the factory, create a component.
        var component = FACTORY.createComponent();

        // Get and convert the relevant fields from object; set component fields.
        component.setId(object.get(ConfigurationTypeAdapter.COMPONENT_ID).getAsString());
        component.setName(object.get(ConfigurationTypeAdapter.COMPONENT_NAME).getAsString());
        component.setType(object.get(ConfigurationTypeAdapter.COMPONENT_TYPE).getAsString());
        component.setQuantity(object.get(ConfigurationTypeAdapter.COMPONENT_QUANTITY).getAsLong());
        component.setMass_kg(object.get(ConfigurationTypeAdapter.COMPONENT_MASS).getAsDouble());
        return component;
    }

   @Override
    public JsonElement serialize(Configuration config, Type typeOfSrc, JsonSerializationContext context) {
        // Create the main configuration object.
        var configObject = new JsonObject();
        // Add primitive fields.
        configObject.addProperty(ConfigurationTypeAdapter.CONFIGURATION_SYSTEM_ID, config.getId());
        configObject.addProperty(ConfigurationTypeAdapter.CONFIGURATION_DESCRIPTION,config.getDescription());
        configObject.addProperty(ConfigurationTypeAdapter.CONFIGURATION_MASS, config.getMass_kg());

        // Create array of components and fill it.
        var componentArray = new JsonArray();
        config.getComponents().forEach(component -> componentArray.add(serializeComponent(component)));
        configObject.add("components", componentArray);
        return configObject;
    }

    private JsonElement serializeComponent(Component component) {
        // Create the component object.
        var componentObject = new JsonObject();

        // Add fields, and return.
        componentObject.addProperty(ConfigurationTypeAdapter.COMPONENT_ID, component.getId());
        componentObject.addProperty(ConfigurationTypeAdapter.COMPONENT_NAME, component.getName());
        componentObject.addProperty(ConfigurationTypeAdapter.COMPONENT_TYPE, component.getType());
        componentObject.addProperty(ConfigurationTypeAdapter.COMPONENT_QUANTITY, component.getQuantity());
        componentObject.addProperty(ConfigurationTypeAdapter.COMPONENT_MASS, component.getMass_kg());
        return componentObject;
    }
}

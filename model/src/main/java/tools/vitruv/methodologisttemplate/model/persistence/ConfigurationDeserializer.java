package tools.vitruv.methodologisttemplate.model.persistence;

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
public class ConfigurationDeserializer implements JsonDeserializer<Configuration>, JsonSerializer<Configuration> {
    /**
     * Factory object to create model elements.
     */
    private static final System_DecompositionFactory FACTORY = System_DecompositionPackage.eINSTANCE.getSystem_DecompositionFactory();

    @Override
    public Configuration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        // Create a new object for the Configuration.
        var object = json.getAsJsonObject();
        var configuration = FACTORY.createConfiguration();

        // Parse basic fields.
        configuration.setId(object.get("system_id").getAsString());
        configuration.setDescription(object.get("description").getAsString());
        configuration.setMass_kg(object.get("calculated_total_mass_kg").getAsDouble());

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
        component.setId(object.get("id").getAsString());
        component.setName(object.get("name").getAsString());
        component.setType(object.get("type").getAsString());
        component.setQuantity(object.get("quantity").getAsLong());
        component.setMass_kg(object.get("mass_kg_per_unit").getAsDouble());
        return component;
    }

    @Override
    public JsonElement serialize(Configuration config, Type typeOfSrc, JsonSerializationContext context) {
        // Create the main configuration object.
        var configObject = new JsonObject();
        // Add primitive fields.
        configObject.addProperty("system_id", config.getId());
        configObject.addProperty("description",config.getDescription());
        configObject.addProperty("calculated_total_mass_kg", config.getMass_kg());

        // Create array of components and fill it.
        var componentArray = new JsonArray();
        config.getComponents().forEach(component -> componentArray.add(serializeComponent(component)));
        return configObject;
    }

    private JsonElement serializeComponent(Component component) {
        // Create the component object.
        var componentObject = new JsonObject();

        // Add fields, and return.
        componentObject.addProperty("id", component.getId());
        componentObject.addProperty("name", component.getName());
        componentObject.addProperty("type", component.getType());
        componentObject.addProperty("quantity", component.getQuantity());
        componentObject.addProperty("mass_kg_per_unit", component.getMass_kg());
        return componentObject;
    }
}

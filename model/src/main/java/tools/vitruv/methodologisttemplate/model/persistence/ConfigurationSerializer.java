package tools.vitruv.methodologisttemplate.model.persistence;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import tools.vitruv.methodologisttemplate.model.System_Decomposition.Component;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.Configuration;

public class ConfigurationSerializer implements JsonSerializer<Configuration>{
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

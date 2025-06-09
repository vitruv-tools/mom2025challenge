package tools.vitruv.methodologisttemplate.model.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import tools.vitruv.methodologisttemplate.model.System_Decomposition.Configuration;

/**
 * An implementation of ResourceImpl that serializes and deserializes 
 * Configuration objects with Gson.
 */
public class JSONSystemDecompositionResource extends ResourceImpl {
    /**
     * The Gson-based parser we work with.
     */
    private final Gson parser;

    /**
     * Creates a new JSONSystemDecompositionResource.
     * 
     * @param uri - URI
     */
    public JSONSystemDecompositionResource(URI uri) {
        super(uri);
        // Set up the parser.
        parser = new GsonBuilder()
            .registerTypeHierarchyAdapter(Configuration.class, new ConfigurationTypeAdapter())
            .addSerializationExclusionStrategy(new ConfigurationInclusionStrategy())
            .create();
    }

    /**
     * Uses the parser to read an configuration and add its as sole
     * content of the resource.
     */
    @Override
    protected void doLoad(InputStream input, Map<?, ?> options) {
        var reader = new InputStreamReader(input);
        var configuration = parser.fromJson(reader, Configuration.class);

        var contents = getContents();
        contents.clear();
        contents.add(configuration);
    }

    /**
     * Uses the parser to serialize one (all) configurations.
     * 
     * @throws JsonIOException
     */
    @Override
    protected void doSave(OutputStream output, Map<?, ?> options) throws IOException {
        var writer = new OutputStreamWriter(output);
        for (var rootElement: getContents()) {
            String text = parser.toJson(rootElement);
            writer.write(text);
        }
        writer.close();
    }
}

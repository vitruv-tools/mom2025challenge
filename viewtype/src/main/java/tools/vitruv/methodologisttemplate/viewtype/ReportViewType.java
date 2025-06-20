package tools.vitruv.methodologisttemplate.viewtype;

import tools.vitruv.framework.vsum.VirtualModel;
import tools.vitruv.methodologisttemplate.model.Ontology.Component;
import tools.vitruv.methodologisttemplate.model.System_Decomposition.Configuration;
import tools.vitruv.methodologisttemplate.model.requirement_specification.Constraint;
import tools.vitruv.methodologisttemplate.model.requirement_specification.Requirement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReportViewType extends SaveableViewType {

    public static final String REPORT_CONTENT = """
            # System Verification Report - SatAlphaV1
                        
            **Date:** %s
            **Version:** 1.0
                        
            ## 1. System Configuration Summary
                        
            - **System ID:** %s
            - **Description:** %s
            - **Components List:**
            
            %s
            - **Calculated Total Mass:** %s kg
                        
            ## 2. Requirements Verification
                        
            | Requirement ID | Description                                                              | Parameter                  | Constraint | Actual Value | Unit | Status    |
            |----------------|--------------------------------------------------------------------------|----------------------------|------------|--------------|------|-----------|
            %s
                       
            *(Note: Actual value for REQ002 refers to the heaviest component, MainThruster001)*
                        
            ## 3. Conclusion
                        
            All checked requirements for the `SatAlphaV1` system configuration have been **%s** based on the current data in `system_config.json` and `requirements.csv`.\s
            
            """;

    public ReportViewType(String name) {
        super(name);
    }

    @Override
    public void save(String path, VirtualModel vsum) {
        // date, system config id, system config description,
        // component list in format - name (id): quantity unit(s) at mass_kg_per_unit kg/unit
        // - **Calculated Total Mass:** systemconfig.calculated_total_mass_kg kg
        // | REQ001         | Total system mass for SatAlphaV1 configuration must be acceptable        | calculated_total_mass_kg   | <= 350     | 340.9        | kg   | SATISFIED |

        var configuration = getDefaultView(vsum, List.of(Configuration.class)).getRootObjects(Configuration.class).iterator().next();
        var requirements = getDefaultView(vsum, List.of(Requirement.class)).getRootObjects(Requirement.class).stream().toList();
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy HH:mm:ss"));

        var req = new java.util.ArrayList<>(requirements.stream().map(it -> {
                    return "|" + it.getRequirementID()
                            + "|" + it.getDescription() + "|" + it.getConstraint().getParameter()
                            + "|" + it.getConstraint().getOperator() + "|" + it.getConstraint().getValue()
                            + "|" + it.getConstraint().getUnit() + "|" + (verifyRequirement(it, configuration) ? "SATISFIED" : "NOT SATISFIED") + "|";
                })
                .toList());
        req.sort(String::compareTo);
        var req_output = req.stream().collect(Collectors.joining(System.lineSeparator()));

        var content = REPORT_CONTENT.formatted(formattedDateTime,
                configuration.getId(), configuration.getDescription(),
                configuration.getComponents().stream().map(it -> {
                    return "\t - " + it.getName() + " (" + it.getId() + "):" + it.getQuantity() + " unit(s) at " + it.getMass_kg() + " kg/unit";
                }).collect(Collectors.joining(System.lineSeparator())),
                configuration.getMass_kg(),
                req_output,
                requirements.stream().map(it -> verifyRequirement(it, configuration)).reduce((a, b) -> a && b).get() ? "SATISFIED" : "NOT SATISFIED"
        );
        try {
            Files.write(Path.of(new File(path).getAbsolutePath()), content.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean verifyRequirement(Requirement requirement, Configuration configuration) {
        if (!requirement.getConstraint().getOperator().equals("<=")) {
            throw new UnsupportedOperationException("Operators other than <= are currently not supported!");
        }
        return switch (requirement.getConstraint().getParameter()) {
            case "calculated_total_mass_kg" ->
                    configuration.getMass_kg() <= Double.parseDouble(requirement.getConstraint().getValue());
            case "component_mass_max" ->
                    configuration.getComponents().stream().map(it -> it.getMass_kg() <= Double.parseDouble(requirement.getConstraint().getValue())).reduce((a, b) -> a && b).get();
            default -> false;
        };
    }


}

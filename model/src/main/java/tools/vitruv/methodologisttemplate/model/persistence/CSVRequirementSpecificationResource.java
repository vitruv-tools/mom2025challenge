package tools.vitruv.methodologisttemplate.model.persistence;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.csv.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import tools.vitruv.methodologisttemplate.model.requirement_specification.Constraint;
import tools.vitruv.methodologisttemplate.model.requirement_specification.Requirement;
import tools.vitruv.methodologisttemplate.model.requirement_specification.Requirement_SpecificationFactory;
import tools.vitruv.methodologisttemplate.model.requirement_specification.Requirement_SpecificationPackage;

/**
 * Read *.csv files:
 * RequirementID,Description,Parameter,Operator,Value,Unit
 */
public class CSVRequirementSpecificationResource extends ResourceImpl {

    private static final Requirement_SpecificationFactory FACTORY =
            Requirement_SpecificationPackage.eINSTANCE.getRequirement_SpecificationFactory();

    public CSVRequirementSpecificationResource(URI uri) { super(uri); }

    /* Load  */
    @Override
    protected void doLoad(InputStream in, @SuppressWarnings("rawtypes") Map options) throws IOException {
        try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withTrim()
                     .parse(reader)) {

            getContents().clear();

            for (CSVRecord record : parser) {
                Requirement req = FACTORY.createRequirement();
                req.setRequirementID(record.get("RequirementID"));
                req.setDescription(record.get("Description"));
                req.setStatus("");                     // status default empty

                Constraint c = FACTORY.createConstraint();
                c.setParameter(record.get("Parameter"));
                c.setOperator(record.get("Operator"));
                c.setValue(record.get("Value"));
                c.setUnit(record.get("Unit"));

                
                req.setConstraint(c);
                c.setRequirement(req);

                getContents().add(req);               
            }
        }
    }

    /* Save*/
    @Override
    protected void doSave(OutputStream out, @SuppressWarnings("rawtypes") Map options) throws IOException {
        try (Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             CSVPrinter printer = CSVFormat.DEFAULT
                     .withHeader("RequirementID","Description","Parameter",
                                 "Operator","Value","Unit")
                     .print(writer)) {

            for (var eObject : getContents()) {
                if (eObject instanceof Requirement req && req.getConstraint()!=null) {
                    Constraint c = req.getConstraint();
                    printer.printRecord(
                            req.getRequirementID(),
                            req.getDescription(),
                            c.getParameter(),
                            c.getOperator(),
                            c.getValue(),
                            c.getUnit());
                }
            }
        }
    }
}

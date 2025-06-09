/**
 */
package ontology;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see ontology.OntologyPackage
 * @generated
 */
public interface OntologyFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	OntologyFactory eINSTANCE = ontology.impl.OntologyFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Solar Panel Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Solar Panel Component</em>'.
	 * @generated
	 */
	SolarPanelComponent createSolarPanelComponent();

	/**
	 * Returns a new object of class '<em>Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Component</em>'.
	 * @generated
	 */
	Component createComponent();

	/**
	 * Returns a new object of class '<em>Antenna Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Antenna Component</em>'.
	 * @generated
	 */
	AntennaComponent createAntennaComponent();

	/**
	 * Returns a new object of class '<em>Thruster Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Thruster Component</em>'.
	 * @generated
	 */
	ThrusterComponent createThrusterComponent();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	OntologyPackage getOntologyPackage();

} //OntologyFactory

/**
 */
package ontology;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ontology.Component#getComponentID <em>Component ID</em>}</li>
 *   <li>{@link ontology.Component#getHasMass <em>Has Mass</em>}</li>
 *   <li>{@link ontology.Component#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see ontology.OntologyPackage#getComponent()
 * @model
 * @generated
 */
public interface Component extends EObject {
	/**
	 * Returns the value of the '<em><b>Component ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component ID</em>' attribute.
	 * @see #setComponentID(String)
	 * @see ontology.OntologyPackage#getComponent_ComponentID()
	 * @model
	 * @generated
	 */
	String getComponentID();

	/**
	 * Sets the value of the '{@link ontology.Component#getComponentID <em>Component ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Component ID</em>' attribute.
	 * @see #getComponentID()
	 * @generated
	 */
	void setComponentID(String value);

	/**
	 * Returns the value of the '<em><b>Has Mass</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Has Mass</em>' attribute.
	 * @see #setHasMass(double)
	 * @see ontology.OntologyPackage#getComponent_HasMass()
	 * @model
	 * @generated
	 */
	double getHasMass();

	/**
	 * Sets the value of the '{@link ontology.Component#getHasMass <em>Has Mass</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Has Mass</em>' attribute.
	 * @see #getHasMass()
	 * @generated
	 */
	void setHasMass(double value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see ontology.OntologyPackage#getComponent_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link ontology.Component#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // Component

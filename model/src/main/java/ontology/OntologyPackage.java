/**
 */
package ontology;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see ontology.OntologyFactory
 * @model kind="package"
 * @generated
 */
public interface OntologyPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "ontology";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.com/satellite";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ontology";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	OntologyPackage eINSTANCE = ontology.impl.OntologyPackageImpl.init();

	/**
	 * The meta object id for the '{@link ontology.impl.ComponentImpl <em>Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ontology.impl.ComponentImpl
	 * @see ontology.impl.OntologyPackageImpl#getComponent()
	 * @generated
	 */
	int COMPONENT = 1;

	/**
	 * The feature id for the '<em><b>Component ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__COMPONENT_ID = 0;

	/**
	 * The feature id for the '<em><b>Has Mass</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__HAS_MASS = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__NAME = 2;

	/**
	 * The number of structural features of the '<em>Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ontology.impl.SolarPanelComponentImpl <em>Solar Panel Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ontology.impl.SolarPanelComponentImpl
	 * @see ontology.impl.OntologyPackageImpl#getSolarPanelComponent()
	 * @generated
	 */
	int SOLAR_PANEL_COMPONENT = 0;

	/**
	 * The feature id for the '<em><b>Component ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLAR_PANEL_COMPONENT__COMPONENT_ID = COMPONENT__COMPONENT_ID;

	/**
	 * The feature id for the '<em><b>Has Mass</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLAR_PANEL_COMPONENT__HAS_MASS = COMPONENT__HAS_MASS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLAR_PANEL_COMPONENT__NAME = COMPONENT__NAME;

	/**
	 * The number of structural features of the '<em>Solar Panel Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLAR_PANEL_COMPONENT_FEATURE_COUNT = COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Solar Panel Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLAR_PANEL_COMPONENT_OPERATION_COUNT = COMPONENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ontology.impl.AntennaComponentImpl <em>Antenna Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ontology.impl.AntennaComponentImpl
	 * @see ontology.impl.OntologyPackageImpl#getAntennaComponent()
	 * @generated
	 */
	int ANTENNA_COMPONENT = 2;

	/**
	 * The feature id for the '<em><b>Component ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANTENNA_COMPONENT__COMPONENT_ID = COMPONENT__COMPONENT_ID;

	/**
	 * The feature id for the '<em><b>Has Mass</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANTENNA_COMPONENT__HAS_MASS = COMPONENT__HAS_MASS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANTENNA_COMPONENT__NAME = COMPONENT__NAME;

	/**
	 * The number of structural features of the '<em>Antenna Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANTENNA_COMPONENT_FEATURE_COUNT = COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Antenna Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANTENNA_COMPONENT_OPERATION_COUNT = COMPONENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ontology.impl.ThrusterComponentImpl <em>Thruster Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ontology.impl.ThrusterComponentImpl
	 * @see ontology.impl.OntologyPackageImpl#getThrusterComponent()
	 * @generated
	 */
	int THRUSTER_COMPONENT = 3;

	/**
	 * The feature id for the '<em><b>Component ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int THRUSTER_COMPONENT__COMPONENT_ID = COMPONENT__COMPONENT_ID;

	/**
	 * The feature id for the '<em><b>Has Mass</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int THRUSTER_COMPONENT__HAS_MASS = COMPONENT__HAS_MASS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int THRUSTER_COMPONENT__NAME = COMPONENT__NAME;

	/**
	 * The number of structural features of the '<em>Thruster Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int THRUSTER_COMPONENT_FEATURE_COUNT = COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Thruster Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int THRUSTER_COMPONENT_OPERATION_COUNT = COMPONENT_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link ontology.SolarPanelComponent <em>Solar Panel Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Solar Panel Component</em>'.
	 * @see ontology.SolarPanelComponent
	 * @generated
	 */
	EClass getSolarPanelComponent();

	/**
	 * Returns the meta object for class '{@link ontology.Component <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component</em>'.
	 * @see ontology.Component
	 * @generated
	 */
	EClass getComponent();

	/**
	 * Returns the meta object for the attribute '{@link ontology.Component#getComponentID <em>Component ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Component ID</em>'.
	 * @see ontology.Component#getComponentID()
	 * @see #getComponent()
	 * @generated
	 */
	EAttribute getComponent_ComponentID();

	/**
	 * Returns the meta object for the attribute '{@link ontology.Component#getHasMass <em>Has Mass</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Has Mass</em>'.
	 * @see ontology.Component#getHasMass()
	 * @see #getComponent()
	 * @generated
	 */
	EAttribute getComponent_HasMass();

	/**
	 * Returns the meta object for the attribute '{@link ontology.Component#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see ontology.Component#getName()
	 * @see #getComponent()
	 * @generated
	 */
	EAttribute getComponent_Name();

	/**
	 * Returns the meta object for class '{@link ontology.AntennaComponent <em>Antenna Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Antenna Component</em>'.
	 * @see ontology.AntennaComponent
	 * @generated
	 */
	EClass getAntennaComponent();

	/**
	 * Returns the meta object for class '{@link ontology.ThrusterComponent <em>Thruster Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Thruster Component</em>'.
	 * @see ontology.ThrusterComponent
	 * @generated
	 */
	EClass getThrusterComponent();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	OntologyFactory getOntologyFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link ontology.impl.SolarPanelComponentImpl <em>Solar Panel Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ontology.impl.SolarPanelComponentImpl
		 * @see ontology.impl.OntologyPackageImpl#getSolarPanelComponent()
		 * @generated
		 */
		EClass SOLAR_PANEL_COMPONENT = eINSTANCE.getSolarPanelComponent();

		/**
		 * The meta object literal for the '{@link ontology.impl.ComponentImpl <em>Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ontology.impl.ComponentImpl
		 * @see ontology.impl.OntologyPackageImpl#getComponent()
		 * @generated
		 */
		EClass COMPONENT = eINSTANCE.getComponent();

		/**
		 * The meta object literal for the '<em><b>Component ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT__COMPONENT_ID = eINSTANCE.getComponent_ComponentID();

		/**
		 * The meta object literal for the '<em><b>Has Mass</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT__HAS_MASS = eINSTANCE.getComponent_HasMass();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT__NAME = eINSTANCE.getComponent_Name();

		/**
		 * The meta object literal for the '{@link ontology.impl.AntennaComponentImpl <em>Antenna Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ontology.impl.AntennaComponentImpl
		 * @see ontology.impl.OntologyPackageImpl#getAntennaComponent()
		 * @generated
		 */
		EClass ANTENNA_COMPONENT = eINSTANCE.getAntennaComponent();

		/**
		 * The meta object literal for the '{@link ontology.impl.ThrusterComponentImpl <em>Thruster Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ontology.impl.ThrusterComponentImpl
		 * @see ontology.impl.OntologyPackageImpl#getThrusterComponent()
		 * @generated
		 */
		EClass THRUSTER_COMPONENT = eINSTANCE.getThrusterComponent();

	}

} //OntologyPackage

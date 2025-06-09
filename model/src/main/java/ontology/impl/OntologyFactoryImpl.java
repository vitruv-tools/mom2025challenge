/**
 */
package ontology.impl;

import ontology.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class OntologyFactoryImpl extends EFactoryImpl implements OntologyFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OntologyFactory init() {
		try {
			OntologyFactory theOntologyFactory = (OntologyFactory)EPackage.Registry.INSTANCE.getEFactory(OntologyPackage.eNS_URI);
			if (theOntologyFactory != null) {
				return theOntologyFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new OntologyFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OntologyFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case OntologyPackage.SOLAR_PANEL_COMPONENT: return createSolarPanelComponent();
			case OntologyPackage.COMPONENT: return createComponent();
			case OntologyPackage.ANTENNA_COMPONENT: return createAntennaComponent();
			case OntologyPackage.THRUSTER_COMPONENT: return createThrusterComponent();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SolarPanelComponent createSolarPanelComponent() {
		SolarPanelComponentImpl solarPanelComponent = new SolarPanelComponentImpl();
		return solarPanelComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Component createComponent() {
		ComponentImpl component = new ComponentImpl();
		return component;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AntennaComponent createAntennaComponent() {
		AntennaComponentImpl antennaComponent = new AntennaComponentImpl();
		return antennaComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ThrusterComponent createThrusterComponent() {
		ThrusterComponentImpl thrusterComponent = new ThrusterComponentImpl();
		return thrusterComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OntologyPackage getOntologyPackage() {
		return (OntologyPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static OntologyPackage getPackage() {
		return OntologyPackage.eINSTANCE;
	}

} //OntologyFactoryImpl

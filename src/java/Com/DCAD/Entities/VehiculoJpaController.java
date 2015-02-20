/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Com.DCAD.Entities;

import Com.DCAD.Entities.exceptions.IllegalOrphanException;
import Com.DCAD.Entities.exceptions.NonexistentEntityException;
import Com.DCAD.Entities.exceptions.PreexistingEntityException;
import Com.DCAD.Entities.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class VehiculoJpaController implements Serializable {

    public VehiculoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vehiculo vehiculo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (vehiculo.getPropietarioHasVehiculoList() == null) {
            vehiculo.setPropietarioHasVehiculoList(new ArrayList<PropietarioHasVehiculo>());
        }
        if (vehiculo.getVehiculoHasRutasList() == null) {
            vehiculo.setVehiculoHasRutasList(new ArrayList<VehiculoHasRutas>());
        }
        if (vehiculo.getVehiculoHasEstadoList() == null) {
            vehiculo.setVehiculoHasEstadoList(new ArrayList<VehiculoHasEstado>());
        }
        if (vehiculo.getConductoresHasVehiculoList() == null) {
            vehiculo.setConductoresHasVehiculoList(new ArrayList<ConductoresHasVehiculo>());
        }
        if (vehiculo.getSociosList() == null) {
            vehiculo.setSociosList(new ArrayList<Socios>());
        }
        if (vehiculo.getAseguradoraList() == null) {
            vehiculo.setAseguradoraList(new ArrayList<Aseguradora>());
        }
        if (vehiculo.getVehiculoHasCentroDiagnosticoList() == null) {
            vehiculo.setVehiculoHasCentroDiagnosticoList(new ArrayList<VehiculoHasCentroDiagnostico>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PropietarioHasVehiculo> attachedPropietarioHasVehiculoList = new ArrayList<PropietarioHasVehiculo>();
            for (PropietarioHasVehiculo propietarioHasVehiculoListPropietarioHasVehiculoToAttach : vehiculo.getPropietarioHasVehiculoList()) {
                propietarioHasVehiculoListPropietarioHasVehiculoToAttach = em.getReference(propietarioHasVehiculoListPropietarioHasVehiculoToAttach.getClass(), propietarioHasVehiculoListPropietarioHasVehiculoToAttach.getIdPropietarioVehiculo());
                attachedPropietarioHasVehiculoList.add(propietarioHasVehiculoListPropietarioHasVehiculoToAttach);
            }
            vehiculo.setPropietarioHasVehiculoList(attachedPropietarioHasVehiculoList);
            List<VehiculoHasRutas> attachedVehiculoHasRutasList = new ArrayList<VehiculoHasRutas>();
            for (VehiculoHasRutas vehiculoHasRutasListVehiculoHasRutasToAttach : vehiculo.getVehiculoHasRutasList()) {
                vehiculoHasRutasListVehiculoHasRutasToAttach = em.getReference(vehiculoHasRutasListVehiculoHasRutasToAttach.getClass(), vehiculoHasRutasListVehiculoHasRutasToAttach.getIdVehiculosRuta());
                attachedVehiculoHasRutasList.add(vehiculoHasRutasListVehiculoHasRutasToAttach);
            }
            vehiculo.setVehiculoHasRutasList(attachedVehiculoHasRutasList);
            List<VehiculoHasEstado> attachedVehiculoHasEstadoList = new ArrayList<VehiculoHasEstado>();
            for (VehiculoHasEstado vehiculoHasEstadoListVehiculoHasEstadoToAttach : vehiculo.getVehiculoHasEstadoList()) {
                vehiculoHasEstadoListVehiculoHasEstadoToAttach = em.getReference(vehiculoHasEstadoListVehiculoHasEstadoToAttach.getClass(), vehiculoHasEstadoListVehiculoHasEstadoToAttach.getIdEstadoVehiculo());
                attachedVehiculoHasEstadoList.add(vehiculoHasEstadoListVehiculoHasEstadoToAttach);
            }
            vehiculo.setVehiculoHasEstadoList(attachedVehiculoHasEstadoList);
            List<ConductoresHasVehiculo> attachedConductoresHasVehiculoList = new ArrayList<ConductoresHasVehiculo>();
            for (ConductoresHasVehiculo conductoresHasVehiculoListConductoresHasVehiculoToAttach : vehiculo.getConductoresHasVehiculoList()) {
                conductoresHasVehiculoListConductoresHasVehiculoToAttach = em.getReference(conductoresHasVehiculoListConductoresHasVehiculoToAttach.getClass(), conductoresHasVehiculoListConductoresHasVehiculoToAttach.getIdConductoresVehiculocol());
                attachedConductoresHasVehiculoList.add(conductoresHasVehiculoListConductoresHasVehiculoToAttach);
            }
            vehiculo.setConductoresHasVehiculoList(attachedConductoresHasVehiculoList);
            List<Socios> attachedSociosList = new ArrayList<Socios>();
            for (Socios sociosListSociosToAttach : vehiculo.getSociosList()) {
                sociosListSociosToAttach = em.getReference(sociosListSociosToAttach.getClass(), sociosListSociosToAttach.getCodigoSocio());
                attachedSociosList.add(sociosListSociosToAttach);
            }
            vehiculo.setSociosList(attachedSociosList);
            List<Aseguradora> attachedAseguradoraList = new ArrayList<Aseguradora>();
            for (Aseguradora aseguradoraListAseguradoraToAttach : vehiculo.getAseguradoraList()) {
                aseguradoraListAseguradoraToAttach = em.getReference(aseguradoraListAseguradoraToAttach.getClass(), aseguradoraListAseguradoraToAttach.getCodAsegurador());
                attachedAseguradoraList.add(aseguradoraListAseguradoraToAttach);
            }
            vehiculo.setAseguradoraList(attachedAseguradoraList);
            List<VehiculoHasCentroDiagnostico> attachedVehiculoHasCentroDiagnosticoList = new ArrayList<VehiculoHasCentroDiagnostico>();
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnosticoToAttach : vehiculo.getVehiculoHasCentroDiagnosticoList()) {
                vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnosticoToAttach = em.getReference(vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnosticoToAttach.getClass(), vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnosticoToAttach.getIdVehiculohasCentro());
                attachedVehiculoHasCentroDiagnosticoList.add(vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnosticoToAttach);
            }
            vehiculo.setVehiculoHasCentroDiagnosticoList(attachedVehiculoHasCentroDiagnosticoList);
            em.persist(vehiculo);
            for (PropietarioHasVehiculo propietarioHasVehiculoListPropietarioHasVehiculo : vehiculo.getPropietarioHasVehiculoList()) {
                Vehiculo oldVehiculoPlacaOfPropietarioHasVehiculoListPropietarioHasVehiculo = propietarioHasVehiculoListPropietarioHasVehiculo.getVehiculoPlaca();
                propietarioHasVehiculoListPropietarioHasVehiculo.setVehiculoPlaca(vehiculo);
                propietarioHasVehiculoListPropietarioHasVehiculo = em.merge(propietarioHasVehiculoListPropietarioHasVehiculo);
                if (oldVehiculoPlacaOfPropietarioHasVehiculoListPropietarioHasVehiculo != null) {
                    oldVehiculoPlacaOfPropietarioHasVehiculoListPropietarioHasVehiculo.getPropietarioHasVehiculoList().remove(propietarioHasVehiculoListPropietarioHasVehiculo);
                    oldVehiculoPlacaOfPropietarioHasVehiculoListPropietarioHasVehiculo = em.merge(oldVehiculoPlacaOfPropietarioHasVehiculoListPropietarioHasVehiculo);
                }
            }
            for (VehiculoHasRutas vehiculoHasRutasListVehiculoHasRutas : vehiculo.getVehiculoHasRutasList()) {
                Vehiculo oldVehiculoPlacaOfVehiculoHasRutasListVehiculoHasRutas = vehiculoHasRutasListVehiculoHasRutas.getVehiculoPlaca();
                vehiculoHasRutasListVehiculoHasRutas.setVehiculoPlaca(vehiculo);
                vehiculoHasRutasListVehiculoHasRutas = em.merge(vehiculoHasRutasListVehiculoHasRutas);
                if (oldVehiculoPlacaOfVehiculoHasRutasListVehiculoHasRutas != null) {
                    oldVehiculoPlacaOfVehiculoHasRutasListVehiculoHasRutas.getVehiculoHasRutasList().remove(vehiculoHasRutasListVehiculoHasRutas);
                    oldVehiculoPlacaOfVehiculoHasRutasListVehiculoHasRutas = em.merge(oldVehiculoPlacaOfVehiculoHasRutasListVehiculoHasRutas);
                }
            }
            for (VehiculoHasEstado vehiculoHasEstadoListVehiculoHasEstado : vehiculo.getVehiculoHasEstadoList()) {
                Vehiculo oldVehiculoPlacaOfVehiculoHasEstadoListVehiculoHasEstado = vehiculoHasEstadoListVehiculoHasEstado.getVehiculoPlaca();
                vehiculoHasEstadoListVehiculoHasEstado.setVehiculoPlaca(vehiculo);
                vehiculoHasEstadoListVehiculoHasEstado = em.merge(vehiculoHasEstadoListVehiculoHasEstado);
                if (oldVehiculoPlacaOfVehiculoHasEstadoListVehiculoHasEstado != null) {
                    oldVehiculoPlacaOfVehiculoHasEstadoListVehiculoHasEstado.getVehiculoHasEstadoList().remove(vehiculoHasEstadoListVehiculoHasEstado);
                    oldVehiculoPlacaOfVehiculoHasEstadoListVehiculoHasEstado = em.merge(oldVehiculoPlacaOfVehiculoHasEstadoListVehiculoHasEstado);
                }
            }
            for (ConductoresHasVehiculo conductoresHasVehiculoListConductoresHasVehiculo : vehiculo.getConductoresHasVehiculoList()) {
                Vehiculo oldVehiculoPlacaOfConductoresHasVehiculoListConductoresHasVehiculo = conductoresHasVehiculoListConductoresHasVehiculo.getVehiculoPlaca();
                conductoresHasVehiculoListConductoresHasVehiculo.setVehiculoPlaca(vehiculo);
                conductoresHasVehiculoListConductoresHasVehiculo = em.merge(conductoresHasVehiculoListConductoresHasVehiculo);
                if (oldVehiculoPlacaOfConductoresHasVehiculoListConductoresHasVehiculo != null) {
                    oldVehiculoPlacaOfConductoresHasVehiculoListConductoresHasVehiculo.getConductoresHasVehiculoList().remove(conductoresHasVehiculoListConductoresHasVehiculo);
                    oldVehiculoPlacaOfConductoresHasVehiculoListConductoresHasVehiculo = em.merge(oldVehiculoPlacaOfConductoresHasVehiculoListConductoresHasVehiculo);
                }
            }
            for (Socios sociosListSocios : vehiculo.getSociosList()) {
                Vehiculo oldVehiculoPlacaOfSociosListSocios = sociosListSocios.getVehiculoPlaca();
                sociosListSocios.setVehiculoPlaca(vehiculo);
                sociosListSocios = em.merge(sociosListSocios);
                if (oldVehiculoPlacaOfSociosListSocios != null) {
                    oldVehiculoPlacaOfSociosListSocios.getSociosList().remove(sociosListSocios);
                    oldVehiculoPlacaOfSociosListSocios = em.merge(oldVehiculoPlacaOfSociosListSocios);
                }
            }
            for (Aseguradora aseguradoraListAseguradora : vehiculo.getAseguradoraList()) {
                Vehiculo oldVehiculoPlacaOfAseguradoraListAseguradora = aseguradoraListAseguradora.getVehiculoPlaca();
                aseguradoraListAseguradora.setVehiculoPlaca(vehiculo);
                aseguradoraListAseguradora = em.merge(aseguradoraListAseguradora);
                if (oldVehiculoPlacaOfAseguradoraListAseguradora != null) {
                    oldVehiculoPlacaOfAseguradoraListAseguradora.getAseguradoraList().remove(aseguradoraListAseguradora);
                    oldVehiculoPlacaOfAseguradoraListAseguradora = em.merge(oldVehiculoPlacaOfAseguradoraListAseguradora);
                }
            }
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico : vehiculo.getVehiculoHasCentroDiagnosticoList()) {
                Vehiculo oldVehiculoPlacaOfVehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico = vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico.getVehiculoPlaca();
                vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico.setVehiculoPlaca(vehiculo);
                vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico = em.merge(vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico);
                if (oldVehiculoPlacaOfVehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico != null) {
                    oldVehiculoPlacaOfVehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico.getVehiculoHasCentroDiagnosticoList().remove(vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico);
                    oldVehiculoPlacaOfVehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico = em.merge(oldVehiculoPlacaOfVehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVehiculo(vehiculo.getPlaca()) != null) {
                throw new PreexistingEntityException("Vehiculo " + vehiculo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vehiculo vehiculo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Vehiculo persistentVehiculo = em.find(Vehiculo.class, vehiculo.getPlaca());
            List<PropietarioHasVehiculo> propietarioHasVehiculoListOld = persistentVehiculo.getPropietarioHasVehiculoList();
            List<PropietarioHasVehiculo> propietarioHasVehiculoListNew = vehiculo.getPropietarioHasVehiculoList();
            List<VehiculoHasRutas> vehiculoHasRutasListOld = persistentVehiculo.getVehiculoHasRutasList();
            List<VehiculoHasRutas> vehiculoHasRutasListNew = vehiculo.getVehiculoHasRutasList();
            List<VehiculoHasEstado> vehiculoHasEstadoListOld = persistentVehiculo.getVehiculoHasEstadoList();
            List<VehiculoHasEstado> vehiculoHasEstadoListNew = vehiculo.getVehiculoHasEstadoList();
            List<ConductoresHasVehiculo> conductoresHasVehiculoListOld = persistentVehiculo.getConductoresHasVehiculoList();
            List<ConductoresHasVehiculo> conductoresHasVehiculoListNew = vehiculo.getConductoresHasVehiculoList();
            List<Socios> sociosListOld = persistentVehiculo.getSociosList();
            List<Socios> sociosListNew = vehiculo.getSociosList();
            List<Aseguradora> aseguradoraListOld = persistentVehiculo.getAseguradoraList();
            List<Aseguradora> aseguradoraListNew = vehiculo.getAseguradoraList();
            List<VehiculoHasCentroDiagnostico> vehiculoHasCentroDiagnosticoListOld = persistentVehiculo.getVehiculoHasCentroDiagnosticoList();
            List<VehiculoHasCentroDiagnostico> vehiculoHasCentroDiagnosticoListNew = vehiculo.getVehiculoHasCentroDiagnosticoList();
            List<String> illegalOrphanMessages = null;
            for (PropietarioHasVehiculo propietarioHasVehiculoListOldPropietarioHasVehiculo : propietarioHasVehiculoListOld) {
                if (!propietarioHasVehiculoListNew.contains(propietarioHasVehiculoListOldPropietarioHasVehiculo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PropietarioHasVehiculo " + propietarioHasVehiculoListOldPropietarioHasVehiculo + " since its vehiculoPlaca field is not nullable.");
                }
            }
            for (VehiculoHasRutas vehiculoHasRutasListOldVehiculoHasRutas : vehiculoHasRutasListOld) {
                if (!vehiculoHasRutasListNew.contains(vehiculoHasRutasListOldVehiculoHasRutas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VehiculoHasRutas " + vehiculoHasRutasListOldVehiculoHasRutas + " since its vehiculoPlaca field is not nullable.");
                }
            }
            for (VehiculoHasEstado vehiculoHasEstadoListOldVehiculoHasEstado : vehiculoHasEstadoListOld) {
                if (!vehiculoHasEstadoListNew.contains(vehiculoHasEstadoListOldVehiculoHasEstado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VehiculoHasEstado " + vehiculoHasEstadoListOldVehiculoHasEstado + " since its vehiculoPlaca field is not nullable.");
                }
            }
            for (ConductoresHasVehiculo conductoresHasVehiculoListOldConductoresHasVehiculo : conductoresHasVehiculoListOld) {
                if (!conductoresHasVehiculoListNew.contains(conductoresHasVehiculoListOldConductoresHasVehiculo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConductoresHasVehiculo " + conductoresHasVehiculoListOldConductoresHasVehiculo + " since its vehiculoPlaca field is not nullable.");
                }
            }
            for (Socios sociosListOldSocios : sociosListOld) {
                if (!sociosListNew.contains(sociosListOldSocios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Socios " + sociosListOldSocios + " since its vehiculoPlaca field is not nullable.");
                }
            }
            for (Aseguradora aseguradoraListOldAseguradora : aseguradoraListOld) {
                if (!aseguradoraListNew.contains(aseguradoraListOldAseguradora)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Aseguradora " + aseguradoraListOldAseguradora + " since its vehiculoPlaca field is not nullable.");
                }
            }
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListOldVehiculoHasCentroDiagnostico : vehiculoHasCentroDiagnosticoListOld) {
                if (!vehiculoHasCentroDiagnosticoListNew.contains(vehiculoHasCentroDiagnosticoListOldVehiculoHasCentroDiagnostico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VehiculoHasCentroDiagnostico " + vehiculoHasCentroDiagnosticoListOldVehiculoHasCentroDiagnostico + " since its vehiculoPlaca field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PropietarioHasVehiculo> attachedPropietarioHasVehiculoListNew = new ArrayList<PropietarioHasVehiculo>();
            for (PropietarioHasVehiculo propietarioHasVehiculoListNewPropietarioHasVehiculoToAttach : propietarioHasVehiculoListNew) {
                propietarioHasVehiculoListNewPropietarioHasVehiculoToAttach = em.getReference(propietarioHasVehiculoListNewPropietarioHasVehiculoToAttach.getClass(), propietarioHasVehiculoListNewPropietarioHasVehiculoToAttach.getIdPropietarioVehiculo());
                attachedPropietarioHasVehiculoListNew.add(propietarioHasVehiculoListNewPropietarioHasVehiculoToAttach);
            }
            propietarioHasVehiculoListNew = attachedPropietarioHasVehiculoListNew;
            vehiculo.setPropietarioHasVehiculoList(propietarioHasVehiculoListNew);
            List<VehiculoHasRutas> attachedVehiculoHasRutasListNew = new ArrayList<VehiculoHasRutas>();
            for (VehiculoHasRutas vehiculoHasRutasListNewVehiculoHasRutasToAttach : vehiculoHasRutasListNew) {
                vehiculoHasRutasListNewVehiculoHasRutasToAttach = em.getReference(vehiculoHasRutasListNewVehiculoHasRutasToAttach.getClass(), vehiculoHasRutasListNewVehiculoHasRutasToAttach.getIdVehiculosRuta());
                attachedVehiculoHasRutasListNew.add(vehiculoHasRutasListNewVehiculoHasRutasToAttach);
            }
            vehiculoHasRutasListNew = attachedVehiculoHasRutasListNew;
            vehiculo.setVehiculoHasRutasList(vehiculoHasRutasListNew);
            List<VehiculoHasEstado> attachedVehiculoHasEstadoListNew = new ArrayList<VehiculoHasEstado>();
            for (VehiculoHasEstado vehiculoHasEstadoListNewVehiculoHasEstadoToAttach : vehiculoHasEstadoListNew) {
                vehiculoHasEstadoListNewVehiculoHasEstadoToAttach = em.getReference(vehiculoHasEstadoListNewVehiculoHasEstadoToAttach.getClass(), vehiculoHasEstadoListNewVehiculoHasEstadoToAttach.getIdEstadoVehiculo());
                attachedVehiculoHasEstadoListNew.add(vehiculoHasEstadoListNewVehiculoHasEstadoToAttach);
            }
            vehiculoHasEstadoListNew = attachedVehiculoHasEstadoListNew;
            vehiculo.setVehiculoHasEstadoList(vehiculoHasEstadoListNew);
            List<ConductoresHasVehiculo> attachedConductoresHasVehiculoListNew = new ArrayList<ConductoresHasVehiculo>();
            for (ConductoresHasVehiculo conductoresHasVehiculoListNewConductoresHasVehiculoToAttach : conductoresHasVehiculoListNew) {
                conductoresHasVehiculoListNewConductoresHasVehiculoToAttach = em.getReference(conductoresHasVehiculoListNewConductoresHasVehiculoToAttach.getClass(), conductoresHasVehiculoListNewConductoresHasVehiculoToAttach.getIdConductoresVehiculocol());
                attachedConductoresHasVehiculoListNew.add(conductoresHasVehiculoListNewConductoresHasVehiculoToAttach);
            }
            conductoresHasVehiculoListNew = attachedConductoresHasVehiculoListNew;
            vehiculo.setConductoresHasVehiculoList(conductoresHasVehiculoListNew);
            List<Socios> attachedSociosListNew = new ArrayList<Socios>();
            for (Socios sociosListNewSociosToAttach : sociosListNew) {
                sociosListNewSociosToAttach = em.getReference(sociosListNewSociosToAttach.getClass(), sociosListNewSociosToAttach.getCodigoSocio());
                attachedSociosListNew.add(sociosListNewSociosToAttach);
            }
            sociosListNew = attachedSociosListNew;
            vehiculo.setSociosList(sociosListNew);
            List<Aseguradora> attachedAseguradoraListNew = new ArrayList<Aseguradora>();
            for (Aseguradora aseguradoraListNewAseguradoraToAttach : aseguradoraListNew) {
                aseguradoraListNewAseguradoraToAttach = em.getReference(aseguradoraListNewAseguradoraToAttach.getClass(), aseguradoraListNewAseguradoraToAttach.getCodAsegurador());
                attachedAseguradoraListNew.add(aseguradoraListNewAseguradoraToAttach);
            }
            aseguradoraListNew = attachedAseguradoraListNew;
            vehiculo.setAseguradoraList(aseguradoraListNew);
            List<VehiculoHasCentroDiagnostico> attachedVehiculoHasCentroDiagnosticoListNew = new ArrayList<VehiculoHasCentroDiagnostico>();
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnosticoToAttach : vehiculoHasCentroDiagnosticoListNew) {
                vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnosticoToAttach = em.getReference(vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnosticoToAttach.getClass(), vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnosticoToAttach.getIdVehiculohasCentro());
                attachedVehiculoHasCentroDiagnosticoListNew.add(vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnosticoToAttach);
            }
            vehiculoHasCentroDiagnosticoListNew = attachedVehiculoHasCentroDiagnosticoListNew;
            vehiculo.setVehiculoHasCentroDiagnosticoList(vehiculoHasCentroDiagnosticoListNew);
            vehiculo = em.merge(vehiculo);
            for (PropietarioHasVehiculo propietarioHasVehiculoListNewPropietarioHasVehiculo : propietarioHasVehiculoListNew) {
                if (!propietarioHasVehiculoListOld.contains(propietarioHasVehiculoListNewPropietarioHasVehiculo)) {
                    Vehiculo oldVehiculoPlacaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo = propietarioHasVehiculoListNewPropietarioHasVehiculo.getVehiculoPlaca();
                    propietarioHasVehiculoListNewPropietarioHasVehiculo.setVehiculoPlaca(vehiculo);
                    propietarioHasVehiculoListNewPropietarioHasVehiculo = em.merge(propietarioHasVehiculoListNewPropietarioHasVehiculo);
                    if (oldVehiculoPlacaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo != null && !oldVehiculoPlacaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo.equals(vehiculo)) {
                        oldVehiculoPlacaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo.getPropietarioHasVehiculoList().remove(propietarioHasVehiculoListNewPropietarioHasVehiculo);
                        oldVehiculoPlacaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo = em.merge(oldVehiculoPlacaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo);
                    }
                }
            }
            for (VehiculoHasRutas vehiculoHasRutasListNewVehiculoHasRutas : vehiculoHasRutasListNew) {
                if (!vehiculoHasRutasListOld.contains(vehiculoHasRutasListNewVehiculoHasRutas)) {
                    Vehiculo oldVehiculoPlacaOfVehiculoHasRutasListNewVehiculoHasRutas = vehiculoHasRutasListNewVehiculoHasRutas.getVehiculoPlaca();
                    vehiculoHasRutasListNewVehiculoHasRutas.setVehiculoPlaca(vehiculo);
                    vehiculoHasRutasListNewVehiculoHasRutas = em.merge(vehiculoHasRutasListNewVehiculoHasRutas);
                    if (oldVehiculoPlacaOfVehiculoHasRutasListNewVehiculoHasRutas != null && !oldVehiculoPlacaOfVehiculoHasRutasListNewVehiculoHasRutas.equals(vehiculo)) {
                        oldVehiculoPlacaOfVehiculoHasRutasListNewVehiculoHasRutas.getVehiculoHasRutasList().remove(vehiculoHasRutasListNewVehiculoHasRutas);
                        oldVehiculoPlacaOfVehiculoHasRutasListNewVehiculoHasRutas = em.merge(oldVehiculoPlacaOfVehiculoHasRutasListNewVehiculoHasRutas);
                    }
                }
            }
            for (VehiculoHasEstado vehiculoHasEstadoListNewVehiculoHasEstado : vehiculoHasEstadoListNew) {
                if (!vehiculoHasEstadoListOld.contains(vehiculoHasEstadoListNewVehiculoHasEstado)) {
                    Vehiculo oldVehiculoPlacaOfVehiculoHasEstadoListNewVehiculoHasEstado = vehiculoHasEstadoListNewVehiculoHasEstado.getVehiculoPlaca();
                    vehiculoHasEstadoListNewVehiculoHasEstado.setVehiculoPlaca(vehiculo);
                    vehiculoHasEstadoListNewVehiculoHasEstado = em.merge(vehiculoHasEstadoListNewVehiculoHasEstado);
                    if (oldVehiculoPlacaOfVehiculoHasEstadoListNewVehiculoHasEstado != null && !oldVehiculoPlacaOfVehiculoHasEstadoListNewVehiculoHasEstado.equals(vehiculo)) {
                        oldVehiculoPlacaOfVehiculoHasEstadoListNewVehiculoHasEstado.getVehiculoHasEstadoList().remove(vehiculoHasEstadoListNewVehiculoHasEstado);
                        oldVehiculoPlacaOfVehiculoHasEstadoListNewVehiculoHasEstado = em.merge(oldVehiculoPlacaOfVehiculoHasEstadoListNewVehiculoHasEstado);
                    }
                }
            }
            for (ConductoresHasVehiculo conductoresHasVehiculoListNewConductoresHasVehiculo : conductoresHasVehiculoListNew) {
                if (!conductoresHasVehiculoListOld.contains(conductoresHasVehiculoListNewConductoresHasVehiculo)) {
                    Vehiculo oldVehiculoPlacaOfConductoresHasVehiculoListNewConductoresHasVehiculo = conductoresHasVehiculoListNewConductoresHasVehiculo.getVehiculoPlaca();
                    conductoresHasVehiculoListNewConductoresHasVehiculo.setVehiculoPlaca(vehiculo);
                    conductoresHasVehiculoListNewConductoresHasVehiculo = em.merge(conductoresHasVehiculoListNewConductoresHasVehiculo);
                    if (oldVehiculoPlacaOfConductoresHasVehiculoListNewConductoresHasVehiculo != null && !oldVehiculoPlacaOfConductoresHasVehiculoListNewConductoresHasVehiculo.equals(vehiculo)) {
                        oldVehiculoPlacaOfConductoresHasVehiculoListNewConductoresHasVehiculo.getConductoresHasVehiculoList().remove(conductoresHasVehiculoListNewConductoresHasVehiculo);
                        oldVehiculoPlacaOfConductoresHasVehiculoListNewConductoresHasVehiculo = em.merge(oldVehiculoPlacaOfConductoresHasVehiculoListNewConductoresHasVehiculo);
                    }
                }
            }
            for (Socios sociosListNewSocios : sociosListNew) {
                if (!sociosListOld.contains(sociosListNewSocios)) {
                    Vehiculo oldVehiculoPlacaOfSociosListNewSocios = sociosListNewSocios.getVehiculoPlaca();
                    sociosListNewSocios.setVehiculoPlaca(vehiculo);
                    sociosListNewSocios = em.merge(sociosListNewSocios);
                    if (oldVehiculoPlacaOfSociosListNewSocios != null && !oldVehiculoPlacaOfSociosListNewSocios.equals(vehiculo)) {
                        oldVehiculoPlacaOfSociosListNewSocios.getSociosList().remove(sociosListNewSocios);
                        oldVehiculoPlacaOfSociosListNewSocios = em.merge(oldVehiculoPlacaOfSociosListNewSocios);
                    }
                }
            }
            for (Aseguradora aseguradoraListNewAseguradora : aseguradoraListNew) {
                if (!aseguradoraListOld.contains(aseguradoraListNewAseguradora)) {
                    Vehiculo oldVehiculoPlacaOfAseguradoraListNewAseguradora = aseguradoraListNewAseguradora.getVehiculoPlaca();
                    aseguradoraListNewAseguradora.setVehiculoPlaca(vehiculo);
                    aseguradoraListNewAseguradora = em.merge(aseguradoraListNewAseguradora);
                    if (oldVehiculoPlacaOfAseguradoraListNewAseguradora != null && !oldVehiculoPlacaOfAseguradoraListNewAseguradora.equals(vehiculo)) {
                        oldVehiculoPlacaOfAseguradoraListNewAseguradora.getAseguradoraList().remove(aseguradoraListNewAseguradora);
                        oldVehiculoPlacaOfAseguradoraListNewAseguradora = em.merge(oldVehiculoPlacaOfAseguradoraListNewAseguradora);
                    }
                }
            }
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico : vehiculoHasCentroDiagnosticoListNew) {
                if (!vehiculoHasCentroDiagnosticoListOld.contains(vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico)) {
                    Vehiculo oldVehiculoPlacaOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico = vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico.getVehiculoPlaca();
                    vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico.setVehiculoPlaca(vehiculo);
                    vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico = em.merge(vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico);
                    if (oldVehiculoPlacaOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico != null && !oldVehiculoPlacaOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico.equals(vehiculo)) {
                        oldVehiculoPlacaOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico.getVehiculoHasCentroDiagnosticoList().remove(vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico);
                        oldVehiculoPlacaOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico = em.merge(oldVehiculoPlacaOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = vehiculo.getPlaca();
                if (findVehiculo(id) == null) {
                    throw new NonexistentEntityException("The vehiculo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Vehiculo vehiculo;
            try {
                vehiculo = em.getReference(Vehiculo.class, id);
                vehiculo.getPlaca();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vehiculo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PropietarioHasVehiculo> propietarioHasVehiculoListOrphanCheck = vehiculo.getPropietarioHasVehiculoList();
            for (PropietarioHasVehiculo propietarioHasVehiculoListOrphanCheckPropietarioHasVehiculo : propietarioHasVehiculoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the PropietarioHasVehiculo " + propietarioHasVehiculoListOrphanCheckPropietarioHasVehiculo + " in its propietarioHasVehiculoList field has a non-nullable vehiculoPlaca field.");
            }
            List<VehiculoHasRutas> vehiculoHasRutasListOrphanCheck = vehiculo.getVehiculoHasRutasList();
            for (VehiculoHasRutas vehiculoHasRutasListOrphanCheckVehiculoHasRutas : vehiculoHasRutasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the VehiculoHasRutas " + vehiculoHasRutasListOrphanCheckVehiculoHasRutas + " in its vehiculoHasRutasList field has a non-nullable vehiculoPlaca field.");
            }
            List<VehiculoHasEstado> vehiculoHasEstadoListOrphanCheck = vehiculo.getVehiculoHasEstadoList();
            for (VehiculoHasEstado vehiculoHasEstadoListOrphanCheckVehiculoHasEstado : vehiculoHasEstadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the VehiculoHasEstado " + vehiculoHasEstadoListOrphanCheckVehiculoHasEstado + " in its vehiculoHasEstadoList field has a non-nullable vehiculoPlaca field.");
            }
            List<ConductoresHasVehiculo> conductoresHasVehiculoListOrphanCheck = vehiculo.getConductoresHasVehiculoList();
            for (ConductoresHasVehiculo conductoresHasVehiculoListOrphanCheckConductoresHasVehiculo : conductoresHasVehiculoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the ConductoresHasVehiculo " + conductoresHasVehiculoListOrphanCheckConductoresHasVehiculo + " in its conductoresHasVehiculoList field has a non-nullable vehiculoPlaca field.");
            }
            List<Socios> sociosListOrphanCheck = vehiculo.getSociosList();
            for (Socios sociosListOrphanCheckSocios : sociosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the Socios " + sociosListOrphanCheckSocios + " in its sociosList field has a non-nullable vehiculoPlaca field.");
            }
            List<Aseguradora> aseguradoraListOrphanCheck = vehiculo.getAseguradoraList();
            for (Aseguradora aseguradoraListOrphanCheckAseguradora : aseguradoraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the Aseguradora " + aseguradoraListOrphanCheckAseguradora + " in its aseguradoraList field has a non-nullable vehiculoPlaca field.");
            }
            List<VehiculoHasCentroDiagnostico> vehiculoHasCentroDiagnosticoListOrphanCheck = vehiculo.getVehiculoHasCentroDiagnosticoList();
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListOrphanCheckVehiculoHasCentroDiagnostico : vehiculoHasCentroDiagnosticoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the VehiculoHasCentroDiagnostico " + vehiculoHasCentroDiagnosticoListOrphanCheckVehiculoHasCentroDiagnostico + " in its vehiculoHasCentroDiagnosticoList field has a non-nullable vehiculoPlaca field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(vehiculo);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vehiculo> findVehiculoEntities() {
        return findVehiculoEntities(true, -1, -1);
    }

    public List<Vehiculo> findVehiculoEntities(int maxResults, int firstResult) {
        return findVehiculoEntities(false, maxResults, firstResult);
    }

    private List<Vehiculo> findVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vehiculo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Vehiculo findVehiculo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vehiculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getVehiculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vehiculo> rt = cq.from(Vehiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

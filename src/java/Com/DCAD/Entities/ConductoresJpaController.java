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
public class ConductoresJpaController implements Serializable {

    public ConductoresJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Conductores conductores) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (conductores.getConductoresHasVehiculoList() == null) {
            conductores.setConductoresHasVehiculoList(new ArrayList<ConductoresHasVehiculo>());
        }
        if (conductores.getLicenciaConduccionList() == null) {
            conductores.setLicenciaConduccionList(new ArrayList<LicenciaConduccion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ConductoresHasVehiculo> attachedConductoresHasVehiculoList = new ArrayList<ConductoresHasVehiculo>();
            for (ConductoresHasVehiculo conductoresHasVehiculoListConductoresHasVehiculoToAttach : conductores.getConductoresHasVehiculoList()) {
                conductoresHasVehiculoListConductoresHasVehiculoToAttach = em.getReference(conductoresHasVehiculoListConductoresHasVehiculoToAttach.getClass(), conductoresHasVehiculoListConductoresHasVehiculoToAttach.getIdConductoresVehiculocol());
                attachedConductoresHasVehiculoList.add(conductoresHasVehiculoListConductoresHasVehiculoToAttach);
            }
            conductores.setConductoresHasVehiculoList(attachedConductoresHasVehiculoList);
            List<LicenciaConduccion> attachedLicenciaConduccionList = new ArrayList<LicenciaConduccion>();
            for (LicenciaConduccion licenciaConduccionListLicenciaConduccionToAttach : conductores.getLicenciaConduccionList()) {
                licenciaConduccionListLicenciaConduccionToAttach = em.getReference(licenciaConduccionListLicenciaConduccionToAttach.getClass(), licenciaConduccionListLicenciaConduccionToAttach.getNumero());
                attachedLicenciaConduccionList.add(licenciaConduccionListLicenciaConduccionToAttach);
            }
            conductores.setLicenciaConduccionList(attachedLicenciaConduccionList);
            em.persist(conductores);
            for (ConductoresHasVehiculo conductoresHasVehiculoListConductoresHasVehiculo : conductores.getConductoresHasVehiculoList()) {
                Conductores oldConductoresCedulaOfConductoresHasVehiculoListConductoresHasVehiculo = conductoresHasVehiculoListConductoresHasVehiculo.getConductoresCedula();
                conductoresHasVehiculoListConductoresHasVehiculo.setConductoresCedula(conductores);
                conductoresHasVehiculoListConductoresHasVehiculo = em.merge(conductoresHasVehiculoListConductoresHasVehiculo);
                if (oldConductoresCedulaOfConductoresHasVehiculoListConductoresHasVehiculo != null) {
                    oldConductoresCedulaOfConductoresHasVehiculoListConductoresHasVehiculo.getConductoresHasVehiculoList().remove(conductoresHasVehiculoListConductoresHasVehiculo);
                    oldConductoresCedulaOfConductoresHasVehiculoListConductoresHasVehiculo = em.merge(oldConductoresCedulaOfConductoresHasVehiculoListConductoresHasVehiculo);
                }
            }
            for (LicenciaConduccion licenciaConduccionListLicenciaConduccion : conductores.getLicenciaConduccionList()) {
                Conductores oldConductoresCedulaOfLicenciaConduccionListLicenciaConduccion = licenciaConduccionListLicenciaConduccion.getConductoresCedula();
                licenciaConduccionListLicenciaConduccion.setConductoresCedula(conductores);
                licenciaConduccionListLicenciaConduccion = em.merge(licenciaConduccionListLicenciaConduccion);
                if (oldConductoresCedulaOfLicenciaConduccionListLicenciaConduccion != null) {
                    oldConductoresCedulaOfLicenciaConduccionListLicenciaConduccion.getLicenciaConduccionList().remove(licenciaConduccionListLicenciaConduccion);
                    oldConductoresCedulaOfLicenciaConduccionListLicenciaConduccion = em.merge(oldConductoresCedulaOfLicenciaConduccionListLicenciaConduccion);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findConductores(conductores.getCedula()) != null) {
                throw new PreexistingEntityException("Conductores " + conductores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Conductores conductores) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Conductores persistentConductores = em.find(Conductores.class, conductores.getCedula());
            List<ConductoresHasVehiculo> conductoresHasVehiculoListOld = persistentConductores.getConductoresHasVehiculoList();
            List<ConductoresHasVehiculo> conductoresHasVehiculoListNew = conductores.getConductoresHasVehiculoList();
            List<LicenciaConduccion> licenciaConduccionListOld = persistentConductores.getLicenciaConduccionList();
            List<LicenciaConduccion> licenciaConduccionListNew = conductores.getLicenciaConduccionList();
            List<String> illegalOrphanMessages = null;
            for (ConductoresHasVehiculo conductoresHasVehiculoListOldConductoresHasVehiculo : conductoresHasVehiculoListOld) {
                if (!conductoresHasVehiculoListNew.contains(conductoresHasVehiculoListOldConductoresHasVehiculo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConductoresHasVehiculo " + conductoresHasVehiculoListOldConductoresHasVehiculo + " since its conductoresCedula field is not nullable.");
                }
            }
            for (LicenciaConduccion licenciaConduccionListOldLicenciaConduccion : licenciaConduccionListOld) {
                if (!licenciaConduccionListNew.contains(licenciaConduccionListOldLicenciaConduccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LicenciaConduccion " + licenciaConduccionListOldLicenciaConduccion + " since its conductoresCedula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ConductoresHasVehiculo> attachedConductoresHasVehiculoListNew = new ArrayList<ConductoresHasVehiculo>();
            for (ConductoresHasVehiculo conductoresHasVehiculoListNewConductoresHasVehiculoToAttach : conductoresHasVehiculoListNew) {
                conductoresHasVehiculoListNewConductoresHasVehiculoToAttach = em.getReference(conductoresHasVehiculoListNewConductoresHasVehiculoToAttach.getClass(), conductoresHasVehiculoListNewConductoresHasVehiculoToAttach.getIdConductoresVehiculocol());
                attachedConductoresHasVehiculoListNew.add(conductoresHasVehiculoListNewConductoresHasVehiculoToAttach);
            }
            conductoresHasVehiculoListNew = attachedConductoresHasVehiculoListNew;
            conductores.setConductoresHasVehiculoList(conductoresHasVehiculoListNew);
            List<LicenciaConduccion> attachedLicenciaConduccionListNew = new ArrayList<LicenciaConduccion>();
            for (LicenciaConduccion licenciaConduccionListNewLicenciaConduccionToAttach : licenciaConduccionListNew) {
                licenciaConduccionListNewLicenciaConduccionToAttach = em.getReference(licenciaConduccionListNewLicenciaConduccionToAttach.getClass(), licenciaConduccionListNewLicenciaConduccionToAttach.getNumero());
                attachedLicenciaConduccionListNew.add(licenciaConduccionListNewLicenciaConduccionToAttach);
            }
            licenciaConduccionListNew = attachedLicenciaConduccionListNew;
            conductores.setLicenciaConduccionList(licenciaConduccionListNew);
            conductores = em.merge(conductores);
            for (ConductoresHasVehiculo conductoresHasVehiculoListNewConductoresHasVehiculo : conductoresHasVehiculoListNew) {
                if (!conductoresHasVehiculoListOld.contains(conductoresHasVehiculoListNewConductoresHasVehiculo)) {
                    Conductores oldConductoresCedulaOfConductoresHasVehiculoListNewConductoresHasVehiculo = conductoresHasVehiculoListNewConductoresHasVehiculo.getConductoresCedula();
                    conductoresHasVehiculoListNewConductoresHasVehiculo.setConductoresCedula(conductores);
                    conductoresHasVehiculoListNewConductoresHasVehiculo = em.merge(conductoresHasVehiculoListNewConductoresHasVehiculo);
                    if (oldConductoresCedulaOfConductoresHasVehiculoListNewConductoresHasVehiculo != null && !oldConductoresCedulaOfConductoresHasVehiculoListNewConductoresHasVehiculo.equals(conductores)) {
                        oldConductoresCedulaOfConductoresHasVehiculoListNewConductoresHasVehiculo.getConductoresHasVehiculoList().remove(conductoresHasVehiculoListNewConductoresHasVehiculo);
                        oldConductoresCedulaOfConductoresHasVehiculoListNewConductoresHasVehiculo = em.merge(oldConductoresCedulaOfConductoresHasVehiculoListNewConductoresHasVehiculo);
                    }
                }
            }
            for (LicenciaConduccion licenciaConduccionListNewLicenciaConduccion : licenciaConduccionListNew) {
                if (!licenciaConduccionListOld.contains(licenciaConduccionListNewLicenciaConduccion)) {
                    Conductores oldConductoresCedulaOfLicenciaConduccionListNewLicenciaConduccion = licenciaConduccionListNewLicenciaConduccion.getConductoresCedula();
                    licenciaConduccionListNewLicenciaConduccion.setConductoresCedula(conductores);
                    licenciaConduccionListNewLicenciaConduccion = em.merge(licenciaConduccionListNewLicenciaConduccion);
                    if (oldConductoresCedulaOfLicenciaConduccionListNewLicenciaConduccion != null && !oldConductoresCedulaOfLicenciaConduccionListNewLicenciaConduccion.equals(conductores)) {
                        oldConductoresCedulaOfLicenciaConduccionListNewLicenciaConduccion.getLicenciaConduccionList().remove(licenciaConduccionListNewLicenciaConduccion);
                        oldConductoresCedulaOfLicenciaConduccionListNewLicenciaConduccion = em.merge(oldConductoresCedulaOfLicenciaConduccionListNewLicenciaConduccion);
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
                Integer id = conductores.getCedula();
                if (findConductores(id) == null) {
                    throw new NonexistentEntityException("The conductores with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Conductores conductores;
            try {
                conductores = em.getReference(Conductores.class, id);
                conductores.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The conductores with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ConductoresHasVehiculo> conductoresHasVehiculoListOrphanCheck = conductores.getConductoresHasVehiculoList();
            for (ConductoresHasVehiculo conductoresHasVehiculoListOrphanCheckConductoresHasVehiculo : conductoresHasVehiculoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Conductores (" + conductores + ") cannot be destroyed since the ConductoresHasVehiculo " + conductoresHasVehiculoListOrphanCheckConductoresHasVehiculo + " in its conductoresHasVehiculoList field has a non-nullable conductoresCedula field.");
            }
            List<LicenciaConduccion> licenciaConduccionListOrphanCheck = conductores.getLicenciaConduccionList();
            for (LicenciaConduccion licenciaConduccionListOrphanCheckLicenciaConduccion : licenciaConduccionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Conductores (" + conductores + ") cannot be destroyed since the LicenciaConduccion " + licenciaConduccionListOrphanCheckLicenciaConduccion + " in its licenciaConduccionList field has a non-nullable conductoresCedula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(conductores);
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

    public List<Conductores> findConductoresEntities() {
        return findConductoresEntities(true, -1, -1);
    }

    public List<Conductores> findConductoresEntities(int maxResults, int firstResult) {
        return findConductoresEntities(false, maxResults, firstResult);
    }

    private List<Conductores> findConductoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Conductores.class));
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

    public Conductores findConductores(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Conductores.class, id);
        } finally {
            em.close();
        }
    }

    public int getConductoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Conductores> rt = cq.from(Conductores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

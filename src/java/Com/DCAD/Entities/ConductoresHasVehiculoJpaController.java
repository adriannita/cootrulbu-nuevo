/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Com.DCAD.Entities;

import Com.DCAD.Entities.exceptions.NonexistentEntityException;
import Com.DCAD.Entities.exceptions.PreexistingEntityException;
import Com.DCAD.Entities.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author cpe
 */
public class ConductoresHasVehiculoJpaController implements Serializable {

    public ConductoresHasVehiculoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConductoresHasVehiculo conductoresHasVehiculo) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Conductores conductoresCedula = conductoresHasVehiculo.getConductoresCedula();
            if (conductoresCedula != null) {
                conductoresCedula = em.getReference(conductoresCedula.getClass(), conductoresCedula.getCedula());
                conductoresHasVehiculo.setConductoresCedula(conductoresCedula);
            }
            Vehiculo vehiculoPlaca = conductoresHasVehiculo.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca = em.getReference(vehiculoPlaca.getClass(), vehiculoPlaca.getPlaca());
                conductoresHasVehiculo.setVehiculoPlaca(vehiculoPlaca);
            }
            em.persist(conductoresHasVehiculo);
            if (conductoresCedula != null) {
                conductoresCedula.getConductoresHasVehiculoList().add(conductoresHasVehiculo);
                conductoresCedula = em.merge(conductoresCedula);
            }
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getConductoresHasVehiculoList().add(conductoresHasVehiculo);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findConductoresHasVehiculo(conductoresHasVehiculo.getIdConductoresVehiculocol()) != null) {
                throw new PreexistingEntityException("ConductoresHasVehiculo " + conductoresHasVehiculo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConductoresHasVehiculo conductoresHasVehiculo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ConductoresHasVehiculo persistentConductoresHasVehiculo = em.find(ConductoresHasVehiculo.class, conductoresHasVehiculo.getIdConductoresVehiculocol());
            Conductores conductoresCedulaOld = persistentConductoresHasVehiculo.getConductoresCedula();
            Conductores conductoresCedulaNew = conductoresHasVehiculo.getConductoresCedula();
            Vehiculo vehiculoPlacaOld = persistentConductoresHasVehiculo.getVehiculoPlaca();
            Vehiculo vehiculoPlacaNew = conductoresHasVehiculo.getVehiculoPlaca();
            if (conductoresCedulaNew != null) {
                conductoresCedulaNew = em.getReference(conductoresCedulaNew.getClass(), conductoresCedulaNew.getCedula());
                conductoresHasVehiculo.setConductoresCedula(conductoresCedulaNew);
            }
            if (vehiculoPlacaNew != null) {
                vehiculoPlacaNew = em.getReference(vehiculoPlacaNew.getClass(), vehiculoPlacaNew.getPlaca());
                conductoresHasVehiculo.setVehiculoPlaca(vehiculoPlacaNew);
            }
            conductoresHasVehiculo = em.merge(conductoresHasVehiculo);
            if (conductoresCedulaOld != null && !conductoresCedulaOld.equals(conductoresCedulaNew)) {
                conductoresCedulaOld.getConductoresHasVehiculoList().remove(conductoresHasVehiculo);
                conductoresCedulaOld = em.merge(conductoresCedulaOld);
            }
            if (conductoresCedulaNew != null && !conductoresCedulaNew.equals(conductoresCedulaOld)) {
                conductoresCedulaNew.getConductoresHasVehiculoList().add(conductoresHasVehiculo);
                conductoresCedulaNew = em.merge(conductoresCedulaNew);
            }
            if (vehiculoPlacaOld != null && !vehiculoPlacaOld.equals(vehiculoPlacaNew)) {
                vehiculoPlacaOld.getConductoresHasVehiculoList().remove(conductoresHasVehiculo);
                vehiculoPlacaOld = em.merge(vehiculoPlacaOld);
            }
            if (vehiculoPlacaNew != null && !vehiculoPlacaNew.equals(vehiculoPlacaOld)) {
                vehiculoPlacaNew.getConductoresHasVehiculoList().add(conductoresHasVehiculo);
                vehiculoPlacaNew = em.merge(vehiculoPlacaNew);
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
                Integer id = conductoresHasVehiculo.getIdConductoresVehiculocol();
                if (findConductoresHasVehiculo(id) == null) {
                    throw new NonexistentEntityException("The conductoresHasVehiculo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ConductoresHasVehiculo conductoresHasVehiculo;
            try {
                conductoresHasVehiculo = em.getReference(ConductoresHasVehiculo.class, id);
                conductoresHasVehiculo.getIdConductoresVehiculocol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The conductoresHasVehiculo with id " + id + " no longer exists.", enfe);
            }
            Conductores conductoresCedula = conductoresHasVehiculo.getConductoresCedula();
            if (conductoresCedula != null) {
                conductoresCedula.getConductoresHasVehiculoList().remove(conductoresHasVehiculo);
                conductoresCedula = em.merge(conductoresCedula);
            }
            Vehiculo vehiculoPlaca = conductoresHasVehiculo.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getConductoresHasVehiculoList().remove(conductoresHasVehiculo);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            em.remove(conductoresHasVehiculo);
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

    public List<ConductoresHasVehiculo> findConductoresHasVehiculoEntities() {
        return findConductoresHasVehiculoEntities(true, -1, -1);
    }

    public List<ConductoresHasVehiculo> findConductoresHasVehiculoEntities(int maxResults, int firstResult) {
        return findConductoresHasVehiculoEntities(false, maxResults, firstResult);
    }

    private List<ConductoresHasVehiculo> findConductoresHasVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConductoresHasVehiculo.class));
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

    public ConductoresHasVehiculo findConductoresHasVehiculo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConductoresHasVehiculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getConductoresHasVehiculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConductoresHasVehiculo> rt = cq.from(ConductoresHasVehiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

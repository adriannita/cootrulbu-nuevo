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
public class VehiculoHasCentroDiagnosticoJpaController implements Serializable {

    public VehiculoHasCentroDiagnosticoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnostico) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CentroDiagnostico centroDiagnosticoidCentroDiagnostico = vehiculoHasCentroDiagnostico.getCentroDiagnosticoidCentroDiagnostico();
            if (centroDiagnosticoidCentroDiagnostico != null) {
                centroDiagnosticoidCentroDiagnostico = em.getReference(centroDiagnosticoidCentroDiagnostico.getClass(), centroDiagnosticoidCentroDiagnostico.getIdCentroDiagnostico());
                vehiculoHasCentroDiagnostico.setCentroDiagnosticoidCentroDiagnostico(centroDiagnosticoidCentroDiagnostico);
            }
            Vehiculo vehiculoPlaca = vehiculoHasCentroDiagnostico.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca = em.getReference(vehiculoPlaca.getClass(), vehiculoPlaca.getPlaca());
                vehiculoHasCentroDiagnostico.setVehiculoPlaca(vehiculoPlaca);
            }
            em.persist(vehiculoHasCentroDiagnostico);
            if (centroDiagnosticoidCentroDiagnostico != null) {
                centroDiagnosticoidCentroDiagnostico.getVehiculoHasCentroDiagnosticoList().add(vehiculoHasCentroDiagnostico);
                centroDiagnosticoidCentroDiagnostico = em.merge(centroDiagnosticoidCentroDiagnostico);
            }
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getVehiculoHasCentroDiagnosticoList().add(vehiculoHasCentroDiagnostico);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVehiculoHasCentroDiagnostico(vehiculoHasCentroDiagnostico.getIdVehiculohasCentro()) != null) {
                throw new PreexistingEntityException("VehiculoHasCentroDiagnostico " + vehiculoHasCentroDiagnostico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnostico) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            VehiculoHasCentroDiagnostico persistentVehiculoHasCentroDiagnostico = em.find(VehiculoHasCentroDiagnostico.class, vehiculoHasCentroDiagnostico.getIdVehiculohasCentro());
            CentroDiagnostico centroDiagnosticoidCentroDiagnosticoOld = persistentVehiculoHasCentroDiagnostico.getCentroDiagnosticoidCentroDiagnostico();
            CentroDiagnostico centroDiagnosticoidCentroDiagnosticoNew = vehiculoHasCentroDiagnostico.getCentroDiagnosticoidCentroDiagnostico();
            Vehiculo vehiculoPlacaOld = persistentVehiculoHasCentroDiagnostico.getVehiculoPlaca();
            Vehiculo vehiculoPlacaNew = vehiculoHasCentroDiagnostico.getVehiculoPlaca();
            if (centroDiagnosticoidCentroDiagnosticoNew != null) {
                centroDiagnosticoidCentroDiagnosticoNew = em.getReference(centroDiagnosticoidCentroDiagnosticoNew.getClass(), centroDiagnosticoidCentroDiagnosticoNew.getIdCentroDiagnostico());
                vehiculoHasCentroDiagnostico.setCentroDiagnosticoidCentroDiagnostico(centroDiagnosticoidCentroDiagnosticoNew);
            }
            if (vehiculoPlacaNew != null) {
                vehiculoPlacaNew = em.getReference(vehiculoPlacaNew.getClass(), vehiculoPlacaNew.getPlaca());
                vehiculoHasCentroDiagnostico.setVehiculoPlaca(vehiculoPlacaNew);
            }
            vehiculoHasCentroDiagnostico = em.merge(vehiculoHasCentroDiagnostico);
            if (centroDiagnosticoidCentroDiagnosticoOld != null && !centroDiagnosticoidCentroDiagnosticoOld.equals(centroDiagnosticoidCentroDiagnosticoNew)) {
                centroDiagnosticoidCentroDiagnosticoOld.getVehiculoHasCentroDiagnosticoList().remove(vehiculoHasCentroDiagnostico);
                centroDiagnosticoidCentroDiagnosticoOld = em.merge(centroDiagnosticoidCentroDiagnosticoOld);
            }
            if (centroDiagnosticoidCentroDiagnosticoNew != null && !centroDiagnosticoidCentroDiagnosticoNew.equals(centroDiagnosticoidCentroDiagnosticoOld)) {
                centroDiagnosticoidCentroDiagnosticoNew.getVehiculoHasCentroDiagnosticoList().add(vehiculoHasCentroDiagnostico);
                centroDiagnosticoidCentroDiagnosticoNew = em.merge(centroDiagnosticoidCentroDiagnosticoNew);
            }
            if (vehiculoPlacaOld != null && !vehiculoPlacaOld.equals(vehiculoPlacaNew)) {
                vehiculoPlacaOld.getVehiculoHasCentroDiagnosticoList().remove(vehiculoHasCentroDiagnostico);
                vehiculoPlacaOld = em.merge(vehiculoPlacaOld);
            }
            if (vehiculoPlacaNew != null && !vehiculoPlacaNew.equals(vehiculoPlacaOld)) {
                vehiculoPlacaNew.getVehiculoHasCentroDiagnosticoList().add(vehiculoHasCentroDiagnostico);
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
                Integer id = vehiculoHasCentroDiagnostico.getIdVehiculohasCentro();
                if (findVehiculoHasCentroDiagnostico(id) == null) {
                    throw new NonexistentEntityException("The vehiculoHasCentroDiagnostico with id " + id + " no longer exists.");
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
            VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnostico;
            try {
                vehiculoHasCentroDiagnostico = em.getReference(VehiculoHasCentroDiagnostico.class, id);
                vehiculoHasCentroDiagnostico.getIdVehiculohasCentro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vehiculoHasCentroDiagnostico with id " + id + " no longer exists.", enfe);
            }
            CentroDiagnostico centroDiagnosticoidCentroDiagnostico = vehiculoHasCentroDiagnostico.getCentroDiagnosticoidCentroDiagnostico();
            if (centroDiagnosticoidCentroDiagnostico != null) {
                centroDiagnosticoidCentroDiagnostico.getVehiculoHasCentroDiagnosticoList().remove(vehiculoHasCentroDiagnostico);
                centroDiagnosticoidCentroDiagnostico = em.merge(centroDiagnosticoidCentroDiagnostico);
            }
            Vehiculo vehiculoPlaca = vehiculoHasCentroDiagnostico.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getVehiculoHasCentroDiagnosticoList().remove(vehiculoHasCentroDiagnostico);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            em.remove(vehiculoHasCentroDiagnostico);
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

    public List<VehiculoHasCentroDiagnostico> findVehiculoHasCentroDiagnosticoEntities() {
        return findVehiculoHasCentroDiagnosticoEntities(true, -1, -1);
    }

    public List<VehiculoHasCentroDiagnostico> findVehiculoHasCentroDiagnosticoEntities(int maxResults, int firstResult) {
        return findVehiculoHasCentroDiagnosticoEntities(false, maxResults, firstResult);
    }

    private List<VehiculoHasCentroDiagnostico> findVehiculoHasCentroDiagnosticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VehiculoHasCentroDiagnostico.class));
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

    public VehiculoHasCentroDiagnostico findVehiculoHasCentroDiagnostico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VehiculoHasCentroDiagnostico.class, id);
        } finally {
            em.close();
        }
    }

    public int getVehiculoHasCentroDiagnosticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VehiculoHasCentroDiagnostico> rt = cq.from(VehiculoHasCentroDiagnostico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

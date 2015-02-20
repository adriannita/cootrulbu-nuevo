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
public class PropietarioHasVehiculoJpaController implements Serializable {

    public PropietarioHasVehiculoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PropietarioHasVehiculo propietarioHasVehiculo) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Propietario propietarioCedula = propietarioHasVehiculo.getPropietarioCedula();
            if (propietarioCedula != null) {
                propietarioCedula = em.getReference(propietarioCedula.getClass(), propietarioCedula.getCedula());
                propietarioHasVehiculo.setPropietarioCedula(propietarioCedula);
            }
            Vehiculo vehiculoPlaca = propietarioHasVehiculo.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca = em.getReference(vehiculoPlaca.getClass(), vehiculoPlaca.getPlaca());
                propietarioHasVehiculo.setVehiculoPlaca(vehiculoPlaca);
            }
            em.persist(propietarioHasVehiculo);
            if (propietarioCedula != null) {
                propietarioCedula.getPropietarioHasVehiculoList().add(propietarioHasVehiculo);
                propietarioCedula = em.merge(propietarioCedula);
            }
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getPropietarioHasVehiculoList().add(propietarioHasVehiculo);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPropietarioHasVehiculo(propietarioHasVehiculo.getIdPropietarioVehiculo()) != null) {
                throw new PreexistingEntityException("PropietarioHasVehiculo " + propietarioHasVehiculo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PropietarioHasVehiculo propietarioHasVehiculo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PropietarioHasVehiculo persistentPropietarioHasVehiculo = em.find(PropietarioHasVehiculo.class, propietarioHasVehiculo.getIdPropietarioVehiculo());
            Propietario propietarioCedulaOld = persistentPropietarioHasVehiculo.getPropietarioCedula();
            Propietario propietarioCedulaNew = propietarioHasVehiculo.getPropietarioCedula();
            Vehiculo vehiculoPlacaOld = persistentPropietarioHasVehiculo.getVehiculoPlaca();
            Vehiculo vehiculoPlacaNew = propietarioHasVehiculo.getVehiculoPlaca();
            if (propietarioCedulaNew != null) {
                propietarioCedulaNew = em.getReference(propietarioCedulaNew.getClass(), propietarioCedulaNew.getCedula());
                propietarioHasVehiculo.setPropietarioCedula(propietarioCedulaNew);
            }
            if (vehiculoPlacaNew != null) {
                vehiculoPlacaNew = em.getReference(vehiculoPlacaNew.getClass(), vehiculoPlacaNew.getPlaca());
                propietarioHasVehiculo.setVehiculoPlaca(vehiculoPlacaNew);
            }
            propietarioHasVehiculo = em.merge(propietarioHasVehiculo);
            if (propietarioCedulaOld != null && !propietarioCedulaOld.equals(propietarioCedulaNew)) {
                propietarioCedulaOld.getPropietarioHasVehiculoList().remove(propietarioHasVehiculo);
                propietarioCedulaOld = em.merge(propietarioCedulaOld);
            }
            if (propietarioCedulaNew != null && !propietarioCedulaNew.equals(propietarioCedulaOld)) {
                propietarioCedulaNew.getPropietarioHasVehiculoList().add(propietarioHasVehiculo);
                propietarioCedulaNew = em.merge(propietarioCedulaNew);
            }
            if (vehiculoPlacaOld != null && !vehiculoPlacaOld.equals(vehiculoPlacaNew)) {
                vehiculoPlacaOld.getPropietarioHasVehiculoList().remove(propietarioHasVehiculo);
                vehiculoPlacaOld = em.merge(vehiculoPlacaOld);
            }
            if (vehiculoPlacaNew != null && !vehiculoPlacaNew.equals(vehiculoPlacaOld)) {
                vehiculoPlacaNew.getPropietarioHasVehiculoList().add(propietarioHasVehiculo);
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
                Integer id = propietarioHasVehiculo.getIdPropietarioVehiculo();
                if (findPropietarioHasVehiculo(id) == null) {
                    throw new NonexistentEntityException("The propietarioHasVehiculo with id " + id + " no longer exists.");
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
            PropietarioHasVehiculo propietarioHasVehiculo;
            try {
                propietarioHasVehiculo = em.getReference(PropietarioHasVehiculo.class, id);
                propietarioHasVehiculo.getIdPropietarioVehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The propietarioHasVehiculo with id " + id + " no longer exists.", enfe);
            }
            Propietario propietarioCedula = propietarioHasVehiculo.getPropietarioCedula();
            if (propietarioCedula != null) {
                propietarioCedula.getPropietarioHasVehiculoList().remove(propietarioHasVehiculo);
                propietarioCedula = em.merge(propietarioCedula);
            }
            Vehiculo vehiculoPlaca = propietarioHasVehiculo.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getPropietarioHasVehiculoList().remove(propietarioHasVehiculo);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            em.remove(propietarioHasVehiculo);
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

    public List<PropietarioHasVehiculo> findPropietarioHasVehiculoEntities() {
        return findPropietarioHasVehiculoEntities(true, -1, -1);
    }

    public List<PropietarioHasVehiculo> findPropietarioHasVehiculoEntities(int maxResults, int firstResult) {
        return findPropietarioHasVehiculoEntities(false, maxResults, firstResult);
    }

    private List<PropietarioHasVehiculo> findPropietarioHasVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PropietarioHasVehiculo.class));
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

    public PropietarioHasVehiculo findPropietarioHasVehiculo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PropietarioHasVehiculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPropietarioHasVehiculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PropietarioHasVehiculo> rt = cq.from(PropietarioHasVehiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

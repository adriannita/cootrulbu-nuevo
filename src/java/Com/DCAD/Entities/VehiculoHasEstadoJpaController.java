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
public class VehiculoHasEstadoJpaController implements Serializable {

    public VehiculoHasEstadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VehiculoHasEstado vehiculoHasEstado) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Estado estadoVehiculoidEstado = vehiculoHasEstado.getEstadoVehiculoidEstado();
            if (estadoVehiculoidEstado != null) {
                estadoVehiculoidEstado = em.getReference(estadoVehiculoidEstado.getClass(), estadoVehiculoidEstado.getIdEstado());
                vehiculoHasEstado.setEstadoVehiculoidEstado(estadoVehiculoidEstado);
            }
            Vehiculo vehiculoPlaca = vehiculoHasEstado.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca = em.getReference(vehiculoPlaca.getClass(), vehiculoPlaca.getPlaca());
                vehiculoHasEstado.setVehiculoPlaca(vehiculoPlaca);
            }
            em.persist(vehiculoHasEstado);
            if (estadoVehiculoidEstado != null) {
                estadoVehiculoidEstado.getVehiculoHasEstadoList().add(vehiculoHasEstado);
                estadoVehiculoidEstado = em.merge(estadoVehiculoidEstado);
            }
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getVehiculoHasEstadoList().add(vehiculoHasEstado);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVehiculoHasEstado(vehiculoHasEstado.getIdEstadoVehiculo()) != null) {
                throw new PreexistingEntityException("VehiculoHasEstado " + vehiculoHasEstado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VehiculoHasEstado vehiculoHasEstado) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            VehiculoHasEstado persistentVehiculoHasEstado = em.find(VehiculoHasEstado.class, vehiculoHasEstado.getIdEstadoVehiculo());
            Estado estadoVehiculoidEstadoOld = persistentVehiculoHasEstado.getEstadoVehiculoidEstado();
            Estado estadoVehiculoidEstadoNew = vehiculoHasEstado.getEstadoVehiculoidEstado();
            Vehiculo vehiculoPlacaOld = persistentVehiculoHasEstado.getVehiculoPlaca();
            Vehiculo vehiculoPlacaNew = vehiculoHasEstado.getVehiculoPlaca();
            if (estadoVehiculoidEstadoNew != null) {
                estadoVehiculoidEstadoNew = em.getReference(estadoVehiculoidEstadoNew.getClass(), estadoVehiculoidEstadoNew.getIdEstado());
                vehiculoHasEstado.setEstadoVehiculoidEstado(estadoVehiculoidEstadoNew);
            }
            if (vehiculoPlacaNew != null) {
                vehiculoPlacaNew = em.getReference(vehiculoPlacaNew.getClass(), vehiculoPlacaNew.getPlaca());
                vehiculoHasEstado.setVehiculoPlaca(vehiculoPlacaNew);
            }
            vehiculoHasEstado = em.merge(vehiculoHasEstado);
            if (estadoVehiculoidEstadoOld != null && !estadoVehiculoidEstadoOld.equals(estadoVehiculoidEstadoNew)) {
                estadoVehiculoidEstadoOld.getVehiculoHasEstadoList().remove(vehiculoHasEstado);
                estadoVehiculoidEstadoOld = em.merge(estadoVehiculoidEstadoOld);
            }
            if (estadoVehiculoidEstadoNew != null && !estadoVehiculoidEstadoNew.equals(estadoVehiculoidEstadoOld)) {
                estadoVehiculoidEstadoNew.getVehiculoHasEstadoList().add(vehiculoHasEstado);
                estadoVehiculoidEstadoNew = em.merge(estadoVehiculoidEstadoNew);
            }
            if (vehiculoPlacaOld != null && !vehiculoPlacaOld.equals(vehiculoPlacaNew)) {
                vehiculoPlacaOld.getVehiculoHasEstadoList().remove(vehiculoHasEstado);
                vehiculoPlacaOld = em.merge(vehiculoPlacaOld);
            }
            if (vehiculoPlacaNew != null && !vehiculoPlacaNew.equals(vehiculoPlacaOld)) {
                vehiculoPlacaNew.getVehiculoHasEstadoList().add(vehiculoHasEstado);
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
                Integer id = vehiculoHasEstado.getIdEstadoVehiculo();
                if (findVehiculoHasEstado(id) == null) {
                    throw new NonexistentEntityException("The vehiculoHasEstado with id " + id + " no longer exists.");
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
            VehiculoHasEstado vehiculoHasEstado;
            try {
                vehiculoHasEstado = em.getReference(VehiculoHasEstado.class, id);
                vehiculoHasEstado.getIdEstadoVehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vehiculoHasEstado with id " + id + " no longer exists.", enfe);
            }
            Estado estadoVehiculoidEstado = vehiculoHasEstado.getEstadoVehiculoidEstado();
            if (estadoVehiculoidEstado != null) {
                estadoVehiculoidEstado.getVehiculoHasEstadoList().remove(vehiculoHasEstado);
                estadoVehiculoidEstado = em.merge(estadoVehiculoidEstado);
            }
            Vehiculo vehiculoPlaca = vehiculoHasEstado.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getVehiculoHasEstadoList().remove(vehiculoHasEstado);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            em.remove(vehiculoHasEstado);
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

    public List<VehiculoHasEstado> findVehiculoHasEstadoEntities() {
        return findVehiculoHasEstadoEntities(true, -1, -1);
    }

    public List<VehiculoHasEstado> findVehiculoHasEstadoEntities(int maxResults, int firstResult) {
        return findVehiculoHasEstadoEntities(false, maxResults, firstResult);
    }

    private List<VehiculoHasEstado> findVehiculoHasEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VehiculoHasEstado.class));
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

    public VehiculoHasEstado findVehiculoHasEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VehiculoHasEstado.class, id);
        } finally {
            em.close();
        }
    }

    public int getVehiculoHasEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VehiculoHasEstado> rt = cq.from(VehiculoHasEstado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

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
public class EstadoJpaController implements Serializable {

    public EstadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estado estado) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (estado.getVehiculoHasEstadoList() == null) {
            estado.setVehiculoHasEstadoList(new ArrayList<VehiculoHasEstado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<VehiculoHasEstado> attachedVehiculoHasEstadoList = new ArrayList<VehiculoHasEstado>();
            for (VehiculoHasEstado vehiculoHasEstadoListVehiculoHasEstadoToAttach : estado.getVehiculoHasEstadoList()) {
                vehiculoHasEstadoListVehiculoHasEstadoToAttach = em.getReference(vehiculoHasEstadoListVehiculoHasEstadoToAttach.getClass(), vehiculoHasEstadoListVehiculoHasEstadoToAttach.getIdEstadoVehiculo());
                attachedVehiculoHasEstadoList.add(vehiculoHasEstadoListVehiculoHasEstadoToAttach);
            }
            estado.setVehiculoHasEstadoList(attachedVehiculoHasEstadoList);
            em.persist(estado);
            for (VehiculoHasEstado vehiculoHasEstadoListVehiculoHasEstado : estado.getVehiculoHasEstadoList()) {
                Estado oldEstadoVehiculoidEstadoOfVehiculoHasEstadoListVehiculoHasEstado = vehiculoHasEstadoListVehiculoHasEstado.getEstadoVehiculoidEstado();
                vehiculoHasEstadoListVehiculoHasEstado.setEstadoVehiculoidEstado(estado);
                vehiculoHasEstadoListVehiculoHasEstado = em.merge(vehiculoHasEstadoListVehiculoHasEstado);
                if (oldEstadoVehiculoidEstadoOfVehiculoHasEstadoListVehiculoHasEstado != null) {
                    oldEstadoVehiculoidEstadoOfVehiculoHasEstadoListVehiculoHasEstado.getVehiculoHasEstadoList().remove(vehiculoHasEstadoListVehiculoHasEstado);
                    oldEstadoVehiculoidEstadoOfVehiculoHasEstadoListVehiculoHasEstado = em.merge(oldEstadoVehiculoidEstadoOfVehiculoHasEstadoListVehiculoHasEstado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEstado(estado.getIdEstado()) != null) {
                throw new PreexistingEntityException("Estado " + estado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estado estado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Estado persistentEstado = em.find(Estado.class, estado.getIdEstado());
            List<VehiculoHasEstado> vehiculoHasEstadoListOld = persistentEstado.getVehiculoHasEstadoList();
            List<VehiculoHasEstado> vehiculoHasEstadoListNew = estado.getVehiculoHasEstadoList();
            List<String> illegalOrphanMessages = null;
            for (VehiculoHasEstado vehiculoHasEstadoListOldVehiculoHasEstado : vehiculoHasEstadoListOld) {
                if (!vehiculoHasEstadoListNew.contains(vehiculoHasEstadoListOldVehiculoHasEstado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VehiculoHasEstado " + vehiculoHasEstadoListOldVehiculoHasEstado + " since its estadoVehiculoidEstado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<VehiculoHasEstado> attachedVehiculoHasEstadoListNew = new ArrayList<VehiculoHasEstado>();
            for (VehiculoHasEstado vehiculoHasEstadoListNewVehiculoHasEstadoToAttach : vehiculoHasEstadoListNew) {
                vehiculoHasEstadoListNewVehiculoHasEstadoToAttach = em.getReference(vehiculoHasEstadoListNewVehiculoHasEstadoToAttach.getClass(), vehiculoHasEstadoListNewVehiculoHasEstadoToAttach.getIdEstadoVehiculo());
                attachedVehiculoHasEstadoListNew.add(vehiculoHasEstadoListNewVehiculoHasEstadoToAttach);
            }
            vehiculoHasEstadoListNew = attachedVehiculoHasEstadoListNew;
            estado.setVehiculoHasEstadoList(vehiculoHasEstadoListNew);
            estado = em.merge(estado);
            for (VehiculoHasEstado vehiculoHasEstadoListNewVehiculoHasEstado : vehiculoHasEstadoListNew) {
                if (!vehiculoHasEstadoListOld.contains(vehiculoHasEstadoListNewVehiculoHasEstado)) {
                    Estado oldEstadoVehiculoidEstadoOfVehiculoHasEstadoListNewVehiculoHasEstado = vehiculoHasEstadoListNewVehiculoHasEstado.getEstadoVehiculoidEstado();
                    vehiculoHasEstadoListNewVehiculoHasEstado.setEstadoVehiculoidEstado(estado);
                    vehiculoHasEstadoListNewVehiculoHasEstado = em.merge(vehiculoHasEstadoListNewVehiculoHasEstado);
                    if (oldEstadoVehiculoidEstadoOfVehiculoHasEstadoListNewVehiculoHasEstado != null && !oldEstadoVehiculoidEstadoOfVehiculoHasEstadoListNewVehiculoHasEstado.equals(estado)) {
                        oldEstadoVehiculoidEstadoOfVehiculoHasEstadoListNewVehiculoHasEstado.getVehiculoHasEstadoList().remove(vehiculoHasEstadoListNewVehiculoHasEstado);
                        oldEstadoVehiculoidEstadoOfVehiculoHasEstadoListNewVehiculoHasEstado = em.merge(oldEstadoVehiculoidEstadoOfVehiculoHasEstadoListNewVehiculoHasEstado);
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
                Integer id = estado.getIdEstado();
                if (findEstado(id) == null) {
                    throw new NonexistentEntityException("The estado with id " + id + " no longer exists.");
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
            Estado estado;
            try {
                estado = em.getReference(Estado.class, id);
                estado.getIdEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<VehiculoHasEstado> vehiculoHasEstadoListOrphanCheck = estado.getVehiculoHasEstadoList();
            for (VehiculoHasEstado vehiculoHasEstadoListOrphanCheckVehiculoHasEstado : vehiculoHasEstadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the VehiculoHasEstado " + vehiculoHasEstadoListOrphanCheckVehiculoHasEstado + " in its vehiculoHasEstadoList field has a non-nullable estadoVehiculoidEstado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estado);
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

    public List<Estado> findEstadoEntities() {
        return findEstadoEntities(true, -1, -1);
    }

    public List<Estado> findEstadoEntities(int maxResults, int firstResult) {
        return findEstadoEntities(false, maxResults, firstResult);
    }

    private List<Estado> findEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estado.class));
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

    public Estado findEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estado> rt = cq.from(Estado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

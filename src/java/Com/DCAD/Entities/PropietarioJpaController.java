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
public class PropietarioJpaController implements Serializable {

    public PropietarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Propietario propietario) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (propietario.getPropietarioHasVehiculoList() == null) {
            propietario.setPropietarioHasVehiculoList(new ArrayList<PropietarioHasVehiculo>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PropietarioHasVehiculo> attachedPropietarioHasVehiculoList = new ArrayList<PropietarioHasVehiculo>();
            for (PropietarioHasVehiculo propietarioHasVehiculoListPropietarioHasVehiculoToAttach : propietario.getPropietarioHasVehiculoList()) {
                propietarioHasVehiculoListPropietarioHasVehiculoToAttach = em.getReference(propietarioHasVehiculoListPropietarioHasVehiculoToAttach.getClass(), propietarioHasVehiculoListPropietarioHasVehiculoToAttach.getIdPropietarioVehiculo());
                attachedPropietarioHasVehiculoList.add(propietarioHasVehiculoListPropietarioHasVehiculoToAttach);
            }
            propietario.setPropietarioHasVehiculoList(attachedPropietarioHasVehiculoList);
            em.persist(propietario);
            for (PropietarioHasVehiculo propietarioHasVehiculoListPropietarioHasVehiculo : propietario.getPropietarioHasVehiculoList()) {
                Propietario oldPropietarioCedulaOfPropietarioHasVehiculoListPropietarioHasVehiculo = propietarioHasVehiculoListPropietarioHasVehiculo.getPropietarioCedula();
                propietarioHasVehiculoListPropietarioHasVehiculo.setPropietarioCedula(propietario);
                propietarioHasVehiculoListPropietarioHasVehiculo = em.merge(propietarioHasVehiculoListPropietarioHasVehiculo);
                if (oldPropietarioCedulaOfPropietarioHasVehiculoListPropietarioHasVehiculo != null) {
                    oldPropietarioCedulaOfPropietarioHasVehiculoListPropietarioHasVehiculo.getPropietarioHasVehiculoList().remove(propietarioHasVehiculoListPropietarioHasVehiculo);
                    oldPropietarioCedulaOfPropietarioHasVehiculoListPropietarioHasVehiculo = em.merge(oldPropietarioCedulaOfPropietarioHasVehiculoListPropietarioHasVehiculo);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPropietario(propietario.getCedula()) != null) {
                throw new PreexistingEntityException("Propietario " + propietario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Propietario propietario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Propietario persistentPropietario = em.find(Propietario.class, propietario.getCedula());
            List<PropietarioHasVehiculo> propietarioHasVehiculoListOld = persistentPropietario.getPropietarioHasVehiculoList();
            List<PropietarioHasVehiculo> propietarioHasVehiculoListNew = propietario.getPropietarioHasVehiculoList();
            List<String> illegalOrphanMessages = null;
            for (PropietarioHasVehiculo propietarioHasVehiculoListOldPropietarioHasVehiculo : propietarioHasVehiculoListOld) {
                if (!propietarioHasVehiculoListNew.contains(propietarioHasVehiculoListOldPropietarioHasVehiculo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PropietarioHasVehiculo " + propietarioHasVehiculoListOldPropietarioHasVehiculo + " since its propietarioCedula field is not nullable.");
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
            propietario.setPropietarioHasVehiculoList(propietarioHasVehiculoListNew);
            propietario = em.merge(propietario);
            for (PropietarioHasVehiculo propietarioHasVehiculoListNewPropietarioHasVehiculo : propietarioHasVehiculoListNew) {
                if (!propietarioHasVehiculoListOld.contains(propietarioHasVehiculoListNewPropietarioHasVehiculo)) {
                    Propietario oldPropietarioCedulaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo = propietarioHasVehiculoListNewPropietarioHasVehiculo.getPropietarioCedula();
                    propietarioHasVehiculoListNewPropietarioHasVehiculo.setPropietarioCedula(propietario);
                    propietarioHasVehiculoListNewPropietarioHasVehiculo = em.merge(propietarioHasVehiculoListNewPropietarioHasVehiculo);
                    if (oldPropietarioCedulaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo != null && !oldPropietarioCedulaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo.equals(propietario)) {
                        oldPropietarioCedulaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo.getPropietarioHasVehiculoList().remove(propietarioHasVehiculoListNewPropietarioHasVehiculo);
                        oldPropietarioCedulaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo = em.merge(oldPropietarioCedulaOfPropietarioHasVehiculoListNewPropietarioHasVehiculo);
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
                Integer id = propietario.getCedula();
                if (findPropietario(id) == null) {
                    throw new NonexistentEntityException("The propietario with id " + id + " no longer exists.");
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
            Propietario propietario;
            try {
                propietario = em.getReference(Propietario.class, id);
                propietario.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The propietario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PropietarioHasVehiculo> propietarioHasVehiculoListOrphanCheck = propietario.getPropietarioHasVehiculoList();
            for (PropietarioHasVehiculo propietarioHasVehiculoListOrphanCheckPropietarioHasVehiculo : propietarioHasVehiculoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Propietario (" + propietario + ") cannot be destroyed since the PropietarioHasVehiculo " + propietarioHasVehiculoListOrphanCheckPropietarioHasVehiculo + " in its propietarioHasVehiculoList field has a non-nullable propietarioCedula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(propietario);
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

    public List<Propietario> findPropietarioEntities() {
        return findPropietarioEntities(true, -1, -1);
    }

    public List<Propietario> findPropietarioEntities(int maxResults, int firstResult) {
        return findPropietarioEntities(false, maxResults, firstResult);
    }

    private List<Propietario> findPropietarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Propietario.class));
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

    public Propietario findPropietario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Propietario.class, id);
        } finally {
            em.close();
        }
    }

    public int getPropietarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Propietario> rt = cq.from(Propietario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

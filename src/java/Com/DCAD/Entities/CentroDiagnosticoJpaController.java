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
public class CentroDiagnosticoJpaController implements Serializable {

    public CentroDiagnosticoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CentroDiagnostico centroDiagnostico) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (centroDiagnostico.getVehiculoHasCentroDiagnosticoList() == null) {
            centroDiagnostico.setVehiculoHasCentroDiagnosticoList(new ArrayList<VehiculoHasCentroDiagnostico>());
        }
        if (centroDiagnostico.getTecnomecanicaList() == null) {
            centroDiagnostico.setTecnomecanicaList(new ArrayList<Tecnomecanica>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<VehiculoHasCentroDiagnostico> attachedVehiculoHasCentroDiagnosticoList = new ArrayList<VehiculoHasCentroDiagnostico>();
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnosticoToAttach : centroDiagnostico.getVehiculoHasCentroDiagnosticoList()) {
                vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnosticoToAttach = em.getReference(vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnosticoToAttach.getClass(), vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnosticoToAttach.getIdVehiculohasCentro());
                attachedVehiculoHasCentroDiagnosticoList.add(vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnosticoToAttach);
            }
            centroDiagnostico.setVehiculoHasCentroDiagnosticoList(attachedVehiculoHasCentroDiagnosticoList);
            List<Tecnomecanica> attachedTecnomecanicaList = new ArrayList<Tecnomecanica>();
            for (Tecnomecanica tecnomecanicaListTecnomecanicaToAttach : centroDiagnostico.getTecnomecanicaList()) {
                tecnomecanicaListTecnomecanicaToAttach = em.getReference(tecnomecanicaListTecnomecanicaToAttach.getClass(), tecnomecanicaListTecnomecanicaToAttach.getNumControl());
                attachedTecnomecanicaList.add(tecnomecanicaListTecnomecanicaToAttach);
            }
            centroDiagnostico.setTecnomecanicaList(attachedTecnomecanicaList);
            em.persist(centroDiagnostico);
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico : centroDiagnostico.getVehiculoHasCentroDiagnosticoList()) {
                CentroDiagnostico oldCentroDiagnosticoidCentroDiagnosticoOfVehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico = vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico.getCentroDiagnosticoidCentroDiagnostico();
                vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico.setCentroDiagnosticoidCentroDiagnostico(centroDiagnostico);
                vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico = em.merge(vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico);
                if (oldCentroDiagnosticoidCentroDiagnosticoOfVehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico != null) {
                    oldCentroDiagnosticoidCentroDiagnosticoOfVehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico.getVehiculoHasCentroDiagnosticoList().remove(vehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico);
                    oldCentroDiagnosticoidCentroDiagnosticoOfVehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico = em.merge(oldCentroDiagnosticoidCentroDiagnosticoOfVehiculoHasCentroDiagnosticoListVehiculoHasCentroDiagnostico);
                }
            }
            for (Tecnomecanica tecnomecanicaListTecnomecanica : centroDiagnostico.getTecnomecanicaList()) {
                CentroDiagnostico oldCentroDiagnosticoidCentroDiagnosticoOfTecnomecanicaListTecnomecanica = tecnomecanicaListTecnomecanica.getCentroDiagnosticoidCentroDiagnostico();
                tecnomecanicaListTecnomecanica.setCentroDiagnosticoidCentroDiagnostico(centroDiagnostico);
                tecnomecanicaListTecnomecanica = em.merge(tecnomecanicaListTecnomecanica);
                if (oldCentroDiagnosticoidCentroDiagnosticoOfTecnomecanicaListTecnomecanica != null) {
                    oldCentroDiagnosticoidCentroDiagnosticoOfTecnomecanicaListTecnomecanica.getTecnomecanicaList().remove(tecnomecanicaListTecnomecanica);
                    oldCentroDiagnosticoidCentroDiagnosticoOfTecnomecanicaListTecnomecanica = em.merge(oldCentroDiagnosticoidCentroDiagnosticoOfTecnomecanicaListTecnomecanica);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCentroDiagnostico(centroDiagnostico.getIdCentroDiagnostico()) != null) {
                throw new PreexistingEntityException("CentroDiagnostico " + centroDiagnostico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CentroDiagnostico centroDiagnostico) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CentroDiagnostico persistentCentroDiagnostico = em.find(CentroDiagnostico.class, centroDiagnostico.getIdCentroDiagnostico());
            List<VehiculoHasCentroDiagnostico> vehiculoHasCentroDiagnosticoListOld = persistentCentroDiagnostico.getVehiculoHasCentroDiagnosticoList();
            List<VehiculoHasCentroDiagnostico> vehiculoHasCentroDiagnosticoListNew = centroDiagnostico.getVehiculoHasCentroDiagnosticoList();
            List<Tecnomecanica> tecnomecanicaListOld = persistentCentroDiagnostico.getTecnomecanicaList();
            List<Tecnomecanica> tecnomecanicaListNew = centroDiagnostico.getTecnomecanicaList();
            List<String> illegalOrphanMessages = null;
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListOldVehiculoHasCentroDiagnostico : vehiculoHasCentroDiagnosticoListOld) {
                if (!vehiculoHasCentroDiagnosticoListNew.contains(vehiculoHasCentroDiagnosticoListOldVehiculoHasCentroDiagnostico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VehiculoHasCentroDiagnostico " + vehiculoHasCentroDiagnosticoListOldVehiculoHasCentroDiagnostico + " since its centroDiagnosticoidCentroDiagnostico field is not nullable.");
                }
            }
            for (Tecnomecanica tecnomecanicaListOldTecnomecanica : tecnomecanicaListOld) {
                if (!tecnomecanicaListNew.contains(tecnomecanicaListOldTecnomecanica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tecnomecanica " + tecnomecanicaListOldTecnomecanica + " since its centroDiagnosticoidCentroDiagnostico field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<VehiculoHasCentroDiagnostico> attachedVehiculoHasCentroDiagnosticoListNew = new ArrayList<VehiculoHasCentroDiagnostico>();
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnosticoToAttach : vehiculoHasCentroDiagnosticoListNew) {
                vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnosticoToAttach = em.getReference(vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnosticoToAttach.getClass(), vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnosticoToAttach.getIdVehiculohasCentro());
                attachedVehiculoHasCentroDiagnosticoListNew.add(vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnosticoToAttach);
            }
            vehiculoHasCentroDiagnosticoListNew = attachedVehiculoHasCentroDiagnosticoListNew;
            centroDiagnostico.setVehiculoHasCentroDiagnosticoList(vehiculoHasCentroDiagnosticoListNew);
            List<Tecnomecanica> attachedTecnomecanicaListNew = new ArrayList<Tecnomecanica>();
            for (Tecnomecanica tecnomecanicaListNewTecnomecanicaToAttach : tecnomecanicaListNew) {
                tecnomecanicaListNewTecnomecanicaToAttach = em.getReference(tecnomecanicaListNewTecnomecanicaToAttach.getClass(), tecnomecanicaListNewTecnomecanicaToAttach.getNumControl());
                attachedTecnomecanicaListNew.add(tecnomecanicaListNewTecnomecanicaToAttach);
            }
            tecnomecanicaListNew = attachedTecnomecanicaListNew;
            centroDiagnostico.setTecnomecanicaList(tecnomecanicaListNew);
            centroDiagnostico = em.merge(centroDiagnostico);
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico : vehiculoHasCentroDiagnosticoListNew) {
                if (!vehiculoHasCentroDiagnosticoListOld.contains(vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico)) {
                    CentroDiagnostico oldCentroDiagnosticoidCentroDiagnosticoOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico = vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico.getCentroDiagnosticoidCentroDiagnostico();
                    vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico.setCentroDiagnosticoidCentroDiagnostico(centroDiagnostico);
                    vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico = em.merge(vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico);
                    if (oldCentroDiagnosticoidCentroDiagnosticoOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico != null && !oldCentroDiagnosticoidCentroDiagnosticoOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico.equals(centroDiagnostico)) {
                        oldCentroDiagnosticoidCentroDiagnosticoOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico.getVehiculoHasCentroDiagnosticoList().remove(vehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico);
                        oldCentroDiagnosticoidCentroDiagnosticoOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico = em.merge(oldCentroDiagnosticoidCentroDiagnosticoOfVehiculoHasCentroDiagnosticoListNewVehiculoHasCentroDiagnostico);
                    }
                }
            }
            for (Tecnomecanica tecnomecanicaListNewTecnomecanica : tecnomecanicaListNew) {
                if (!tecnomecanicaListOld.contains(tecnomecanicaListNewTecnomecanica)) {
                    CentroDiagnostico oldCentroDiagnosticoidCentroDiagnosticoOfTecnomecanicaListNewTecnomecanica = tecnomecanicaListNewTecnomecanica.getCentroDiagnosticoidCentroDiagnostico();
                    tecnomecanicaListNewTecnomecanica.setCentroDiagnosticoidCentroDiagnostico(centroDiagnostico);
                    tecnomecanicaListNewTecnomecanica = em.merge(tecnomecanicaListNewTecnomecanica);
                    if (oldCentroDiagnosticoidCentroDiagnosticoOfTecnomecanicaListNewTecnomecanica != null && !oldCentroDiagnosticoidCentroDiagnosticoOfTecnomecanicaListNewTecnomecanica.equals(centroDiagnostico)) {
                        oldCentroDiagnosticoidCentroDiagnosticoOfTecnomecanicaListNewTecnomecanica.getTecnomecanicaList().remove(tecnomecanicaListNewTecnomecanica);
                        oldCentroDiagnosticoidCentroDiagnosticoOfTecnomecanicaListNewTecnomecanica = em.merge(oldCentroDiagnosticoidCentroDiagnosticoOfTecnomecanicaListNewTecnomecanica);
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
                Integer id = centroDiagnostico.getIdCentroDiagnostico();
                if (findCentroDiagnostico(id) == null) {
                    throw new NonexistentEntityException("The centroDiagnostico with id " + id + " no longer exists.");
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
            CentroDiagnostico centroDiagnostico;
            try {
                centroDiagnostico = em.getReference(CentroDiagnostico.class, id);
                centroDiagnostico.getIdCentroDiagnostico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The centroDiagnostico with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<VehiculoHasCentroDiagnostico> vehiculoHasCentroDiagnosticoListOrphanCheck = centroDiagnostico.getVehiculoHasCentroDiagnosticoList();
            for (VehiculoHasCentroDiagnostico vehiculoHasCentroDiagnosticoListOrphanCheckVehiculoHasCentroDiagnostico : vehiculoHasCentroDiagnosticoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CentroDiagnostico (" + centroDiagnostico + ") cannot be destroyed since the VehiculoHasCentroDiagnostico " + vehiculoHasCentroDiagnosticoListOrphanCheckVehiculoHasCentroDiagnostico + " in its vehiculoHasCentroDiagnosticoList field has a non-nullable centroDiagnosticoidCentroDiagnostico field.");
            }
            List<Tecnomecanica> tecnomecanicaListOrphanCheck = centroDiagnostico.getTecnomecanicaList();
            for (Tecnomecanica tecnomecanicaListOrphanCheckTecnomecanica : tecnomecanicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CentroDiagnostico (" + centroDiagnostico + ") cannot be destroyed since the Tecnomecanica " + tecnomecanicaListOrphanCheckTecnomecanica + " in its tecnomecanicaList field has a non-nullable centroDiagnosticoidCentroDiagnostico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(centroDiagnostico);
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

    public List<CentroDiagnostico> findCentroDiagnosticoEntities() {
        return findCentroDiagnosticoEntities(true, -1, -1);
    }

    public List<CentroDiagnostico> findCentroDiagnosticoEntities(int maxResults, int firstResult) {
        return findCentroDiagnosticoEntities(false, maxResults, firstResult);
    }

    private List<CentroDiagnostico> findCentroDiagnosticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CentroDiagnostico.class));
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

    public CentroDiagnostico findCentroDiagnostico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CentroDiagnostico.class, id);
        } finally {
            em.close();
        }
    }

    public int getCentroDiagnosticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CentroDiagnostico> rt = cq.from(CentroDiagnostico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

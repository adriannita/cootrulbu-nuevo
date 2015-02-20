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
public class PasajeJpaController implements Serializable {

    public PasajeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pasaje pasaje) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (pasaje.getPasajeHasRutasList() == null) {
            pasaje.setPasajeHasRutasList(new ArrayList<PasajeHasRutas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PasajeHasRutas> attachedPasajeHasRutasList = new ArrayList<PasajeHasRutas>();
            for (PasajeHasRutas pasajeHasRutasListPasajeHasRutasToAttach : pasaje.getPasajeHasRutasList()) {
                pasajeHasRutasListPasajeHasRutasToAttach = em.getReference(pasajeHasRutasListPasajeHasRutasToAttach.getClass(), pasajeHasRutasListPasajeHasRutasToAttach.getIdPasajeRuta());
                attachedPasajeHasRutasList.add(pasajeHasRutasListPasajeHasRutasToAttach);
            }
            pasaje.setPasajeHasRutasList(attachedPasajeHasRutasList);
            em.persist(pasaje);
            for (PasajeHasRutas pasajeHasRutasListPasajeHasRutas : pasaje.getPasajeHasRutasList()) {
                Pasaje oldPasajeCodigoPasajeOfPasajeHasRutasListPasajeHasRutas = pasajeHasRutasListPasajeHasRutas.getPasajeCodigoPasaje();
                pasajeHasRutasListPasajeHasRutas.setPasajeCodigoPasaje(pasaje);
                pasajeHasRutasListPasajeHasRutas = em.merge(pasajeHasRutasListPasajeHasRutas);
                if (oldPasajeCodigoPasajeOfPasajeHasRutasListPasajeHasRutas != null) {
                    oldPasajeCodigoPasajeOfPasajeHasRutasListPasajeHasRutas.getPasajeHasRutasList().remove(pasajeHasRutasListPasajeHasRutas);
                    oldPasajeCodigoPasajeOfPasajeHasRutasListPasajeHasRutas = em.merge(oldPasajeCodigoPasajeOfPasajeHasRutasListPasajeHasRutas);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPasaje(pasaje.getCodigoPasaje()) != null) {
                throw new PreexistingEntityException("Pasaje " + pasaje + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pasaje pasaje) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pasaje persistentPasaje = em.find(Pasaje.class, pasaje.getCodigoPasaje());
            List<PasajeHasRutas> pasajeHasRutasListOld = persistentPasaje.getPasajeHasRutasList();
            List<PasajeHasRutas> pasajeHasRutasListNew = pasaje.getPasajeHasRutasList();
            List<String> illegalOrphanMessages = null;
            for (PasajeHasRutas pasajeHasRutasListOldPasajeHasRutas : pasajeHasRutasListOld) {
                if (!pasajeHasRutasListNew.contains(pasajeHasRutasListOldPasajeHasRutas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PasajeHasRutas " + pasajeHasRutasListOldPasajeHasRutas + " since its pasajeCodigoPasaje field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PasajeHasRutas> attachedPasajeHasRutasListNew = new ArrayList<PasajeHasRutas>();
            for (PasajeHasRutas pasajeHasRutasListNewPasajeHasRutasToAttach : pasajeHasRutasListNew) {
                pasajeHasRutasListNewPasajeHasRutasToAttach = em.getReference(pasajeHasRutasListNewPasajeHasRutasToAttach.getClass(), pasajeHasRutasListNewPasajeHasRutasToAttach.getIdPasajeRuta());
                attachedPasajeHasRutasListNew.add(pasajeHasRutasListNewPasajeHasRutasToAttach);
            }
            pasajeHasRutasListNew = attachedPasajeHasRutasListNew;
            pasaje.setPasajeHasRutasList(pasajeHasRutasListNew);
            pasaje = em.merge(pasaje);
            for (PasajeHasRutas pasajeHasRutasListNewPasajeHasRutas : pasajeHasRutasListNew) {
                if (!pasajeHasRutasListOld.contains(pasajeHasRutasListNewPasajeHasRutas)) {
                    Pasaje oldPasajeCodigoPasajeOfPasajeHasRutasListNewPasajeHasRutas = pasajeHasRutasListNewPasajeHasRutas.getPasajeCodigoPasaje();
                    pasajeHasRutasListNewPasajeHasRutas.setPasajeCodigoPasaje(pasaje);
                    pasajeHasRutasListNewPasajeHasRutas = em.merge(pasajeHasRutasListNewPasajeHasRutas);
                    if (oldPasajeCodigoPasajeOfPasajeHasRutasListNewPasajeHasRutas != null && !oldPasajeCodigoPasajeOfPasajeHasRutasListNewPasajeHasRutas.equals(pasaje)) {
                        oldPasajeCodigoPasajeOfPasajeHasRutasListNewPasajeHasRutas.getPasajeHasRutasList().remove(pasajeHasRutasListNewPasajeHasRutas);
                        oldPasajeCodigoPasajeOfPasajeHasRutasListNewPasajeHasRutas = em.merge(oldPasajeCodigoPasajeOfPasajeHasRutasListNewPasajeHasRutas);
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
                Integer id = pasaje.getCodigoPasaje();
                if (findPasaje(id) == null) {
                    throw new NonexistentEntityException("The pasaje with id " + id + " no longer exists.");
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
            Pasaje pasaje;
            try {
                pasaje = em.getReference(Pasaje.class, id);
                pasaje.getCodigoPasaje();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pasaje with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PasajeHasRutas> pasajeHasRutasListOrphanCheck = pasaje.getPasajeHasRutasList();
            for (PasajeHasRutas pasajeHasRutasListOrphanCheckPasajeHasRutas : pasajeHasRutasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pasaje (" + pasaje + ") cannot be destroyed since the PasajeHasRutas " + pasajeHasRutasListOrphanCheckPasajeHasRutas + " in its pasajeHasRutasList field has a non-nullable pasajeCodigoPasaje field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pasaje);
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

    public List<Pasaje> findPasajeEntities() {
        return findPasajeEntities(true, -1, -1);
    }

    public List<Pasaje> findPasajeEntities(int maxResults, int firstResult) {
        return findPasajeEntities(false, maxResults, firstResult);
    }

    private List<Pasaje> findPasajeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pasaje.class));
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

    public Pasaje findPasaje(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pasaje.class, id);
        } finally {
            em.close();
        }
    }

    public int getPasajeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pasaje> rt = cq.from(Pasaje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

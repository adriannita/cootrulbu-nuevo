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
public class SoatJpaController implements Serializable {

    public SoatJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Soat soat) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Aseguradora aseguradoraCodAsegurador = soat.getAseguradoraCodAsegurador();
            if (aseguradoraCodAsegurador != null) {
                aseguradoraCodAsegurador = em.getReference(aseguradoraCodAsegurador.getClass(), aseguradoraCodAsegurador.getCodAsegurador());
                soat.setAseguradoraCodAsegurador(aseguradoraCodAsegurador);
            }
            em.persist(soat);
            if (aseguradoraCodAsegurador != null) {
                aseguradoraCodAsegurador.getSoatList().add(soat);
                aseguradoraCodAsegurador = em.merge(aseguradoraCodAsegurador);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSoat(soat.getIsSoat()) != null) {
                throw new PreexistingEntityException("Soat " + soat + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Soat soat) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Soat persistentSoat = em.find(Soat.class, soat.getIsSoat());
            Aseguradora aseguradoraCodAseguradorOld = persistentSoat.getAseguradoraCodAsegurador();
            Aseguradora aseguradoraCodAseguradorNew = soat.getAseguradoraCodAsegurador();
            if (aseguradoraCodAseguradorNew != null) {
                aseguradoraCodAseguradorNew = em.getReference(aseguradoraCodAseguradorNew.getClass(), aseguradoraCodAseguradorNew.getCodAsegurador());
                soat.setAseguradoraCodAsegurador(aseguradoraCodAseguradorNew);
            }
            soat = em.merge(soat);
            if (aseguradoraCodAseguradorOld != null && !aseguradoraCodAseguradorOld.equals(aseguradoraCodAseguradorNew)) {
                aseguradoraCodAseguradorOld.getSoatList().remove(soat);
                aseguradoraCodAseguradorOld = em.merge(aseguradoraCodAseguradorOld);
            }
            if (aseguradoraCodAseguradorNew != null && !aseguradoraCodAseguradorNew.equals(aseguradoraCodAseguradorOld)) {
                aseguradoraCodAseguradorNew.getSoatList().add(soat);
                aseguradoraCodAseguradorNew = em.merge(aseguradoraCodAseguradorNew);
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
                Integer id = soat.getIsSoat();
                if (findSoat(id) == null) {
                    throw new NonexistentEntityException("The soat with id " + id + " no longer exists.");
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
            Soat soat;
            try {
                soat = em.getReference(Soat.class, id);
                soat.getIsSoat();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The soat with id " + id + " no longer exists.", enfe);
            }
            Aseguradora aseguradoraCodAsegurador = soat.getAseguradoraCodAsegurador();
            if (aseguradoraCodAsegurador != null) {
                aseguradoraCodAsegurador.getSoatList().remove(soat);
                aseguradoraCodAsegurador = em.merge(aseguradoraCodAsegurador);
            }
            em.remove(soat);
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

    public List<Soat> findSoatEntities() {
        return findSoatEntities(true, -1, -1);
    }

    public List<Soat> findSoatEntities(int maxResults, int firstResult) {
        return findSoatEntities(false, maxResults, firstResult);
    }

    private List<Soat> findSoatEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Soat.class));
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

    public Soat findSoat(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Soat.class, id);
        } finally {
            em.close();
        }
    }

    public int getSoatCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Soat> rt = cq.from(Soat.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

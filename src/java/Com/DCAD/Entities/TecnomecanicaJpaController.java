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
public class TecnomecanicaJpaController implements Serializable {

    public TecnomecanicaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tecnomecanica tecnomecanica) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Aseguradora aseguradoraCodAsegurador = tecnomecanica.getAseguradoraCodAsegurador();
            if (aseguradoraCodAsegurador != null) {
                aseguradoraCodAsegurador = em.getReference(aseguradoraCodAsegurador.getClass(), aseguradoraCodAsegurador.getCodAsegurador());
                tecnomecanica.setAseguradoraCodAsegurador(aseguradoraCodAsegurador);
            }
            CentroDiagnostico centroDiagnosticoidCentroDiagnostico = tecnomecanica.getCentroDiagnosticoidCentroDiagnostico();
            if (centroDiagnosticoidCentroDiagnostico != null) {
                centroDiagnosticoidCentroDiagnostico = em.getReference(centroDiagnosticoidCentroDiagnostico.getClass(), centroDiagnosticoidCentroDiagnostico.getIdCentroDiagnostico());
                tecnomecanica.setCentroDiagnosticoidCentroDiagnostico(centroDiagnosticoidCentroDiagnostico);
            }
            em.persist(tecnomecanica);
            if (aseguradoraCodAsegurador != null) {
                aseguradoraCodAsegurador.getTecnomecanicaList().add(tecnomecanica);
                aseguradoraCodAsegurador = em.merge(aseguradoraCodAsegurador);
            }
            if (centroDiagnosticoidCentroDiagnostico != null) {
                centroDiagnosticoidCentroDiagnostico.getTecnomecanicaList().add(tecnomecanica);
                centroDiagnosticoidCentroDiagnostico = em.merge(centroDiagnosticoidCentroDiagnostico);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTecnomecanica(tecnomecanica.getNumControl()) != null) {
                throw new PreexistingEntityException("Tecnomecanica " + tecnomecanica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tecnomecanica tecnomecanica) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tecnomecanica persistentTecnomecanica = em.find(Tecnomecanica.class, tecnomecanica.getNumControl());
            Aseguradora aseguradoraCodAseguradorOld = persistentTecnomecanica.getAseguradoraCodAsegurador();
            Aseguradora aseguradoraCodAseguradorNew = tecnomecanica.getAseguradoraCodAsegurador();
            CentroDiagnostico centroDiagnosticoidCentroDiagnosticoOld = persistentTecnomecanica.getCentroDiagnosticoidCentroDiagnostico();
            CentroDiagnostico centroDiagnosticoidCentroDiagnosticoNew = tecnomecanica.getCentroDiagnosticoidCentroDiagnostico();
            if (aseguradoraCodAseguradorNew != null) {
                aseguradoraCodAseguradorNew = em.getReference(aseguradoraCodAseguradorNew.getClass(), aseguradoraCodAseguradorNew.getCodAsegurador());
                tecnomecanica.setAseguradoraCodAsegurador(aseguradoraCodAseguradorNew);
            }
            if (centroDiagnosticoidCentroDiagnosticoNew != null) {
                centroDiagnosticoidCentroDiagnosticoNew = em.getReference(centroDiagnosticoidCentroDiagnosticoNew.getClass(), centroDiagnosticoidCentroDiagnosticoNew.getIdCentroDiagnostico());
                tecnomecanica.setCentroDiagnosticoidCentroDiagnostico(centroDiagnosticoidCentroDiagnosticoNew);
            }
            tecnomecanica = em.merge(tecnomecanica);
            if (aseguradoraCodAseguradorOld != null && !aseguradoraCodAseguradorOld.equals(aseguradoraCodAseguradorNew)) {
                aseguradoraCodAseguradorOld.getTecnomecanicaList().remove(tecnomecanica);
                aseguradoraCodAseguradorOld = em.merge(aseguradoraCodAseguradorOld);
            }
            if (aseguradoraCodAseguradorNew != null && !aseguradoraCodAseguradorNew.equals(aseguradoraCodAseguradorOld)) {
                aseguradoraCodAseguradorNew.getTecnomecanicaList().add(tecnomecanica);
                aseguradoraCodAseguradorNew = em.merge(aseguradoraCodAseguradorNew);
            }
            if (centroDiagnosticoidCentroDiagnosticoOld != null && !centroDiagnosticoidCentroDiagnosticoOld.equals(centroDiagnosticoidCentroDiagnosticoNew)) {
                centroDiagnosticoidCentroDiagnosticoOld.getTecnomecanicaList().remove(tecnomecanica);
                centroDiagnosticoidCentroDiagnosticoOld = em.merge(centroDiagnosticoidCentroDiagnosticoOld);
            }
            if (centroDiagnosticoidCentroDiagnosticoNew != null && !centroDiagnosticoidCentroDiagnosticoNew.equals(centroDiagnosticoidCentroDiagnosticoOld)) {
                centroDiagnosticoidCentroDiagnosticoNew.getTecnomecanicaList().add(tecnomecanica);
                centroDiagnosticoidCentroDiagnosticoNew = em.merge(centroDiagnosticoidCentroDiagnosticoNew);
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
                Integer id = tecnomecanica.getNumControl();
                if (findTecnomecanica(id) == null) {
                    throw new NonexistentEntityException("The tecnomecanica with id " + id + " no longer exists.");
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
            Tecnomecanica tecnomecanica;
            try {
                tecnomecanica = em.getReference(Tecnomecanica.class, id);
                tecnomecanica.getNumControl();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tecnomecanica with id " + id + " no longer exists.", enfe);
            }
            Aseguradora aseguradoraCodAsegurador = tecnomecanica.getAseguradoraCodAsegurador();
            if (aseguradoraCodAsegurador != null) {
                aseguradoraCodAsegurador.getTecnomecanicaList().remove(tecnomecanica);
                aseguradoraCodAsegurador = em.merge(aseguradoraCodAsegurador);
            }
            CentroDiagnostico centroDiagnosticoidCentroDiagnostico = tecnomecanica.getCentroDiagnosticoidCentroDiagnostico();
            if (centroDiagnosticoidCentroDiagnostico != null) {
                centroDiagnosticoidCentroDiagnostico.getTecnomecanicaList().remove(tecnomecanica);
                centroDiagnosticoidCentroDiagnostico = em.merge(centroDiagnosticoidCentroDiagnostico);
            }
            em.remove(tecnomecanica);
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

    public List<Tecnomecanica> findTecnomecanicaEntities() {
        return findTecnomecanicaEntities(true, -1, -1);
    }

    public List<Tecnomecanica> findTecnomecanicaEntities(int maxResults, int firstResult) {
        return findTecnomecanicaEntities(false, maxResults, firstResult);
    }

    private List<Tecnomecanica> findTecnomecanicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tecnomecanica.class));
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

    public Tecnomecanica findTecnomecanica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tecnomecanica.class, id);
        } finally {
            em.close();
        }
    }

    public int getTecnomecanicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tecnomecanica> rt = cq.from(Tecnomecanica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

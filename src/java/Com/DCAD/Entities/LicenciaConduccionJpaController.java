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
public class LicenciaConduccionJpaController implements Serializable {

    public LicenciaConduccionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LicenciaConduccion licenciaConduccion) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Conductores conductoresCedula = licenciaConduccion.getConductoresCedula();
            if (conductoresCedula != null) {
                conductoresCedula = em.getReference(conductoresCedula.getClass(), conductoresCedula.getCedula());
                licenciaConduccion.setConductoresCedula(conductoresCedula);
            }
            em.persist(licenciaConduccion);
            if (conductoresCedula != null) {
                conductoresCedula.getLicenciaConduccionList().add(licenciaConduccion);
                conductoresCedula = em.merge(conductoresCedula);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findLicenciaConduccion(licenciaConduccion.getNumero()) != null) {
                throw new PreexistingEntityException("LicenciaConduccion " + licenciaConduccion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LicenciaConduccion licenciaConduccion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            LicenciaConduccion persistentLicenciaConduccion = em.find(LicenciaConduccion.class, licenciaConduccion.getNumero());
            Conductores conductoresCedulaOld = persistentLicenciaConduccion.getConductoresCedula();
            Conductores conductoresCedulaNew = licenciaConduccion.getConductoresCedula();
            if (conductoresCedulaNew != null) {
                conductoresCedulaNew = em.getReference(conductoresCedulaNew.getClass(), conductoresCedulaNew.getCedula());
                licenciaConduccion.setConductoresCedula(conductoresCedulaNew);
            }
            licenciaConduccion = em.merge(licenciaConduccion);
            if (conductoresCedulaOld != null && !conductoresCedulaOld.equals(conductoresCedulaNew)) {
                conductoresCedulaOld.getLicenciaConduccionList().remove(licenciaConduccion);
                conductoresCedulaOld = em.merge(conductoresCedulaOld);
            }
            if (conductoresCedulaNew != null && !conductoresCedulaNew.equals(conductoresCedulaOld)) {
                conductoresCedulaNew.getLicenciaConduccionList().add(licenciaConduccion);
                conductoresCedulaNew = em.merge(conductoresCedulaNew);
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
                Integer id = licenciaConduccion.getNumero();
                if (findLicenciaConduccion(id) == null) {
                    throw new NonexistentEntityException("The licenciaConduccion with id " + id + " no longer exists.");
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
            LicenciaConduccion licenciaConduccion;
            try {
                licenciaConduccion = em.getReference(LicenciaConduccion.class, id);
                licenciaConduccion.getNumero();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The licenciaConduccion with id " + id + " no longer exists.", enfe);
            }
            Conductores conductoresCedula = licenciaConduccion.getConductoresCedula();
            if (conductoresCedula != null) {
                conductoresCedula.getLicenciaConduccionList().remove(licenciaConduccion);
                conductoresCedula = em.merge(conductoresCedula);
            }
            em.remove(licenciaConduccion);
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

    public List<LicenciaConduccion> findLicenciaConduccionEntities() {
        return findLicenciaConduccionEntities(true, -1, -1);
    }

    public List<LicenciaConduccion> findLicenciaConduccionEntities(int maxResults, int firstResult) {
        return findLicenciaConduccionEntities(false, maxResults, firstResult);
    }

    private List<LicenciaConduccion> findLicenciaConduccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LicenciaConduccion.class));
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

    public LicenciaConduccion findLicenciaConduccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LicenciaConduccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getLicenciaConduccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LicenciaConduccion> rt = cq.from(LicenciaConduccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

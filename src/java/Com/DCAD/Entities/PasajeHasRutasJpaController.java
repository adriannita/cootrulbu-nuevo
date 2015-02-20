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
public class PasajeHasRutasJpaController implements Serializable {

    public PasajeHasRutasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PasajeHasRutas pasajeHasRutas) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pasaje pasajeCodigoPasaje = pasajeHasRutas.getPasajeCodigoPasaje();
            if (pasajeCodigoPasaje != null) {
                pasajeCodigoPasaje = em.getReference(pasajeCodigoPasaje.getClass(), pasajeCodigoPasaje.getCodigoPasaje());
                pasajeHasRutas.setPasajeCodigoPasaje(pasajeCodigoPasaje);
            }
            Rutas rutasCodigoRuta = pasajeHasRutas.getRutasCodigoRuta();
            if (rutasCodigoRuta != null) {
                rutasCodigoRuta = em.getReference(rutasCodigoRuta.getClass(), rutasCodigoRuta.getCodigoRuta());
                pasajeHasRutas.setRutasCodigoRuta(rutasCodigoRuta);
            }
            em.persist(pasajeHasRutas);
            if (pasajeCodigoPasaje != null) {
                pasajeCodigoPasaje.getPasajeHasRutasList().add(pasajeHasRutas);
                pasajeCodigoPasaje = em.merge(pasajeCodigoPasaje);
            }
            if (rutasCodigoRuta != null) {
                rutasCodigoRuta.getPasajeHasRutasList().add(pasajeHasRutas);
                rutasCodigoRuta = em.merge(rutasCodigoRuta);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPasajeHasRutas(pasajeHasRutas.getIdPasajeRuta()) != null) {
                throw new PreexistingEntityException("PasajeHasRutas " + pasajeHasRutas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PasajeHasRutas pasajeHasRutas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PasajeHasRutas persistentPasajeHasRutas = em.find(PasajeHasRutas.class, pasajeHasRutas.getIdPasajeRuta());
            Pasaje pasajeCodigoPasajeOld = persistentPasajeHasRutas.getPasajeCodigoPasaje();
            Pasaje pasajeCodigoPasajeNew = pasajeHasRutas.getPasajeCodigoPasaje();
            Rutas rutasCodigoRutaOld = persistentPasajeHasRutas.getRutasCodigoRuta();
            Rutas rutasCodigoRutaNew = pasajeHasRutas.getRutasCodigoRuta();
            if (pasajeCodigoPasajeNew != null) {
                pasajeCodigoPasajeNew = em.getReference(pasajeCodigoPasajeNew.getClass(), pasajeCodigoPasajeNew.getCodigoPasaje());
                pasajeHasRutas.setPasajeCodigoPasaje(pasajeCodigoPasajeNew);
            }
            if (rutasCodigoRutaNew != null) {
                rutasCodigoRutaNew = em.getReference(rutasCodigoRutaNew.getClass(), rutasCodigoRutaNew.getCodigoRuta());
                pasajeHasRutas.setRutasCodigoRuta(rutasCodigoRutaNew);
            }
            pasajeHasRutas = em.merge(pasajeHasRutas);
            if (pasajeCodigoPasajeOld != null && !pasajeCodigoPasajeOld.equals(pasajeCodigoPasajeNew)) {
                pasajeCodigoPasajeOld.getPasajeHasRutasList().remove(pasajeHasRutas);
                pasajeCodigoPasajeOld = em.merge(pasajeCodigoPasajeOld);
            }
            if (pasajeCodigoPasajeNew != null && !pasajeCodigoPasajeNew.equals(pasajeCodigoPasajeOld)) {
                pasajeCodigoPasajeNew.getPasajeHasRutasList().add(pasajeHasRutas);
                pasajeCodigoPasajeNew = em.merge(pasajeCodigoPasajeNew);
            }
            if (rutasCodigoRutaOld != null && !rutasCodigoRutaOld.equals(rutasCodigoRutaNew)) {
                rutasCodigoRutaOld.getPasajeHasRutasList().remove(pasajeHasRutas);
                rutasCodigoRutaOld = em.merge(rutasCodigoRutaOld);
            }
            if (rutasCodigoRutaNew != null && !rutasCodigoRutaNew.equals(rutasCodigoRutaOld)) {
                rutasCodigoRutaNew.getPasajeHasRutasList().add(pasajeHasRutas);
                rutasCodigoRutaNew = em.merge(rutasCodigoRutaNew);
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
                Integer id = pasajeHasRutas.getIdPasajeRuta();
                if (findPasajeHasRutas(id) == null) {
                    throw new NonexistentEntityException("The pasajeHasRutas with id " + id + " no longer exists.");
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
            PasajeHasRutas pasajeHasRutas;
            try {
                pasajeHasRutas = em.getReference(PasajeHasRutas.class, id);
                pasajeHasRutas.getIdPasajeRuta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pasajeHasRutas with id " + id + " no longer exists.", enfe);
            }
            Pasaje pasajeCodigoPasaje = pasajeHasRutas.getPasajeCodigoPasaje();
            if (pasajeCodigoPasaje != null) {
                pasajeCodigoPasaje.getPasajeHasRutasList().remove(pasajeHasRutas);
                pasajeCodigoPasaje = em.merge(pasajeCodigoPasaje);
            }
            Rutas rutasCodigoRuta = pasajeHasRutas.getRutasCodigoRuta();
            if (rutasCodigoRuta != null) {
                rutasCodigoRuta.getPasajeHasRutasList().remove(pasajeHasRutas);
                rutasCodigoRuta = em.merge(rutasCodigoRuta);
            }
            em.remove(pasajeHasRutas);
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

    public List<PasajeHasRutas> findPasajeHasRutasEntities() {
        return findPasajeHasRutasEntities(true, -1, -1);
    }

    public List<PasajeHasRutas> findPasajeHasRutasEntities(int maxResults, int firstResult) {
        return findPasajeHasRutasEntities(false, maxResults, firstResult);
    }

    private List<PasajeHasRutas> findPasajeHasRutasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PasajeHasRutas.class));
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

    public PasajeHasRutas findPasajeHasRutas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PasajeHasRutas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPasajeHasRutasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PasajeHasRutas> rt = cq.from(PasajeHasRutas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

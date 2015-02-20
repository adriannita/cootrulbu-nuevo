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
public class VehiculoHasRutasJpaController implements Serializable {

    public VehiculoHasRutasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VehiculoHasRutas vehiculoHasRutas) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rutas rutasCodigoRuta = vehiculoHasRutas.getRutasCodigoRuta();
            if (rutasCodigoRuta != null) {
                rutasCodigoRuta = em.getReference(rutasCodigoRuta.getClass(), rutasCodigoRuta.getCodigoRuta());
                vehiculoHasRutas.setRutasCodigoRuta(rutasCodigoRuta);
            }
            Vehiculo vehiculoPlaca = vehiculoHasRutas.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca = em.getReference(vehiculoPlaca.getClass(), vehiculoPlaca.getPlaca());
                vehiculoHasRutas.setVehiculoPlaca(vehiculoPlaca);
            }
            em.persist(vehiculoHasRutas);
            if (rutasCodigoRuta != null) {
                rutasCodigoRuta.getVehiculoHasRutasList().add(vehiculoHasRutas);
                rutasCodigoRuta = em.merge(rutasCodigoRuta);
            }
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getVehiculoHasRutasList().add(vehiculoHasRutas);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVehiculoHasRutas(vehiculoHasRutas.getIdVehiculosRuta()) != null) {
                throw new PreexistingEntityException("VehiculoHasRutas " + vehiculoHasRutas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VehiculoHasRutas vehiculoHasRutas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            VehiculoHasRutas persistentVehiculoHasRutas = em.find(VehiculoHasRutas.class, vehiculoHasRutas.getIdVehiculosRuta());
            Rutas rutasCodigoRutaOld = persistentVehiculoHasRutas.getRutasCodigoRuta();
            Rutas rutasCodigoRutaNew = vehiculoHasRutas.getRutasCodigoRuta();
            Vehiculo vehiculoPlacaOld = persistentVehiculoHasRutas.getVehiculoPlaca();
            Vehiculo vehiculoPlacaNew = vehiculoHasRutas.getVehiculoPlaca();
            if (rutasCodigoRutaNew != null) {
                rutasCodigoRutaNew = em.getReference(rutasCodigoRutaNew.getClass(), rutasCodigoRutaNew.getCodigoRuta());
                vehiculoHasRutas.setRutasCodigoRuta(rutasCodigoRutaNew);
            }
            if (vehiculoPlacaNew != null) {
                vehiculoPlacaNew = em.getReference(vehiculoPlacaNew.getClass(), vehiculoPlacaNew.getPlaca());
                vehiculoHasRutas.setVehiculoPlaca(vehiculoPlacaNew);
            }
            vehiculoHasRutas = em.merge(vehiculoHasRutas);
            if (rutasCodigoRutaOld != null && !rutasCodigoRutaOld.equals(rutasCodigoRutaNew)) {
                rutasCodigoRutaOld.getVehiculoHasRutasList().remove(vehiculoHasRutas);
                rutasCodigoRutaOld = em.merge(rutasCodigoRutaOld);
            }
            if (rutasCodigoRutaNew != null && !rutasCodigoRutaNew.equals(rutasCodigoRutaOld)) {
                rutasCodigoRutaNew.getVehiculoHasRutasList().add(vehiculoHasRutas);
                rutasCodigoRutaNew = em.merge(rutasCodigoRutaNew);
            }
            if (vehiculoPlacaOld != null && !vehiculoPlacaOld.equals(vehiculoPlacaNew)) {
                vehiculoPlacaOld.getVehiculoHasRutasList().remove(vehiculoHasRutas);
                vehiculoPlacaOld = em.merge(vehiculoPlacaOld);
            }
            if (vehiculoPlacaNew != null && !vehiculoPlacaNew.equals(vehiculoPlacaOld)) {
                vehiculoPlacaNew.getVehiculoHasRutasList().add(vehiculoHasRutas);
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
                Integer id = vehiculoHasRutas.getIdVehiculosRuta();
                if (findVehiculoHasRutas(id) == null) {
                    throw new NonexistentEntityException("The vehiculoHasRutas with id " + id + " no longer exists.");
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
            VehiculoHasRutas vehiculoHasRutas;
            try {
                vehiculoHasRutas = em.getReference(VehiculoHasRutas.class, id);
                vehiculoHasRutas.getIdVehiculosRuta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vehiculoHasRutas with id " + id + " no longer exists.", enfe);
            }
            Rutas rutasCodigoRuta = vehiculoHasRutas.getRutasCodigoRuta();
            if (rutasCodigoRuta != null) {
                rutasCodigoRuta.getVehiculoHasRutasList().remove(vehiculoHasRutas);
                rutasCodigoRuta = em.merge(rutasCodigoRuta);
            }
            Vehiculo vehiculoPlaca = vehiculoHasRutas.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getVehiculoHasRutasList().remove(vehiculoHasRutas);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            em.remove(vehiculoHasRutas);
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

    public List<VehiculoHasRutas> findVehiculoHasRutasEntities() {
        return findVehiculoHasRutasEntities(true, -1, -1);
    }

    public List<VehiculoHasRutas> findVehiculoHasRutasEntities(int maxResults, int firstResult) {
        return findVehiculoHasRutasEntities(false, maxResults, firstResult);
    }

    private List<VehiculoHasRutas> findVehiculoHasRutasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VehiculoHasRutas.class));
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

    public VehiculoHasRutas findVehiculoHasRutas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VehiculoHasRutas.class, id);
        } finally {
            em.close();
        }
    }

    public int getVehiculoHasRutasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VehiculoHasRutas> rt = cq.from(VehiculoHasRutas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

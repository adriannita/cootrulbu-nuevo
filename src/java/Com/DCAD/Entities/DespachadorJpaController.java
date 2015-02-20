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
public class DespachadorJpaController implements Serializable {

    public DespachadorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Despachador despachador) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (despachador.getRutasList() == null) {
            despachador.setRutasList(new ArrayList<Rutas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario usuarioidUsuario = despachador.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario = em.getReference(usuarioidUsuario.getClass(), usuarioidUsuario.getIdUsuario());
                despachador.setUsuarioidUsuario(usuarioidUsuario);
            }
            List<Rutas> attachedRutasList = new ArrayList<Rutas>();
            for (Rutas rutasListRutasToAttach : despachador.getRutasList()) {
                rutasListRutasToAttach = em.getReference(rutasListRutasToAttach.getClass(), rutasListRutasToAttach.getCodigoRuta());
                attachedRutasList.add(rutasListRutasToAttach);
            }
            despachador.setRutasList(attachedRutasList);
            em.persist(despachador);
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getDespachadorList().add(despachador);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            for (Rutas rutasListRutas : despachador.getRutasList()) {
                Despachador oldDespachadorCodDespachadorOfRutasListRutas = rutasListRutas.getDespachadorCodDespachador();
                rutasListRutas.setDespachadorCodDespachador(despachador);
                rutasListRutas = em.merge(rutasListRutas);
                if (oldDespachadorCodDespachadorOfRutasListRutas != null) {
                    oldDespachadorCodDespachadorOfRutasListRutas.getRutasList().remove(rutasListRutas);
                    oldDespachadorCodDespachadorOfRutasListRutas = em.merge(oldDespachadorCodDespachadorOfRutasListRutas);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDespachador(despachador.getCodDespachador()) != null) {
                throw new PreexistingEntityException("Despachador " + despachador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Despachador despachador) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Despachador persistentDespachador = em.find(Despachador.class, despachador.getCodDespachador());
            Usuario usuarioidUsuarioOld = persistentDespachador.getUsuarioidUsuario();
            Usuario usuarioidUsuarioNew = despachador.getUsuarioidUsuario();
            List<Rutas> rutasListOld = persistentDespachador.getRutasList();
            List<Rutas> rutasListNew = despachador.getRutasList();
            List<String> illegalOrphanMessages = null;
            for (Rutas rutasListOldRutas : rutasListOld) {
                if (!rutasListNew.contains(rutasListOldRutas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rutas " + rutasListOldRutas + " since its despachadorCodDespachador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioidUsuarioNew != null) {
                usuarioidUsuarioNew = em.getReference(usuarioidUsuarioNew.getClass(), usuarioidUsuarioNew.getIdUsuario());
                despachador.setUsuarioidUsuario(usuarioidUsuarioNew);
            }
            List<Rutas> attachedRutasListNew = new ArrayList<Rutas>();
            for (Rutas rutasListNewRutasToAttach : rutasListNew) {
                rutasListNewRutasToAttach = em.getReference(rutasListNewRutasToAttach.getClass(), rutasListNewRutasToAttach.getCodigoRuta());
                attachedRutasListNew.add(rutasListNewRutasToAttach);
            }
            rutasListNew = attachedRutasListNew;
            despachador.setRutasList(rutasListNew);
            despachador = em.merge(despachador);
            if (usuarioidUsuarioOld != null && !usuarioidUsuarioOld.equals(usuarioidUsuarioNew)) {
                usuarioidUsuarioOld.getDespachadorList().remove(despachador);
                usuarioidUsuarioOld = em.merge(usuarioidUsuarioOld);
            }
            if (usuarioidUsuarioNew != null && !usuarioidUsuarioNew.equals(usuarioidUsuarioOld)) {
                usuarioidUsuarioNew.getDespachadorList().add(despachador);
                usuarioidUsuarioNew = em.merge(usuarioidUsuarioNew);
            }
            for (Rutas rutasListNewRutas : rutasListNew) {
                if (!rutasListOld.contains(rutasListNewRutas)) {
                    Despachador oldDespachadorCodDespachadorOfRutasListNewRutas = rutasListNewRutas.getDespachadorCodDespachador();
                    rutasListNewRutas.setDespachadorCodDespachador(despachador);
                    rutasListNewRutas = em.merge(rutasListNewRutas);
                    if (oldDespachadorCodDespachadorOfRutasListNewRutas != null && !oldDespachadorCodDespachadorOfRutasListNewRutas.equals(despachador)) {
                        oldDespachadorCodDespachadorOfRutasListNewRutas.getRutasList().remove(rutasListNewRutas);
                        oldDespachadorCodDespachadorOfRutasListNewRutas = em.merge(oldDespachadorCodDespachadorOfRutasListNewRutas);
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
                Integer id = despachador.getCodDespachador();
                if (findDespachador(id) == null) {
                    throw new NonexistentEntityException("The despachador with id " + id + " no longer exists.");
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
            Despachador despachador;
            try {
                despachador = em.getReference(Despachador.class, id);
                despachador.getCodDespachador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The despachador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Rutas> rutasListOrphanCheck = despachador.getRutasList();
            for (Rutas rutasListOrphanCheckRutas : rutasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Despachador (" + despachador + ") cannot be destroyed since the Rutas " + rutasListOrphanCheckRutas + " in its rutasList field has a non-nullable despachadorCodDespachador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario usuarioidUsuario = despachador.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getDespachadorList().remove(despachador);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            em.remove(despachador);
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

    public List<Despachador> findDespachadorEntities() {
        return findDespachadorEntities(true, -1, -1);
    }

    public List<Despachador> findDespachadorEntities(int maxResults, int firstResult) {
        return findDespachadorEntities(false, maxResults, firstResult);
    }

    private List<Despachador> findDespachadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Despachador.class));
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

    public Despachador findDespachador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Despachador.class, id);
        } finally {
            em.close();
        }
    }

    public int getDespachadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Despachador> rt = cq.from(Despachador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

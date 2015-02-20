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
public class RutasJpaController implements Serializable {

    public RutasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rutas rutas) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (rutas.getVehiculoHasRutasList() == null) {
            rutas.setVehiculoHasRutasList(new ArrayList<VehiculoHasRutas>());
        }
        if (rutas.getPasajeHasRutasList() == null) {
            rutas.setPasajeHasRutasList(new ArrayList<PasajeHasRutas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Despachador despachadorCodDespachador = rutas.getDespachadorCodDespachador();
            if (despachadorCodDespachador != null) {
                despachadorCodDespachador = em.getReference(despachadorCodDespachador.getClass(), despachadorCodDespachador.getCodDespachador());
                rutas.setDespachadorCodDespachador(despachadorCodDespachador);
            }
            List<VehiculoHasRutas> attachedVehiculoHasRutasList = new ArrayList<VehiculoHasRutas>();
            for (VehiculoHasRutas vehiculoHasRutasListVehiculoHasRutasToAttach : rutas.getVehiculoHasRutasList()) {
                vehiculoHasRutasListVehiculoHasRutasToAttach = em.getReference(vehiculoHasRutasListVehiculoHasRutasToAttach.getClass(), vehiculoHasRutasListVehiculoHasRutasToAttach.getIdVehiculosRuta());
                attachedVehiculoHasRutasList.add(vehiculoHasRutasListVehiculoHasRutasToAttach);
            }
            rutas.setVehiculoHasRutasList(attachedVehiculoHasRutasList);
            List<PasajeHasRutas> attachedPasajeHasRutasList = new ArrayList<PasajeHasRutas>();
            for (PasajeHasRutas pasajeHasRutasListPasajeHasRutasToAttach : rutas.getPasajeHasRutasList()) {
                pasajeHasRutasListPasajeHasRutasToAttach = em.getReference(pasajeHasRutasListPasajeHasRutasToAttach.getClass(), pasajeHasRutasListPasajeHasRutasToAttach.getIdPasajeRuta());
                attachedPasajeHasRutasList.add(pasajeHasRutasListPasajeHasRutasToAttach);
            }
            rutas.setPasajeHasRutasList(attachedPasajeHasRutasList);
            em.persist(rutas);
            if (despachadorCodDespachador != null) {
                despachadorCodDespachador.getRutasList().add(rutas);
                despachadorCodDespachador = em.merge(despachadorCodDespachador);
            }
            for (VehiculoHasRutas vehiculoHasRutasListVehiculoHasRutas : rutas.getVehiculoHasRutasList()) {
                Rutas oldRutasCodigoRutaOfVehiculoHasRutasListVehiculoHasRutas = vehiculoHasRutasListVehiculoHasRutas.getRutasCodigoRuta();
                vehiculoHasRutasListVehiculoHasRutas.setRutasCodigoRuta(rutas);
                vehiculoHasRutasListVehiculoHasRutas = em.merge(vehiculoHasRutasListVehiculoHasRutas);
                if (oldRutasCodigoRutaOfVehiculoHasRutasListVehiculoHasRutas != null) {
                    oldRutasCodigoRutaOfVehiculoHasRutasListVehiculoHasRutas.getVehiculoHasRutasList().remove(vehiculoHasRutasListVehiculoHasRutas);
                    oldRutasCodigoRutaOfVehiculoHasRutasListVehiculoHasRutas = em.merge(oldRutasCodigoRutaOfVehiculoHasRutasListVehiculoHasRutas);
                }
            }
            for (PasajeHasRutas pasajeHasRutasListPasajeHasRutas : rutas.getPasajeHasRutasList()) {
                Rutas oldRutasCodigoRutaOfPasajeHasRutasListPasajeHasRutas = pasajeHasRutasListPasajeHasRutas.getRutasCodigoRuta();
                pasajeHasRutasListPasajeHasRutas.setRutasCodigoRuta(rutas);
                pasajeHasRutasListPasajeHasRutas = em.merge(pasajeHasRutasListPasajeHasRutas);
                if (oldRutasCodigoRutaOfPasajeHasRutasListPasajeHasRutas != null) {
                    oldRutasCodigoRutaOfPasajeHasRutasListPasajeHasRutas.getPasajeHasRutasList().remove(pasajeHasRutasListPasajeHasRutas);
                    oldRutasCodigoRutaOfPasajeHasRutasListPasajeHasRutas = em.merge(oldRutasCodigoRutaOfPasajeHasRutasListPasajeHasRutas);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRutas(rutas.getCodigoRuta()) != null) {
                throw new PreexistingEntityException("Rutas " + rutas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rutas rutas) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rutas persistentRutas = em.find(Rutas.class, rutas.getCodigoRuta());
            Despachador despachadorCodDespachadorOld = persistentRutas.getDespachadorCodDespachador();
            Despachador despachadorCodDespachadorNew = rutas.getDespachadorCodDespachador();
            List<VehiculoHasRutas> vehiculoHasRutasListOld = persistentRutas.getVehiculoHasRutasList();
            List<VehiculoHasRutas> vehiculoHasRutasListNew = rutas.getVehiculoHasRutasList();
            List<PasajeHasRutas> pasajeHasRutasListOld = persistentRutas.getPasajeHasRutasList();
            List<PasajeHasRutas> pasajeHasRutasListNew = rutas.getPasajeHasRutasList();
            List<String> illegalOrphanMessages = null;
            for (VehiculoHasRutas vehiculoHasRutasListOldVehiculoHasRutas : vehiculoHasRutasListOld) {
                if (!vehiculoHasRutasListNew.contains(vehiculoHasRutasListOldVehiculoHasRutas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VehiculoHasRutas " + vehiculoHasRutasListOldVehiculoHasRutas + " since its rutasCodigoRuta field is not nullable.");
                }
            }
            for (PasajeHasRutas pasajeHasRutasListOldPasajeHasRutas : pasajeHasRutasListOld) {
                if (!pasajeHasRutasListNew.contains(pasajeHasRutasListOldPasajeHasRutas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PasajeHasRutas " + pasajeHasRutasListOldPasajeHasRutas + " since its rutasCodigoRuta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (despachadorCodDespachadorNew != null) {
                despachadorCodDespachadorNew = em.getReference(despachadorCodDespachadorNew.getClass(), despachadorCodDespachadorNew.getCodDespachador());
                rutas.setDespachadorCodDespachador(despachadorCodDespachadorNew);
            }
            List<VehiculoHasRutas> attachedVehiculoHasRutasListNew = new ArrayList<VehiculoHasRutas>();
            for (VehiculoHasRutas vehiculoHasRutasListNewVehiculoHasRutasToAttach : vehiculoHasRutasListNew) {
                vehiculoHasRutasListNewVehiculoHasRutasToAttach = em.getReference(vehiculoHasRutasListNewVehiculoHasRutasToAttach.getClass(), vehiculoHasRutasListNewVehiculoHasRutasToAttach.getIdVehiculosRuta());
                attachedVehiculoHasRutasListNew.add(vehiculoHasRutasListNewVehiculoHasRutasToAttach);
            }
            vehiculoHasRutasListNew = attachedVehiculoHasRutasListNew;
            rutas.setVehiculoHasRutasList(vehiculoHasRutasListNew);
            List<PasajeHasRutas> attachedPasajeHasRutasListNew = new ArrayList<PasajeHasRutas>();
            for (PasajeHasRutas pasajeHasRutasListNewPasajeHasRutasToAttach : pasajeHasRutasListNew) {
                pasajeHasRutasListNewPasajeHasRutasToAttach = em.getReference(pasajeHasRutasListNewPasajeHasRutasToAttach.getClass(), pasajeHasRutasListNewPasajeHasRutasToAttach.getIdPasajeRuta());
                attachedPasajeHasRutasListNew.add(pasajeHasRutasListNewPasajeHasRutasToAttach);
            }
            pasajeHasRutasListNew = attachedPasajeHasRutasListNew;
            rutas.setPasajeHasRutasList(pasajeHasRutasListNew);
            rutas = em.merge(rutas);
            if (despachadorCodDespachadorOld != null && !despachadorCodDespachadorOld.equals(despachadorCodDespachadorNew)) {
                despachadorCodDespachadorOld.getRutasList().remove(rutas);
                despachadorCodDespachadorOld = em.merge(despachadorCodDespachadorOld);
            }
            if (despachadorCodDespachadorNew != null && !despachadorCodDespachadorNew.equals(despachadorCodDespachadorOld)) {
                despachadorCodDespachadorNew.getRutasList().add(rutas);
                despachadorCodDespachadorNew = em.merge(despachadorCodDespachadorNew);
            }
            for (VehiculoHasRutas vehiculoHasRutasListNewVehiculoHasRutas : vehiculoHasRutasListNew) {
                if (!vehiculoHasRutasListOld.contains(vehiculoHasRutasListNewVehiculoHasRutas)) {
                    Rutas oldRutasCodigoRutaOfVehiculoHasRutasListNewVehiculoHasRutas = vehiculoHasRutasListNewVehiculoHasRutas.getRutasCodigoRuta();
                    vehiculoHasRutasListNewVehiculoHasRutas.setRutasCodigoRuta(rutas);
                    vehiculoHasRutasListNewVehiculoHasRutas = em.merge(vehiculoHasRutasListNewVehiculoHasRutas);
                    if (oldRutasCodigoRutaOfVehiculoHasRutasListNewVehiculoHasRutas != null && !oldRutasCodigoRutaOfVehiculoHasRutasListNewVehiculoHasRutas.equals(rutas)) {
                        oldRutasCodigoRutaOfVehiculoHasRutasListNewVehiculoHasRutas.getVehiculoHasRutasList().remove(vehiculoHasRutasListNewVehiculoHasRutas);
                        oldRutasCodigoRutaOfVehiculoHasRutasListNewVehiculoHasRutas = em.merge(oldRutasCodigoRutaOfVehiculoHasRutasListNewVehiculoHasRutas);
                    }
                }
            }
            for (PasajeHasRutas pasajeHasRutasListNewPasajeHasRutas : pasajeHasRutasListNew) {
                if (!pasajeHasRutasListOld.contains(pasajeHasRutasListNewPasajeHasRutas)) {
                    Rutas oldRutasCodigoRutaOfPasajeHasRutasListNewPasajeHasRutas = pasajeHasRutasListNewPasajeHasRutas.getRutasCodigoRuta();
                    pasajeHasRutasListNewPasajeHasRutas.setRutasCodigoRuta(rutas);
                    pasajeHasRutasListNewPasajeHasRutas = em.merge(pasajeHasRutasListNewPasajeHasRutas);
                    if (oldRutasCodigoRutaOfPasajeHasRutasListNewPasajeHasRutas != null && !oldRutasCodigoRutaOfPasajeHasRutasListNewPasajeHasRutas.equals(rutas)) {
                        oldRutasCodigoRutaOfPasajeHasRutasListNewPasajeHasRutas.getPasajeHasRutasList().remove(pasajeHasRutasListNewPasajeHasRutas);
                        oldRutasCodigoRutaOfPasajeHasRutasListNewPasajeHasRutas = em.merge(oldRutasCodigoRutaOfPasajeHasRutasListNewPasajeHasRutas);
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
                Integer id = rutas.getCodigoRuta();
                if (findRutas(id) == null) {
                    throw new NonexistentEntityException("The rutas with id " + id + " no longer exists.");
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
            Rutas rutas;
            try {
                rutas = em.getReference(Rutas.class, id);
                rutas.getCodigoRuta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rutas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<VehiculoHasRutas> vehiculoHasRutasListOrphanCheck = rutas.getVehiculoHasRutasList();
            for (VehiculoHasRutas vehiculoHasRutasListOrphanCheckVehiculoHasRutas : vehiculoHasRutasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rutas (" + rutas + ") cannot be destroyed since the VehiculoHasRutas " + vehiculoHasRutasListOrphanCheckVehiculoHasRutas + " in its vehiculoHasRutasList field has a non-nullable rutasCodigoRuta field.");
            }
            List<PasajeHasRutas> pasajeHasRutasListOrphanCheck = rutas.getPasajeHasRutasList();
            for (PasajeHasRutas pasajeHasRutasListOrphanCheckPasajeHasRutas : pasajeHasRutasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rutas (" + rutas + ") cannot be destroyed since the PasajeHasRutas " + pasajeHasRutasListOrphanCheckPasajeHasRutas + " in its pasajeHasRutasList field has a non-nullable rutasCodigoRuta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Despachador despachadorCodDespachador = rutas.getDespachadorCodDespachador();
            if (despachadorCodDespachador != null) {
                despachadorCodDespachador.getRutasList().remove(rutas);
                despachadorCodDespachador = em.merge(despachadorCodDespachador);
            }
            em.remove(rutas);
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

    public List<Rutas> findRutasEntities() {
        return findRutasEntities(true, -1, -1);
    }

    public List<Rutas> findRutasEntities(int maxResults, int firstResult) {
        return findRutasEntities(false, maxResults, firstResult);
    }

    private List<Rutas> findRutasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rutas.class));
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

    public Rutas findRutas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rutas.class, id);
        } finally {
            em.close();
        }
    }

    public int getRutasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rutas> rt = cq.from(Rutas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

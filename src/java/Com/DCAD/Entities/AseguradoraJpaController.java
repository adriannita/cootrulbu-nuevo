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
public class AseguradoraJpaController implements Serializable {

    public AseguradoraJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Aseguradora aseguradora) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (aseguradora.getTecnomecanicaList() == null) {
            aseguradora.setTecnomecanicaList(new ArrayList<Tecnomecanica>());
        }
        if (aseguradora.getSoatList() == null) {
            aseguradora.setSoatList(new ArrayList<Soat>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Vehiculo vehiculoPlaca = aseguradora.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca = em.getReference(vehiculoPlaca.getClass(), vehiculoPlaca.getPlaca());
                aseguradora.setVehiculoPlaca(vehiculoPlaca);
            }
            List<Tecnomecanica> attachedTecnomecanicaList = new ArrayList<Tecnomecanica>();
            for (Tecnomecanica tecnomecanicaListTecnomecanicaToAttach : aseguradora.getTecnomecanicaList()) {
                tecnomecanicaListTecnomecanicaToAttach = em.getReference(tecnomecanicaListTecnomecanicaToAttach.getClass(), tecnomecanicaListTecnomecanicaToAttach.getNumControl());
                attachedTecnomecanicaList.add(tecnomecanicaListTecnomecanicaToAttach);
            }
            aseguradora.setTecnomecanicaList(attachedTecnomecanicaList);
            List<Soat> attachedSoatList = new ArrayList<Soat>();
            for (Soat soatListSoatToAttach : aseguradora.getSoatList()) {
                soatListSoatToAttach = em.getReference(soatListSoatToAttach.getClass(), soatListSoatToAttach.getIsSoat());
                attachedSoatList.add(soatListSoatToAttach);
            }
            aseguradora.setSoatList(attachedSoatList);
            em.persist(aseguradora);
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getAseguradoraList().add(aseguradora);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            for (Tecnomecanica tecnomecanicaListTecnomecanica : aseguradora.getTecnomecanicaList()) {
                Aseguradora oldAseguradoraCodAseguradorOfTecnomecanicaListTecnomecanica = tecnomecanicaListTecnomecanica.getAseguradoraCodAsegurador();
                tecnomecanicaListTecnomecanica.setAseguradoraCodAsegurador(aseguradora);
                tecnomecanicaListTecnomecanica = em.merge(tecnomecanicaListTecnomecanica);
                if (oldAseguradoraCodAseguradorOfTecnomecanicaListTecnomecanica != null) {
                    oldAseguradoraCodAseguradorOfTecnomecanicaListTecnomecanica.getTecnomecanicaList().remove(tecnomecanicaListTecnomecanica);
                    oldAseguradoraCodAseguradorOfTecnomecanicaListTecnomecanica = em.merge(oldAseguradoraCodAseguradorOfTecnomecanicaListTecnomecanica);
                }
            }
            for (Soat soatListSoat : aseguradora.getSoatList()) {
                Aseguradora oldAseguradoraCodAseguradorOfSoatListSoat = soatListSoat.getAseguradoraCodAsegurador();
                soatListSoat.setAseguradoraCodAsegurador(aseguradora);
                soatListSoat = em.merge(soatListSoat);
                if (oldAseguradoraCodAseguradorOfSoatListSoat != null) {
                    oldAseguradoraCodAseguradorOfSoatListSoat.getSoatList().remove(soatListSoat);
                    oldAseguradoraCodAseguradorOfSoatListSoat = em.merge(oldAseguradoraCodAseguradorOfSoatListSoat);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAseguradora(aseguradora.getCodAsegurador()) != null) {
                throw new PreexistingEntityException("Aseguradora " + aseguradora + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Aseguradora aseguradora) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Aseguradora persistentAseguradora = em.find(Aseguradora.class, aseguradora.getCodAsegurador());
            Vehiculo vehiculoPlacaOld = persistentAseguradora.getVehiculoPlaca();
            Vehiculo vehiculoPlacaNew = aseguradora.getVehiculoPlaca();
            List<Tecnomecanica> tecnomecanicaListOld = persistentAseguradora.getTecnomecanicaList();
            List<Tecnomecanica> tecnomecanicaListNew = aseguradora.getTecnomecanicaList();
            List<Soat> soatListOld = persistentAseguradora.getSoatList();
            List<Soat> soatListNew = aseguradora.getSoatList();
            List<String> illegalOrphanMessages = null;
            for (Tecnomecanica tecnomecanicaListOldTecnomecanica : tecnomecanicaListOld) {
                if (!tecnomecanicaListNew.contains(tecnomecanicaListOldTecnomecanica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tecnomecanica " + tecnomecanicaListOldTecnomecanica + " since its aseguradoraCodAsegurador field is not nullable.");
                }
            }
            for (Soat soatListOldSoat : soatListOld) {
                if (!soatListNew.contains(soatListOldSoat)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Soat " + soatListOldSoat + " since its aseguradoraCodAsegurador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (vehiculoPlacaNew != null) {
                vehiculoPlacaNew = em.getReference(vehiculoPlacaNew.getClass(), vehiculoPlacaNew.getPlaca());
                aseguradora.setVehiculoPlaca(vehiculoPlacaNew);
            }
            List<Tecnomecanica> attachedTecnomecanicaListNew = new ArrayList<Tecnomecanica>();
            for (Tecnomecanica tecnomecanicaListNewTecnomecanicaToAttach : tecnomecanicaListNew) {
                tecnomecanicaListNewTecnomecanicaToAttach = em.getReference(tecnomecanicaListNewTecnomecanicaToAttach.getClass(), tecnomecanicaListNewTecnomecanicaToAttach.getNumControl());
                attachedTecnomecanicaListNew.add(tecnomecanicaListNewTecnomecanicaToAttach);
            }
            tecnomecanicaListNew = attachedTecnomecanicaListNew;
            aseguradora.setTecnomecanicaList(tecnomecanicaListNew);
            List<Soat> attachedSoatListNew = new ArrayList<Soat>();
            for (Soat soatListNewSoatToAttach : soatListNew) {
                soatListNewSoatToAttach = em.getReference(soatListNewSoatToAttach.getClass(), soatListNewSoatToAttach.getIsSoat());
                attachedSoatListNew.add(soatListNewSoatToAttach);
            }
            soatListNew = attachedSoatListNew;
            aseguradora.setSoatList(soatListNew);
            aseguradora = em.merge(aseguradora);
            if (vehiculoPlacaOld != null && !vehiculoPlacaOld.equals(vehiculoPlacaNew)) {
                vehiculoPlacaOld.getAseguradoraList().remove(aseguradora);
                vehiculoPlacaOld = em.merge(vehiculoPlacaOld);
            }
            if (vehiculoPlacaNew != null && !vehiculoPlacaNew.equals(vehiculoPlacaOld)) {
                vehiculoPlacaNew.getAseguradoraList().add(aseguradora);
                vehiculoPlacaNew = em.merge(vehiculoPlacaNew);
            }
            for (Tecnomecanica tecnomecanicaListNewTecnomecanica : tecnomecanicaListNew) {
                if (!tecnomecanicaListOld.contains(tecnomecanicaListNewTecnomecanica)) {
                    Aseguradora oldAseguradoraCodAseguradorOfTecnomecanicaListNewTecnomecanica = tecnomecanicaListNewTecnomecanica.getAseguradoraCodAsegurador();
                    tecnomecanicaListNewTecnomecanica.setAseguradoraCodAsegurador(aseguradora);
                    tecnomecanicaListNewTecnomecanica = em.merge(tecnomecanicaListNewTecnomecanica);
                    if (oldAseguradoraCodAseguradorOfTecnomecanicaListNewTecnomecanica != null && !oldAseguradoraCodAseguradorOfTecnomecanicaListNewTecnomecanica.equals(aseguradora)) {
                        oldAseguradoraCodAseguradorOfTecnomecanicaListNewTecnomecanica.getTecnomecanicaList().remove(tecnomecanicaListNewTecnomecanica);
                        oldAseguradoraCodAseguradorOfTecnomecanicaListNewTecnomecanica = em.merge(oldAseguradoraCodAseguradorOfTecnomecanicaListNewTecnomecanica);
                    }
                }
            }
            for (Soat soatListNewSoat : soatListNew) {
                if (!soatListOld.contains(soatListNewSoat)) {
                    Aseguradora oldAseguradoraCodAseguradorOfSoatListNewSoat = soatListNewSoat.getAseguradoraCodAsegurador();
                    soatListNewSoat.setAseguradoraCodAsegurador(aseguradora);
                    soatListNewSoat = em.merge(soatListNewSoat);
                    if (oldAseguradoraCodAseguradorOfSoatListNewSoat != null && !oldAseguradoraCodAseguradorOfSoatListNewSoat.equals(aseguradora)) {
                        oldAseguradoraCodAseguradorOfSoatListNewSoat.getSoatList().remove(soatListNewSoat);
                        oldAseguradoraCodAseguradorOfSoatListNewSoat = em.merge(oldAseguradoraCodAseguradorOfSoatListNewSoat);
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
                Integer id = aseguradora.getCodAsegurador();
                if (findAseguradora(id) == null) {
                    throw new NonexistentEntityException("The aseguradora with id " + id + " no longer exists.");
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
            Aseguradora aseguradora;
            try {
                aseguradora = em.getReference(Aseguradora.class, id);
                aseguradora.getCodAsegurador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aseguradora with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tecnomecanica> tecnomecanicaListOrphanCheck = aseguradora.getTecnomecanicaList();
            for (Tecnomecanica tecnomecanicaListOrphanCheckTecnomecanica : tecnomecanicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Aseguradora (" + aseguradora + ") cannot be destroyed since the Tecnomecanica " + tecnomecanicaListOrphanCheckTecnomecanica + " in its tecnomecanicaList field has a non-nullable aseguradoraCodAsegurador field.");
            }
            List<Soat> soatListOrphanCheck = aseguradora.getSoatList();
            for (Soat soatListOrphanCheckSoat : soatListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Aseguradora (" + aseguradora + ") cannot be destroyed since the Soat " + soatListOrphanCheckSoat + " in its soatList field has a non-nullable aseguradoraCodAsegurador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Vehiculo vehiculoPlaca = aseguradora.getVehiculoPlaca();
            if (vehiculoPlaca != null) {
                vehiculoPlaca.getAseguradoraList().remove(aseguradora);
                vehiculoPlaca = em.merge(vehiculoPlaca);
            }
            em.remove(aseguradora);
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

    public List<Aseguradora> findAseguradoraEntities() {
        return findAseguradoraEntities(true, -1, -1);
    }

    public List<Aseguradora> findAseguradoraEntities(int maxResults, int firstResult) {
        return findAseguradoraEntities(false, maxResults, firstResult);
    }

    private List<Aseguradora> findAseguradoraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Aseguradora.class));
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

    public Aseguradora findAseguradora(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Aseguradora.class, id);
        } finally {
            em.close();
        }
    }

    public int getAseguradoraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Aseguradora> rt = cq.from(Aseguradora.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

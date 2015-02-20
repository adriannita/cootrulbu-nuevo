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
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (usuario.getDespachadorList() == null) {
            usuario.setDespachadorList(new ArrayList<Despachador>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cargo cargoidCargo = usuario.getCargoidCargo();
            if (cargoidCargo != null) {
                cargoidCargo = em.getReference(cargoidCargo.getClass(), cargoidCargo.getIdCargo());
                usuario.setCargoidCargo(cargoidCargo);
            }
            Roles rolesCodRol = usuario.getRolesCodRol();
            if (rolesCodRol != null) {
                rolesCodRol = em.getReference(rolesCodRol.getClass(), rolesCodRol.getCodRol());
                usuario.setRolesCodRol(rolesCodRol);
            }
            List<Despachador> attachedDespachadorList = new ArrayList<Despachador>();
            for (Despachador despachadorListDespachadorToAttach : usuario.getDespachadorList()) {
                despachadorListDespachadorToAttach = em.getReference(despachadorListDespachadorToAttach.getClass(), despachadorListDespachadorToAttach.getCodDespachador());
                attachedDespachadorList.add(despachadorListDespachadorToAttach);
            }
            usuario.setDespachadorList(attachedDespachadorList);
            em.persist(usuario);
            if (cargoidCargo != null) {
                cargoidCargo.getUsuarioList().add(usuario);
                cargoidCargo = em.merge(cargoidCargo);
            }
            if (rolesCodRol != null) {
                rolesCodRol.getUsuarioList().add(usuario);
                rolesCodRol = em.merge(rolesCodRol);
            }
            for (Despachador despachadorListDespachador : usuario.getDespachadorList()) {
                Usuario oldUsuarioidUsuarioOfDespachadorListDespachador = despachadorListDespachador.getUsuarioidUsuario();
                despachadorListDespachador.setUsuarioidUsuario(usuario);
                despachadorListDespachador = em.merge(despachadorListDespachador);
                if (oldUsuarioidUsuarioOfDespachadorListDespachador != null) {
                    oldUsuarioidUsuarioOfDespachadorListDespachador.getDespachadorList().remove(despachadorListDespachador);
                    oldUsuarioidUsuarioOfDespachadorListDespachador = em.merge(oldUsuarioidUsuarioOfDespachadorListDespachador);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsuario(usuario.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Cargo cargoidCargoOld = persistentUsuario.getCargoidCargo();
            Cargo cargoidCargoNew = usuario.getCargoidCargo();
            Roles rolesCodRolOld = persistentUsuario.getRolesCodRol();
            Roles rolesCodRolNew = usuario.getRolesCodRol();
            List<Despachador> despachadorListOld = persistentUsuario.getDespachadorList();
            List<Despachador> despachadorListNew = usuario.getDespachadorList();
            List<String> illegalOrphanMessages = null;
            for (Despachador despachadorListOldDespachador : despachadorListOld) {
                if (!despachadorListNew.contains(despachadorListOldDespachador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Despachador " + despachadorListOldDespachador + " since its usuarioidUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cargoidCargoNew != null) {
                cargoidCargoNew = em.getReference(cargoidCargoNew.getClass(), cargoidCargoNew.getIdCargo());
                usuario.setCargoidCargo(cargoidCargoNew);
            }
            if (rolesCodRolNew != null) {
                rolesCodRolNew = em.getReference(rolesCodRolNew.getClass(), rolesCodRolNew.getCodRol());
                usuario.setRolesCodRol(rolesCodRolNew);
            }
            List<Despachador> attachedDespachadorListNew = new ArrayList<Despachador>();
            for (Despachador despachadorListNewDespachadorToAttach : despachadorListNew) {
                despachadorListNewDespachadorToAttach = em.getReference(despachadorListNewDespachadorToAttach.getClass(), despachadorListNewDespachadorToAttach.getCodDespachador());
                attachedDespachadorListNew.add(despachadorListNewDespachadorToAttach);
            }
            despachadorListNew = attachedDespachadorListNew;
            usuario.setDespachadorList(despachadorListNew);
            usuario = em.merge(usuario);
            if (cargoidCargoOld != null && !cargoidCargoOld.equals(cargoidCargoNew)) {
                cargoidCargoOld.getUsuarioList().remove(usuario);
                cargoidCargoOld = em.merge(cargoidCargoOld);
            }
            if (cargoidCargoNew != null && !cargoidCargoNew.equals(cargoidCargoOld)) {
                cargoidCargoNew.getUsuarioList().add(usuario);
                cargoidCargoNew = em.merge(cargoidCargoNew);
            }
            if (rolesCodRolOld != null && !rolesCodRolOld.equals(rolesCodRolNew)) {
                rolesCodRolOld.getUsuarioList().remove(usuario);
                rolesCodRolOld = em.merge(rolesCodRolOld);
            }
            if (rolesCodRolNew != null && !rolesCodRolNew.equals(rolesCodRolOld)) {
                rolesCodRolNew.getUsuarioList().add(usuario);
                rolesCodRolNew = em.merge(rolesCodRolNew);
            }
            for (Despachador despachadorListNewDespachador : despachadorListNew) {
                if (!despachadorListOld.contains(despachadorListNewDespachador)) {
                    Usuario oldUsuarioidUsuarioOfDespachadorListNewDespachador = despachadorListNewDespachador.getUsuarioidUsuario();
                    despachadorListNewDespachador.setUsuarioidUsuario(usuario);
                    despachadorListNewDespachador = em.merge(despachadorListNewDespachador);
                    if (oldUsuarioidUsuarioOfDespachadorListNewDespachador != null && !oldUsuarioidUsuarioOfDespachadorListNewDespachador.equals(usuario)) {
                        oldUsuarioidUsuarioOfDespachadorListNewDespachador.getDespachadorList().remove(despachadorListNewDespachador);
                        oldUsuarioidUsuarioOfDespachadorListNewDespachador = em.merge(oldUsuarioidUsuarioOfDespachadorListNewDespachador);
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
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Despachador> despachadorListOrphanCheck = usuario.getDespachadorList();
            for (Despachador despachadorListOrphanCheckDespachador : despachadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Despachador " + despachadorListOrphanCheckDespachador + " in its despachadorList field has a non-nullable usuarioidUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cargo cargoidCargo = usuario.getCargoidCargo();
            if (cargoidCargo != null) {
                cargoidCargo.getUsuarioList().remove(usuario);
                cargoidCargo = em.merge(cargoidCargo);
            }
            Roles rolesCodRol = usuario.getRolesCodRol();
            if (rolesCodRol != null) {
                rolesCodRol.getUsuarioList().remove(usuario);
                rolesCodRol = em.merge(rolesCodRol);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

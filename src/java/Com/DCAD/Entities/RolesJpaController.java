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
public class RolesJpaController implements Serializable {

    public RolesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Roles roles) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (roles.getPermisosHasRolesList() == null) {
            roles.setPermisosHasRolesList(new ArrayList<PermisosHasRoles>());
        }
        if (roles.getUsuarioList() == null) {
            roles.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PermisosHasRoles> attachedPermisosHasRolesList = new ArrayList<PermisosHasRoles>();
            for (PermisosHasRoles permisosHasRolesListPermisosHasRolesToAttach : roles.getPermisosHasRolesList()) {
                permisosHasRolesListPermisosHasRolesToAttach = em.getReference(permisosHasRolesListPermisosHasRolesToAttach.getClass(), permisosHasRolesListPermisosHasRolesToAttach.getIdPermisoshasRolescol());
                attachedPermisosHasRolesList.add(permisosHasRolesListPermisosHasRolesToAttach);
            }
            roles.setPermisosHasRolesList(attachedPermisosHasRolesList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : roles.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            roles.setUsuarioList(attachedUsuarioList);
            em.persist(roles);
            for (PermisosHasRoles permisosHasRolesListPermisosHasRoles : roles.getPermisosHasRolesList()) {
                Roles oldRolesCodRolOfPermisosHasRolesListPermisosHasRoles = permisosHasRolesListPermisosHasRoles.getRolesCodRol();
                permisosHasRolesListPermisosHasRoles.setRolesCodRol(roles);
                permisosHasRolesListPermisosHasRoles = em.merge(permisosHasRolesListPermisosHasRoles);
                if (oldRolesCodRolOfPermisosHasRolesListPermisosHasRoles != null) {
                    oldRolesCodRolOfPermisosHasRolesListPermisosHasRoles.getPermisosHasRolesList().remove(permisosHasRolesListPermisosHasRoles);
                    oldRolesCodRolOfPermisosHasRolesListPermisosHasRoles = em.merge(oldRolesCodRolOfPermisosHasRolesListPermisosHasRoles);
                }
            }
            for (Usuario usuarioListUsuario : roles.getUsuarioList()) {
                Roles oldRolesCodRolOfUsuarioListUsuario = usuarioListUsuario.getRolesCodRol();
                usuarioListUsuario.setRolesCodRol(roles);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldRolesCodRolOfUsuarioListUsuario != null) {
                    oldRolesCodRolOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldRolesCodRolOfUsuarioListUsuario = em.merge(oldRolesCodRolOfUsuarioListUsuario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRoles(roles.getCodRol()) != null) {
                throw new PreexistingEntityException("Roles " + roles + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Roles roles) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Roles persistentRoles = em.find(Roles.class, roles.getCodRol());
            List<PermisosHasRoles> permisosHasRolesListOld = persistentRoles.getPermisosHasRolesList();
            List<PermisosHasRoles> permisosHasRolesListNew = roles.getPermisosHasRolesList();
            List<Usuario> usuarioListOld = persistentRoles.getUsuarioList();
            List<Usuario> usuarioListNew = roles.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (PermisosHasRoles permisosHasRolesListOldPermisosHasRoles : permisosHasRolesListOld) {
                if (!permisosHasRolesListNew.contains(permisosHasRolesListOldPermisosHasRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PermisosHasRoles " + permisosHasRolesListOldPermisosHasRoles + " since its rolesCodRol field is not nullable.");
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its rolesCodRol field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PermisosHasRoles> attachedPermisosHasRolesListNew = new ArrayList<PermisosHasRoles>();
            for (PermisosHasRoles permisosHasRolesListNewPermisosHasRolesToAttach : permisosHasRolesListNew) {
                permisosHasRolesListNewPermisosHasRolesToAttach = em.getReference(permisosHasRolesListNewPermisosHasRolesToAttach.getClass(), permisosHasRolesListNewPermisosHasRolesToAttach.getIdPermisoshasRolescol());
                attachedPermisosHasRolesListNew.add(permisosHasRolesListNewPermisosHasRolesToAttach);
            }
            permisosHasRolesListNew = attachedPermisosHasRolesListNew;
            roles.setPermisosHasRolesList(permisosHasRolesListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            roles.setUsuarioList(usuarioListNew);
            roles = em.merge(roles);
            for (PermisosHasRoles permisosHasRolesListNewPermisosHasRoles : permisosHasRolesListNew) {
                if (!permisosHasRolesListOld.contains(permisosHasRolesListNewPermisosHasRoles)) {
                    Roles oldRolesCodRolOfPermisosHasRolesListNewPermisosHasRoles = permisosHasRolesListNewPermisosHasRoles.getRolesCodRol();
                    permisosHasRolesListNewPermisosHasRoles.setRolesCodRol(roles);
                    permisosHasRolesListNewPermisosHasRoles = em.merge(permisosHasRolesListNewPermisosHasRoles);
                    if (oldRolesCodRolOfPermisosHasRolesListNewPermisosHasRoles != null && !oldRolesCodRolOfPermisosHasRolesListNewPermisosHasRoles.equals(roles)) {
                        oldRolesCodRolOfPermisosHasRolesListNewPermisosHasRoles.getPermisosHasRolesList().remove(permisosHasRolesListNewPermisosHasRoles);
                        oldRolesCodRolOfPermisosHasRolesListNewPermisosHasRoles = em.merge(oldRolesCodRolOfPermisosHasRolesListNewPermisosHasRoles);
                    }
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Roles oldRolesCodRolOfUsuarioListNewUsuario = usuarioListNewUsuario.getRolesCodRol();
                    usuarioListNewUsuario.setRolesCodRol(roles);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldRolesCodRolOfUsuarioListNewUsuario != null && !oldRolesCodRolOfUsuarioListNewUsuario.equals(roles)) {
                        oldRolesCodRolOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldRolesCodRolOfUsuarioListNewUsuario = em.merge(oldRolesCodRolOfUsuarioListNewUsuario);
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
                Integer id = roles.getCodRol();
                if (findRoles(id) == null) {
                    throw new NonexistentEntityException("The roles with id " + id + " no longer exists.");
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
            Roles roles;
            try {
                roles = em.getReference(Roles.class, id);
                roles.getCodRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roles with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PermisosHasRoles> permisosHasRolesListOrphanCheck = roles.getPermisosHasRolesList();
            for (PermisosHasRoles permisosHasRolesListOrphanCheckPermisosHasRoles : permisosHasRolesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Roles (" + roles + ") cannot be destroyed since the PermisosHasRoles " + permisosHasRolesListOrphanCheckPermisosHasRoles + " in its permisosHasRolesList field has a non-nullable rolesCodRol field.");
            }
            List<Usuario> usuarioListOrphanCheck = roles.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Roles (" + roles + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable rolesCodRol field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(roles);
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

    public List<Roles> findRolesEntities() {
        return findRolesEntities(true, -1, -1);
    }

    public List<Roles> findRolesEntities(int maxResults, int firstResult) {
        return findRolesEntities(false, maxResults, firstResult);
    }

    private List<Roles> findRolesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Roles.class));
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

    public Roles findRoles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Roles.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Roles> rt = cq.from(Roles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

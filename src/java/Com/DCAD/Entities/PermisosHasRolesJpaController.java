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
public class PermisosHasRolesJpaController implements Serializable {

    public PermisosHasRolesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PermisosHasRoles permisosHasRoles) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Permisos permisosidPermisos = permisosHasRoles.getPermisosidPermisos();
            if (permisosidPermisos != null) {
                permisosidPermisos = em.getReference(permisosidPermisos.getClass(), permisosidPermisos.getIdPermisos());
                permisosHasRoles.setPermisosidPermisos(permisosidPermisos);
            }
            Roles rolesCodRol = permisosHasRoles.getRolesCodRol();
            if (rolesCodRol != null) {
                rolesCodRol = em.getReference(rolesCodRol.getClass(), rolesCodRol.getCodRol());
                permisosHasRoles.setRolesCodRol(rolesCodRol);
            }
            em.persist(permisosHasRoles);
            if (permisosidPermisos != null) {
                permisosidPermisos.getPermisosHasRolesList().add(permisosHasRoles);
                permisosidPermisos = em.merge(permisosidPermisos);
            }
            if (rolesCodRol != null) {
                rolesCodRol.getPermisosHasRolesList().add(permisosHasRoles);
                rolesCodRol = em.merge(rolesCodRol);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPermisosHasRoles(permisosHasRoles.getIdPermisoshasRolescol()) != null) {
                throw new PreexistingEntityException("PermisosHasRoles " + permisosHasRoles + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PermisosHasRoles permisosHasRoles) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PermisosHasRoles persistentPermisosHasRoles = em.find(PermisosHasRoles.class, permisosHasRoles.getIdPermisoshasRolescol());
            Permisos permisosidPermisosOld = persistentPermisosHasRoles.getPermisosidPermisos();
            Permisos permisosidPermisosNew = permisosHasRoles.getPermisosidPermisos();
            Roles rolesCodRolOld = persistentPermisosHasRoles.getRolesCodRol();
            Roles rolesCodRolNew = permisosHasRoles.getRolesCodRol();
            if (permisosidPermisosNew != null) {
                permisosidPermisosNew = em.getReference(permisosidPermisosNew.getClass(), permisosidPermisosNew.getIdPermisos());
                permisosHasRoles.setPermisosidPermisos(permisosidPermisosNew);
            }
            if (rolesCodRolNew != null) {
                rolesCodRolNew = em.getReference(rolesCodRolNew.getClass(), rolesCodRolNew.getCodRol());
                permisosHasRoles.setRolesCodRol(rolesCodRolNew);
            }
            permisosHasRoles = em.merge(permisosHasRoles);
            if (permisosidPermisosOld != null && !permisosidPermisosOld.equals(permisosidPermisosNew)) {
                permisosidPermisosOld.getPermisosHasRolesList().remove(permisosHasRoles);
                permisosidPermisosOld = em.merge(permisosidPermisosOld);
            }
            if (permisosidPermisosNew != null && !permisosidPermisosNew.equals(permisosidPermisosOld)) {
                permisosidPermisosNew.getPermisosHasRolesList().add(permisosHasRoles);
                permisosidPermisosNew = em.merge(permisosidPermisosNew);
            }
            if (rolesCodRolOld != null && !rolesCodRolOld.equals(rolesCodRolNew)) {
                rolesCodRolOld.getPermisosHasRolesList().remove(permisosHasRoles);
                rolesCodRolOld = em.merge(rolesCodRolOld);
            }
            if (rolesCodRolNew != null && !rolesCodRolNew.equals(rolesCodRolOld)) {
                rolesCodRolNew.getPermisosHasRolesList().add(permisosHasRoles);
                rolesCodRolNew = em.merge(rolesCodRolNew);
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
                String id = permisosHasRoles.getIdPermisoshasRolescol();
                if (findPermisosHasRoles(id) == null) {
                    throw new NonexistentEntityException("The permisosHasRoles with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PermisosHasRoles permisosHasRoles;
            try {
                permisosHasRoles = em.getReference(PermisosHasRoles.class, id);
                permisosHasRoles.getIdPermisoshasRolescol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permisosHasRoles with id " + id + " no longer exists.", enfe);
            }
            Permisos permisosidPermisos = permisosHasRoles.getPermisosidPermisos();
            if (permisosidPermisos != null) {
                permisosidPermisos.getPermisosHasRolesList().remove(permisosHasRoles);
                permisosidPermisos = em.merge(permisosidPermisos);
            }
            Roles rolesCodRol = permisosHasRoles.getRolesCodRol();
            if (rolesCodRol != null) {
                rolesCodRol.getPermisosHasRolesList().remove(permisosHasRoles);
                rolesCodRol = em.merge(rolesCodRol);
            }
            em.remove(permisosHasRoles);
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

    public List<PermisosHasRoles> findPermisosHasRolesEntities() {
        return findPermisosHasRolesEntities(true, -1, -1);
    }

    public List<PermisosHasRoles> findPermisosHasRolesEntities(int maxResults, int firstResult) {
        return findPermisosHasRolesEntities(false, maxResults, firstResult);
    }

    private List<PermisosHasRoles> findPermisosHasRolesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PermisosHasRoles.class));
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

    public PermisosHasRoles findPermisosHasRoles(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PermisosHasRoles.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisosHasRolesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PermisosHasRoles> rt = cq.from(PermisosHasRoles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

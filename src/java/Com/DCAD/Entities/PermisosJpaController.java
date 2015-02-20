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
public class PermisosJpaController implements Serializable {

    public PermisosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permisos permisos) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (permisos.getPermisosHasRolesList() == null) {
            permisos.setPermisosHasRolesList(new ArrayList<PermisosHasRoles>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PermisosHasRoles> attachedPermisosHasRolesList = new ArrayList<PermisosHasRoles>();
            for (PermisosHasRoles permisosHasRolesListPermisosHasRolesToAttach : permisos.getPermisosHasRolesList()) {
                permisosHasRolesListPermisosHasRolesToAttach = em.getReference(permisosHasRolesListPermisosHasRolesToAttach.getClass(), permisosHasRolesListPermisosHasRolesToAttach.getIdPermisoshasRolescol());
                attachedPermisosHasRolesList.add(permisosHasRolesListPermisosHasRolesToAttach);
            }
            permisos.setPermisosHasRolesList(attachedPermisosHasRolesList);
            em.persist(permisos);
            for (PermisosHasRoles permisosHasRolesListPermisosHasRoles : permisos.getPermisosHasRolesList()) {
                Permisos oldPermisosidPermisosOfPermisosHasRolesListPermisosHasRoles = permisosHasRolesListPermisosHasRoles.getPermisosidPermisos();
                permisosHasRolesListPermisosHasRoles.setPermisosidPermisos(permisos);
                permisosHasRolesListPermisosHasRoles = em.merge(permisosHasRolesListPermisosHasRoles);
                if (oldPermisosidPermisosOfPermisosHasRolesListPermisosHasRoles != null) {
                    oldPermisosidPermisosOfPermisosHasRolesListPermisosHasRoles.getPermisosHasRolesList().remove(permisosHasRolesListPermisosHasRoles);
                    oldPermisosidPermisosOfPermisosHasRolesListPermisosHasRoles = em.merge(oldPermisosidPermisosOfPermisosHasRolesListPermisosHasRoles);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPermisos(permisos.getIdPermisos()) != null) {
                throw new PreexistingEntityException("Permisos " + permisos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permisos permisos) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Permisos persistentPermisos = em.find(Permisos.class, permisos.getIdPermisos());
            List<PermisosHasRoles> permisosHasRolesListOld = persistentPermisos.getPermisosHasRolesList();
            List<PermisosHasRoles> permisosHasRolesListNew = permisos.getPermisosHasRolesList();
            List<String> illegalOrphanMessages = null;
            for (PermisosHasRoles permisosHasRolesListOldPermisosHasRoles : permisosHasRolesListOld) {
                if (!permisosHasRolesListNew.contains(permisosHasRolesListOldPermisosHasRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PermisosHasRoles " + permisosHasRolesListOldPermisosHasRoles + " since its permisosidPermisos field is not nullable.");
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
            permisos.setPermisosHasRolesList(permisosHasRolesListNew);
            permisos = em.merge(permisos);
            for (PermisosHasRoles permisosHasRolesListNewPermisosHasRoles : permisosHasRolesListNew) {
                if (!permisosHasRolesListOld.contains(permisosHasRolesListNewPermisosHasRoles)) {
                    Permisos oldPermisosidPermisosOfPermisosHasRolesListNewPermisosHasRoles = permisosHasRolesListNewPermisosHasRoles.getPermisosidPermisos();
                    permisosHasRolesListNewPermisosHasRoles.setPermisosidPermisos(permisos);
                    permisosHasRolesListNewPermisosHasRoles = em.merge(permisosHasRolesListNewPermisosHasRoles);
                    if (oldPermisosidPermisosOfPermisosHasRolesListNewPermisosHasRoles != null && !oldPermisosidPermisosOfPermisosHasRolesListNewPermisosHasRoles.equals(permisos)) {
                        oldPermisosidPermisosOfPermisosHasRolesListNewPermisosHasRoles.getPermisosHasRolesList().remove(permisosHasRolesListNewPermisosHasRoles);
                        oldPermisosidPermisosOfPermisosHasRolesListNewPermisosHasRoles = em.merge(oldPermisosidPermisosOfPermisosHasRolesListNewPermisosHasRoles);
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
                Integer id = permisos.getIdPermisos();
                if (findPermisos(id) == null) {
                    throw new NonexistentEntityException("The permisos with id " + id + " no longer exists.");
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
            Permisos permisos;
            try {
                permisos = em.getReference(Permisos.class, id);
                permisos.getIdPermisos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permisos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PermisosHasRoles> permisosHasRolesListOrphanCheck = permisos.getPermisosHasRolesList();
            for (PermisosHasRoles permisosHasRolesListOrphanCheckPermisosHasRoles : permisosHasRolesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Permisos (" + permisos + ") cannot be destroyed since the PermisosHasRoles " + permisosHasRolesListOrphanCheckPermisosHasRoles + " in its permisosHasRolesList field has a non-nullable permisosidPermisos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(permisos);
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

    public List<Permisos> findPermisosEntities() {
        return findPermisosEntities(true, -1, -1);
    }

    public List<Permisos> findPermisosEntities(int maxResults, int firstResult) {
        return findPermisosEntities(false, maxResults, firstResult);
    }

    private List<Permisos> findPermisosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permisos.class));
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

    public Permisos findPermisos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permisos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permisos> rt = cq.from(Permisos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

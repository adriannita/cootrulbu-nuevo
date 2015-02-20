/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Com.DCAD.Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "permisos_has_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PermisosHasRoles.findAll", query = "SELECT p FROM PermisosHasRoles p"),
    @NamedQuery(name = "PermisosHasRoles.findByIdPermisoshasRolescol", query = "SELECT p FROM PermisosHasRoles p WHERE p.idPermisoshasRolescol = :idPermisoshasRolescol")})
public class PermisosHasRoles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "id_Permisos_has_Rolescol")
    private String idPermisoshasRolescol;
    @JoinColumn(name = "Permisos_id_Permisos", referencedColumnName = "id_Permisos")
    @ManyToOne(optional = false)
    private Permisos permisosidPermisos;
    @JoinColumn(name = "Roles_Cod_Rol", referencedColumnName = "Cod_Rol")
    @ManyToOne(optional = false)
    private Roles rolesCodRol;

    public PermisosHasRoles() {
    }

    public PermisosHasRoles(String idPermisoshasRolescol) {
        this.idPermisoshasRolescol = idPermisoshasRolescol;
    }

    public String getIdPermisoshasRolescol() {
        return idPermisoshasRolescol;
    }

    public void setIdPermisoshasRolescol(String idPermisoshasRolescol) {
        this.idPermisoshasRolescol = idPermisoshasRolescol;
    }

    public Permisos getPermisosidPermisos() {
        return permisosidPermisos;
    }

    public void setPermisosidPermisos(Permisos permisosidPermisos) {
        this.permisosidPermisos = permisosidPermisos;
    }

    public Roles getRolesCodRol() {
        return rolesCodRol;
    }

    public void setRolesCodRol(Roles rolesCodRol) {
        this.rolesCodRol = rolesCodRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPermisoshasRolescol != null ? idPermisoshasRolescol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisosHasRoles)) {
            return false;
        }
        PermisosHasRoles other = (PermisosHasRoles) object;
        if ((this.idPermisoshasRolescol == null && other.idPermisoshasRolescol != null) || (this.idPermisoshasRolescol != null && !this.idPermisoshasRolescol.equals(other.idPermisoshasRolescol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.PermisosHasRoles[ idPermisoshasRolescol=" + idPermisoshasRolescol + " ]";
    }
    
}

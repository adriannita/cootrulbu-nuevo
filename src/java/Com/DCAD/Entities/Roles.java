/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Com.DCAD.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Roles.findAll", query = "SELECT r FROM Roles r"),
    @NamedQuery(name = "Roles.findByCodRol", query = "SELECT r FROM Roles r WHERE r.codRol = :codRol"),
    @NamedQuery(name = "Roles.findByNombreRol", query = "SELECT r FROM Roles r WHERE r.nombreRol = :nombreRol"),
    @NamedQuery(name = "Roles.findByGerente", query = "SELECT r FROM Roles r WHERE r.gerente = :gerente"),
    @NamedQuery(name = "Roles.findByTesorera", query = "SELECT r FROM Roles r WHERE r.tesorera = :tesorera"),
    @NamedQuery(name = "Roles.findByDespachador", query = "SELECT r FROM Roles r WHERE r.despachador = :despachador")})
public class Roles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cod_Rol")
    private Integer codRol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Nombre_Rol")
    private String nombreRol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Gerente")
    private String gerente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Tesorera")
    private String tesorera;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Despachador")
    private String despachador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rolesCodRol")
    private List<PermisosHasRoles> permisosHasRolesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rolesCodRol")
    private List<Usuario> usuarioList;

    public Roles() {
    }

    public Roles(Integer codRol) {
        this.codRol = codRol;
    }

    public Roles(Integer codRol, String nombreRol, String gerente, String tesorera, String despachador) {
        this.codRol = codRol;
        this.nombreRol = nombreRol;
        this.gerente = gerente;
        this.tesorera = tesorera;
        this.despachador = despachador;
    }

    public Integer getCodRol() {
        return codRol;
    }

    public void setCodRol(Integer codRol) {
        this.codRol = codRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getGerente() {
        return gerente;
    }

    public void setGerente(String gerente) {
        this.gerente = gerente;
    }

    public String getTesorera() {
        return tesorera;
    }

    public void setTesorera(String tesorera) {
        this.tesorera = tesorera;
    }

    public String getDespachador() {
        return despachador;
    }

    public void setDespachador(String despachador) {
        this.despachador = despachador;
    }

    @XmlTransient
    public List<PermisosHasRoles> getPermisosHasRolesList() {
        return permisosHasRolesList;
    }

    public void setPermisosHasRolesList(List<PermisosHasRoles> permisosHasRolesList) {
        this.permisosHasRolesList = permisosHasRolesList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codRol != null ? codRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Roles)) {
            return false;
        }
        Roles other = (Roles) object;
        if ((this.codRol == null && other.codRol != null) || (this.codRol != null && !this.codRol.equals(other.codRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.Roles[ codRol=" + codRol + " ]";
    }
    
}

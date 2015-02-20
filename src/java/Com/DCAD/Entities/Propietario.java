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
@Table(name = "propietario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Propietario.findAll", query = "SELECT p FROM Propietario p"),
    @NamedQuery(name = "Propietario.findByCedula", query = "SELECT p FROM Propietario p WHERE p.cedula = :cedula"),
    @NamedQuery(name = "Propietario.findByNombre1", query = "SELECT p FROM Propietario p WHERE p.nombre1 = :nombre1"),
    @NamedQuery(name = "Propietario.findByNombre2", query = "SELECT p FROM Propietario p WHERE p.nombre2 = :nombre2"),
    @NamedQuery(name = "Propietario.findByApellido1", query = "SELECT p FROM Propietario p WHERE p.apellido1 = :apellido1"),
    @NamedQuery(name = "Propietario.findByApellido2", query = "SELECT p FROM Propietario p WHERE p.apellido2 = :apellido2"),
    @NamedQuery(name = "Propietario.findByDireccion", query = "SELECT p FROM Propietario p WHERE p.direccion = :direccion")})
public class Propietario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cedula")
    private Integer cedula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Nombre1")
    private String nombre1;
    @Size(max = 25)
    @Column(name = "Nombre2")
    private String nombre2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Apellido1")
    private String apellido1;
    @Size(max = 25)
    @Column(name = "Apellido2")
    private String apellido2;
    @Size(max = 45)
    @Column(name = "Direccion")
    private String direccion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propietarioCedula")
    private List<PropietarioHasVehiculo> propietarioHasVehiculoList;

    public Propietario() {
    }

    public Propietario(Integer cedula) {
        this.cedula = cedula;
    }

    public Propietario(Integer cedula, String nombre1, String apellido1) {
        this.cedula = cedula;
        this.nombre1 = nombre1;
        this.apellido1 = apellido1;
    }

    public Integer getCedula() {
        return cedula;
    }

    public void setCedula(Integer cedula) {
        this.cedula = cedula;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @XmlTransient
    public List<PropietarioHasVehiculo> getPropietarioHasVehiculoList() {
        return propietarioHasVehiculoList;
    }

    public void setPropietarioHasVehiculoList(List<PropietarioHasVehiculo> propietarioHasVehiculoList) {
        this.propietarioHasVehiculoList = propietarioHasVehiculoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedula != null ? cedula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Propietario)) {
            return false;
        }
        Propietario other = (Propietario) object;
        if ((this.cedula == null && other.cedula != null) || (this.cedula != null && !this.cedula.equals(other.cedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.Propietario[ cedula=" + cedula + " ]";
    }
    
}

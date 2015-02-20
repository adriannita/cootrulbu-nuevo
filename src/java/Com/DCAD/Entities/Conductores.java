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
@Table(name = "conductores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conductores.findAll", query = "SELECT c FROM Conductores c"),
    @NamedQuery(name = "Conductores.findByCedula", query = "SELECT c FROM Conductores c WHERE c.cedula = :cedula"),
    @NamedQuery(name = "Conductores.findByNombre1", query = "SELECT c FROM Conductores c WHERE c.nombre1 = :nombre1"),
    @NamedQuery(name = "Conductores.findByNombre2", query = "SELECT c FROM Conductores c WHERE c.nombre2 = :nombre2"),
    @NamedQuery(name = "Conductores.findByApellido1", query = "SELECT c FROM Conductores c WHERE c.apellido1 = :apellido1"),
    @NamedQuery(name = "Conductores.findByApellido2", query = "SELECT c FROM Conductores c WHERE c.apellido2 = :apellido2"),
    @NamedQuery(name = "Conductores.findByDireccion", query = "SELECT c FROM Conductores c WHERE c.direccion = :direccion"),
    @NamedQuery(name = "Conductores.findByTelefono", query = "SELECT c FROM Conductores c WHERE c.telefono = :telefono")})
public class Conductores implements Serializable {
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Telefono")
    private int telefono;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conductoresCedula")
    private List<ConductoresHasVehiculo> conductoresHasVehiculoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conductoresCedula")
    private List<LicenciaConduccion> licenciaConduccionList;

    public Conductores() {
    }

    public Conductores(Integer cedula) {
        this.cedula = cedula;
    }

    public Conductores(Integer cedula, String nombre1, String apellido1, String direccion, int telefono) {
        this.cedula = cedula;
        this.nombre1 = nombre1;
        this.apellido1 = apellido1;
        this.direccion = direccion;
        this.telefono = telefono;
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

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    @XmlTransient
    public List<ConductoresHasVehiculo> getConductoresHasVehiculoList() {
        return conductoresHasVehiculoList;
    }

    public void setConductoresHasVehiculoList(List<ConductoresHasVehiculo> conductoresHasVehiculoList) {
        this.conductoresHasVehiculoList = conductoresHasVehiculoList;
    }

    @XmlTransient
    public List<LicenciaConduccion> getLicenciaConduccionList() {
        return licenciaConduccionList;
    }

    public void setLicenciaConduccionList(List<LicenciaConduccion> licenciaConduccionList) {
        this.licenciaConduccionList = licenciaConduccionList;
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
        if (!(object instanceof Conductores)) {
            return false;
        }
        Conductores other = (Conductores) object;
        if ((this.cedula == null && other.cedula != null) || (this.cedula != null && !this.cedula.equals(other.cedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.Conductores[ cedula=" + cedula + " ]";
    }
    
}

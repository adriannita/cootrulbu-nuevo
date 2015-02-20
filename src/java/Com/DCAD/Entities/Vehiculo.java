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
@Table(name = "vehiculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vehiculo.findAll", query = "SELECT v FROM Vehiculo v"),
    @NamedQuery(name = "Vehiculo.findByPlaca", query = "SELECT v FROM Vehiculo v WHERE v.placa = :placa"),
    @NamedQuery(name = "Vehiculo.findByMatricula", query = "SELECT v FROM Vehiculo v WHERE v.matricula = :matricula"),
    @NamedQuery(name = "Vehiculo.findByModelo", query = "SELECT v FROM Vehiculo v WHERE v.modelo = :modelo"),
    @NamedQuery(name = "Vehiculo.findByColor", query = "SELECT v FROM Vehiculo v WHERE v.color = :color"),
    @NamedQuery(name = "Vehiculo.findByMarca", query = "SELECT v FROM Vehiculo v WHERE v.marca = :marca"),
    @NamedQuery(name = "Vehiculo.findByNumChasis", query = "SELECT v FROM Vehiculo v WHERE v.numChasis = :numChasis")})
public class Vehiculo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Placa")
    private String placa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Matricula")
    private String matricula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "Modelo")
    private String modelo;
    @Size(max = 10)
    @Column(name = "Color")
    private String color;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Marca")
    private String marca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Num_Chasis")
    private String numChasis;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehiculoPlaca")
    private List<PropietarioHasVehiculo> propietarioHasVehiculoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehiculoPlaca")
    private List<VehiculoHasRutas> vehiculoHasRutasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehiculoPlaca")
    private List<VehiculoHasEstado> vehiculoHasEstadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehiculoPlaca")
    private List<ConductoresHasVehiculo> conductoresHasVehiculoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehiculoPlaca")
    private List<Socios> sociosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehiculoPlaca")
    private List<Aseguradora> aseguradoraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehiculoPlaca")
    private List<VehiculoHasCentroDiagnostico> vehiculoHasCentroDiagnosticoList;

    public Vehiculo() {
    }

    public Vehiculo(String placa) {
        this.placa = placa;
    }

    public Vehiculo(String placa, String matricula, String modelo, String marca, String numChasis) {
        this.placa = placa;
        this.matricula = matricula;
        this.modelo = modelo;
        this.marca = marca;
        this.numChasis = numChasis;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNumChasis() {
        return numChasis;
    }

    public void setNumChasis(String numChasis) {
        this.numChasis = numChasis;
    }

    @XmlTransient
    public List<PropietarioHasVehiculo> getPropietarioHasVehiculoList() {
        return propietarioHasVehiculoList;
    }

    public void setPropietarioHasVehiculoList(List<PropietarioHasVehiculo> propietarioHasVehiculoList) {
        this.propietarioHasVehiculoList = propietarioHasVehiculoList;
    }

    @XmlTransient
    public List<VehiculoHasRutas> getVehiculoHasRutasList() {
        return vehiculoHasRutasList;
    }

    public void setVehiculoHasRutasList(List<VehiculoHasRutas> vehiculoHasRutasList) {
        this.vehiculoHasRutasList = vehiculoHasRutasList;
    }

    @XmlTransient
    public List<VehiculoHasEstado> getVehiculoHasEstadoList() {
        return vehiculoHasEstadoList;
    }

    public void setVehiculoHasEstadoList(List<VehiculoHasEstado> vehiculoHasEstadoList) {
        this.vehiculoHasEstadoList = vehiculoHasEstadoList;
    }

    @XmlTransient
    public List<ConductoresHasVehiculo> getConductoresHasVehiculoList() {
        return conductoresHasVehiculoList;
    }

    public void setConductoresHasVehiculoList(List<ConductoresHasVehiculo> conductoresHasVehiculoList) {
        this.conductoresHasVehiculoList = conductoresHasVehiculoList;
    }

    @XmlTransient
    public List<Socios> getSociosList() {
        return sociosList;
    }

    public void setSociosList(List<Socios> sociosList) {
        this.sociosList = sociosList;
    }

    @XmlTransient
    public List<Aseguradora> getAseguradoraList() {
        return aseguradoraList;
    }

    public void setAseguradoraList(List<Aseguradora> aseguradoraList) {
        this.aseguradoraList = aseguradoraList;
    }

    @XmlTransient
    public List<VehiculoHasCentroDiagnostico> getVehiculoHasCentroDiagnosticoList() {
        return vehiculoHasCentroDiagnosticoList;
    }

    public void setVehiculoHasCentroDiagnosticoList(List<VehiculoHasCentroDiagnostico> vehiculoHasCentroDiagnosticoList) {
        this.vehiculoHasCentroDiagnosticoList = vehiculoHasCentroDiagnosticoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (placa != null ? placa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vehiculo)) {
            return false;
        }
        Vehiculo other = (Vehiculo) object;
        if ((this.placa == null && other.placa != null) || (this.placa != null && !this.placa.equals(other.placa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.Vehiculo[ placa=" + placa + " ]";
    }
    
}

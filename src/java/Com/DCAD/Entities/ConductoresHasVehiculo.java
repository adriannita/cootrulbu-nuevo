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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "conductores_has_vehiculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConductoresHasVehiculo.findAll", query = "SELECT c FROM ConductoresHasVehiculo c"),
    @NamedQuery(name = "ConductoresHasVehiculo.findByIdConductoresVehiculocol", query = "SELECT c FROM ConductoresHasVehiculo c WHERE c.idConductoresVehiculocol = :idConductoresVehiculocol")})
public class ConductoresHasVehiculo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_Conductores_Vehiculocol")
    private Integer idConductoresVehiculocol;
    @JoinColumn(name = "Conductores_Cedula", referencedColumnName = "Cedula")
    @ManyToOne(optional = false)
    private Conductores conductoresCedula;
    @JoinColumn(name = "Vehiculo_Placa", referencedColumnName = "Placa")
    @ManyToOne(optional = false)
    private Vehiculo vehiculoPlaca;

    public ConductoresHasVehiculo() {
    }

    public ConductoresHasVehiculo(Integer idConductoresVehiculocol) {
        this.idConductoresVehiculocol = idConductoresVehiculocol;
    }

    public Integer getIdConductoresVehiculocol() {
        return idConductoresVehiculocol;
    }

    public void setIdConductoresVehiculocol(Integer idConductoresVehiculocol) {
        this.idConductoresVehiculocol = idConductoresVehiculocol;
    }

    public Conductores getConductoresCedula() {
        return conductoresCedula;
    }

    public void setConductoresCedula(Conductores conductoresCedula) {
        this.conductoresCedula = conductoresCedula;
    }

    public Vehiculo getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(Vehiculo vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConductoresVehiculocol != null ? idConductoresVehiculocol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConductoresHasVehiculo)) {
            return false;
        }
        ConductoresHasVehiculo other = (ConductoresHasVehiculo) object;
        if ((this.idConductoresVehiculocol == null && other.idConductoresVehiculocol != null) || (this.idConductoresVehiculocol != null && !this.idConductoresVehiculocol.equals(other.idConductoresVehiculocol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.ConductoresHasVehiculo[ idConductoresVehiculocol=" + idConductoresVehiculocol + " ]";
    }
    
}

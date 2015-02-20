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
@Table(name = "propietario_has_vehiculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PropietarioHasVehiculo.findAll", query = "SELECT p FROM PropietarioHasVehiculo p"),
    @NamedQuery(name = "PropietarioHasVehiculo.findByIdPropietarioVehiculo", query = "SELECT p FROM PropietarioHasVehiculo p WHERE p.idPropietarioVehiculo = :idPropietarioVehiculo")})
public class PropietarioHasVehiculo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_Propietario_Vehiculo")
    private Integer idPropietarioVehiculo;
    @JoinColumn(name = "Propietario_Cedula", referencedColumnName = "Cedula")
    @ManyToOne(optional = false)
    private Propietario propietarioCedula;
    @JoinColumn(name = "Vehiculo_Placa", referencedColumnName = "Placa")
    @ManyToOne(optional = false)
    private Vehiculo vehiculoPlaca;

    public PropietarioHasVehiculo() {
    }

    public PropietarioHasVehiculo(Integer idPropietarioVehiculo) {
        this.idPropietarioVehiculo = idPropietarioVehiculo;
    }

    public Integer getIdPropietarioVehiculo() {
        return idPropietarioVehiculo;
    }

    public void setIdPropietarioVehiculo(Integer idPropietarioVehiculo) {
        this.idPropietarioVehiculo = idPropietarioVehiculo;
    }

    public Propietario getPropietarioCedula() {
        return propietarioCedula;
    }

    public void setPropietarioCedula(Propietario propietarioCedula) {
        this.propietarioCedula = propietarioCedula;
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
        hash += (idPropietarioVehiculo != null ? idPropietarioVehiculo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PropietarioHasVehiculo)) {
            return false;
        }
        PropietarioHasVehiculo other = (PropietarioHasVehiculo) object;
        if ((this.idPropietarioVehiculo == null && other.idPropietarioVehiculo != null) || (this.idPropietarioVehiculo != null && !this.idPropietarioVehiculo.equals(other.idPropietarioVehiculo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.PropietarioHasVehiculo[ idPropietarioVehiculo=" + idPropietarioVehiculo + " ]";
    }
    
}

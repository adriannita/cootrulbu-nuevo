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
@Table(name = "vehiculo_has_estado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehiculoHasEstado.findAll", query = "SELECT v FROM VehiculoHasEstado v"),
    @NamedQuery(name = "VehiculoHasEstado.findByIdEstadoVehiculo", query = "SELECT v FROM VehiculoHasEstado v WHERE v.idEstadoVehiculo = :idEstadoVehiculo")})
public class VehiculoHasEstado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_Estado_Vehiculo")
    private Integer idEstadoVehiculo;
    @JoinColumn(name = "Estado_Vehiculo_id_Estado", referencedColumnName = "id_Estado")
    @ManyToOne(optional = false)
    private Estado estadoVehiculoidEstado;
    @JoinColumn(name = "Vehiculo_Placa", referencedColumnName = "Placa")
    @ManyToOne(optional = false)
    private Vehiculo vehiculoPlaca;

    public VehiculoHasEstado() {
    }

    public VehiculoHasEstado(Integer idEstadoVehiculo) {
        this.idEstadoVehiculo = idEstadoVehiculo;
    }

    public Integer getIdEstadoVehiculo() {
        return idEstadoVehiculo;
    }

    public void setIdEstadoVehiculo(Integer idEstadoVehiculo) {
        this.idEstadoVehiculo = idEstadoVehiculo;
    }

    public Estado getEstadoVehiculoidEstado() {
        return estadoVehiculoidEstado;
    }

    public void setEstadoVehiculoidEstado(Estado estadoVehiculoidEstado) {
        this.estadoVehiculoidEstado = estadoVehiculoidEstado;
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
        hash += (idEstadoVehiculo != null ? idEstadoVehiculo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehiculoHasEstado)) {
            return false;
        }
        VehiculoHasEstado other = (VehiculoHasEstado) object;
        if ((this.idEstadoVehiculo == null && other.idEstadoVehiculo != null) || (this.idEstadoVehiculo != null && !this.idEstadoVehiculo.equals(other.idEstadoVehiculo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.VehiculoHasEstado[ idEstadoVehiculo=" + idEstadoVehiculo + " ]";
    }
    
}

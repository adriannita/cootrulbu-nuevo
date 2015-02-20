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
@Table(name = "vehiculo_has_centro_diagnostico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehiculoHasCentroDiagnostico.findAll", query = "SELECT v FROM VehiculoHasCentroDiagnostico v"),
    @NamedQuery(name = "VehiculoHasCentroDiagnostico.findByIdVehiculohasCentro", query = "SELECT v FROM VehiculoHasCentroDiagnostico v WHERE v.idVehiculohasCentro = :idVehiculohasCentro")})
public class VehiculoHasCentroDiagnostico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_Vehiculo_has_Centro")
    private Integer idVehiculohasCentro;
    @JoinColumn(name = "Centro_Diagnostico_idCentro_Diagnostico", referencedColumnName = "idCentro_Diagnostico")
    @ManyToOne(optional = false)
    private CentroDiagnostico centroDiagnosticoidCentroDiagnostico;
    @JoinColumn(name = "Vehiculo_Placa", referencedColumnName = "Placa")
    @ManyToOne(optional = false)
    private Vehiculo vehiculoPlaca;

    public VehiculoHasCentroDiagnostico() {
    }

    public VehiculoHasCentroDiagnostico(Integer idVehiculohasCentro) {
        this.idVehiculohasCentro = idVehiculohasCentro;
    }

    public Integer getIdVehiculohasCentro() {
        return idVehiculohasCentro;
    }

    public void setIdVehiculohasCentro(Integer idVehiculohasCentro) {
        this.idVehiculohasCentro = idVehiculohasCentro;
    }

    public CentroDiagnostico getCentroDiagnosticoidCentroDiagnostico() {
        return centroDiagnosticoidCentroDiagnostico;
    }

    public void setCentroDiagnosticoidCentroDiagnostico(CentroDiagnostico centroDiagnosticoidCentroDiagnostico) {
        this.centroDiagnosticoidCentroDiagnostico = centroDiagnosticoidCentroDiagnostico;
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
        hash += (idVehiculohasCentro != null ? idVehiculohasCentro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehiculoHasCentroDiagnostico)) {
            return false;
        }
        VehiculoHasCentroDiagnostico other = (VehiculoHasCentroDiagnostico) object;
        if ((this.idVehiculohasCentro == null && other.idVehiculohasCentro != null) || (this.idVehiculohasCentro != null && !this.idVehiculohasCentro.equals(other.idVehiculohasCentro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.VehiculoHasCentroDiagnostico[ idVehiculohasCentro=" + idVehiculohasCentro + " ]";
    }
    
}

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
@Table(name = "vehiculo_has_rutas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehiculoHasRutas.findAll", query = "SELECT v FROM VehiculoHasRutas v"),
    @NamedQuery(name = "VehiculoHasRutas.findByIdVehiculosRuta", query = "SELECT v FROM VehiculoHasRutas v WHERE v.idVehiculosRuta = :idVehiculosRuta"),
    @NamedQuery(name = "VehiculoHasRutas.findByValorRuta", query = "SELECT v FROM VehiculoHasRutas v WHERE v.valorRuta = :valorRuta")})
public class VehiculoHasRutas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_Vehiculos_Ruta")
    private Integer idVehiculosRuta;
    @Column(name = "Valor_Ruta")
    private Integer valorRuta;
    @JoinColumn(name = "Rutas_Codigo_Ruta", referencedColumnName = "Codigo_Ruta")
    @ManyToOne(optional = false)
    private Rutas rutasCodigoRuta;
    @JoinColumn(name = "Vehiculo_Placa", referencedColumnName = "Placa")
    @ManyToOne(optional = false)
    private Vehiculo vehiculoPlaca;

    public VehiculoHasRutas() {
    }

    public VehiculoHasRutas(Integer idVehiculosRuta) {
        this.idVehiculosRuta = idVehiculosRuta;
    }

    public Integer getIdVehiculosRuta() {
        return idVehiculosRuta;
    }

    public void setIdVehiculosRuta(Integer idVehiculosRuta) {
        this.idVehiculosRuta = idVehiculosRuta;
    }

    public Integer getValorRuta() {
        return valorRuta;
    }

    public void setValorRuta(Integer valorRuta) {
        this.valorRuta = valorRuta;
    }

    public Rutas getRutasCodigoRuta() {
        return rutasCodigoRuta;
    }

    public void setRutasCodigoRuta(Rutas rutasCodigoRuta) {
        this.rutasCodigoRuta = rutasCodigoRuta;
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
        hash += (idVehiculosRuta != null ? idVehiculosRuta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehiculoHasRutas)) {
            return false;
        }
        VehiculoHasRutas other = (VehiculoHasRutas) object;
        if ((this.idVehiculosRuta == null && other.idVehiculosRuta != null) || (this.idVehiculosRuta != null && !this.idVehiculosRuta.equals(other.idVehiculosRuta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.VehiculoHasRutas[ idVehiculosRuta=" + idVehiculosRuta + " ]";
    }
    
}

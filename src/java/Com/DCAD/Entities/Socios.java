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
@Table(name = "socios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Socios.findAll", query = "SELECT s FROM Socios s"),
    @NamedQuery(name = "Socios.findByCodigoSocio", query = "SELECT s FROM Socios s WHERE s.codigoSocio = :codigoSocio"),
    @NamedQuery(name = "Socios.findBySocios", query = "SELECT s FROM Socios s WHERE s.socios = :socios")})
public class Socios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Codigo_Socio")
    private Integer codigoSocio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Socios")
    private String socios;
    @JoinColumn(name = "Vehiculo_Placa", referencedColumnName = "Placa")
    @ManyToOne(optional = false)
    private Vehiculo vehiculoPlaca;

    public Socios() {
    }

    public Socios(Integer codigoSocio) {
        this.codigoSocio = codigoSocio;
    }

    public Socios(Integer codigoSocio, String socios) {
        this.codigoSocio = codigoSocio;
        this.socios = socios;
    }

    public Integer getCodigoSocio() {
        return codigoSocio;
    }

    public void setCodigoSocio(Integer codigoSocio) {
        this.codigoSocio = codigoSocio;
    }

    public String getSocios() {
        return socios;
    }

    public void setSocios(String socios) {
        this.socios = socios;
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
        hash += (codigoSocio != null ? codigoSocio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Socios)) {
            return false;
        }
        Socios other = (Socios) object;
        if ((this.codigoSocio == null && other.codigoSocio != null) || (this.codigoSocio != null && !this.codigoSocio.equals(other.codigoSocio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.Socios[ codigoSocio=" + codigoSocio + " ]";
    }
    
}

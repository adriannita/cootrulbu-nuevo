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
@Table(name = "pasaje_has_rutas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PasajeHasRutas.findAll", query = "SELECT p FROM PasajeHasRutas p"),
    @NamedQuery(name = "PasajeHasRutas.findByIdPasajeRuta", query = "SELECT p FROM PasajeHasRutas p WHERE p.idPasajeRuta = :idPasajeRuta")})
public class PasajeHasRutas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_Pasaje_Ruta")
    private Integer idPasajeRuta;
    @JoinColumn(name = "Pasaje_Codigo_Pasaje", referencedColumnName = "Codigo_Pasaje")
    @ManyToOne(optional = false)
    private Pasaje pasajeCodigoPasaje;
    @JoinColumn(name = "Rutas_Codigo_Ruta", referencedColumnName = "Codigo_Ruta")
    @ManyToOne(optional = false)
    private Rutas rutasCodigoRuta;

    public PasajeHasRutas() {
    }

    public PasajeHasRutas(Integer idPasajeRuta) {
        this.idPasajeRuta = idPasajeRuta;
    }

    public Integer getIdPasajeRuta() {
        return idPasajeRuta;
    }

    public void setIdPasajeRuta(Integer idPasajeRuta) {
        this.idPasajeRuta = idPasajeRuta;
    }

    public Pasaje getPasajeCodigoPasaje() {
        return pasajeCodigoPasaje;
    }

    public void setPasajeCodigoPasaje(Pasaje pasajeCodigoPasaje) {
        this.pasajeCodigoPasaje = pasajeCodigoPasaje;
    }

    public Rutas getRutasCodigoRuta() {
        return rutasCodigoRuta;
    }

    public void setRutasCodigoRuta(Rutas rutasCodigoRuta) {
        this.rutasCodigoRuta = rutasCodigoRuta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPasajeRuta != null ? idPasajeRuta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PasajeHasRutas)) {
            return false;
        }
        PasajeHasRutas other = (PasajeHasRutas) object;
        if ((this.idPasajeRuta == null && other.idPasajeRuta != null) || (this.idPasajeRuta != null && !this.idPasajeRuta.equals(other.idPasajeRuta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.PasajeHasRutas[ idPasajeRuta=" + idPasajeRuta + " ]";
    }
    
}

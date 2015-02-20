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
@Table(name = "pasaje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pasaje.findAll", query = "SELECT p FROM Pasaje p"),
    @NamedQuery(name = "Pasaje.findByCodigoPasaje", query = "SELECT p FROM Pasaje p WHERE p.codigoPasaje = :codigoPasaje"),
    @NamedQuery(name = "Pasaje.findByPasaje", query = "SELECT p FROM Pasaje p WHERE p.pasaje = :pasaje")})
public class Pasaje implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Codigo_Pasaje")
    private Integer codigoPasaje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Pasaje")
    private String pasaje;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pasajeCodigoPasaje")
    private List<PasajeHasRutas> pasajeHasRutasList;

    public Pasaje() {
    }

    public Pasaje(Integer codigoPasaje) {
        this.codigoPasaje = codigoPasaje;
    }

    public Pasaje(Integer codigoPasaje, String pasaje) {
        this.codigoPasaje = codigoPasaje;
        this.pasaje = pasaje;
    }

    public Integer getCodigoPasaje() {
        return codigoPasaje;
    }

    public void setCodigoPasaje(Integer codigoPasaje) {
        this.codigoPasaje = codigoPasaje;
    }

    public String getPasaje() {
        return pasaje;
    }

    public void setPasaje(String pasaje) {
        this.pasaje = pasaje;
    }

    @XmlTransient
    public List<PasajeHasRutas> getPasajeHasRutasList() {
        return pasajeHasRutasList;
    }

    public void setPasajeHasRutasList(List<PasajeHasRutas> pasajeHasRutasList) {
        this.pasajeHasRutasList = pasajeHasRutasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoPasaje != null ? codigoPasaje.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pasaje)) {
            return false;
        }
        Pasaje other = (Pasaje) object;
        if ((this.codigoPasaje == null && other.codigoPasaje != null) || (this.codigoPasaje != null && !this.codigoPasaje.equals(other.codigoPasaje))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.Pasaje[ codigoPasaje=" + codigoPasaje + " ]";
    }
    
}

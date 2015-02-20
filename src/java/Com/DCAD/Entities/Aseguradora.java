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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "aseguradora")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aseguradora.findAll", query = "SELECT a FROM Aseguradora a"),
    @NamedQuery(name = "Aseguradora.findByCodAsegurador", query = "SELECT a FROM Aseguradora a WHERE a.codAsegurador = :codAsegurador"),
    @NamedQuery(name = "Aseguradora.findByAseguradora", query = "SELECT a FROM Aseguradora a WHERE a.aseguradora = :aseguradora")})
public class Aseguradora implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cod_Asegurador")
    private Integer codAsegurador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Aseguradora")
    private String aseguradora;
    @JoinColumn(name = "Vehiculo_Placa", referencedColumnName = "Placa")
    @ManyToOne(optional = false)
    private Vehiculo vehiculoPlaca;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aseguradoraCodAsegurador")
    private List<Tecnomecanica> tecnomecanicaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aseguradoraCodAsegurador")
    private List<Soat> soatList;

    public Aseguradora() {
    }

    public Aseguradora(Integer codAsegurador) {
        this.codAsegurador = codAsegurador;
    }

    public Aseguradora(Integer codAsegurador, String aseguradora) {
        this.codAsegurador = codAsegurador;
        this.aseguradora = aseguradora;
    }

    public Integer getCodAsegurador() {
        return codAsegurador;
    }

    public void setCodAsegurador(Integer codAsegurador) {
        this.codAsegurador = codAsegurador;
    }

    public String getAseguradora() {
        return aseguradora;
    }

    public void setAseguradora(String aseguradora) {
        this.aseguradora = aseguradora;
    }

    public Vehiculo getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(Vehiculo vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    @XmlTransient
    public List<Tecnomecanica> getTecnomecanicaList() {
        return tecnomecanicaList;
    }

    public void setTecnomecanicaList(List<Tecnomecanica> tecnomecanicaList) {
        this.tecnomecanicaList = tecnomecanicaList;
    }

    @XmlTransient
    public List<Soat> getSoatList() {
        return soatList;
    }

    public void setSoatList(List<Soat> soatList) {
        this.soatList = soatList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAsegurador != null ? codAsegurador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aseguradora)) {
            return false;
        }
        Aseguradora other = (Aseguradora) object;
        if ((this.codAsegurador == null && other.codAsegurador != null) || (this.codAsegurador != null && !this.codAsegurador.equals(other.codAsegurador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.Aseguradora[ codAsegurador=" + codAsegurador + " ]";
    }
    
}

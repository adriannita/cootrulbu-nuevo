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
@Table(name = "rutas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rutas.findAll", query = "SELECT r FROM Rutas r"),
    @NamedQuery(name = "Rutas.findByCodigoRuta", query = "SELECT r FROM Rutas r WHERE r.codigoRuta = :codigoRuta"),
    @NamedQuery(name = "Rutas.findByRuta", query = "SELECT r FROM Rutas r WHERE r.ruta = :ruta"),
    @NamedQuery(name = "Rutas.findByValorRuta", query = "SELECT r FROM Rutas r WHERE r.valorRuta = :valorRuta")})
public class Rutas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Codigo_Ruta")
    private Integer codigoRuta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Valor_Ruta")
    private int valorRuta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rutasCodigoRuta")
    private List<VehiculoHasRutas> vehiculoHasRutasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rutasCodigoRuta")
    private List<PasajeHasRutas> pasajeHasRutasList;
    @JoinColumn(name = "Despachador_Cod_Despachador", referencedColumnName = "Cod_Despachador")
    @ManyToOne(optional = false)
    private Despachador despachadorCodDespachador;

    public Rutas() {
    }

    public Rutas(Integer codigoRuta) {
        this.codigoRuta = codigoRuta;
    }

    public Rutas(Integer codigoRuta, String ruta, int valorRuta) {
        this.codigoRuta = codigoRuta;
        this.ruta = ruta;
        this.valorRuta = valorRuta;
    }

    public Integer getCodigoRuta() {
        return codigoRuta;
    }

    public void setCodigoRuta(Integer codigoRuta) {
        this.codigoRuta = codigoRuta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getValorRuta() {
        return valorRuta;
    }

    public void setValorRuta(int valorRuta) {
        this.valorRuta = valorRuta;
    }

    @XmlTransient
    public List<VehiculoHasRutas> getVehiculoHasRutasList() {
        return vehiculoHasRutasList;
    }

    public void setVehiculoHasRutasList(List<VehiculoHasRutas> vehiculoHasRutasList) {
        this.vehiculoHasRutasList = vehiculoHasRutasList;
    }

    @XmlTransient
    public List<PasajeHasRutas> getPasajeHasRutasList() {
        return pasajeHasRutasList;
    }

    public void setPasajeHasRutasList(List<PasajeHasRutas> pasajeHasRutasList) {
        this.pasajeHasRutasList = pasajeHasRutasList;
    }

    public Despachador getDespachadorCodDespachador() {
        return despachadorCodDespachador;
    }

    public void setDespachadorCodDespachador(Despachador despachadorCodDespachador) {
        this.despachadorCodDespachador = despachadorCodDespachador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoRuta != null ? codigoRuta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rutas)) {
            return false;
        }
        Rutas other = (Rutas) object;
        if ((this.codigoRuta == null && other.codigoRuta != null) || (this.codigoRuta != null && !this.codigoRuta.equals(other.codigoRuta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.Rutas[ codigoRuta=" + codigoRuta + " ]";
    }
    
}

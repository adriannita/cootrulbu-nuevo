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
@Table(name = "despachador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Despachador.findAll", query = "SELECT d FROM Despachador d"),
    @NamedQuery(name = "Despachador.findByCodDespachador", query = "SELECT d FROM Despachador d WHERE d.codDespachador = :codDespachador"),
    @NamedQuery(name = "Despachador.findByDespachador", query = "SELECT d FROM Despachador d WHERE d.despachador = :despachador")})
public class Despachador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cod_Despachador")
    private Integer codDespachador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Despachador")
    private String despachador;
    @JoinColumn(name = "Usuario_id_Usuario", referencedColumnName = "id_Usuario")
    @ManyToOne(optional = false)
    private Usuario usuarioidUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "despachadorCodDespachador")
    private List<Rutas> rutasList;

    public Despachador() {
    }

    public Despachador(Integer codDespachador) {
        this.codDespachador = codDespachador;
    }

    public Despachador(Integer codDespachador, String despachador) {
        this.codDespachador = codDespachador;
        this.despachador = despachador;
    }

    public Integer getCodDespachador() {
        return codDespachador;
    }

    public void setCodDespachador(Integer codDespachador) {
        this.codDespachador = codDespachador;
    }

    public String getDespachador() {
        return despachador;
    }

    public void setDespachador(String despachador) {
        this.despachador = despachador;
    }

    public Usuario getUsuarioidUsuario() {
        return usuarioidUsuario;
    }

    public void setUsuarioidUsuario(Usuario usuarioidUsuario) {
        this.usuarioidUsuario = usuarioidUsuario;
    }

    @XmlTransient
    public List<Rutas> getRutasList() {
        return rutasList;
    }

    public void setRutasList(List<Rutas> rutasList) {
        this.rutasList = rutasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codDespachador != null ? codDespachador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Despachador)) {
            return false;
        }
        Despachador other = (Despachador) object;
        if ((this.codDespachador == null && other.codDespachador != null) || (this.codDespachador != null && !this.codDespachador.equals(other.codDespachador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.Despachador[ codDespachador=" + codDespachador + " ]";
    }
    
}

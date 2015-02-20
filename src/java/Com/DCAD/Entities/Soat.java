/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Com.DCAD.Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "soat")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Soat.findAll", query = "SELECT s FROM Soat s"),
    @NamedQuery(name = "Soat.findByIsSoat", query = "SELECT s FROM Soat s WHERE s.isSoat = :isSoat"),
    @NamedQuery(name = "Soat.findByFechaExpedicion", query = "SELECT s FROM Soat s WHERE s.fechaExpedicion = :fechaExpedicion"),
    @NamedQuery(name = "Soat.findByFechaInicio", query = "SELECT s FROM Soat s WHERE s.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Soat.findByFechaVencimiento", query = "SELECT s FROM Soat s WHERE s.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "Soat.findByValor", query = "SELECT s FROM Soat s WHERE s.valor = :valor")})
public class Soat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_Soat")
    private Integer isSoat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha_Expedicion")
    @Temporal(TemporalType.DATE)
    private Date fechaExpedicion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha_Inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha_Vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Valor")
    private int valor;
    @JoinColumn(name = "Aseguradora_Cod_Asegurador", referencedColumnName = "Cod_Asegurador")
    @ManyToOne(optional = false)
    private Aseguradora aseguradoraCodAsegurador;

    public Soat() {
    }

    public Soat(Integer isSoat) {
        this.isSoat = isSoat;
    }

    public Soat(Integer isSoat, Date fechaExpedicion, Date fechaInicio, Date fechaVencimiento, int valor) {
        this.isSoat = isSoat;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.valor = valor;
    }

    public Integer getIsSoat() {
        return isSoat;
    }

    public void setIsSoat(Integer isSoat) {
        this.isSoat = isSoat;
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Aseguradora getAseguradoraCodAsegurador() {
        return aseguradoraCodAsegurador;
    }

    public void setAseguradoraCodAsegurador(Aseguradora aseguradoraCodAsegurador) {
        this.aseguradoraCodAsegurador = aseguradoraCodAsegurador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (isSoat != null ? isSoat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Soat)) {
            return false;
        }
        Soat other = (Soat) object;
        if ((this.isSoat == null && other.isSoat != null) || (this.isSoat != null && !this.isSoat.equals(other.isSoat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.Soat[ isSoat=" + isSoat + " ]";
    }
    
}

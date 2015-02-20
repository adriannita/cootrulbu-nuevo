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
@Table(name = "licencia_conduccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LicenciaConduccion.findAll", query = "SELECT l FROM LicenciaConduccion l"),
    @NamedQuery(name = "LicenciaConduccion.findByNumero", query = "SELECT l FROM LicenciaConduccion l WHERE l.numero = :numero"),
    @NamedQuery(name = "LicenciaConduccion.findByFechaNacimiento", query = "SELECT l FROM LicenciaConduccion l WHERE l.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "LicenciaConduccion.findByFechaExpedicion", query = "SELECT l FROM LicenciaConduccion l WHERE l.fechaExpedicion = :fechaExpedicion"),
    @NamedQuery(name = "LicenciaConduccion.findByFechaVencimiento", query = "SELECT l FROM LicenciaConduccion l WHERE l.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "LicenciaConduccion.findByRestricciones", query = "SELECT l FROM LicenciaConduccion l WHERE l.restricciones = :restricciones")})
public class LicenciaConduccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Numero")
    private Integer numero;
    @Column(name = "Fecha_Nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "Fecha_Expedicion")
    @Temporal(TemporalType.DATE)
    private Date fechaExpedicion;
    @Column(name = "Fecha_Vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Column(name = "Restricciones")
    private Integer restricciones;
    @JoinColumn(name = "Conductores_Cedula", referencedColumnName = "Cedula")
    @ManyToOne(optional = false)
    private Conductores conductoresCedula;

    public LicenciaConduccion() {
    }

    public LicenciaConduccion(Integer numero) {
        this.numero = numero;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Integer getRestricciones() {
        return restricciones;
    }

    public void setRestricciones(Integer restricciones) {
        this.restricciones = restricciones;
    }

    public Conductores getConductoresCedula() {
        return conductoresCedula;
    }

    public void setConductoresCedula(Conductores conductoresCedula) {
        this.conductoresCedula = conductoresCedula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numero != null ? numero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LicenciaConduccion)) {
            return false;
        }
        LicenciaConduccion other = (LicenciaConduccion) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.LicenciaConduccion[ numero=" + numero + " ]";
    }
    
}

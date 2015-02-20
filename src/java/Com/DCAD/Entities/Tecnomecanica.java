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
@Table(name = "tecnomecanica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tecnomecanica.findAll", query = "SELECT t FROM Tecnomecanica t"),
    @NamedQuery(name = "Tecnomecanica.findByNumControl", query = "SELECT t FROM Tecnomecanica t WHERE t.numControl = :numControl"),
    @NamedQuery(name = "Tecnomecanica.findByFechaExpedicion", query = "SELECT t FROM Tecnomecanica t WHERE t.fechaExpedicion = :fechaExpedicion"),
    @NamedQuery(name = "Tecnomecanica.findByFechaVencimiento", query = "SELECT t FROM Tecnomecanica t WHERE t.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "Tecnomecanica.findByNumRunt", query = "SELECT t FROM Tecnomecanica t WHERE t.numRunt = :numRunt")})
public class Tecnomecanica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Num_Control")
    private Integer numControl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha_Expedicion")
    @Temporal(TemporalType.DATE)
    private Date fechaExpedicion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha_Vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Num_Runt")
    private int numRunt;
    @JoinColumn(name = "Aseguradora_Cod_Asegurador", referencedColumnName = "Cod_Asegurador")
    @ManyToOne(optional = false)
    private Aseguradora aseguradoraCodAsegurador;
    @JoinColumn(name = "Centro_Diagnostico_idCentro_Diagnostico", referencedColumnName = "idCentro_Diagnostico")
    @ManyToOne(optional = false)
    private CentroDiagnostico centroDiagnosticoidCentroDiagnostico;

    public Tecnomecanica() {
    }

    public Tecnomecanica(Integer numControl) {
        this.numControl = numControl;
    }

    public Tecnomecanica(Integer numControl, Date fechaExpedicion, Date fechaVencimiento, int numRunt) {
        this.numControl = numControl;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaVencimiento = fechaVencimiento;
        this.numRunt = numRunt;
    }

    public Integer getNumControl() {
        return numControl;
    }

    public void setNumControl(Integer numControl) {
        this.numControl = numControl;
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

    public int getNumRunt() {
        return numRunt;
    }

    public void setNumRunt(int numRunt) {
        this.numRunt = numRunt;
    }

    public Aseguradora getAseguradoraCodAsegurador() {
        return aseguradoraCodAsegurador;
    }

    public void setAseguradoraCodAsegurador(Aseguradora aseguradoraCodAsegurador) {
        this.aseguradoraCodAsegurador = aseguradoraCodAsegurador;
    }

    public CentroDiagnostico getCentroDiagnosticoidCentroDiagnostico() {
        return centroDiagnosticoidCentroDiagnostico;
    }

    public void setCentroDiagnosticoidCentroDiagnostico(CentroDiagnostico centroDiagnosticoidCentroDiagnostico) {
        this.centroDiagnosticoidCentroDiagnostico = centroDiagnosticoidCentroDiagnostico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numControl != null ? numControl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tecnomecanica)) {
            return false;
        }
        Tecnomecanica other = (Tecnomecanica) object;
        if ((this.numControl == null && other.numControl != null) || (this.numControl != null && !this.numControl.equals(other.numControl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.Tecnomecanica[ numControl=" + numControl + " ]";
    }
    
}

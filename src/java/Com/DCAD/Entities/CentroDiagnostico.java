/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Com.DCAD.Entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cpe
 */
@Entity
@Table(name = "centro_diagnostico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CentroDiagnostico.findAll", query = "SELECT c FROM CentroDiagnostico c"),
    @NamedQuery(name = "CentroDiagnostico.findByIdCentroDiagnostico", query = "SELECT c FROM CentroDiagnostico c WHERE c.idCentroDiagnostico = :idCentroDiagnostico"),
    @NamedQuery(name = "CentroDiagnostico.findByNombreDiagnostico", query = "SELECT c FROM CentroDiagnostico c WHERE c.nombreDiagnostico = :nombreDiagnostico"),
    @NamedQuery(name = "CentroDiagnostico.findByFechaExpedicion", query = "SELECT c FROM CentroDiagnostico c WHERE c.fechaExpedicion = :fechaExpedicion"),
    @NamedQuery(name = "CentroDiagnostico.findByFechaVencimiento", query = "SELECT c FROM CentroDiagnostico c WHERE c.fechaVencimiento = :fechaVencimiento")})
public class CentroDiagnostico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCentro_Diagnostico")
    private Integer idCentroDiagnostico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Nombre_Diagnostico")
    private String nombreDiagnostico;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "centroDiagnosticoidCentroDiagnostico")
    private List<VehiculoHasCentroDiagnostico> vehiculoHasCentroDiagnosticoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "centroDiagnosticoidCentroDiagnostico")
    private List<Tecnomecanica> tecnomecanicaList;

    public CentroDiagnostico() {
    }

    public CentroDiagnostico(Integer idCentroDiagnostico) {
        this.idCentroDiagnostico = idCentroDiagnostico;
    }

    public CentroDiagnostico(Integer idCentroDiagnostico, String nombreDiagnostico, Date fechaExpedicion, Date fechaVencimiento) {
        this.idCentroDiagnostico = idCentroDiagnostico;
        this.nombreDiagnostico = nombreDiagnostico;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaVencimiento = fechaVencimiento;
    }

    public Integer getIdCentroDiagnostico() {
        return idCentroDiagnostico;
    }

    public void setIdCentroDiagnostico(Integer idCentroDiagnostico) {
        this.idCentroDiagnostico = idCentroDiagnostico;
    }

    public String getNombreDiagnostico() {
        return nombreDiagnostico;
    }

    public void setNombreDiagnostico(String nombreDiagnostico) {
        this.nombreDiagnostico = nombreDiagnostico;
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

    @XmlTransient
    public List<VehiculoHasCentroDiagnostico> getVehiculoHasCentroDiagnosticoList() {
        return vehiculoHasCentroDiagnosticoList;
    }

    public void setVehiculoHasCentroDiagnosticoList(List<VehiculoHasCentroDiagnostico> vehiculoHasCentroDiagnosticoList) {
        this.vehiculoHasCentroDiagnosticoList = vehiculoHasCentroDiagnosticoList;
    }

    @XmlTransient
    public List<Tecnomecanica> getTecnomecanicaList() {
        return tecnomecanicaList;
    }

    public void setTecnomecanicaList(List<Tecnomecanica> tecnomecanicaList) {
        this.tecnomecanicaList = tecnomecanicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCentroDiagnostico != null ? idCentroDiagnostico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CentroDiagnostico)) {
            return false;
        }
        CentroDiagnostico other = (CentroDiagnostico) object;
        if ((this.idCentroDiagnostico == null && other.idCentroDiagnostico != null) || (this.idCentroDiagnostico != null && !this.idCentroDiagnostico.equals(other.idCentroDiagnostico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Com.DCAD.Entities.CentroDiagnostico[ idCentroDiagnostico=" + idCentroDiagnostico + " ]";
    }
    
}

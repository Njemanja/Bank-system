/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author Neca
 */
@Entity
@Table(name = "filijala")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Filijala.findAll", query = "SELECT f FROM Filijala f")})
public class Filijala implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDFil")
    private Integer iDFil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Adresa")
    private String adresa;
    @JoinColumn(name = "IDMes", referencedColumnName = "IDMes")
    @ManyToOne(optional = false)
    private Mesto iDMes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDFil")
    private List<IsplataUplata> isplataUplataList;

    public Filijala() {
    }

    public Filijala(Integer iDFil) {
        this.iDFil = iDFil;
    }

    public Filijala(Integer iDFil, String naziv, String adresa) {
        this.iDFil = iDFil;
        this.naziv = naziv;
        this.adresa = adresa;
    }

    public Integer getIDFil() {
        return iDFil;
    }

    public void setIDFil(Integer iDFil) {
        this.iDFil = iDFil;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Mesto getIDMes() {
        return iDMes;
    }

    public void setIDMes(Mesto iDMes) {
        this.iDMes = iDMes;
    }

    @XmlTransient
    public List<IsplataUplata> getIsplataUplataList() {
        return isplataUplataList;
    }

    public void setIsplataUplataList(List<IsplataUplata> isplataUplataList) {
        this.isplataUplataList = isplataUplataList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDFil != null ? iDFil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Filijala)) {
            return false;
        }
        Filijala other = (Filijala) object;
        if ((this.iDFil == null && other.iDFil != null) || (this.iDFil != null && !this.iDFil.equals(other.iDFil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Filijala[ iDFil=" + iDFil + " ]";
    }
    
}

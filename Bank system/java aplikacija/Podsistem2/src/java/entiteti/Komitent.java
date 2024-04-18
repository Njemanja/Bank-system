/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author Neca
 */
@Entity
@Table(name = "komitent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Komitent.findAll", query = "SELECT k FROM Komitent k")})
public class Komitent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDKom")
    private Integer iDKom;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "IDMes")
    private String iDMes;
    @OneToMany(mappedBy = "iDKom")
    private List<Racun> racunList;

    public Komitent() {
    }

    public Komitent(Integer iDKom) {
        this.iDKom = iDKom;
    }

    public Komitent(Integer iDKom, String naziv, String adresa, String iDMes) {
        this.iDKom = iDKom;
        this.naziv = naziv;
        this.adresa = adresa;
        this.iDMes = iDMes;
    }

    public Integer getIDKom() {
        return iDKom;
    }

    public void setIDKom(Integer iDKom) {
        this.iDKom = iDKom;
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

    public String getIDMes() {
        return iDMes;
    }

    public void setIDMes(String iDMes) {
        this.iDMes = iDMes;
    }

    @XmlTransient
    public List<Racun> getRacunList() {
        return racunList;
    }

    public void setRacunList(List<Racun> racunList) {
        this.racunList = racunList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDKom != null ? iDKom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Komitent)) {
            return false;
        }
        Komitent other = (Komitent) object;
        if ((this.iDKom == null && other.iDKom != null) || (this.iDKom != null && !this.iDKom.equals(other.iDKom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Komitent[ iDKom=" + iDKom + " ]";
    }
    
}

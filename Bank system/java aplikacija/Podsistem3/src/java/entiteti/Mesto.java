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
@Table(name = "mesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mesto.findAll", query = "SELECT m FROM Mesto m")})
public class Mesto implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "PostanskiBroj")
    private int postanskiBroj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDMes")
    private Integer iDMes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDMes")
    private List<Filijala> filijalaList=null;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDMes")
    private List<Komitent> komitentList=null;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDMes")
    private List<Racun> racunList=null;

    public Mesto() {
    }

    public Mesto(Integer iDMes) {
        this.iDMes = iDMes;
    }

    public Mesto(Integer iDMes, int postanskiBroj, String naziv) {
        this.iDMes = iDMes;
        this.postanskiBroj = postanskiBroj;
        this.naziv = naziv;
    }

    public Integer getIDMes() {
        return iDMes;
    }

    public void setIDMes(Integer iDMes) {
        this.iDMes = iDMes;
    }


    @XmlTransient
    public List<Filijala> getFilijalaList() {
        return filijalaList;
    }

    public void setFilijalaList(List<Filijala> filijalaList) {
        this.filijalaList = filijalaList;
    }

    @XmlTransient
    public List<Komitent> getKomitentList() {
        return komitentList;
    }

    public void setKomitentList(List<Komitent> komitentList) {
        this.komitentList = komitentList;
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
        hash += (iDMes != null ? iDMes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mesto)) {
            return false;
        }
        Mesto other = (Mesto) object;
        if ((this.iDMes == null && other.iDMes != null) || (this.iDMes != null && !this.iDMes.equals(other.iDMes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Mesto[ iDMes=" + iDMes + " ]";
    }

    public int getPostanskiBroj() {
        return postanskiBroj;
    }

    public void setPostanskiBroj(int postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
}

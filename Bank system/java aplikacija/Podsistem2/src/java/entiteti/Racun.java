/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Neca
 */
@Entity
@Table(name = "racun")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Racun.findAll", query = "SELECT r FROM Racun r")})
public class Racun implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDRac")
    private Integer iDRac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BrojTransakcija")
    private int brojTransakcija;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DozvoljenMinus")
    private double dozvoljenMinus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Stanje")
    private double stanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Status")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDMes")
    private int iDMes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDRac")
    private List<Transakcija> transakcijaList;
    @JoinColumn(name = "IDKom", referencedColumnName = "IDKom")
    @ManyToOne
    private Komitent iDKom;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDRacNA")
    private List<Prenos> prenosList;

    public Racun() {
    }

    public Racun(Integer iDRac) {
        this.iDRac = iDRac;
    }

    public Racun(Integer iDRac, int brojTransakcija, double dozvoljenMinus, double stanje, int status, int iDMes, Date datum) {
        this.iDRac = iDRac;
        this.brojTransakcija = brojTransakcija;
        this.dozvoljenMinus = dozvoljenMinus;
        this.stanje = stanje;
        this.status = status;
        this.iDMes = iDMes;
        this.datum = datum;
    }

    public Integer getIDRac() {
        return iDRac;
    }

    public void setIDRac(Integer iDRac) {
        this.iDRac = iDRac;
    }

    public int getBrojTransakcija() {
        return brojTransakcija;
    }

    public void setBrojTransakcija(int brojTransakcija) {
        this.brojTransakcija = brojTransakcija;
    }

    public double getDozvoljenMinus() {
        return dozvoljenMinus;
    }

    public void setDozvoljenMinus(double dozvoljenMinus) {
        this.dozvoljenMinus = dozvoljenMinus;
    }

    public double getStanje() {
        return stanje;
    }

    public void setStanje(double stanje) {
        this.stanje = stanje;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIDMes() {
        return iDMes;
    }

    public void setIDMes(int iDMes) {
        this.iDMes = iDMes;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    @XmlTransient
    public List<Transakcija> getTransakcijaList() {
        return transakcijaList;
    }

    public void setTransakcijaList(List<Transakcija> transakcijaList) {
        this.transakcijaList = transakcijaList;
    }

    public Komitent getIDKom() {
        return iDKom;
    }

    public void setIDKom(Komitent iDKom) {
        this.iDKom = iDKom;
    }

    @XmlTransient
    public List<Prenos> getPrenosList() {
        return prenosList;
    }

    public void setPrenosList(List<Prenos> prenosList) {
        this.prenosList = prenosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDRac != null ? iDRac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Racun)) {
            return false;
        }
        Racun other = (Racun) object;
        if ((this.iDRac == null && other.iDRac != null) || (this.iDRac != null && !this.iDRac.equals(other.iDRac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Racun[ iDRac=" + iDRac + " ]";
    }
    
}

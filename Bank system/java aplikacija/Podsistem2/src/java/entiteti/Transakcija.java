/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Neca
 */
@Entity
@Table(name = "transakcija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDTra")
    private Integer iDTra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum_Vreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Iznos")
    private double iznos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RedniBroj")
    private int redniBroj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Svrha")
    private String svrha;
    @JoinColumn(name = "IDRac", referencedColumnName = "IDRac")
    @ManyToOne(optional = false)
    private Racun iDRac;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private IsplataUplata isplataUplata;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private Prenos prenos;

    public Transakcija() {
    }

    public Transakcija(Integer iDTra) {
        this.iDTra = iDTra;
    }

    public Transakcija(Integer iDTra, Date datumVreme, double iznos, int redniBroj, String svrha) {
        this.iDTra = iDTra;
        this.datumVreme = datumVreme;
        this.iznos = iznos;
        this.redniBroj = redniBroj;
        this.svrha = svrha;
    }

    public Integer getIDTra() {
        return iDTra;
    }

    public void setIDTra(Integer iDTra) {
        this.iDTra = iDTra;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public int getRedniBroj() {
        return redniBroj;
    }

    public void setRedniBroj(int redniBroj) {
        this.redniBroj = redniBroj;
    }

    public String getSvrha() {
        return svrha;
    }

    public void setSvrha(String svrha) {
        this.svrha = svrha;
    }

    public Racun getIDRac() {
        return iDRac;
    }

    public void setIDRac(Racun iDRac) {
        this.iDRac = iDRac;
    }

    public IsplataUplata getIsplataUplata() {
        return isplataUplata;
    }

    public void setIsplataUplata(IsplataUplata isplataUplata) {
        this.isplataUplata = isplataUplata;
    }

    public Prenos getPrenos() {
        return prenos;
    }

    public void setPrenos(Prenos prenos) {
        this.prenos = prenos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDTra != null ? iDTra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.iDTra == null && other.iDTra != null) || (this.iDTra != null && !this.iDTra.equals(other.iDTra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Transakcija[ iDTra=" + iDTra + " ]";
    }
    
}

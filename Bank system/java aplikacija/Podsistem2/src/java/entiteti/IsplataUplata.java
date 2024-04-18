/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Neca
 */
@Entity
@Table(name = "isplata_uplata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IsplataUplata.findAll", query = "SELECT i FROM IsplataUplata i")})
public class IsplataUplata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDTra")
    private Integer iDTra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDFil")
    private int iDFil;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Tip")
    private Character tip;
    @JoinColumn(name = "IDTra", referencedColumnName = "IDTra", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transakcija transakcija;

    public IsplataUplata() {
    }

    public IsplataUplata(Integer iDTra) {
        this.iDTra = iDTra;
    }

    public IsplataUplata(Integer iDTra, int iDFil, Character tip) {
        this.iDTra = iDTra;
        this.iDFil = iDFil;
        this.tip = tip;
    }

    public Integer getIDTra() {
        return iDTra;
    }

    public void setIDTra(Integer iDTra) {
        this.iDTra = iDTra;
    }

    public int getIDFil() {
        return iDFil;
    }

    public void setIDFil(int iDFil) {
        this.iDFil = iDFil;
    }

    public Character getTip() {
        return tip;
    }

    public void setTip(Character tip) {
        this.tip = tip;
    }

    public Transakcija getTransakcija() {
        return transakcija;
    }

    public void setTransakcija(Transakcija transakcija) {
        this.transakcija = transakcija;
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
        if (!(object instanceof IsplataUplata)) {
            return false;
        }
        IsplataUplata other = (IsplataUplata) object;
        if ((this.iDTra == null && other.iDTra != null) || (this.iDTra != null && !this.iDTra.equals(other.iDTra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.IsplataUplata[ iDTra=" + iDTra + " ]";
    }
    
}

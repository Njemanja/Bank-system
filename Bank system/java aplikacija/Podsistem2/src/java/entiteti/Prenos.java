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
import javax.persistence.ManyToOne;
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
@Table(name = "prenos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prenos.findAll", query = "SELECT p FROM Prenos p")})
public class Prenos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDTra")
    private Integer iDTra;
    @JoinColumn(name = "IDRacNA", referencedColumnName = "IDRac")
    @ManyToOne(optional = false)
    private Racun iDRacNA;
    @JoinColumn(name = "IDTra", referencedColumnName = "IDTra", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transakcija transakcija;

    public Prenos() {
    }

    public Prenos(Integer iDTra) {
        this.iDTra = iDTra;
    }

    public Integer getIDTra() {
        return iDTra;
    }

    public void setIDTra(Integer iDTra) {
        this.iDTra = iDTra;
    }

    public Racun getIDRacNA() {
        return iDRacNA;
    }

    public void setIDRacNA(Racun iDRacNA) {
        this.iDRacNA = iDRacNA;
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
        if (!(object instanceof Prenos)) {
            return false;
        }
        Prenos other = (Prenos) object;
        if ((this.iDTra == null && other.iDTra != null) || (this.iDTra != null && !this.iDTra.equals(other.iDTra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Prenos[ iDTra=" + iDTra + " ]";
    }
    
}

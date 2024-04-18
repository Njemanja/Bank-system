/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entiteti.IsplataUplata;
import entiteti.Komitent;
import entiteti.Prenos;
import entiteti.Racun;
import entiteti.Transakcija;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.*;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Neca
 */
public class MainPodsistem2{

   @Resource(lookup="projekatConnectionFactory")
    public static ConnectionFactory connectionFactory;
    @Resource(lookup="projekatQueue")
    public static Queue myQueue;
    private static  EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem2PU");
    private static EntityManager em;
    static {
         em = emf.createEntityManager();
         
        }
    
    
    public static void KreiranjeKomitenta(TextMessage m){
       try {
           em.getTransaction().begin();
           String naziv=m.getStringProperty("naziv");
           String adresa=m.getStringProperty("adresa");
           String mesto=m.getStringProperty("mesto");
           int idk=m.getIntProperty("idk");
           Komitent k=new Komitent();
           k.setAdresa(adresa);
           k.setIDKom(idk);
           k.setNaziv(naziv);
           k.setIDMes(mesto);
           em.persist(k);
           em.flush();
           em.getTransaction().commit();
       } catch (JMSException ex) {
           Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
       }
        
    }
    public static void PromenaSedistaZaZadatogKomitenta(TextMessage m){
       try {
           em.getTransaction().begin();
           int idk=m.getIntProperty("idk");
           String idm=m.getStringProperty("mesto");
           TypedQuery<Komitent> query2=em.createQuery("UPDATE Komitent k SET k.iDMes= :idm  WHERE k.iDKom= :idk ", Komitent.class);
                query2.setParameter("idm", idm);
                query2.setParameter("idk", idk);
                int uradi=query2.executeUpdate();
       } catch (JMSException ex) {
           Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
       }
        em.flush();
         em.getTransaction().commit();
        
    }
    public static void KreiranjeRacuna(int dozvoljenminus, int status, int idk, int idm,JMSContext context,JMSProducer producer ){
              ObjectMessage objM=context.createObjectMessage();
        try {
            em.getTransaction().begin();
            System.out.println("Pocinje kreiranje racuna");
                        
            TypedQuery<Racun> query = em.createQuery("SELECT r From Racun r ", Racun.class);
            List<Racun> r=query.getResultList();
            int idr=r.size()+1;
            
            TypedQuery<Komitent> query2 = em.createQuery("SELECT k From Komitent k WHERE k.iDKom= :idk ", Komitent.class);
            query2.setParameter("idk", idk);
            List<Komitent> k=query2.getResultList();

            objM.setIntProperty("podsistem", 5);
            
            if(k.isEmpty()){
                
                objM.setIntProperty("rezultat", 0);
               
            }
            else{
                 Racun racun=new Racun();
            
                racun.setIDRac(idr);
                racun.setStanje(0);
                racun.setBrojTransakcija(0);
                racun.setDozvoljenMinus(dozvoljenminus);
                racun.setIDKom(k.get(0));
                racun.setStatus(status);
                racun.setIDMes(idm);
                racun.setDatum(new Date());
                em.persist(racun);
                objM.setIntProperty("rezultat", 1);
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
        }
             
            producer.send(myQueue, objM);
            em.getTransaction().commit();
            //System.out.println("podsistem1.MainPodsistem1.KreiranjeMesta()");
    }
    public static void BrisanjeRacuna(int idr, JMSContext context,JMSProducer producer ){
        ObjectMessage objM=context.createObjectMessage();
        try {
            em.getTransaction().begin();
            System.out.println("Pocinje brisanje racuna");
            
            
            
            TypedQuery<Racun> query = em.createQuery("SELECT r From Racun r WHERE r.iDRac= :idr", Racun.class);
            query.setParameter("idr", idr);
            List<Racun> r=query.getResultList();

            objM.setIntProperty("podsistem", 5);
            
            if(r.isEmpty()){
                
                objM.setIntProperty("rezultat", 0);
               
            }
            else{
                TypedQuery<Racun> query2 = em.createQuery("UPDATE Racun k SET k.status= :status WHERE k.iDRac= :idr", Racun.class);
                query2.setParameter("status", -1);
                query2.setParameter("idr", idr);
                int del=query2.executeUpdate();
                objM.setIntProperty("rezultat", 1);
               
            }
            
             objM.setObject(null);
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
        }
           
            producer.send(myQueue, objM);
             
            em.getTransaction().commit();
            System.out.println("podsistem1.MainPodsistem1.BrisanjeRacuna()");
    }
    
    
    public static void Isplata(int idr, double iznos, int idf,JMSContext context,JMSProducer producer){
        ObjectMessage objM=context.createObjectMessage();
        int y=0;
        int flagisplata=0;
         
        try {
            em.getTransaction().begin();
            em.flush();
            System.out.println("Pocinje isplata. ");
         
            TypedQuery<Racun> query = em.createQuery("SELECT r From Racun r WHERE r.iDRac= :idr", Racun.class);
            query.setParameter("idr", idr);
            List<Racun> r=query.getResultList();
            
           TypedQuery<Transakcija> query12 = em.createQuery("SELECT r From Transakcija r", Transakcija.class);
           List<Transakcija> trans=query12.getResultList();
           
            objM.setIntProperty("podsistem", 5);
            double ostatak=0;
           
            if(r.isEmpty() || ((r.get(0).getStanje()+r.get(0).getDozvoljenMinus())<0) || r.get(0).getStatus()==0 ||r.get(0).getStatus()==-1){
                
                objM.setIntProperty("rezultat", 0);
               
            }
            else{
                TypedQuery<Transakcija> query10 = em.createQuery("SELECT r From Transakcija r WHERE r.iDRac=  :idr", Transakcija.class);
                query10.setParameter("idr", r.get(0) );
                List<Transakcija> tran=query10.getResultList();
                int a=tran.size()-1;
                
                if(a==-1){
                    y=1;
                }else{
                    y=tran.get(a).getRedniBroj()+1;
                }
                double stanje=r.get(0).getStanje();
                double dozvoljenminus=r.get(0).getDozvoljenMinus();
                if(stanje+dozvoljenminus>=iznos){
                    ostatak=stanje-iznos;
                    TypedQuery<Racun> query2 = em.createQuery("UPDATE Racun k SET k.stanje= :ostatak WHERE k.iDRac= :idr", Racun.class);
                    query2.setParameter("ostatak", ostatak);
                    query2.setParameter("idr", idr);
                    int del=query2.executeUpdate();
                    em.flush();
                    flagisplata=1;
                    if((-1*ostatak)==dozvoljenminus){
                        
                        TypedQuery<Racun> query1 = em.createQuery("UPDATE Racun k SET k.status= :status WHERE k.iDRac= :idr", Racun.class);
                        query1.setParameter("status", 0);
                        query1.setParameter("idr", idr);
                        int de=query1.executeUpdate();
                        em.flush();
                    }
                    
                    //uvecavamo redni broj na racunu
                    TypedQuery<Racun> query4 = em.createQuery("UPDATE Racun k SET k.brojTransakcija= :br WHERE k.iDRac= :idr", Racun.class);
                    query4.setParameter("br", r.get(0).getBrojTransakcija()+1);
                    query4.setParameter("idr", idr);
                    int de4=query4.executeUpdate();
                    em.flush();
                    
                    
                  
                }else{
                    
                    objM.setIntProperty("rezultat", 0);
                }
 
            }
              if(flagisplata==1) {
                Transakcija transakcija=new Transakcija();
                transakcija.setDatumVreme(new Date());
                transakcija.setIDRac(r.get(0));
                transakcija.setSvrha("I");
                transakcija.setIznos(iznos);
                transakcija.setIDTra(trans.size()+1);
                transakcija.setRedniBroj(y);
                
                IsplataUplata isplata=new IsplataUplata();
                isplata.setIDFil(idf);
                isplata.setIDTra(trans.size()+1);
                isplata.setTip('I');
                em.persist(transakcija);
                em.persist(isplata);
                objM.setIntProperty("rezultat", 1);
            }
             
            
             objM.setObject(null);
            } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
            } 
          
           
            producer.send(myQueue, objM);
             em.flush();
            em.getTransaction().commit();
           
        
    }
    
    public static void Uplata(int idr, double iznos, int idf,JMSContext context,JMSProducer producer){
        ObjectMessage objM=context.createObjectMessage();
        int flagisplata=0;
         
        try {
            em.getTransaction().begin();
            em.flush();
            System.out.println("Pocinje uplata. ");
                   
            TypedQuery<Racun> query = em.createQuery("SELECT r From Racun r WHERE r.iDRac= :idr", Racun.class);
            query.setParameter("idr", idr);
            List<Racun> r=query.getResultList();
            
           TypedQuery<Transakcija> query12 = em.createQuery("SELECT r From Transakcija r", Transakcija.class);
           List<Transakcija> trans=query12.getResultList();
 
          
            int y=0;
            objM.setIntProperty("podsistem", 5);
            double ostatak=0;
            if(r.isEmpty() || r.get(0).getStatus()!=1 ){
                
                objM.setIntProperty("rezultat", 0);
               
            }
            else{
                TypedQuery<Transakcija> query10 = em.createQuery("SELECT r From Transakcija r WHERE r.iDRac=  :idr", Transakcija.class);
                query10.setParameter("idr", r.get(0) );
                List<Transakcija> tran=query10.getResultList();
                int a=tran.size()-1;
                
                if(a==-1){
                    y=1;
                }else{
                    y=tran.get(a).getRedniBroj()+1;
                }
                double stanje=r.get(0).getStanje();
                double dozvoljenminus=r.get(0).getDozvoljenMinus();
                ostatak=stanje+iznos;
                
                double xx=r.get(0).getDozvoljenMinus();
                int yy=r.get(0).getStatus();
                if(r.get(0).getStatus()==0){
                        
                        TypedQuery<Racun> query1 = em.createQuery("UPDATE Racun k SET k.status= :status WHERE k.iDRac= :idr", Racun.class);
                        query1.setParameter("status", 1);
                        query1.setParameter("idr", idr);
                        int de=query1.executeUpdate();
                        em.flush();
                }
                
                
                //uvecavamo stanje
                TypedQuery<Racun> query2 = em.createQuery("UPDATE Racun k SET k.stanje= :ostatak WHERE k.iDRac= :idr", Racun.class);
                query2.setParameter("ostatak", ostatak);
                query2.setParameter("idr", idr);
                int del=query2.executeUpdate();
                flagisplata=1;
                em.flush();
                del=r.get(0).getBrojTransakcija()+1;
                //uvecavamo redni broj na racunu
                TypedQuery<Racun> query4 = em.createQuery("UPDATE Racun k SET k.brojTransakcija= :br WHERE k.iDRac= :idr", Racun.class);
                query4.setParameter("br", del);
                query4.setParameter("idr", idr);
                int de4=query4.executeUpdate();
                em.flush(); 

 
            }
              if(flagisplata==1) {
                Transakcija transakcija=new Transakcija();
                transakcija.setDatumVreme(new Date());
                transakcija.setIDRac(r.get(0));
                transakcija.setSvrha("U");
                transakcija.setIznos(iznos);
                Integer w=trans.size()+1;
                transakcija.setIDTra(w);
                //ovde uvecavamo za 1 u transakciji a gore u racunu
                
                transakcija.setRedniBroj(y);
                
                IsplataUplata isplata=new IsplataUplata();
                isplata.setIDFil(idf);
                isplata.setIDTra(trans.size()+1);
                isplata.setTip('U');
                em.persist(isplata);
                em.persist(transakcija);
                
                objM.setIntProperty("rezultat",1);
            }
             
            objM.setObject(null);
            
            } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
            } 
          
            
            producer.send(myQueue, objM);
            em.flush();
            em.getTransaction().commit();
            
        
    }

    
   public static void Prenos(int idr1, int idr2, double iznos,JMSContext context,JMSProducer producer){
        int flagprenos=0;
        int y=0;
       
        ObjectMessage objM=context.createObjectMessage();
       try {
           em.getTransaction().begin();
            em.flush();
           
           TypedQuery<Racun> query = em.createQuery("SELECT r From Racun r WHERE r.iDRac= :idr", Racun.class);
           query.setParameter("idr", idr1);
           List<Racun> r1=query.getResultList();
           TypedQuery<Racun> query1 = em.createQuery("SELECT r From Racun r WHERE r.iDRac= :idr", Racun.class);
           query1.setParameter("idr", idr2);
           List<Racun> r2=query1.getResultList();
           
           TypedQuery<Transakcija> query11 = em.createQuery("SELECT r From Transakcija r", Transakcija.class);
           List<Transakcija> trans=query11.getResultList();

           if(r2.isEmpty() || r1.isEmpty()){
               objM.setIntProperty("rezultat", 0);
               
           }
           else{
                TypedQuery<Transakcija> query10 = em.createQuery("SELECT r From Transakcija r WHERE r.iDRac=  :idr", Transakcija.class);
                query10.setParameter("idr", r1.get(0) );
                 List<Transakcija> tran=query10.getResultList();
                int a=tran.size()-1;
                
                if(a==-1){
                    y=1;
                }else{
                    y=tran.get(a).getRedniBroj()+1;
                }
                double stanje=r1.get(0).getStanje();
                double stanje2=r2.get(0).getStanje();
                double dozvoljenminus=r1.get(0).getDozvoljenMinus();
                if(stanje+dozvoljenminus>=iznos && r1.get(0).getStatus()==1 && r2.get(0).getStatus()==1){
                    double ostatak=stanje-iznos;
                    
                    TypedQuery<Racun> query2 = em.createQuery("UPDATE Racun k SET k.stanje= :ostatak WHERE k.iDRac= :idr", Racun.class);
                    query2.setParameter("ostatak", ostatak);
                    query2.setParameter("idr", idr1);
                    int del=query2.executeUpdate();
                    em.flush();
                    TypedQuery<Racun> query3 = em.createQuery("UPDATE Racun k SET k.stanje= :ostatak WHERE k.iDRac= :idr", Racun.class);
                    query3.setParameter("ostatak",stanje2+iznos );
                    query3.setParameter("idr", idr2);
                    int de2=query3.executeUpdate();
                    em.flush();
                    //rednibroj se uvecava na oba racuna
                    TypedQuery<Racun> query4 = em.createQuery("UPDATE Racun k SET k.brojTransakcija= :br WHERE k.iDRac= :idr", Racun.class);
                    query4.setParameter("br", r1.get(0).getBrojTransakcija()+1);
                    query4.setParameter("idr", idr1);
                    int de4=query4.executeUpdate();
                    em.flush();
                    
                    int x1=r1.get(0).getBrojTransakcija()+1;
                    int x2=r2.get(0).getBrojTransakcija()+1;
                    TypedQuery<Racun> query5 = em.createQuery("UPDATE Racun k SET k.brojTransakcija= :br WHERE k.iDRac= :idr", Racun.class);
                    query5.setParameter("br",r2.get(0).getBrojTransakcija()+1 );
                    query5.setParameter("idr", idr2);
                    int de5=query5.executeUpdate();
                    em.flush();
                    if(r2.get(0).getStatus()==0 && iznos>0){
                        
                        TypedQuery<Racun> query6 = em.createQuery("UPDATE Racun k SET k.status= :status WHERE k.iDRac= :idr", Racun.class);
                        query6.setParameter("status", 1);
                        query6.setParameter("idr", idr2);
                        int de=query6.executeUpdate();
                        em.flush();
                    }
                    if((-1*ostatak)==dozvoljenminus){
                        
                        TypedQuery<Racun> query7 = em.createQuery("UPDATE Racun k SET k.status= :status WHERE k.iDRac= :idr", Racun.class);
                        query7.setParameter("status", 0);
                        query7.setParameter("idr", idr1);
                        int de=query7.executeUpdate();
                        em.flush();
                    }

                    flagprenos=1;
                    
                }
                
               
               
           }
           if(flagprenos==1){
              
                Transakcija transakcija=new Transakcija();
                transakcija.setDatumVreme(new Date());
                transakcija.setIDRac(r1.get(0));
                transakcija.setSvrha("PSa");
                transakcija.setIznos(iznos);
                Integer w=trans.size()+1;
                transakcija.setIDTra(w);
                //ovde uvecavamo za 1 u transakciji a gore u racunu
                y=r1.get(0).getBrojTransakcija()+1;
                transakcija.setRedniBroj(y);
                
                
                Transakcija transakcija2=new Transakcija();
                transakcija2.setDatumVreme(new Date());
                transakcija2.setIDRac(r2.get(0));
                transakcija2.setSvrha("PNa");
                transakcija2.setIznos(iznos);
                Integer w2=trans.size()+2;
                transakcija2.setIDTra(w2);
                //ovde uvecavamo za 1 u transakciji a gore u racunu
                y=r2.get(0).getBrojTransakcija()+1;
                transakcija2.setRedniBroj(y);
                
                
                Prenos prenos=new Prenos();
                prenos.setIDTra(w);
                prenos.setIDRacNA(r2.get(0));
                
                em.persist(transakcija);
                em.persist(transakcija2);
                em.persist(prenos);

               objM.setIntProperty("rezultat", 1);
           }else{
               objM.setIntProperty("rezultat", 0);
           }

           objM.setIntProperty("podsistem", 5);
           objM.setObject(null);
       } catch (JMSException ex) {
           Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
       }
        em.flush();
        producer.send(myQueue, objM);
        em.getTransaction().commit();
        
    }
    
   public static void DohvatanjeSvihRacunaZaKomitenta(int idk, JMSContext context,JMSProducer producer){
       ObjectMessage objM=context.createObjectMessage();
       ArrayList<String> racuni=new ArrayList<>();
       int flaguspesno=0;
       em.getTransaction().begin();
       
       TypedQuery<Komitent> query = em.createQuery("SELECT k From Komitent k WHERE k.iDKom= :idk", Komitent.class);
       query.setParameter("idk", idk);
       List<Komitent> k=query.getResultList();
       
       try {
        if(k.isEmpty()){

                objM.setIntProperty("rezultat", 0);
                objM.setIntProperty("podsistem", 5);

        }
        else{


                TypedQuery<Racun> query1 = em.createQuery("SELECT r From Racun r WHERE r.iDKom= :idk", Racun.class);
                query1.setParameter("idk", k.get(0));
                List<Racun> r=query1.getResultList();

                for(Racun x: r){
                    racuni.add(x.getIDRac()+" BrTran: "+x.getBrojTransakcija()+" DozMin: "+x.getDozvoljenMinus()+" Stanje: "+x.getStanje()+"Status: "
                            +x.getStatus()+" IDMes: "+ x.getIDMes()+" Datum: "+ x.getDatum());
                    //System.out.println(x.getIDRac()+" BrTran: "+x.getBrojTransakcija()+" DozMin: "+x.getDozvoljenMinus()+" Stanje: "+x.getStanje()+"Status: "
                            //+x.getStatus()+" IDKom: "+ "IDMes: "+ x.getIDMes()+" Datum: "+ x.getDatum());
                }
                
                objM.setIntProperty("rezultat", 1);
                objM.setIntProperty("podsistem", 5);
         }
         objM.setObject(racuni);
       } catch (JMSException ex) {
               Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
       }

       producer.send(myQueue, objM);
       em.getTransaction().commit();
       
       
       
   }
   public static void DohvatanjeSvihTansakcijaZaRacun(int idr, JMSContext context,JMSProducer producer){
       ObjectMessage objM=context.createObjectMessage();
       int flaguspesno=0;
       em.getTransaction().begin();
       ArrayList<String> transakcije=new ArrayList<>();
       TypedQuery<Racun> query = em.createQuery("SELECT r From Racun r WHERE r.iDRac= :idr", Racun.class);
       query.setParameter("idr", idr);
       List<Racun> k=query.getResultList();
       
       try{
        if(k.isEmpty()){

                objM.setIntProperty("rezultat", 0);
                objM.setIntProperty("podsistem", 5);
        }
        else{
                TypedQuery<Transakcija> query1 = em.createQuery("SELECT r From Transakcija r WHERE r.iDRac= :idr", Transakcija.class);
                query1.setParameter("idr", k.get(0));
                List<Transakcija> r=query1.getResultList();

                for(Transakcija x: r){
                    transakcije.add(x.getIDTra()+" "+x.getDatumVreme()+" "+x.getIznos()+" "+x.getSvrha());
                    //System.out.println(x.getIDTra()+" "+x.getDatumVreme()+" "+x.getIznos()+" "+x.getSvrha());
                }
                objM.setIntProperty("rezultat", 1);
                objM.setIntProperty("podsistem", 5);
        }

       objM.setObject(transakcije);
       } catch (JMSException ex) {
               Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
           }
       
       
       producer.send(myQueue, objM);
       
       
       
       em.getTransaction().commit();
       
       
       
   }
   
   
   
   
   
   private static void DohvatiRazlikuUPodacima(JMSContext context, JMSProducer producer) {     
       ObjectMessage objM=context.createObjectMessage();
       ArrayList<String> racuni=new ArrayList<>();
       int flaguspesno=0;
       
       objM=context.createObjectMessage();
        flaguspesno=0;
       em.getTransaction().begin();
       ArrayList<String> transakcije=new ArrayList<>();
       TypedQuery<Racun> query6 = em.createQuery("SELECT r From Racun r ", Racun.class);
      
       List<Racun> k1=query6.getResultList();
       
       try{
        if(k1.isEmpty()){

                objM.setIntProperty("rezultat", 0);
                objM.setIntProperty("podsistem", 3);
        }
        else{
                TypedQuery<Transakcija> query1 = em.createQuery("SELECT r From Transakcija r ", Transakcija.class);

                List<Transakcija> r=query1.getResultList();

                for(Transakcija x: r){
                     transakcije.add("Racun: "+ x.getIDRac().getIDRac()+" Tanaskacija: "+x.getIDTra()+" "+x.getDatumVreme()+" "+x.getIznos()+" "+x.getSvrha().charAt(0));
                    //System.out.println(x.getIDTra()+" "+x.getDatumVreme()+" "+x.getIznos()+" "+x.getSvrha());
                }
                
                objM.setIntProperty("podsistem", 3);
        }

       objM.setObject(transakcije);
       } catch (JMSException ex) {
               Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
           }
       
       
       producer.send(myQueue, objM);
       em.getTransaction().commit();
       em.getTransaction().begin();
       
       TypedQuery<Komitent> query = em.createQuery("SELECT k From Komitent k", Komitent.class);
       List<Komitent> k=query.getResultList();
       
       try {
        if(k.isEmpty()){

                objM.setIntProperty("rezultat", 0);
                objM.setIntProperty("podsistem", 5);

        }
        else{


                TypedQuery<Racun> query1 = em.createQuery("SELECT r From Racun r", Racun.class);
                List<Racun> r=query1.getResultList();

                for(Racun x: r){
                  racuni.add(x.getIDKom().getIDKom()+" IDRac: "+x.getIDRac()+" BrTran: "+x.getBrojTransakcija()+" DozMin: "+x.getDozvoljenMinus()+" Stanje: "+x.getStanje()+"Status: "
                            +x.getStatus()+" IDMes: "+x.getIDMes()+" Datum: "+ x.getDatum());
                    //System.out.println(x.getIDRac()+" BrTran: "+x.getBrojTransakcija()+" DozMin: "+x.getDozvoljenMinus()+" Stanje: "+x.getStanje()+"Status: "
                            //+x.getStatus()+" IDKom: "+ "IDMes: "+ x.getIDMes()+" Datum: "+ x.getDatum());
                }
                
                objM.setIntProperty("podsistem", 3);
         }
         objM.setObject(racuni);
       } catch (JMSException ex) {
               Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
       }

       producer.send(myQueue, objM);
       em.getTransaction().commit();
       
   
    }
   

   
   
   
    private static void Apdejt(JMSProducer producer, JMSContext context) {
        
        
        
        try {
             ObjectMessage msg=context.createObjectMessage();
             msg.setIntProperty("podsistem", 3);   
            
            em.getTransaction().begin();
            TypedQuery<Racun> query = em.createNamedQuery("Racun.findAll", Racun.class);
            ArrayList<Racun> listaracuna = new ArrayList<Racun>(query.getResultList());
            msg.setIntProperty("duzinaracuna", listaracuna.size());
            producer.send(myQueue, msg);
            for(Racun r: listaracuna){
                msg.setIntProperty("idrac", r.getIDRac());
                msg.setIntProperty("idmes", r.getIDMes());
                msg.setIntProperty("brojtransakcija", r.getBrojTransakcija());
                msg.setDoubleProperty("dozvoljenminus", r.getDozvoljenMinus());
                msg.setIntProperty("status", r.getStatus());
                msg.setIntProperty("idk", r.getIDKom().getIDKom());
                msg.setObject(r.getDatum());
                msg.setDoubleProperty("stanje", r.getStanje());
                producer.send(myQueue, msg);
            }

            em.getTransaction().commit();
            em.getTransaction().begin();
            TypedQuery<Transakcija> query1 = em.createNamedQuery("Transakcija.findAll", Transakcija.class);
            ArrayList<Transakcija> llistatranskacija = new ArrayList<Transakcija>(query1.getResultList());
            msg.setIntProperty("duzinatransakcije", llistatranskacija.size());
            producer.send(myQueue, msg);
            for(Transakcija r: llistatranskacija){
                msg.setIntProperty("idrac", r.getIDRac().getIDRac());
                msg.setIntProperty("rednibroj", r.getRedniBroj());
                msg.setDoubleProperty("iznos", r.getIznos());
                msg.setStringProperty("svrha", r.getSvrha());
                msg.setObject(r.getDatumVreme());
                msg.setIntProperty("idtra", r.getIDTra());
                producer.send(myQueue, msg);
            }

            em.getTransaction().commit();
            em.getTransaction().begin();
            TypedQuery<Prenos> query2 = em.createNamedQuery("Prenos.findAll", Prenos.class);
            ArrayList<Prenos> listaprenosa = new ArrayList<Prenos>(query2.getResultList());
            msg.setIntProperty("duzinaprenosa", listaprenosa.size());
            producer.send(myQueue, msg);
            for(Prenos r: listaprenosa){
                msg.setObject(r.getIDRacNA().getIDRac());
                msg.setIntProperty("idtra", r.getIDTra());
                producer.send(myQueue, msg);
            }

            em.getTransaction().commit();
               
            
            em.getTransaction().begin();
            TypedQuery<IsplataUplata> query3 = em.createNamedQuery("IsplataUplata.findAll", IsplataUplata.class);
            ArrayList<IsplataUplata> isplatauplatas = new ArrayList<IsplataUplata>(query3.getResultList());
            msg.setIntProperty("duzinaiu", isplatauplatas.size());
            producer.send(myQueue, msg);
            for(IsplataUplata r: isplatauplatas){
                msg.setIntProperty("idtra", r.getIDTra());
                msg.setIntProperty("idfil", r.getIDFil());
                msg.setObject(r.getTip());
                producer.send(myQueue, msg);
            }

            em.getTransaction().commit();
            
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
    public static void main(String[] args) {
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();


        JMSConsumer consumer=context.createConsumer(myQueue,  "podsistem = 2");
        while(true){
            try {
                Message msg=null;
                System.out.println("Ceka se poruka2!!!!\n");
                int c=0;
//                while(c!=1){
//                    msg= consumer.receive();
//                }
                msg= consumer.receive();
                TextMessage msgR=(TextMessage)msg;
                //system.out.println(msgR.getIntProperty("podsistem"));
                //System.out.println(msgR.getIntProperty("nemanja"));
                String funkcija=msgR.getStringProperty("KojaFunkcija");
                //String x=msgR.getStringProperty("naziv");
                switch (funkcija){
                    case "KreiranjeRacuna":
                        int dozvoljenminus=msg.getIntProperty("dozvoljenminus");
                        int idk=msg.getIntProperty("idk");
                        int status=msg.getIntProperty("status");
                        int idm=msg.getIntProperty("idm");
                        KreiranjeRacuna(dozvoljenminus, status, idk, idm, context, producer);
                        break;
                    case "BrisanjeRacuna":
                        int idkr=msg.getIntProperty("idr");
                        BrisanjeRacuna(idkr, context, producer);
                        break;
                    case "Isplata":
                        double iznos=msg.getDoubleProperty("iznos");
                        int idir=msg.getIntProperty("idr");
                        int idff=msg.getIntProperty("idf");
                        Isplata(idir, iznos,idff ,context, producer);
                        break;
                    case "Uplata":
                        double iznosu=msg.getDoubleProperty("iznos");
                        int idur=msg.getIntProperty("idr");
                        int idfu=msg.getIntProperty("idf");
                        Uplata(idur, iznosu ,idfu ,context, producer);
                        break;
                    case "Prenos":
                        double iznosp=msg.getDoubleProperty("iznos");
                        int idr1=msg.getIntProperty("idr1");
                        int idr2=msg.getIntProperty("idr2");
                        Prenos(idr1, idr2 ,iznosp ,context, producer);
                        break;
                        
                    case "DohvatanjeSvihRacunaZaKomitenta":
                        int idkracuni=msg.getIntProperty("idk");
                        DohvatanjeSvihRacunaZaKomitenta(idkracuni ,context, producer);
                        break;
                    case "DohvatanjeSvihTansakcijaZaRacun":
                        int idrtrans=msg.getIntProperty("idr");
                        DohvatanjeSvihTansakcijaZaRacun(idrtrans ,context, producer);
                        break;
                    case "KreiranjeKomitenta":
                        KreiranjeKomitenta(msgR);
                        break;
                    case "PromenaSedistaZaZadatogKomitenta":
                        PromenaSedistaZaZadatogKomitenta(msgR);
                        break;
                    case "DohvatiRazlikuUPodacima":
                        DohvatiRazlikuUPodacima(context, producer);
                        break;
                    case "Apdejt":
                        Apdejt(producer, context);
                    default: break;
                }
                        
            } catch (JMSException ex) {
                Logger.getLogger(MainPodsistem2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    
}

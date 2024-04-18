/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import entiteti.Filijala;
import entiteti.IsplataUplata;
import entiteti.Komitent;
import entiteti.Mesto;
import entiteti.Prenos;
import entiteti.Racun;
import entiteti.Racun1;
import entiteti.Transakcija;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Text;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSProducer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Path;

/**
 *
 * @author Neca
 */

public class MainPodsistem3 {

    @Resource(lookup="projekatConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(lookup="projekatQueue")
    private static Queue myQueue;
    private static  EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem3PU");
    private static EntityManager em;
    static {
         em = emf.createEntityManager();
         
        }
    
    
    
    public static void DohvatanjeSvihMesta(JMSContext context,JMSProducer producer){
        try {
            em.getTransaction().begin();
            System.out.println("Pocinje dohvatanje svih mesta");
            
            ArrayList<ArrayList<String>> rezultat = new ArrayList<>();
            ArrayList<String> svamesta = new ArrayList<>();
            TypedQuery<Mesto> query = em.createNamedQuery("Mesto.findAll", Mesto.class);
            ArrayList<Mesto> listamesta = new ArrayList<Mesto>(query.getResultList());
            
            for(Mesto m : listamesta){
                //System.out.println(m.getIDMes()+" "+m.getNaziv()+" "+m.getPostanskiBroj());
                svamesta.add(m.getIDMes()+" "+m.getPostanskiBroj()+" "+m.getNaziv());
            }
            rezultat.add(svamesta);
            ObjectMessage objM=context.createObjectMessage();
            objM.setObject(rezultat);
            objM.setIntProperty("podsistem", 5);
            producer.send(myQueue, objM);
            em.getTransaction().commit();
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void DohvatanjeSvihFilijala(JMSContext context,JMSProducer producer){
        try {
            em.getTransaction().begin();
            System.out.println("Pocinje dohvattanje svih filijala");
            
            ArrayList<ArrayList<String>> rezultat = new ArrayList<>();
            ArrayList<String> svefilijale = new ArrayList<>();
            TypedQuery<Filijala> query = em.createNamedQuery("Filijala.findAll", Filijala.class);
            ArrayList<Filijala> listafilijala = new ArrayList<Filijala>(query.getResultList());
            
            for(Filijala m : listafilijala){
                //System.out.println(m.getIDMes()+" "+m.getNaziv()+" "+m.getPostanskiB
                //System.out.println(m.getIDFil()+" "+m.getNaziv()+" "+m.getAdresa()+" "+m.getIDMes());
                TypedQuery<Mesto> query2 = em.createQuery("SELECT m From Mesto m WHERE m.iDMes= "+m.getIDMes().getIDMes(), Mesto.class);
                List<Mesto> mesta=query2.getResultList();
                String x=mesta.get(0).getNaziv();
                svefilijale.add(m.getIDFil()+" "+m.getNaziv()+" "+m.getAdresa()+" "+mesta.get(0).getNaziv());
            }
            rezultat.add(svefilijale);
            ObjectMessage objM=context.createObjectMessage();
            objM.setObject(rezultat);
            objM.setIntProperty("podsistem", 5);
            producer.send(myQueue, objM);
            
            em.getTransaction().commit();
            //System.out.println("Kraj dohvatanja svih filijala");
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void DohvatanjeSvihKomitenata(JMSContext context,JMSProducer producer){
         try {
            em.getTransaction().begin();
            System.out.println("Pocinje dohvattanje svih komitenata");
            
            ArrayList<ArrayList<String>> rezultat = new ArrayList<>();
            ArrayList<String> svikomitenti = new ArrayList<>();
            TypedQuery<Komitent> query = em.createNamedQuery("Komitent.findAll", Komitent.class);
            ArrayList<Komitent> listakomitenata = new ArrayList<Komitent>(query.getResultList());
            
            for(Komitent m : listakomitenata){
                  //System.out.println(m.getIDKom()+" "+m.getNaziv()+" "+m.getAdresa()+" "+m.getIDMes());
                  TypedQuery<Mesto> query2 = em.createQuery("SELECT m From Mesto m WHERE m.iDMes= "+m.getIDMes().getIDMes(), Mesto.class);
                  List<Mesto> mesta=query2.getResultList();
                  String x=mesta.get(0).getNaziv();
                  svikomitenti.add(m.getIDKom()+" "+m.getNaziv()+" "+m.getAdresa()+" "+x);
            }
            rezultat.add(svikomitenti);
            ObjectMessage objM=context.createObjectMessage();
            objM.setObject(rezultat);
            objM.setIntProperty("podsistem", 5);
            producer.send(myQueue, objM);
            
            em.getTransaction().commit();
            System.out.println("Kraj dohvatanja svih filijala");
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void DohvatanjeSvihRacunaZaKomitenta( JMSContext context,JMSProducer producer){
       ObjectMessage objM=context.createObjectMessage();
       ArrayList<String> racuni=new ArrayList<>();
       int flaguspesno=0;
       em.getTransaction().begin();
       
       TypedQuery<Komitent> query = em.createQuery("SELECT k From Komitent k", Komitent.class);
       //query.setParameter("idk", idk);
       List<Komitent> k=query.getResultList();
       
       try {
        if(k.isEmpty()){

                objM.setIntProperty("rezultat", 0);
                objM.setIntProperty("podsistem", 5);

        }
        else{


                TypedQuery<Racun> query1 = em.createQuery("SELECT r From Racun r ", Racun.class);
                //query1.setParameter("idk", k.get(0));
                List<Racun> r=query1.getResultList();

                for(Racun x: r){
                    racuni.add(x.getIDKom().getIDKom()+" IDRac: "+x.getIDRac()+" BrTran: "+x.getBrojTransakcija()+" DozMin: "+x.getDozvoljenMinus()+" Stanje: "+x.getStanje()+"Status: "
                            +x.getStatus()+" IDMes: "+ x.getIDMes().getIDMes()+" Datum: "+ x.getDatum());
                    //System.out.println(x.getIDRac()+" BrTran: "+x.getBrojTransakcija()+" DozMin: "+x.getDozvoljenMinus()+" Stanje: "+x.getStanje()+"Status: "
                            //+x.getStatus()+" IDKom: "+ "IDMes: "+ x.getIDMes()+" Datum: "+ x.getDatum());
                }
                
                objM.setIntProperty("rezultat", 1);
                objM.setIntProperty("podsistem", 5);
         }
         objM.setObject(racuni);
       } catch (JMSException ex) {
               Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
       }

       producer.send(myQueue, objM);
       em.getTransaction().commit();
       
       
       
   }
    public static void DohvatanjeSvihTansakcijaZaRacun( JMSContext context,JMSProducer producer){
       ObjectMessage objM=context.createObjectMessage();
       int flaguspesno=0;
       em.getTransaction().begin();
       ArrayList<String> transakcije=new ArrayList<>();
       TypedQuery<Racun> query = em.createQuery("SELECT r From Racun r", Racun.class);
       //query.setParameter("idr", idr);
       List<Racun> k=query.getResultList();
       
       try{
        if(k.isEmpty()){

                objM.setIntProperty("rezultat", 0);
                objM.setIntProperty("podsistem", 5);
        }
        else{
                TypedQuery<Transakcija> query1 = em.createQuery("SELECT r From Transakcija r", Transakcija.class);
                //query1.setParameter("idr", k.get(0));
                List<Transakcija> r=query1.getResultList();

                for(Transakcija x: r){
                    transakcije.add("Racun: "+ x.getIDRac().getIDRac()+" Tanaskacija: "+x.getIDTra()+" "+x.getDatumVreme()+" "+x.getIznos()+" "+x.getSvrha());
                    //System.out.println(x.getIDTra()+" "+x.getDatumVreme()+" "+x.getIznos()+" "+x.getSvrha());
                }
                objM.setIntProperty("rezultat", 1);
                objM.setIntProperty("podsistem", 5);
        }

       objM.setObject(transakcije);
       } catch (JMSException ex) {
               Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
           }
       
       
       producer.send(myQueue, objM);
       
       
       
       em.getTransaction().commit();
       
       
       
   }
    
    public static void DohvatiSveIzKopije(JMSProducer producer, JMSContext context ){
        
        DohvatanjeSvihMesta(context, producer);
        DohvatanjeSvihFilijala(context, producer);
        DohvatanjeSvihKomitenata(context, producer);
        DohvatanjeSvihRacunaZaKomitenta(context, producer);
        DohvatanjeSvihTansakcijaZaRacun(context, producer);
    }
    
    
     private static void DohvatiRazlikuUPodacima(JMSProducer producer, JMSContext context, JMSConsumer consumer) {
        
            Message msg=null;
            msg= consumer.receive();
            ObjectMessage msgR=(ObjectMessage)msg;
            
            
            Message msg1=null;
            msg1= consumer.receive();
            ObjectMessage msgR1=(ObjectMessage)msg1;
            
            Message msg2=null;
            msg2= consumer.receive();
            ObjectMessage msgR2=(ObjectMessage)msg2;
            
            
            
            
         //MESTA*************************************************************************************
         try {
                       
            
            em.getTransaction().begin();
            em.flush();
            System.out.println("Pocinje dohvatanje svih mesta");
            ArrayList<ArrayList<String>> rezultat = new ArrayList<>();
            ArrayList<String> svamesta = new ArrayList<>();
            TypedQuery<Mesto> query = em.createNamedQuery("Mesto.findAll", Mesto.class);
            ArrayList<Mesto> listamesta = new ArrayList<Mesto>(query.getResultList());
            for(Mesto m : listamesta){
                //System.out.println(m.getIDMes()+" "+m.getNaziv()+" "+m.getPostanskiBroj());
                svamesta.add(m.getIDMes()+" "+m.getPostanskiBroj()+" "+m.getNaziv());
            }
            
            ArrayList<ArrayList<String>> mesta = (ArrayList<ArrayList<String>>) msgR.getObject();
            ArrayList<String> mesta1=mesta.get(0);
            int l1=svamesta.size();
            int l2=mesta1.size();
            int l3= MAX(l1, l2);
            ArrayList<Integer> mestarazlika = new ArrayList<>();
            
            for (int counter = 0; counter < l3; counter++) { 
                
                if(l1-1-counter<0){
                    mestarazlika.add(counter);
                }else if(l2-1-counter<0){
                    mestarazlika.add(counter);
                }
                else{
                    if(!svamesta.get(counter).equals(mesta1.get(counter))){
                    mestarazlika.add(counter+1);
                    }
                }
            } 
            ObjectMessage razlikem=context.createObjectMessage();
            razlikem.setObject(mestarazlika);
            razlikem.setIntProperty("podsistem", 5);
            producer.send(myQueue, razlikem);
            em.getTransaction().commit();
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
        }
        //FILAJLE************************************************************
        try {
            em.getTransaction().begin();
            System.out.println("Pocinje dohvattanje svih filijala");
            
            ArrayList<ArrayList<String>> rezultat = new ArrayList<>();
            ArrayList<String> svefilijale = new ArrayList<>();
            TypedQuery<Filijala> query = em.createNamedQuery("Filijala.findAll", Filijala.class);
            ArrayList<Filijala> listafilijala = new ArrayList<Filijala>(query.getResultList());
            
            for(Filijala m : listafilijala){
                //System.out.println(m.getIDMes()+" "+m.getNaziv()+" "+m.getPostanskiB
                //System.out.println(m.getIDFil()+" "+m.getNaziv()+" "+m.getAdresa()+" "+m.getIDMes());
                TypedQuery<Mesto> query2 = em.createQuery("SELECT m From Mesto m WHERE m.iDMes= "+m.getIDMes().getIDMes(), Mesto.class);
                List<Mesto> mesta=query2.getResultList();
                String x=mesta.get(0).getNaziv();
                svefilijale.add(m.getIDFil()+" "+m.getNaziv()+" "+m.getAdresa()+" "+mesta.get(0).getNaziv());
            }
            ArrayList<ArrayList<String>> filijale = (ArrayList<ArrayList<String>>) msgR1.getObject();
            ArrayList<String> filijale1=filijale.get(0);
            int l1=svefilijale.size();
            int l2=filijale1.size();
            int l3= MAX(l1, l2);
            ArrayList<Integer> filijalerazlika = new ArrayList<>();
            
            for (int counter = 0; counter < l3; counter++) { 
                
                if(l1-1-counter<0){
                    filijalerazlika.add(counter);
                }else if(l2-1-counter<0){
                    filijalerazlika.add(counter);
                }
                else{
                    if(!svefilijale.get(counter).equals(filijale1.get(counter))){
                    filijalerazlika.add(counter+1);
                    }
                }

            }
            ObjectMessage razlikef=context.createObjectMessage();
            razlikef.setObject(filijalerazlika);
             razlikef.setIntProperty("podsistem", 5);
            producer.send(myQueue, razlikef);
            em.getTransaction().commit();
            //System.out.println("Kraj dohvatanja svih filijala");
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        //kOMITENTI************************************************************
            
        try {
            em.getTransaction().begin();
            System.out.println("Pocinje dohvattanje svih komitenata");
            
            ArrayList<ArrayList<String>> rezultat = new ArrayList<>();
            ArrayList<String> svikomitenti = new ArrayList<>();
            TypedQuery<Komitent> query = em.createNamedQuery("Komitent.findAll", Komitent.class);
            ArrayList<Komitent> listakomitenata = new ArrayList<Komitent>(query.getResultList());
            
            for(Komitent m : listakomitenata){
                  //System.out.println(m.getIDKom()+" "+m.getNaziv()+" "+m.getAdresa()+" "+m.getIDMes());
                  TypedQuery<Mesto> query2 = em.createQuery("SELECT m From Mesto m WHERE m.iDMes= "+m.getIDMes().getIDMes(), Mesto.class);
                  List<Mesto> mesta=query2.getResultList();
                  String x=mesta.get(0).getNaziv();
                  svikomitenti.add(m.getIDKom()+" "+m.getNaziv()+" "+m.getAdresa()+" "+x);
            }
            ArrayList<ArrayList<String>> komitenti = (ArrayList<ArrayList<String>>) msgR2.getObject();
            ArrayList<String> komitenti1=komitenti.get(0);
            int l1=svikomitenti.size();
            int l2=komitenti1.size();
            int l3= MAX(l1, l2);
            ArrayList<Integer> komitentirazlika = new ArrayList<>();
            
            for (int counter = 0; counter < l3; counter++) { 
                
                if(l1-1-counter<0){
                    komitentirazlika.add(counter);
                }else if(l2-1-counter<0){
                    komitentirazlika.add(counter);
                }
                else{
                    if(!svikomitenti.get(counter).equals(komitenti1.get(counter))){
                    komitentirazlika.add(counter+1);
                    }
                }

            }
             ObjectMessage razlikek=context.createObjectMessage();
            razlikek.setObject(komitentirazlika);
             razlikek.setIntProperty("podsistem", 5);
            producer.send(myQueue, razlikek);
            em.getTransaction().commit();
            System.out.println("Kraj dohvatanja svih filijala");
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
  
      private static void DohvatiRazlikuUPodacima2(JMSProducer producer, JMSContext context, JMSConsumer consumer) {
        
//            Message msg=null;
//            msg= consumer.receive();
//            ObjectMessage msgR=(ObjectMessage)msg;
            
            
       Message msg1=null;
       msg1= consumer.receive();
       ObjectMessage msgR1=(ObjectMessage)msg1;
       ObjectMessage objM=context.createObjectMessage();
       int flaguspesno=0;
       em.getTransaction().begin();
       ArrayList<String> transakcije=new ArrayList<>();
       TypedQuery<Racun> query = em.createQuery("SELECT r From Racun r", Racun.class);
       //query.setParameter("idr", idr);
       List<Racun> k=query.getResultList();
       
       try{
        if(k.isEmpty()){

                objM.setIntProperty("rezultat", 0);
                objM.setIntProperty("podsistem", 5);
        }
        else{
                TypedQuery<Transakcija> query1 = em.createQuery("SELECT r From Transakcija r", Transakcija.class);
                //query1.setParameter("idr", k.get(0));
                List<Transakcija> r=query1.getResultList();

                for(Transakcija x: r){
                    transakcije.add("Racun: "+ x.getIDRac().getIDRac()+" Tanaskacija: "+x.getIDTra()+" "+x.getDatumVreme()+" "+x.getIznos()+" "+x.getSvrha());
                    //System.out.println(x.getIDTra()+" "+x.getDatumVreme()+" "+x.getIznos()+" "+x.getSvrha());
                }
        }
        
           ArrayList<ArrayList<String>> transakcije1 = (ArrayList<ArrayList<String>>) msgR1.getObject();
           //ArrayList<String> transakcije2=transakcije1.get(0);
           int l1=transakcije1.size();
           int l2=transakcije.size();
           int l3= MAX(l1, l2);
            ArrayList<Integer> transakcijaazlika = new ArrayList<>();
            
            for (int counter = 0; counter < l3; counter++) { 
                
                if(l1-1-counter<0){
                    transakcijaazlika.add(counter+1);
                }else if(l2-1-counter<0){
                    transakcijaazlika.add(counter+1);
                }
                else{
                    if(!transakcije.get(counter).equals(transakcije1.get(counter))){
                    transakcijaazlika.add(counter+1);
                    }
                }

            }
        objM.setObject(transakcijaazlika);
        objM.setIntProperty("podsistem", 5);
        
       } catch (JMSException ex) {
               Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
           }
       
       
       producer.send(myQueue, objM);
       em.getTransaction().commit();
            
       
       
       
       
        msg1=null;
        msg1= consumer.receive();
        msgR1=(ObjectMessage)msg1;
       
       objM=context.createObjectMessage();
       ArrayList<String> racuni=new ArrayList<>();
       flaguspesno=0;
       em.getTransaction().begin();
       
       TypedQuery<Komitent> query3 = em.createQuery("SELECT k From Komitent k", Komitent.class);
       //query.setParameter("idk", idk);
       List<Komitent> k1=query3.getResultList();
       
       try {
        if(k1.isEmpty()){

                objM.setIntProperty("rezultat", 0);
                objM.setIntProperty("podsistem", 5);

        }
        else{


                TypedQuery<Racun> query1 = em.createQuery("SELECT r From Racun r ", Racun.class);
                //query1.setParameter("idk", k.get(0));
                List<Racun> r=query1.getResultList();

                for(Racun x: r){
                    racuni.add(x.getIDKom().getIDKom()+" IDRac: "+x.getIDRac()+" BrTran: "+x.getBrojTransakcija()+" DozMin: "+x.getDozvoljenMinus()+" Stanje: "+x.getStanje()+"Status: "
                            +x.getStatus()+" IDMes: "+ x.getIDMes().getIDMes()+" Datum: "+ x.getDatum());
                    //System.out.println(x.getIDRac()+" BrTran: "+x.getBrojTransakcija()+" DozMin: "+x.getDozvoljenMinus()+" Stanje: "+x.getStanje()+"Status: "
                            //+x.getStatus()+" IDKom: "+ "IDMes: "+ x.getIDMes()+" Datum: "+ x.getDatum());
                }
                
              
                    ArrayList<ArrayList<String>> racuni1 = (ArrayList<ArrayList<String>>) msgR1.getObject();
                    ArrayList<Integer> racunirazlika = new ArrayList<>();
                    int l1=racuni.size();
                    int l2=racuni1.size();
                    int l3= MAX(l1, l2);
                  
            
            for (int counter = 0; counter < l3; counter++) { 
                
                if(l1-1-counter<0){
                    racunirazlika.add(counter+1);
                }else if(l2-1-counter<0){
                    racunirazlika.add(counter+1);
                }
                else{
                    //5 IDRac: 5 BrTran: 0 DozMin: 1000.0 Stanje: 1500.0Status: 1 IDMes: 4 Datum: Fri Aug 06 16:00:55 CEST 2021
                    //5 IDRac: 5 BrTran: 0 DozMin: 1000.0 Stanje: 1500.0Status: 1 IDMes: 4 Datum: Fri Aug 06 16:00:55 CEST 2021
                    if(!racuni.get(counter).equals(racuni1.get(counter))){
                    racunirazlika.add(counter+1);
                    }
                }

            }
                objM.setObject(racunirazlika);
                
                //objM.setIntProperty("rezultat", 1);
                objM.setIntProperty("podsistem", 5);
         }
         //objM.setObject(racuni);
       } catch (JMSException ex) {
               Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
       }
       producer.send(myQueue, objM);
       em.getTransaction().commit();
 
       
            
      }
      
     
     
     
    private static int MAX(int l1, int l2) {
        if(l1>l2){
            return l1;
        }
        return l2;
    }
    
    private static java.sql.Connection connection = null;
    private static java.sql.Connection getConnection() throws Exception {
        if (connection != null) {
            return connection;
        }
        connection = (java.sql.Connection) DriverManager.getConnection("jdbc:mysql://localhost/banka_backup?user=root&password=123");
        return connection;
    }
    
     
    
    public static void Apdejt(JMSConsumer consumer, JMSContext context){
        
        try {
            em.getTransaction().begin();
          
            
            em.flush();
            TypedQuery<IsplataUplata> query4 = em.createQuery("DELETE FROM IsplataUplata m", IsplataUplata.class);
            int de4=query4.executeUpdate();
            
            TypedQuery<Prenos> query5 = em.createQuery("DELETE FROM Prenos m", Prenos.class);
            int de5=query5.executeUpdate();
            
             TypedQuery<Transakcija> query6 = em.createQuery("DELETE FROM Transakcija m", Transakcija.class);
            int de6=query6.executeUpdate();
            
            TypedQuery<Racun> query7 = em.createQuery("DELETE FROM Racun m", Racun.class);
            int de7=query7.executeUpdate();
            
            em.getTransaction().commit();
            em.getTransaction().begin();
            
            TypedQuery<Filijala> query2 = em.createQuery("DELETE FROM Filijala m", Filijala.class);
            int de2=query2.executeUpdate();
            
            TypedQuery<Komitent> query1 = em.createQuery("DELETE FROM Komitent m", Komitent.class);
            int del=query1.executeUpdate();
            
            TypedQuery<Mesto> query3 = em.createQuery("DELETE FROM Mesto m WHERE m.iDMes>0", Mesto.class);
            int de3=query3.executeUpdate();
            
            
           
            
            em.getTransaction().commit();
            em.getTransaction().begin();
            Statement statement = getConnection().createStatement();
            
            statement.execute("ALTER TABLE banka_backup.Mesto auto_increment=0");
            statement.execute("ALTER TABLE banka_backup.Filijala auto_increment=0");
            statement.execute("ALTER TABLE banka_backup.Komitent auto_increment=0");
            statement.execute("ALTER TABLE banka_backup.Racun auto_increment=0");
            statement.execute("ALTER TABLE banka_backup.Transakcija auto_increment=0");
            
            em.getTransaction().commit();
            em.getTransaction().begin();
            Message msg1=null;
            msg1= consumer.receive();
            ObjectMessage msgR1=(ObjectMessage)msg1;
            ObjectMessage objM=context.createObjectMessage();
            ArrayList<Mesto> podaci = (ArrayList<Mesto>) msgR1.getObject();
            for(Mesto m: podaci){
                m.setFilijalaList(null);
                m.setKomitentList(null);
            }
            for(Mesto m: podaci){
                em.persist(m);
                em.flush();
            }
            em.getTransaction().commit();
            em.getTransaction().begin();
            msg1= consumer.receive();
            msgR1=(ObjectMessage)msg1;
            ArrayList<Filijala> podaci1 = (ArrayList<Filijala>) msgR1.getObject();
            for(Filijala f: podaci1){
                em.persist(f);
                em.flush();
            }
            em.getTransaction().commit();
            em.getTransaction().begin();
            msg1= consumer.receive();
            msgR1=(ObjectMessage)msg1;
            ArrayList<Komitent> podaci2 = (ArrayList<Komitent>) msgR1.getObject();
            for(Komitent k: podaci2){
                em.persist(k);
                em.flush();
            }
            em.flush();
            em.getTransaction().commit();
            
            
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void Apdejt2(JMSConsumer consumer, JMSContext context){
        
        try {
            sleep(2500);
             em.getTransaction().begin();
          
            
            em.flush();
            
            TypedQuery<IsplataUplata> query14 = em.createQuery("DELETE FROM IsplataUplata m", IsplataUplata.class);
            int de4=query14.executeUpdate();
            
            TypedQuery<Prenos> query15 = em.createQuery("DELETE FROM Prenos m", Prenos.class);
            int de5=query15.executeUpdate();
            
             TypedQuery<Transakcija> query16 = em.createQuery("DELETE FROM Transakcija m", Transakcija.class);
            int de6=query16.executeUpdate();
            
            TypedQuery<Racun> query17 = em.createQuery("DELETE FROM Racun m", Racun.class);
            int de17=query17.executeUpdate();
            
            em.getTransaction().commit();
            em.getTransaction().begin();
            Statement statement = getConnection().createStatement();
            
            
            statement.execute("ALTER TABLE banka_backup.Racun auto_increment=0");
            statement.execute("ALTER TABLE banka_backup.Transakcija auto_increment=0");
            
            em.getTransaction().commit();
            
            
            
            Message msg1; //consumer.receive();
            ObjectMessage msgR1;
            int idk,idr,idm,brtr,status, d;
            double stanje, dozvoljenm; 
            Date date;
            em.getTransaction().begin();
            msg1= consumer.receive();
            msgR1=(ObjectMessage)msg1;
            d=msg1.getIntProperty("duzinaracuna");
            
            while(d>0){
                msg1= consumer.receive();
                msgR1=(ObjectMessage)msg1;
                idr=msgR1.getIntProperty("idrac");
                idm=msgR1.getIntProperty("idmes");
                brtr=msgR1.getIntProperty("brojtransakcija");
                dozvoljenm=msgR1.getDoubleProperty("dozvoljenminus");
                status=msgR1.getIntProperty("status");
                idk=msgR1.getIntProperty("idk");
                date=(Date)msgR1.getObject();
                stanje=msg1.getDoubleProperty("stanje");
                TypedQuery<Mesto> query = (TypedQuery<Mesto>) em.createQuery("SELECT m From Mesto m WHERE m.iDMes= :idm", Mesto.class);
                query.setParameter("idm", idm);
                List<Mesto> mesta=query.getResultList();
                TypedQuery<Komitent> query1 = (TypedQuery<Komitent>) em.createQuery("SELECT m From Komitent m WHERE m.iDKom= :idm", Komitent.class);
                query1.setParameter("idm", idk);
                List<Komitent> komitenti=query1.getResultList();
                
                
                Racun r=new Racun();
                r.setBrojTransakcija(brtr);
                r.setDatum(date);
                r.setDozvoljenMinus(dozvoljenm);
                r.setIDKom(komitenti.get(0));
                r.setStatus(status);
                r.setStanje(stanje);
                r.setIDRac(idr);
                r.setIDMes(mesta.get(0));
                em.persist(r);
                em.flush();
                d--;
            }
            em.flush();
            em.getTransaction().commit();
            em.getTransaction().begin();
            msg1= consumer.receive();
            msgR1=(ObjectMessage)msg1;
            d=msg1.getIntProperty("duzinatransakcije");
            
             while(d>0){
                msg1= consumer.receive();
                msgR1=(ObjectMessage)msg1;
                idr=msgR1.getIntProperty("idrac");
                
                brtr=msgR1.getIntProperty("rednibroj");
               
                double iznos=msgR1.getDoubleProperty("iznos");
                String svrha=msgR1.getStringProperty("svrha");
                int idtra=msgR1.getIntProperty("idtra");
                date=(Date)msgR1.getObject();
                
                
               
                TypedQuery<Racun> query2 = (TypedQuery<Racun>) em.createQuery("SELECT m From Racun m WHERE m.iDRac= :idm", Racun.class);
                query2.setParameter("idm", idr);
                List<Racun> racuni=query2.getResultList();
                
                
                Transakcija r=new Transakcija();
                r.setDatumVreme(date);
                r.setIDRac(racuni.get(0));
                r.setIDTra(idtra);
                r.setIznos(iznos);
                r.setRedniBroj(brtr);
                r.setSvrha(svrha.charAt(0));
                
                em.persist(r);
                em.flush();
                d--;
            }
             em.flush();
            em.getTransaction().commit();
 
            
            em.getTransaction().begin();
            msg1= consumer.receive();
            msgR1=(ObjectMessage)msg1;
            d=msg1.getIntProperty("duzinaprenosa");
            
             while(d>0){
                msg1= consumer.receive();
                msgR1=(ObjectMessage)msg1;
                int racun=(int)msgR1.getObject();
                int idtra=msgR1.getIntProperty("idtra");
                TypedQuery<Racun> query3 = (TypedQuery<Racun>) em.createQuery("SELECT m From Racun m WHERE m.iDRac= :idm", Racun.class);
                query3.setParameter("idm", racun);
                List<Racun> racuniprenos=query3.getResultList();
                
                
                TypedQuery<Transakcija> query4 = (TypedQuery<Transakcija>) em.createQuery("SELECT m From Transakcija m WHERE m.iDTra= :idm", Transakcija.class);
                query4.setParameter("idm", idtra);
                List<Transakcija> listar=query4.getResultList();
                
                TypedQuery<Transakcija> query5 = (TypedQuery<Transakcija>) em.createQuery("SELECT m From Transakcija m", Transakcija.class);
                //query4.setParameter("idm", idtra);
                List<Transakcija> listar1=query5.getResultList();
                
                
                Prenos r=new Prenos(listar.get(0).getIDTra());
                r.setIDRacNA(racuniprenos.get(0));
               
                
                em.persist(r);
                em.flush();
                d--;
            }
            em.flush();
            em.getTransaction().commit();

            em.getTransaction().begin();
            em.flush();
            msg1= consumer.receive();
            msgR1=(ObjectMessage)msg1;
            d=msg1.getIntProperty("duzinaiu");
            
             while(d>0){
                msg1= consumer.receive();
                msgR1=(ObjectMessage)msg1;
                char tip=(char)msgR1.getObject();
                int idf=msgR1.getIntProperty("idfil");
                int idtra=msgR1.getIntProperty("idtra");
                
                
                TypedQuery<Filijala> query4 = (TypedQuery<Filijala>) em.createQuery("SELECT m From Filijala m WHERE m.iDFil= :idm", Filijala.class);
                query4.setParameter("idm", idf);
                List<Filijala> filijala=query4.getResultList();
                
                
                TypedQuery<Transakcija> query5 = (TypedQuery<Transakcija>) em.createQuery("SELECT m From Transakcija m WHERE m.iDTra= :idm", Transakcija.class);
                query5.setParameter("idm", idtra);
                List<Transakcija> listara=query5.getResultList();
                
               
                
               IsplataUplata r=new IsplataUplata();
               r.setTip(tip);
               r.setIDFil(filijala.get(0));
               //r.setTransakcija(listara.get(0));
               r.setIDTra(idtra);
                
                em.persist(r);
                em.flush();
                d--;
            }
            em.getTransaction().commit();

            
            
            
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void main(String[] args) {
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();        
        JMSConsumer consumer=context.createConsumer(myQueue,  "podsistem = 3");
        while(true){
            try {
                Message msg=null;
                System.out.println("podsistem3.MainPodsistem3.main()");

                  
                  

                msg= consumer.receive();
                int c=0;
//                while(c!=9){
//                    msg= consumer.receive();
//                    //c++;
//                }
                TextMessage msgR=(TextMessage)msg;
                String funkcija=msgR.getStringProperty("KojaFunkcija");
               
                switch (funkcija){
                    case "DohvatiSveIzKopije":
                        DohvatiSveIzKopije(producer, context);
                        break;
                    case "DohvatiRazlikuUPodacima":
                            DohvatiRazlikuUPodacima(producer, context, consumer);
                            DohvatiRazlikuUPodacima2(producer, context, consumer);
                            break;
                    case "Apdejt":
                         Apdejt(consumer, context);
                         Apdejt2(consumer, context);
                        break;
                    default: break;
                }
                        
            } catch (JMSException ex) {
                Logger.getLogger(MainPodsistem3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    

   
}

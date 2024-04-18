/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;


import entiteti.Filijala;
import entiteti.Komitent;
import entiteti.Mesto;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.*;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
/**
 *
 * @author Neca
 */
@Path("Podsistem1")
public class MainPodsistem1 {

    @Resource(lookup="projekatConnectionFactory")
    public static ConnectionFactory connectionFactory;
    @Resource(lookup="projekatQueue")
    public static Queue myQueue;
    private static  EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem1PU");
    private static EntityManager em;
    static {
         em = emf.createEntityManager();
         
        }
    
    
    
    
    public static void KreiranjeMesta(String naziv, int idm, int postanskibroj, JMSContext context,JMSProducer producer){
              ObjectMessage objM=context.createObjectMessage();
        try {
            em.getTransaction().begin();
            System.out.println("Pocinje kreiranje mesta");
            
            
            TypedQuery<Mesto> query2 = em.createQuery("SELECT m From Mesto m ", Mesto.class);
            List<Mesto> m=query2.getResultList();
            idm=m.size()+1;
            
            Mesto mesto=new Mesto();
            mesto.setIDMes(idm);
            mesto.setPostanskiBroj(postanskibroj);
            mesto.setNaziv(naziv);
            TypedQuery<Mesto> query = em.createQuery("SELECT m From Mesto m WHERE m.postanskiBroj= "+postanskibroj, Mesto.class);
            List<Mesto> mesta=query.getResultList();
           
            objM.setIntProperty("podsistem", 5);
            
            if(mesta.isEmpty()){
                
                objM.setIntProperty("rezultat", 1);
                em.persist(mesto); 
            }
            else{
                
                 objM.setIntProperty("rezultat", 0);
            }
            
            
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
             
            producer.send(myQueue, objM);
             
            em.getTransaction().commit();
            System.out.println("podsistem1.MainPodsistem1.KreiranjeMesta()");
             
             
             
    }
    public static void KreiranjeFilijaleUMestu(int idf, String mesto, String adresaf, String naziv,JMSContext context,JMSProducer producer ){
              ObjectMessage objM=context.createObjectMessage();
        try {
            em.getTransaction().begin();
            System.out.println("Pocinje kreiranje mesta");
            
            TypedQuery<Mesto> query = em.createQuery("SELECT m From Mesto m WHERE m.naziv= :naziv", Mesto.class);
            query.setParameter("naziv", mesto);
            List<Mesto> mesta=query.getResultList();
            TypedQuery<Filijala> query1 = em.createQuery("SELECT f From Filijala f WHERE f.naziv= :naziv", Filijala.class);
            query1.setParameter("naziv", naziv);
            List<Filijala> filijalapostoji=query1.getResultList();
            
            TypedQuery<Filijala> query2 = em.createQuery("SELECT f From Filijala f ", Filijala.class);
            List<Filijala> k=query2.getResultList();
            idf=k.size()+1;
            
            
            Filijala filijala=new Filijala();
            filijala.setAdresa(adresaf);
            filijala.setIDFil(idf);
            filijala.setIDMes(mesta.get(0));
            filijala.setNaziv(naziv);
            
            objM.setIntProperty("podsistem", 5);
            
            if(filijalapostoji.isEmpty()){
                
                objM.setIntProperty("rezultat", 1);
                em.persist(filijala); 
            }
            else{
                
                 objM.setIntProperty("rezultat", 0);
            }
            
            
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
             
            producer.send(myQueue, objM);
             
            em.getTransaction().commit();
            System.out.println("podsistem1.MainPodsistem1.KreiranjeMesta()");
    }
    public static void KreiranjeKomitenta(int idk, String mesto, String adresa, String naziv,JMSContext context,JMSProducer producer){
               ObjectMessage objM=context.createObjectMessage();
               TextMessage objM2=context.createTextMessage();
        try {
            em.getTransaction().begin();
            System.out.println("Pocinje kreiranje komitenta");
            
            TypedQuery<Mesto> query = em.createQuery("SELECT m From Mesto m WHERE m.naziv= :naziv", Mesto.class);
            query.setParameter("naziv", mesto);
            List<Mesto> mesta=query.getResultList();
            TypedQuery<Komitent> query1 = em.createQuery("SELECT k FROM Komitent k ", Komitent.class);
            List<Komitent> k=query1.getResultList();
           
            idk= k.size()+1;
            if(mesta.isEmpty()){
                objM.setIntProperty("rezultat", 0);
                
            }
            else{
                Komitent komitent=new Komitent();
                komitent.setAdresa(adresa);
                komitent.setIDKom(idk);
                komitent.setIDMes(mesta.get(0));
                komitent.setNaziv(naziv);

                objM.setIntProperty("rezultat", 1);
                
                objM2.setStringProperty("KojaFunkcija", "KreiranjeKomitenta");
                objM2.setIntProperty("podsistem", 2);
                objM2.setStringProperty("adresa", adresa);
                objM2.setIntProperty("idk", idk);
                objM2.setStringProperty("mesto", ""+mesta.get(0).getIDMes());
                objM2.setStringProperty("naziv", naziv);
                objM2.setStringProperty("KojaFunkcija", "KreiranjeKomitenta");
                em.persist(komitent);
                producer.send(myQueue, objM2);
            }
            objM.setIntProperty("podsistem", 5);
                  
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
             
            producer.send(myQueue, objM);
            
            em.getTransaction().commit();
            
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
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void PromenaSedistaZaZadatogKomitenta(int idk, String nazivmesta,JMSContext context,JMSProducer producer ){
        try {
            em.getTransaction().begin();
            System.out.println("Pocinje promena sedista");
            
            TypedQuery<Mesto> query = em.createQuery("SELECT m From Mesto m WHERE m.naziv= :naziv", Mesto.class);
            query.setParameter("naziv", nazivmesta);
            List<Mesto> mesta=query.getResultList();
            
            TypedQuery<Komitent> query1 = em.createQuery("SELECT k FROM Komitent k WHERE k.iDKom= :idkom ", Komitent.class);
            query1.setParameter("idkom", idk);
            List<Komitent> k=query1.getResultList();
            
            Integer idm=mesta.get(0).getIDMes();
            TextMessage msg=context.createTextMessage();
            ObjectMessage objM=context.createObjectMessage();
            objM.setIntProperty("podsistem", 5);
            
            if(mesta.isEmpty()){
               
                objM.setIntProperty("rezultat", 0);

                
            }else if(k.isEmpty()){
                 objM.setIntProperty("rezultat", 0);
                
            }else{
                TypedQuery<Komitent> query2=em.createQuery("UPDATE Komitent k SET k.iDMes= :idm  WHERE k.iDKom= :idk ", Komitent.class);
                query2.setParameter("idm", mesta.get(0));
                query2.setParameter("idk", idk);
                int uradi=query2.executeUpdate();
                objM.setIntProperty("rezultat", 1);
                
                TextMessage objM2=context.createTextMessage();
                objM2.setStringProperty("mesto", ""+mesta.get(0).getIDMes());
                objM2.setIntProperty("idk", idk);
                objM2.setIntProperty("podsistem", 2);
                objM2.setStringProperty("KojaFunkcija", "PromenaSedistaZaZadatogKomitenta");
                producer.send(myQueue, objM2);
                
                
            }
            em.flush();
            producer.send(myQueue, objM);
            em.getTransaction().commit();
           
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
                    
    
    
     private static void DohvatiRazlikuUPodacima(JMSContext context, JMSProducer producer) {
       try {
           

            sleep(1500);
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
            objM.setIntProperty("podsistem", 3);
            producer.send(myQueue, objM);
            em.getTransaction().commit();
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       try {
            em.getTransaction().begin();
            System.out.println("Pocinje dohvatanje svih filijala");
            
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
            objM.setIntProperty("podsistem", 3);
            producer.send(myQueue, objM);
            
            em.getTransaction().commit();
            //System.out.println("Kraj dohvatanja svih filijala");
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
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
            objM.setIntProperty("podsistem", 3);
            producer.send(myQueue, objM);
            
            em.getTransaction().commit();
            System.out.println("Kraj dohvatanja svih komitenata");
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
    }
    
    
    
public static void Apdejt(JMSProducer producer, JMSContext context){
    
    
    try {
            ObjectMessage msg=context.createObjectMessage();
            msg.setIntProperty("podsistem", 3);
            em.getTransaction().begin();
            TypedQuery<Mesto> query = em.createNamedQuery("Mesto.findAll", Mesto.class);
            ArrayList<Mesto> listamesta = new ArrayList<Mesto>(query.getResultList());
            msg.setObject(listamesta);
            producer.send(myQueue, msg);
            em.getTransaction().commit();
            
            
            
            em.getTransaction().begin();
            TypedQuery<Filijala> query1 = em.createNamedQuery("Filijala.findAll", Filijala.class);
            ArrayList<Filijala> listafilijala = new ArrayList<Filijala>(query1.getResultList());
            msg.setObject(listafilijala);
            producer.send(myQueue, msg);
            em.getTransaction().commit();
            
            em.getTransaction().begin();
            TypedQuery<Komitent> query2 = em.createNamedQuery("Komitent.findAll", Komitent.class);
            ArrayList<Komitent> listakomitenata = new ArrayList<Komitent>(query2.getResultList());
            msg.setObject(listakomitenata);
            producer.send(myQueue, msg);
            em.getTransaction().commit();
        } catch (JMSException ex) {
            Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    
}









public static void main(String[] args) {
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();        
        JMSConsumer consumer=context.createConsumer(myQueue,  "podsistem = 1");
        while(true){
            try {
                Message msg=null;
                System.out.println("podsistem1.MainPodsistem1.main()");
                msg= consumer.receive();
                TextMessage msgR=(TextMessage)msg;
                String funkcija=msgR.getStringProperty("KojaFunkcija");
               
                switch (funkcija){
                    case "KreiranjeMesta":
                        String naziv=msgR.getStringProperty("naziv");
                        int postanskibroj=msgR.getIntProperty("postanskibroj");
                        int idm=msgR.getIntProperty("idm");
                        KreiranjeMesta(naziv, idm, postanskibroj, context, producer);
                        break;
                    case "KreiranjeFilijaleUMestu":
                        String nazivf=msgR.getStringProperty("naziv");
                        int idf=msgR.getIntProperty("idf");
                        String mesto=msgR.getStringProperty("idm");
                        String adresaf=msgR.getStringProperty("adresa");
                        KreiranjeFilijaleUMestu(idf, mesto, adresaf, nazivf, context, producer);
                        break;
                    case "KreiranjeKomitenta":
                        String nazivk=msgR.getStringProperty("naziv");
                        int idk=msgR.getIntProperty("idk");
                        String mestok=msgR.getStringProperty("idm");
                        String adresak=msgR.getStringProperty("adresa");
                        KreiranjeKomitenta(idk,  mestok, adresak,  nazivk, context, producer);
                        break;
                    case "DohvatanjeSvihMesta":
                        DohvatanjeSvihMesta(context, producer);
                        break;
                    case "PromenaSedistaZaZadatogKomitenta":
                        String nazivmesta=msgR.getStringProperty("nazivmesta");
                        int idkom=msgR.getIntProperty("idk");
                        PromenaSedistaZaZadatogKomitenta(idkom, nazivmesta, context, producer);
                        break;
                    case "DohvatanjeSvihFilijala":
                        DohvatanjeSvihFilijala(context, producer);
                        break;
                    case "DohvatanjeSvihKomitenata":
                        DohvatanjeSvihKomitenata(context, producer);
                        break;
                    case "DohvatiRazlikuUPodacima":
                        DohvatiRazlikuUPodacima(context, producer);
                        break;
                        
                    case "Apdejt":
                        Apdejt(producer, context);
                        break;
                    default: break;
                }
                        
            } catch (JMSException ex) {
                Logger.getLogger(MainPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

   
}

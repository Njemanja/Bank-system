/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationclient1;


import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.POST;
import static sun.net.util.IPAddressUtil.scan;




public class Main {

    @Resource(lookup="projekatConnectionFactory")
    public static ConnectionFactory connectionFactory;
    @Resource(lookup="projekatQueue")
    public static Queue myQueue;
    
    
   
    
    
   public static void KreirajMesto(int pbroj, String ime, JMSConsumer consumer){
       
       try {
           System.out.println("SALJE SE ZAHTEV!!!");
           ime=ime.replace(" ", "%20");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem1/KreiranjeMesta/" + pbroj+"/"+ime;
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("POST");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           int c=0;
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           int rezultat= objm.getIntProperty("rezultat");
           if(rezultat==0){
                   System.out.println("Neuspesno kreiranje mesta.");
           }else{
                    System.out.println("Uspesno kreiranje mesta.");
           }
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
   } //radi
   public static void KreirajFilijaluUMestu(String naziv, String adresa, String mesto, JMSConsumer consumer){
       try {
           System.out.println("SALJE SE ZAHTEV!!!");
           naziv=naziv.replaceAll(" ", "%20");
           adresa=adresa.replaceAll(" ", "%20");
           mesto=mesto.replaceAll(" ", "%20");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem1/KreiranjeFilijale/" + naziv+"/"+adresa+"/"+mesto;
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("POST");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           int rezultat= objm.getIntProperty("rezultat");
           if(rezultat==0){
                   System.out.println("Neuspesno kreiranje filijale.");
           }else{
                    System.out.println("Uspesno kreiranje filijale.");
           }
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   public static void KreirajKomitenta(String naziv, String adresa, String mesto, JMSConsumer consumer){
        try {
           System.out.println("SALJE SE ZAHTEV!!!");
           naziv=naziv.replace(" ", "%20");
           adresa=adresa.replace(" ", "%20");
           mesto=mesto.replace(" ", "%20");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem1/KreiranjeKomitenta/1/" + naziv+"/"+adresa+"/"+mesto;
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("POST");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           int rezultat= objm.getIntProperty("rezultat");
           if(rezultat==0){
                   System.out.println("Neuspesno kreiranje komitenta.");
           }else{
                    System.out.println("Uspesno kreiranje komitenta.");
           }
           
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   public static void PromeniMestoKomitenta(int idk, String mesto, JMSConsumer consumer){
       
       
       try {
           System.out.println("SALJE SE ZAHTEV!!!");
           mesto=mesto.replace(" ", "%20");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem1/PromenaSedistaZaZadatogKomitenta/" + idk+"/"+mesto;
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("PUT");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
            Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           int rezultat= objm.getIntProperty("rezultat");
           if(rezultat==0){
                   System.out.println("Neuspesno kreiranje komitenta.");
           }else{
                    System.out.println("Uspesno kreiranje komitenta.");
           }
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
       
   }
  
   public static void DohvatiSvaMesta(JMSConsumer consumer){
       try {
           System.out.println("SALJE SE ZAHTEV!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem1/DohvatiSvaMesta";
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           ArrayList<ArrayList<String>> mesta = (ArrayList<ArrayList<String>>) objm.getObject();
           ArrayList<String> mesta1=mesta.get(0);
           for(String m: mesta1){
                    System.out.println(m);
            }
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   public static void DohvatiSveFilijale(JMSConsumer consumer){
       try {
           System.out.println("SALJE SE ZAHTEV!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem1/DohvatiSveFilijale";
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           ArrayList<ArrayList<String>> filijale = (ArrayList<ArrayList<String>>) objm.getObject();
           ArrayList<String> filijale1=filijale.get(0);
           for(String f: filijale1){
                    System.out.println(f);
            }
           
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   public static void DohvatiSveKomitente(JMSConsumer consumer){
      
        try {
           System.out.println("SALJE SE ZAHTEV!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem1/DohvatiSveKomitente";
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           ArrayList<ArrayList<String>> komitenti = (ArrayList<ArrayList<String>>) objm.getObject();
           ArrayList<String> komitenti1=komitenti.get(0);
           for(String k: komitenti1){
                    System.out.println(k);
            }
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
       
   }
   
   
   
   public static void OtvaranjeRacuna(int dozvoljenminus, int idm, int idk, JMSConsumer consumer){
        try {
           System.out.println("SALJE SE ZAHTEV!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem2/KreiranjeRacuna/1/"+ idk+"/"+idm+"/"+dozvoljenminus;
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("POST");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           System.out.println(objm.getIntProperty("rezultat"));
           int rezultat= objm.getIntProperty("rezultat");
           if(rezultat==0){
                   System.out.println("Neuspesno otvaranje racuna.");
           }else{
                   System.out.println("Uspesno otvaranje racuna.");
           }
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
   }    //radi
   public static void ZatvoriRacun(int idr, JMSConsumer consumer){
       try {
           System.out.println("SALJE SE ZAHTEV!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem2/BrisanjeRacuna/" + idr;
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("POST");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           System.out.println(objm.getIntProperty("rezultat"));
           int rezultat= objm.getIntProperty("rezultat");
           if(rezultat==0){
                   System.out.println("Neuspesno brisanje racuna.");
           }else{
                   System.out.println("Uspesno brisanje racuna.");
           }
      
           
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
   } //radi
   public static void Prenos(int idr1, int idr2, double iznos, JMSConsumer consumer){
       try {
           System.out.println("SALJE SE ZAHTEV!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem2/Prenos/" + idr1+"/"+idr2+"/"+iznos;
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("POST");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           //Message msg1=consumer.receive();
            Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           System.out.println(objm.getIntProperty("rezultat"));
           int rezultat= objm.getIntProperty("rezultat");
           if(rezultat==0){
                   System.out.println("Neuspesno prenos.");
           }else{
                   System.out.println("Uspesno prenos.");
           }
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
   } //radi 
   public static void Uplati(int idr,  int idf,double iznos, JMSConsumer consumer){
        try {
           System.out.println("SALJE SE ZAHTEV!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem2/Uplata/" +idr+"/"+idf +"/"+iznos;
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("POST");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           //System.out.println(objm.getIntProperty("rezultat"));
           int rezultat= objm.getIntProperty("rezultat");
           if(rezultat==0){
                   System.out.println("Neuspesno uplacivanje.");
           }else{
                   System.out.println("Uspesno uplacivanje.");
           }
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    } //radi
   public static void Isplati(int idr,  int idf,double iznos, JMSConsumer consumer){
          try {
           System.out.println("SALJE SE ZAHTEV!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem2/Isplata/" + idr+"/"+idf+"/"+iznos;
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("POST");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           int x=1;
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           System.out.println(objm.getIntProperty("rezultat"));
           int rezultat= objm.getIntProperty("rezultat");
           if(rezultat==0){
                   System.out.println("Neuspesno isplacivanje.");
           }else{
                   System.out.println("Uspesno isplacivanje.");
           }
           
           
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    } //radi
   public static void DohvatiSveRacuneKomitenta(int idk, JMSConsumer consumer) throws JMSException{
        try {
           System.out.println("SALJE SE ZAHTEV!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem2/DohvatanjeSvihRacunaZaKomitenta/" + idk;
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           connection.setDoInput(true);
           int c=0;
           int odgovor = connection.getResponseCode();
           Message msg=consumer.receive();
           
           ObjectMessage objm=(ObjectMessage)msg;
           System.out.println(objm.getIntProperty("rezultat"));
           int rezultat= objm.getIntProperty("rezultat");
           if(rezultat==0){
                   System.out.println("Komitent ne postoji.");
           }else{
                   ArrayList<String> racun=(ArrayList<String>)objm.getObject();
                   for(String r: racun){
                    System.out.println(r);
                   }
           }

       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   public static void DohvatiSveTransakcije(int idr, JMSConsumer consumer){
        
       
       
       try {
           System.out.println("SALJE SE ZAHTEV!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem2/DohvatanjeSvihTansakcijaZaRacun/" + idr;
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           
           Message msg=consumer.receive();
           try {
               ObjectMessage objm=(ObjectMessage)msg;
               System.out.println(objm.getStringProperty("rezultat"));
               int rezultat= objm.getIntProperty("rezultat");
               if(rezultat==0){
                   System.out.println("Racun ne postoji.");
               }else{
                   ArrayList<String> transakcije=(ArrayList<String>)objm.getObject();
                   for(String t: transakcije){
                    System.out.println(t);
                   }
               }
               
               
           } catch (JMSException ex) {
               Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       }
 
       
       
       
       
       
       
       
    }   //radi
  
   
   public static void DohvatiSveIzKopije(JMSConsumer consumer){
       try {
           System.out.println("SALJE SE ZAHTEV!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem3/DohvatiSveIzKopije";
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           
           Message msg1=consumer.receive();
           ObjectMessage objm1=(ObjectMessage)msg1;
           
           Message msg2=consumer.receive();
           ObjectMessage objm2=(ObjectMessage)msg2;
           
           Message msg3=consumer.receive();
           ObjectMessage objm3=(ObjectMessage)msg3;
          
           Message msg4=consumer.receive();
           ObjectMessage objm4=(ObjectMessage)msg4;
           
           
           System.out.println("Sva mesta:");
           ArrayList<ArrayList<String>> mesta = (ArrayList<ArrayList<String>>) objm.getObject();
           ArrayList<String> mesta1=mesta.get(0);
           for(String m: mesta1){
                    System.out.println(m);
            }
           
           
           System.out.println("Sve filijale:");
           ArrayList<ArrayList<String>> filijale = (ArrayList<ArrayList<String>>) objm1.getObject();
           ArrayList<String> filijale1=filijale.get(0);
           for(String f: filijale1){
                    System.out.println(f);
            }
           
           System.out.println("Svi komitenti:");
           ArrayList<ArrayList<String>> komitenti = (ArrayList<ArrayList<String>>) objm2.getObject();
           ArrayList<String> komitenti1=komitenti.get(0);
           for(String k: komitenti1){
                    System.out.println(k);
            }

           
           System.out.println("Svi racuni:");
           //System.out.println(objm.getIntProperty("rezultat"));
           int rezultat= objm3.getIntProperty("rezultat");
           if(rezultat==0){
                   System.out.println("Komitent ne postoji.");
           }else{
                   ArrayList<String> racun=(ArrayList<String>)objm3.getObject();
                   for(String r: racun){
                    System.out.println(r);
                   }
           }
            System.out.println("Sve transakcije:");
            rezultat= objm4.getIntProperty("rezultat");
               if(rezultat==0){
                   System.out.println("Racun ne postoji.");
               }else{
                   ArrayList<String> transakcije=(ArrayList<String>)objm4.getObject();
                   for(String t: transakcije){
                    System.out.println(t);
                   }
               }
               
           
           
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
   
   
   
   public static void DohvatiRazlikuUPodacima(JMSConsumer consumer){
           
       try {
           
           System.out.println("SALJE SE ZAHTEV3!!!");
           String URLAdresa2 = "http://localhost:8080/server/resources/podsistem3/DohvatiRazlikuUPodacima";
           URL url2 = new URL(URLAdresa2);
           HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
           connection2.setRequestMethod("GET");
           connection2.setDoInput(true);
           int odgovor2= connection2.getResponseCode();
           
           
           sleep(5000);
           
           System.out.println("SALJE SE ZAHTEV1!!!");
           String URLAdresa = "http://localhost:8080/server/resources/podsistem1/DohvatiRazlikuUPodacima";
           URL url = new URL(URLAdresa);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           connection.setDoInput(true);
           int odgovor = connection.getResponseCode();
           
           
           
           Message msg=consumer.receive();
           ObjectMessage objm=(ObjectMessage)msg;
           System.out.println("IDFilijala gde se nalaze razlike: ");
           ArrayList<Integer> x=(ArrayList<Integer>)objm.getObject();
           if(x==null){
               System.out.println("Nema razlika");
           }
           else{
                      for(Integer r: x){
                    System.out.print(r+" ");
                        }
            }
             
           System.out.println("\nIDKomitenata gde se nalaze razlike: ");
           Message msg1=consumer.receive();
           ObjectMessage objm1=(ObjectMessage)msg1;
            x=(ArrayList<Integer>)objm1.getObject();
            if(x==null){
               System.out.println("Nema razlika");
           }
           else{
                      for(Integer r: x){
                    System.out.print(r+" ");
                        }
           }
           System.out.println("\nIDMesta gde se nalaze razlike: ");
           Message msg2=consumer.receive();
           ObjectMessage objm2=(ObjectMessage)msg2;
            x=(ArrayList<Integer>)objm2.getObject();
            if(x==null){
               System.out.println("Nema razlika");
           }
           else{
                      for(Integer r: x){
                    System.out.print(r+" ");
                        }
           }
           
           
           System.out.println("\nSALJE SE ZAHTEV2!!!");
           String URLAdresa1 = "http://localhost:8080/server/resources/podsistem2/DohvatiRazlikuUPodacima";
           URL url1 = new URL(URLAdresa1);
           HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
           connection1.setRequestMethod("GET");
           connection1.setDoInput(true);
           int odgovor1 = connection1.getResponseCode();
           System.out.println("\nIDTransakcija gde se nalaze razlike: ");
           msg2=consumer.receive();
           objm2=(ObjectMessage)msg2;
           x=(ArrayList<Integer>)objm2.getObject();
                   for(Integer r: x){
                    System.out.print(r+ " ");
           }
           System.out.println("\nIDRacuna gde se nalaze razlike: ");
           msg2=consumer.receive();
           objm2=(ObjectMessage)msg2;
           x=(ArrayList<Integer>)objm2.getObject();
                   for(Integer r: x){
                    System.out.print(r+ " ");
           }
          
       } catch (MalformedURLException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
   
   public static void Apdejt(JMSConsumer consumer){
       
        try {
            System.out.println("SALJE SE ZAHTEV3!!!");
            String URLAdresa2 = "http://localhost:8080/server/resources/podsistem3/Apdejt";
            URL url2 = new URL(URLAdresa2);
            HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
            connection2.setRequestMethod("POST");
            connection2.setDoInput(true);
            int odgovor2= connection2.getResponseCode();
            
            
           sleep(5000);
           System.out.println("SALJE SE ZAHTEV1!!!");
           URLAdresa2 = "http://localhost:8080/server/resources/podsistem1/Apdejt";
           url2 = new URL(URLAdresa2);
           connection2 = (HttpURLConnection) url2.openConnection();
           connection2.setRequestMethod("GET");
           connection2.setDoInput(true);
           odgovor2= connection2.getResponseCode();
            
            
            System.out.println("SALJE SE ZAHTEV2!!!");
           URLAdresa2 = "http://localhost:8080/server/resources/podsistem2/Apdejt";
           url2 = new URL(URLAdresa2);
           connection2 = (HttpURLConnection) url2.openConnection();
           connection2.setRequestMethod("GET");
           connection2.setDoInput(true);
           odgovor2= connection2.getResponseCode();
            
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
  
   
   
public static class MojaNit implements Runnable {
    public void run(){
        try {
            sleep(1000*60*2);
            JMSContext context=connectionFactory.createContext();
            JMSConsumer consumer=context.createConsumer(myQueue, "podsistem= 5");
           // Apdejt(consumer);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
   
   public static void main(String[] args) {
       JMSContext context=connectionFactory.createContext();
       JMSProducer producer=context.createProducer();
       JMSConsumer consumer=context.createConsumer(myQueue, "podsistem= 5");
       Scanner myObj = new Scanner(System.in);  
       //Thread thread=new Thread(new MojaNit());
       //thread.start();
       while(true){
           try {
               
//               int c=1;
//               while(c>0){
//                   Message msg=consumer.receive();
//               }
               sleep(2000);
               System.out.println("\n\nIzaberi: \n"
                       + "1. Kreiranje mesta\n"
                       + "2. Kreiranje filijale u mestu\n"
                       + "3. Kreiranje komitenta\n"
                       + "4. Promena sedista zadatog komitenta\n"
                       + "5. Otvaranje racuna\n"
                       + "6. Zatvaranje racuna\n"
                       + "7. Kreiranje transakcije koja je prenos sume sa jednog racuna na drugi racun\n"
                       + "8. Kreiranje transakcije koja je uplata novca sa racuna\n"
                       + "9. Kreiranje transakcije koja je isplata novca sa racuna\n"
                       + "10. Dohvatanje svih mesta\n"
                       + "11. Dohvatanje svih filijala\n"
                       + "12. Dohvatanje svih komienata\n"
                       + "13. Dohvatanje svih racuna za komitenta\n"
                       + "14. Dohvatanje svih transakcija za racun\n"
                       + "15. Dohvatanje svih podataka iz rezervne kopije\n"
                       + "16. Dohvatanje razlike u podacima u originalnim podacima i u rezervnoj kopiji\n"+ "\nIzaberi:\n");
               int broj = myObj.nextInt();
               String ime, naziv, adresa, mesto;
               int idR,mesto1;
               String mesto2;
               double iznos;
               switch (broj){
                   case 1:
                       System.out.println("Unesi postanski broj mesta: \n");
                       int pbroj = myObj.nextInt();
                       myObj.nextLine();
                       System.out.println("Unesi ime mesta: \n");
                       ime=myObj.nextLine();
                       myObj.nextLine();
                       KreirajMesto(pbroj, ime, consumer);
                       
                       break;
                   case 2:
                       System.out.println("Unesi naziv filijale: \n");
                       naziv=myObj.nextLine();
                       naziv=myObj.nextLine();
                       System.out.println("Unesi adresu filijale: \n");
                       adresa=myObj.nextLine();
                       //myObj.nextLine();
                       System.out.println("Unesi mesto gde se otvara filijala: \n");
                       mesto2=myObj.nextLine();
                       KreirajFilijaluUMestu(naziv, adresa, mesto2, consumer);
                       
                       break;
                   case 3:
                       System.out.println("Unesi ime komitenta: \n");
                       myObj.nextLine();
                       ime=myObj.nextLine();
                       System.out.println("Unesi adresu : \n");
                       adresa=myObj.nextLine();
                       myObj.nextLine();
                       System.out.println("Unesi mesto: \n");
                       String mesto11=myObj.nextLine();
                       myObj.nextLine();
                       KreirajKomitenta(ime, adresa, mesto11, consumer);
                       break;
                   case 4:
                       System.out.println("Unesi komitenta: \n");
                       broj=myObj.nextInt();
                       myObj.nextLine();
                       System.out.println("Unesi novo mesto: \n");
                       mesto=myObj.nextLine();
                       myObj.nextLine();
                       PromeniMestoKomitenta(broj, mesto, consumer);
                       break;
                   case 5:
                       System.out.println("Unesite dozvoljeni minus racuna: ");
                       int dozvoljenminus = myObj.nextInt();
                       myObj.nextLine();
                       System.out.println("Unesite mesto otvaranja racuna: ");
                       int idMesta =  myObj.nextInt();
                       myObj.nextLine();
                       System.out.println("Unesite  komitenta koji otvara racun: ");
                       int idKom =  myObj.nextInt();
                       myObj.nextLine();
                       OtvaranjeRacuna(dozvoljenminus, idMesta, idKom, consumer);
                       break;
                   case 6:
                       System.out.println("Unesite racun: ");
                       idR = myObj.nextInt();
                       myObj.nextLine();
                       ZatvoriRacun(idR, consumer);
                       break;
                   case 7:
                       System.out.println("Unesite prvi racun: ");
                       int idR1 = myObj.nextInt();
                       myObj.nextLine();
                       System.out.println("Unesite drugi racun: ");
                       int idR2 = myObj.nextInt();
                       myObj.nextLine();
                       System.out.println("Unesite iznos racun: ");
                       iznos = myObj.nextInt();
                       myObj.nextLine();
                       Prenos(idR1, idR2, iznos, consumer);
                       break;
                   case 8:
                       System.out.println("Unesite racun: ");
                       idR = myObj.nextInt();
                       myObj.nextLine();
                       System.out.println("Unesite iznos racun: ");
                       iznos = myObj.nextInt();
                       myObj.nextLine();
                       System.out.println("Unesite filijalu: ");
                       int idf = myObj.nextInt();
                       myObj.nextLine();
                       Uplati(idR,idf,iznos, consumer);
                       break;
                   case 9:
                       System.out.println("Unesite racun: ");
                       idR = myObj.nextInt();
                       myObj.nextLine();
                       System.out.println("Unesite iznos racun: ");
                       iznos = myObj.nextInt();
                       myObj.nextLine();
                       System.out.println("Unesite filijalu: ");
                       int idfi = myObj.nextInt();
                       myObj.nextLine();
                       Isplati(idR,idfi,iznos,consumer);
                       break;
                   case 10:
                       DohvatiSvaMesta(consumer);
                       break;
                   case 11:
                       DohvatiSveFilijale(consumer);
                       break;
                   case 12:
                       DohvatiSveKomitente(consumer);
                       break;
                   case 13:
                       System.out.println("Unesite komitenta: ");
                       idR = myObj.nextInt();
                       myObj.nextLine();
                       DohvatiSveRacuneKomitenta(idR, consumer);
                       break;
                   case 14:
                       System.out.println("Unesite racun: ");
                       idR = myObj.nextInt();
                       myObj.nextLine();
                       DohvatiSveTransakcije(idR,consumer);
                       break;
                   case 15:
                       DohvatiSveIzKopije(consumer);
                       break;
                   case 16:
                       DohvatiRazlikuUPodacima(consumer);
                       break;
                   case 17:
                       Apdejt(consumer);
                       break;
                       
               }
           } catch (JMSException ex) {
               Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
           } catch (InterruptedException ex) {
               Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
      
       
          
    }
    
}

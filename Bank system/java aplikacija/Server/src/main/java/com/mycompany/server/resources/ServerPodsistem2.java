/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server.resources;

import java.*;
import java.util.ArrayList;
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
import javax.jms.TextMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 *
 * @author Neca
 */



@Path("podsistem2")
public class ServerPodsistem2 {
    
    @Resource(lookup="projekatConnectionFactory")
    public  ConnectionFactory connectionFactory;
    @Resource(lookup="projekatQueue")
    public  Queue myQueue;
    
    
    
    @GET
    @Path("DohvatiRazlikuUPodacima")
    public Response  DohvatiRazlikuUPodacima(){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        ArrayList<ArrayList<String>> ansArray=null;
        try {

            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "DohvatiRazlikuUPodacima");
            msg.setIntProperty("podsistem", 2);
            
            producer.send(myQueue, msg);
            
      
                
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().entity(ansArray).build();
        
    }
    
    
    @GET
    @Path("Apdejt")
    public Response  Apdejt(){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "Apdejt");
            msg.setIntProperty("podsistem", 2);
             
            producer.send(myQueue, msg);
           
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
        
    } 
    
    
    
    
    
    
    @POST
    @Path("Isplata/{idr}/{idf}/{iznos}")
    public Response  Isplata(@PathParam("idr") int idr,@PathParam("iznos") double iznos, @PathParam("idf") int idf ){
        
        
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "Isplata");
            msg.setIntProperty("podsistem", 2);

            msg.setDoubleProperty("iznos", iznos);
            msg.setIntProperty("idr", idr);
            msg.setIntProperty("idf", idf);
           
            
            
            producer.send(myQueue, msg);
      
                
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.ok().build();
    }
    
    
    @POST
    @Path("Uplata/{idr}/{idf}/{iznos}")
    public Response  Uplata(@PathParam("idr") int idr,@PathParam("iznos") double iznos, @PathParam("idf") int idf ){
        
        
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "Uplata");
            msg.setIntProperty("podsistem", 2);
            
            
            
            msg.setDoubleProperty("iznos", iznos);
            msg.setIntProperty("idr", idr);
            msg.setIntProperty("idf", idf);

            producer.send(myQueue, msg);
            System.out.println("SERVER-ISPLATA-POSLATA PORUKA");
            
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    

    
    @POST
    @Path("Prenos/{idr1}/{idr2}/{iznos}")
    public Response  Prenos(@PathParam("idr1") int idr1,@PathParam("iznos") double iznos, @PathParam("idr2") int idr2 ){
        
        
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "Prenos");
            msg.setIntProperty("podsistem", 2);

            msg.setDoubleProperty("iznos", iznos);
            msg.setIntProperty("idr1", idr1);
            msg.setIntProperty("idr2", idr2);
           
            
            
            producer.send(myQueue, msg);
          
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
  
    @POST
    @Path("BrisanjeRacuna/{idr}")
    public Response  BrisanjeRacuna(@PathParam("idr") int idr){
        
        
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "BrisanjeRacuna");
            msg.setIntProperty("podsistem", 2);
            msg.setIntProperty("idr", idr);
            producer.send(myQueue, msg);
            System.out.println("SERVER-BRISANJERACUNA-POSLATA PORUKA");
 
      
                
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    
    @POST
    @Path("KreiranjeRacuna/{status}/{idKom}/{idMes}/{dozvoljenminus}")
    public Response  KreiranjeRacuna(@PathParam("idKom") int idk, @PathParam("status") int status, @PathParam("idMes") int idm, @PathParam("dozvoljenminus") int dozvoljenminus){
        
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "KreiranjeRacuna");
            msg.setIntProperty("podsistem", 2);
            msg.setIntProperty("dozvoljenminus", dozvoljenminus);
            msg.setIntProperty("status", status);
            msg.setIntProperty("idk", idk);
            msg.setIntProperty("idm", idm);
            
            
            producer.send(myQueue, msg);

        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
  
        return null;
        
    }




    @GET
    @Path("DohvatanjeSvihRacunaZaKomitenta/{idKom}")
    public Response  DohvatanjeSvihRacunaZaKomitenta(@PathParam("idKom") int idk){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "DohvatanjeSvihRacunaZaKomitenta");
            msg.setIntProperty("podsistem", 2);
            msg.setIntProperty("idk", idk);

            producer.send(myQueue, msg);
                          
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      
        
        return Response.ok().build();
    }

    
    @GET
    @Path("DohvatanjeSvihTansakcijaZaRacun/{idRom}")
    public Response  DohvatanjeSvihTansakcijaZaRacun(@PathParam("idRom") int idr){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "DohvatanjeSvihTansakcijaZaRacun");
            msg.setIntProperty("podsistem", 2);
            msg.setIntProperty("nemanja", 2020);
       
            msg.setIntProperty("idr", idr);

            producer.send(myQueue, msg);
            System.out.println("SERVER-DohvatanjeSvihTansakcijaZaRacun-POSLATA PORUKA");
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    
}

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
        


@Path("podsistem1")
public class ServerPodsistem1 {

    @Resource(lookup="projekatConnectionFactory")
    public  ConnectionFactory connectionFactory;
    @Resource(lookup="projekatQueue")
    public  Queue myQueue;
    
    
    @GET
    public Response ping(){
        return Response
                .ok("ping12")
                .build();
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
            msg.setIntProperty("podsistem", 1);
             
            producer.send(myQueue, msg);
           
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
        
    } 
    
    
    
    @POST
    @Path("KreiranjeKomitenta/{idk}/{naziv}/{adresa}/{idm}")
    public Response  KreiranjeKomitenta(@PathParam("idm") String mesto, @PathParam("idk") int idk, @PathParam("naziv") String naziv, @PathParam("adresa") String adresa){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "KreiranjeKomitenta");
            msg.setIntProperty("podsistem", 1);
            msg.setStringProperty("naziv", naziv);
            msg.setStringProperty("idm", mesto);
            msg.setIntProperty("idk", idk);
            msg.setStringProperty("adresa", adresa);
             
            producer.send(myQueue, msg);
           
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
        
    }
    
    @POST
    @Path("KreiranjeMesta/{postanskibroj}/{naziv}")
    public Response  KreiranjeMesta(@PathParam("postanskibroj") int postanskibroj, @PathParam("naziv") String naziv){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "KreiranjeMesta");
            msg.setIntProperty("podsistem", 1);
            msg.setStringProperty("naziv", naziv);
            msg.setIntProperty("idm", 1);
             msg.setIntProperty("postanskibroj", postanskibroj);
            producer.send(myQueue, msg);
                
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
        
        
    }
  
     @POST
    @Path("KreiranjeFilijale/{naziv}/{adresa}/{idm}")
    public Response  KreiranjeFilijale(@PathParam("idm") String idm,  @PathParam("naziv") String naziv, @PathParam("adresa") String adresa){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "KreiranjeFilijaleUMestu");
            msg.setIntProperty("podsistem", 1);
            msg.setStringProperty("naziv", naziv);
            msg.setStringProperty("idm", idm);
            msg.setIntProperty("idf", 1);
            msg.setStringProperty("adresa", adresa);
             
            producer.send(myQueue, msg);
            
      
                
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
         return Response.ok().build();
        
        
    }

    @PUT
    @Path("PromenaSedistaZaZadatogKomitenta/{idk}/{nazivMesta}")
    public Response PromenaSedistaZaZadatogKomitenta(@PathParam("idk") int idKom, @PathParam("nazivMesta") String nazivMesta){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "PromenaSedistaZaZadatogKomitenta");
            msg.setIntProperty("podsistem", 1);
            msg.setStringProperty("nazivmesta", nazivMesta);
            msg.setIntProperty("idk", idKom);
            
             
            producer.send(myQueue, msg);
          
                
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return null;
    }
    
    @GET
    @Path("DohvatiSvaMesta")
    public Response  DohvatiSvaMesta(){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        ArrayList<ArrayList<String>> ansArray=null;
        try {

            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "DohvatanjeSvihMesta");
            msg.setIntProperty("podsistem", 1);
            
            producer.send(myQueue, msg);
            
      
                
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().entity(ansArray).build();
        
    }
    
    @GET
    @Path("DohvatiSveFilijale")
    public Response  DohvatiSveFilijale(){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        ArrayList<ArrayList<String>> ansArray=null;
        try {

            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "DohvatanjeSvihFilijala");
            msg.setIntProperty("podsistem", 1);
            
            producer.send(myQueue, msg);
            
      
                
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().entity(ansArray).build();
        
    }
    
    @GET
    @Path("DohvatiSveKomitente")
    public Response  DohvatiSveKomitente(){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        ArrayList<ArrayList<String>> ansArray=null;
        try {

            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "DohvatanjeSvihKomitenata");
            msg.setIntProperty("podsistem", 1);
            producer.send(myQueue, msg);
            
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().entity(ansArray).build();
        
    }


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
            msg.setIntProperty("podsistem", 1);
            
            producer.send(myQueue, msg);
            
      
                
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().entity(ansArray).build();
        
    }


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server.resources;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author Neca
 */
@Path("podsistem3")
public class ServerPodsistem3 {
    
    @Resource(lookup="projekatConnectionFactory")
    public  ConnectionFactory connectionFactory;
    @Resource(lookup="projekatQueue")
    public  Queue myQueue;
    
    
    @GET
    @Path("DohvatiSveIzKopije")
    public Response  DohvatiSveIzKopije(){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=3");
        ArrayList<ArrayList<String>> ansArray=null;
        try {

            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "DohvatiSveIzKopije");
            msg.setIntProperty("podsistem", 3);
            
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
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=3");
        ArrayList<ArrayList<String>> ansArray=null;
        try {

            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "DohvatiRazlikuUPodacima");
            msg.setIntProperty("podsistem", 3);
            
            producer.send(myQueue, msg);
    
                
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().entity(ansArray).build();
        
    }
    
    
    @POST
    @Path("Apdejt")
    public Response  Apdejt(){
        JMSContext context=connectionFactory.createContext();
        JMSProducer producer=context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue, "podsistem=4");
        try {
            
            TextMessage msg=context.createTextMessage();
            msg.setStringProperty("KojaFunkcija", "Apdejt");
            msg.setIntProperty("podsistem", 3);
             
            producer.send(myQueue, msg);
           
        } catch (JMSException ex) {
            Logger.getLogger(ServerPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
        
    } 
    
    
}

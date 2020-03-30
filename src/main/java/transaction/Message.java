/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.io.Serializable;

/**
 *
 * @author scott
 */
public class Message implements MessageTypes, Serializable {
    
    int type;
    
    /**
     * Contains the content that is specific to a certain message type
     */
    
    Object content;
    Object amount; 

    public Message(int type, Object content) {
        this.type = type;
        this.content = content;
    }
    
    public Message( int type, Object content, Object amount ) {
        this.type = type; 
        this.content = content; 
        this.amount = amount; 
    }
    
    /**
     * getter and setter methods for message type
     */ 
    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }
    
    public Object getAmount() {
        
        return amount; 
    }
}
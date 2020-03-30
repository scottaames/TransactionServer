/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.io.Serializable;

/**
 * A class that contains all of the information inside a message between the client and server 
 * @author scott
 */
public class Message implements MessageTypes, Serializable {
    
    // The type of message this object is 
    int type;
    
    // The actual content of the message 
    Object content;

    public Message(int type, Object content) {
        this.type = type;
        this.content = content;
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
}
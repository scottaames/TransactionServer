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
public class Message implements Serializable {
    
    public enum MessageTypes {
        OpenTransaction,
        CloseTransaction,
        Read,
        Write
    }
    
    MessageTypes type;
    
    /**
     * Contains the content that is specific to a certain message type
     */
    
    Object content;

    public Message(MessageTypes type, Object content) {
        this.type = type;
        this.content = content;
    }
    
    /**
     * getter and setter methods for message type
     */ 
    public void setType(MessageTypes type) {
        this.type = type;
    }

    public MessageTypes getType() {
        return type;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }
}
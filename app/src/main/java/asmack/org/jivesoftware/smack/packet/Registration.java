/**
 * $RCSfile$
 * $Revision$
 * $Date$
 *
 * Copyright 2003-2007 Jive Software.
 *
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package asmack.org.jivesoftware.smack.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents registration packets. An empty GET query will cause the server to return information
 * about it's registration support. SET queries can be used to create accounts or update
 * existing account information. XMPP servers may require a number of attributes to be set
 * when creating a new account. The standard account attributes are as follows:
 * <ul>
 *      <li>name -- the user's name.
 *      <li>first -- the user's first name.
 *      <li>last -- the user's last name.
 *      <li>email -- the user's email address.
 *      <li>city -- the user's city.
 *      <li>state -- the user's state.
 *      <li>zip -- the user's ZIP code.
 *      <li>phone -- the user's phone number.
 *      <li>url -- the user's website.
 *      <li>date -- the date the registration took place.
 *      <li>misc -- other miscellaneous information to associate with the account.
 *      <li>text -- textual information to associate with the account.
 *      <li>remove -- empty flag to remove account.
 * </ul>
 *
 * @author Matt Tucker
 */
public class Registration extends IQ {

    private String instructions = null;
    private Map<String, String> attributes = new HashMap<String,String>();
    private List<String> requiredFields = new ArrayList<String>();
    private boolean registered = false;
    private boolean remove = false;

    /**
     * Returns the registration instructions, or <tt>null</tt> if no instructions
     * have been set. If present, instructions should be displayed to the end-user
     * that will complete the registration process.
     *
     * @return the registration instructions, or <tt>null</tt> if there are none.
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the registration instructions.
     *
     * @param instructions the registration instructions.
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * Returns the map of String key/value pairs of account attributes.
     *
     * @return the account attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Sets the account attributes. The map must only contain String key/value pairs.
     *
     * @param attributes the account attributes.
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    
    public List<String> getRequiredFields(){
    	return requiredFields;
    }
    
    public void addAttribute(String key, String value){
    	attributes.put(key, value);
    }
    
    public void setRegistered(boolean registered){
    	this.registered = registered;
    }
    
    public boolean isRegistered(){
    	return this.registered;
    }
    
    public String getField(String key){
    	return attributes.get(key);
    }
    
    public List<String> getFieldNames(){
    	return new ArrayList<String>(attributes.keySet());
    }
    
    public void setUsername(String username){
    	attributes.put("username", username);
    }
    
    public void setPassword(String password){
    	attributes.put("password", password);
    }
    
    public void setRemove(boolean remove){
    	this.remove = remove;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:register\">");
        if (instructions != null && !remove) {
            buf.append("<instructions>").append(instructions).append("</instructions>");
        }
        if (attributes != null && attributes.size() > 0 && !remove) {
            for (String name : attributes.keySet()) {
                String value = attributes.get(name);
                buf.append("<").append(name).append(">");
                buf.append(value);
                buf.append("</").append(name).append(">");
            }
        }
        else if(remove){
        	buf.append("</remove>");
        }
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
    }
}
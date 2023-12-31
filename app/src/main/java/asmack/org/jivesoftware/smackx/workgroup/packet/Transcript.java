/**
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

package asmack.org.jivesoftware.smackx.workgroup.packet;

import asmack.org.jivesoftware.smack.packet.IQ;
import asmack.org.jivesoftware.smack.packet.Packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the conversation transcript that occured in a group chat room between an Agent
 * and a user that requested assistance. The transcript contains all the Messages that were sent
 * to the room as well as the sent presences. 
 *
 * @author Gaston Dombiak
 */
public class Transcript extends IQ {
    private String sessionID;
    private List<Packet> packets;

    /**
     * Creates a transcript request for the given sessionID.
     *
     * @param sessionID the id of the session to get the conversation transcript.
     */
    public Transcript(String sessionID) {
        this.sessionID = sessionID;
        this.packets = new ArrayList<Packet>();
    }

    /**
     * Creates a new transcript for the given sessionID and list of packets. The list of packets
     * may include Messages and/or Presences.
     *
     * @param sessionID the id of the session that generated this conversation transcript.
     * @param packets the list of messages and presences send to the room.
     */
    public Transcript(String sessionID, List<Packet> packets) {
        this.sessionID = sessionID;
        this.packets = packets;
    }

    /**
     * Returns id of the session that generated this conversation transcript. The sessionID is a
     * value generated by the server when a new request is received.
     *
     * @return id of the session that generated this conversation transcript.
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * Returns the list of Messages and Presences that were sent to the room.
     *
     * @return the list of Messages and Presences that were sent to the room.
     */
    public List<Packet> getPackets() {
        return Collections.unmodifiableList(packets);
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();

        buf.append("<transcript xmlns=\"http://jivesoftware.com/protocol/workgroup\" sessionID=\"")
                .append(sessionID)
                .append("\">");

        for (Iterator<Packet> it=packets.iterator(); it.hasNext();) {
            Packet packet = it.next();
            buf.append(packet.toXML());
        }

        buf.append("</transcript>");

        return buf.toString();
    }
}

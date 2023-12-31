/**
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

package asmack.org.jivesoftware.smackx.search;

import asmack.org.jivesoftware.smack.packet.IQ;
import asmack.org.jivesoftware.smackx.Form;
import asmack.org.jivesoftware.smackx.FormField;
import asmack.org.jivesoftware.smackx.ReportedData;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SimpleUserSearch is used to support the non-dataform type of JEP 55. This provides
 * the mechanism for allowing always type ReportedData to be returned by any search result,
 * regardless of the form of the data returned from the server.
 *
 * @author Derek DeMoro
 */
class SimpleUserSearch extends IQ {

    private Form form;
    private ReportedData data;

    public void setForm(Form form) {
        this.form = form;
    }

    public ReportedData getReportedData() {
        return data;
    }


    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:search\">");
        buf.append(getItemsToSearch());
        buf.append("</query>");
        return buf.toString();
    }

    private String getItemsToSearch() {
        StringBuilder buf = new StringBuilder();

        if (form == null) {
            form = Form.getFormFrom(this);
        }

        if (form == null) {
            return "";
        }

        Iterator<FormField> fields = form.getFields();
        while (fields.hasNext()) {
            FormField field = fields.next();
            String name = field.getVariable();
            String value = getSingleValue(field);
            if (value.trim().length() > 0) {
                buf.append("<").append(name).append(">").append(value).append("</").append(name).append(">");
            }
        }

        return buf.toString();
    }

    private static String getSingleValue(FormField formField) {
        Iterator<String> values = formField.getValues();
        while (values.hasNext()) {
            return values.next();
        }
        return "";
    }

    protected void parseItems(XmlPullParser parser) throws Exception {
        ReportedData data = new ReportedData();
        data.addColumn(new ReportedData.Column("JID", "jid", "text-single"));

        boolean done = false;

        List<ReportedData.Field> fields = new ArrayList<ReportedData.Field>();
        while (!done) {
            if (parser.getAttributeCount() > 0) {
                String jid = parser.getAttributeValue("", "jid");
                List<String> valueList = new ArrayList<String>();
                valueList.add(jid);
                ReportedData.Field field = new ReportedData.Field("jid", valueList);
                fields.add(field);
            }

            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                fields = new ArrayList<ReportedData.Field>();
            }
            else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("item")) {
                ReportedData.Row row = new ReportedData.Row(fields);
                data.addRow(row);
            }
            else if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                String value = parser.nextText();

                List<String> valueList = new ArrayList<String>();
                valueList.add(value);
                ReportedData.Field field = new ReportedData.Field(name, valueList);
                fields.add(field);

                boolean exists = false;
                Iterator<ReportedData.Column> cols = data.getColumns();
                while (cols.hasNext()) {
                    ReportedData.Column column = cols.next();
                    if (column.getVariable().equals(name)) {
                        exists = true;
                    }
                }

                // Column name should be the same
                if (!exists) {
                    ReportedData.Column column = new ReportedData.Column(name, name, "text-single");
                    data.addColumn(column);
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }

        }

        this.data = data;
    }


}

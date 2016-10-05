/**
 * jira-client - a simple JIRA REST client
 * Copyright (c) 2013 Bob Carroll (bob.carroll@alum.rit.edu)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.rcarz.jiraclient;

import net.rcarz.jiraclient.util.Logger;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Map;
import java.util.Observable;
import java.util.logging.Level;

/**
 * Represents an issue work log.
 */
public class TempoAuthor extends TempoResource {

    private String name = null;
    private String displayName = null;
    private String avatar = null;

    private final static Logger LOGGER = Logger.getLogger(TempoAuthor.class);
    /**
     * Creates an author from a JSON payload.
     *
     * @param restclient REST client instance
     * @param json JSON payload
     */
    protected TempoAuthor(RestClient restclient, JSONObject json) {
        super(restclient);

        if (json != null)
            deserialise(json);
    }

    private void deserialise(JSONObject json) {
        Map map = json;
        self = Field.getString(map.get("self"));
        name = Field.getString(map.get("name"));
        displayName = Field.getString(map.get("displayName"));
        avatar = Field.getString(map.get("avatar"));
    }

    @Override
    public String toString() {
        return self;
    }

    public String toStringFull() {
        return "author{name: " + name + ", " + ", displayName: " + displayName +
                ", avatar: " + avatar +
                ", self: " + self + "}";
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TempoAuthor){
            if(getSelf().equals(((TempoAuthor)obj).getSelf())){
                return true;
            }
        }
        return false;
    }
}


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
import java.util.logging.Level;

/**
 * Represents an issue work log.
 */
public class TempoWorkLog extends TempoResource {

    private int timeSpentSeconds = 0;
    private Date dateStarted = null; //"2016-07-04T09:35:03.000",

    private String comment = null;

    private TempoAuthor author = null;
    //private TempoIssue issue = null;
    private String summary = null;

    private final static Logger LOGGER = Logger.getLogger(TempoWorkLog.class);

    public static final String JIRA_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final DateTimeFormatter JIRA_DATE_TIME_FORMATTER = DateTimeFormat.forPattern(JIRA_DATE_TIME_PATTERN);

    /**
     * Creates a work log from a JSON payload.
     *
     * @param restclient REST client instance
     * @param json JSON payload
     */
    protected TempoWorkLog(RestClient restclient, JSONObject json) {
        super(restclient);

        if (json != null)
            deserialise(json);
    }

    private void deserialise(JSONObject json) {
        LOGGER.log(Level.INFO, "deserialise");
        Map map = json;
        timeSpentSeconds = Field.getInteger(map.get("timeSpentSeconds"));
        dateStarted = Field.getTempoDateTime(map.get("dateStarted"));
        comment = Field.getString(map.get("comment"));
        self = Field.getString(map.get("self"));
        id = Field.getInteger(map.get("id"));
        author = Field.getTempoResource(TempoAuthor.class, map.get("author"), restclient);
        //issue = Field.getResource(TempoIssue.class, map.get("issue"), restclient);
        summary = Field.getString(map.get("summary"));
        LOGGER.log(Level.INFO, "desiri done: " + this.toStringFull());
    }

    /**
     * Retrieves the given work log record.
     *
     * @param restclient REST client instance
     * @param issue Internal JIRA ID of the associated issue
     * @param id Internal JIRA ID of the work log
     *
     * @return a work log instance
     *
     * @throws JiraException when the retrieval fails
     */
    public static TempoWorkLog get(RestClient restclient, String issue, String id)
            throws JiraException {

        JSON result = null;

        try {
            result = restclient.get(getBaseUri() + "issue/" + issue + "/worklog/" + id);
        } catch (Exception ex) {
            throw new JiraException("Failed to retrieve work log " + id + " on issue " + issue, ex);
        }

        if (!(result instanceof JSONObject))
            throw new JiraException("JSON payload is malformed");

        return new TempoWorkLog(restclient, (JSONObject)result);
    }

    /**
     * Retrieves the given work log record.
     *
     * @param restclient REST client instance
     * @param issue the issue key for that you want to create a worklog entry
     * @param description the worklog description
     * @param timeSpentSeconds the time in seconds spent on the worklog
     *
     * @return the work log instance that has been created on the server
     *
     * @throws JiraException when the retrieval fails
     */
    public static TempoWorkLog create(RestClient restclient, String issue, String description, DateTime timeStarted, long timeSpentSeconds)
            throws JiraException {

        JSON result = null;
        JSONObject req = new JSONObject();
        req.put(Field.COMMENT, description);
        addTimeStarted(req, timeStarted);
        req.put(Field.TIME_SPENT_SECONDS, timeSpentSeconds);
        try {
            result = restclient.post(getBaseUri() + "issue/" + issue + "/worklog", req);
        } catch (Exception ex) {
            throw new JiraException("Failed to create work log on issue " + issue, ex);
        }

        if (!(result instanceof JSONObject))
            throw new JiraException("JSON payload is malformed");

        return new TempoWorkLog(restclient, (JSONObject)result);
    }

    private static void addTimeStarted(JSONObject req, DateTime startDate) {
        req.put(Field.STARTED, JIRA_DATE_TIME_FORMATTER.print(startDate));
    }

    @Override
    public String toString() {
        return self;
    }

    public String toStringFull() {
        return "Worklog{author: " + "author" + ", " + ", timeSpentSeconds: " + timeSpentSeconds +
                ", dateStarted: " + dateStarted + ", comment: " + comment + ", id: " + id +
                ", issue: " + "issue" + ", summary: " + summary +
                ", self: " + self + "}";
    }

    public TempoAuthor getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    //public String getIssue() {
    //    return issue;
    // }

    public String getSummary() {
        return summary;
    }

    public int getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TempoWorkLog) {
            TempoWorkLog tempoObj = (TempoWorkLog)obj;
            if(self != null && self.equals(tempoObj.getSelf())){
                return true;
            }
        }
        return false;
    }


    public boolean isDuplicate(TempoWorkLog obj) {
        if(timeSpentSeconds == obj.timeSpentSeconds &&
                saveEqual(dateStarted, obj.dateStarted) &&
                saveEqual(comment, obj.comment) &&
                saveEqual(summary, obj.summary) &&
                saveEqual(author, obj.author)){
            return true;
        }

        return false;
    }

    private static boolean saveEqual(Object obj1, Object obj2){
        return ((obj1 != null && obj1.equals(obj2)) || (obj1 == null && obj2 == null));
    }

}


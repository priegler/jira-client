package net.rcarz.jiraclient;

import net.rcarz.jiraclient.util.Logger;
import net.sf.json.JSON;
import net.sf.json.JSONArray;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by Peter on 05.10.2016.
 */
public class Tempo {

    private static final String DEFAULT_API_REV = "3";
    public static String apirev = DEFAULT_API_REV;

    private final static Logger LOGGER = Logger.getLogger(Tempo.class);

    public static List<TempoWorkLog> getWorklogs(RestClient restclient, String dateFrom, String dateTo, String username) throws JiraException  {
        //http://{JIRA_BASE_URL}/rest/tempo-timesheets/3/worklogs/
        JSONArray obj;
        List<TempoWorkLog> list;
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);

        try {
            URI uri = restclient.buildURI(getRestUri(null), params);
            JSON json = restclient.get(uri);
            obj = (JSONArray) json;
            list = Field.getTempoResourceArray(TempoWorkLog.class, obj, restclient);
            return list;

        } catch (Exception ex) {
            throw new JiraException("Failed to retrieve worklogs for user " + username, ex);
        }
    }

    public static boolean deleteWorklog(RestClient restclient, String id){
        // http://{JIRA_BASE_URL}/rest/tempo-timesheets/3/worklogs/
        try {
            URI uri = restclient.buildURI(getRestUri(id));
            restclient.delete(uri);
            return true;

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Failed to delete worklog with id " + id);
        }
        return false;
    }

    private static String getRestUri(String key) {
        return getBaseUri() + "worklogs/" + (key != null ? key : "");
    }

    /**
     * Resource base URI with API revision number.
     */
    public static String getBaseUri() {
        return String.format("/rest/tempo-timesheets/%s/", apirev);
    }
}

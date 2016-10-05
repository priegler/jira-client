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

/**
 * A base class for JIRA resources.
 */
public abstract class TempoResource {

    public static String apirev = Constants.DEFAULT_TEMPO_API_REV;

    protected RestClient restclient = null;
    protected int id = -1; // not for all tempo resources (e.g. author)
    protected String self = null;

    /**
     * Creates a new JIRA resource.
     *
     * @param restclient REST client instance
     */
    public TempoResource(RestClient restclient) {
        this.restclient = restclient;
    }

    /**
     * Gets the JIRA REST API revision number.
     */
    public static String getApiRev() {
        return apirev;
    }

    /**
     * Sets the JIRA REST API revision number.
     */
    public static void setApiRev(String apirev) {
        TempoResource.apirev = apirev;
    }

    /**
     * Resource base URI with API revision number.
     */
    public static String getBaseUri() {
        return String.format("/rest/tempo-timesheets/%s/", apirev);
    }

    /**
     * Resource base URI with API revision number.
     */
    public static String getAuthUri() {
        return String.format("/rest/auth/%s/", apirev);
    }

    /**
     * Internal Tempo ID.
     */
    public int getId() {
        return id;
    }

    /**
     * REST API resource URL.
     */
    public String getUrl() {
        return self;
    }

    /**
     * Resource URL.
     */
    public String getSelf() {
        return self;
    }
}


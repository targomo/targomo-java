package com.targomo.client.api.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerb on 27.02.18.
 */
public class CurlUtil {

    public static String buildCurlRequest(String url, List<String> headers, String body){

        String method = body == null || body.isEmpty() ? "GET" : "POST";

        return buildCurlRequest(method, url, null, headers, body);
//        List<String> updatedHeader = new ArrayList<>();
//
//        for ( int i = 0 ; headers != null && i < headers.size(); i++)
//            updatedHeader.add(i, String.format("-H '%s'", headers.get(i)));
//
//        // this is a GET request
//        if ( body == null || body.isEmpty() ) {
//            return String.format("curl '%s'%s", url, updatedHeader == null || updatedHeader.isEmpty() ? "" :
//                    " " + StringUtils.join(updatedHeader, " "));
//        }
//        else {
//            return String.format("curl -X POST '%s'%s -d '%s' --insecure --compressed",
//                    url,
//                    updatedHeader == null || updatedHeader.isEmpty() ? "" :
//                        " " + StringUtils.join(updatedHeader, " "), body);
//        }
    }

    /**
     * Build a curl request. Only checks for body if the method is 'POST'.
     * @param method The method of the request
     * @param url The base url of both the host and endpoint
     * @param queryString All query parameters as a string joined using '&'
     * @param headers A list of strings of request headers in the format 'Key: Value'
     * @param body The request body. If there is no body should be an empty string
     * @return A full curl request
     */
    public static String buildCurlRequest(String method, String url, String queryString, List<String> headers, String body) {
        StringBuilder sb = new StringBuilder().append("curl --location -X ").append(method).append(" '")
                .append(url);
        if(!StringUtils.isEmpty(queryString)) {
            sb.append("?").append(queryString);
        }
        sb.append("' \\\n");
        for(String header : headers) {
            sb.append("-H '").append(header).append("' \\\n");
        }
        if(!StringUtils.isEmpty(body)) {
            sb.append("--data-raw '").append(body).append("' \\\n");
        }
        return sb.append("--insecure --compressed").toString();
    }
}

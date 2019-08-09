package com.targomo.client.api.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerb on 27.02.18.
 */
public class CurlUtil {

    public static String buildCurlRequest(String url, List<String> headers, String body){

        List<String> updatedHeader = new ArrayList<>(headers.size());

        for ( int i = 0 ; headers != null && i < headers.size(); i++)
            updatedHeader.add(i, String.format("-H '%s'", headers.get(i)));

        // this is a GET request
        if ( body == null || body.isEmpty() ) {
            return String.format("curl '%s'%s", url, headers == null || headers.isEmpty() ? "" :
                    " " + StringUtils.join(headers, " "));
        }
        else {
            return String.format("curl -X POST '%s'%s -d '%s' --insecure --compressed",
                    url,
                    headers == null || headers.isEmpty() ? "" :
                        " " + StringUtils.join(headers, " "), body);
        }
    }
}

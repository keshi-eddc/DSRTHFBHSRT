package com.eddc.johnsoncrawler.utils.http;


import com.eddc.johnsoncrawler.utils.http.request.Request;
import com.eddc.johnsoncrawler.utils.http.request.RequestMethod;
import com.eddc.johnsoncrawler.utils.http.response.Response;
import org.junit.jupiter.api.Test;


/**
 * mailto:xiaobenma020@gmail.com
 */
public class HttpClientUtilTest {

    @Test
    public void doResponse() throws Exception {
        Request request = new Request("https://api.github.com/orgs/alibaba", RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        System.out.println(response.getResponseText()); //response text
        System.out.println(response.getCode()); //response code
        System.out.println(response.getHeader("Set-Cookie"));
    }

}
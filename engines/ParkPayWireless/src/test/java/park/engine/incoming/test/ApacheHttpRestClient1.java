package park.engine.incoming.test;

import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
 
public class ApacheHttpRestClient1 {
 
  public static void main(String[] args) {
    @SuppressWarnings("resource")
	DefaultHttpClient httpclient = new DefaultHttpClient();
    try {
      // specify the host, protocol, and port
      HttpHost target = new HttpHost("maps.googleapis.com", 80, "http");
       
      // specify the get request
      HttpGet getRequest = new HttpGet("/maps/api/geocode/json?latlng=40.714224,-73.961452");
 
      System.out.println("executing request to " + target);
 
      HttpResponse httpResponse = httpclient.execute(target, getRequest);
      HttpEntity entity = httpResponse.getEntity();
 
      System.out.println("----------------------------------------");
      System.out.println(httpResponse.getStatusLine());
      Header[] headers = httpResponse.getAllHeaders();
      for (int i = 0; i < headers.length; i++) {
        System.out.println(headers[i]);
      }
      System.out.println("----------------------------------------");
 
      if (entity != null) {
        System.out.println(EntityUtils.toString(entity));
      }
 
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // When HttpClient instance is no longer needed,
      // shut down the connection manager to ensure
      // immediate deallocation of all system resources
      httpclient.getConnectionManager().shutdown();
    }
  }
}

package harjoittelu_bot;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Saatiedot {

	private String osoite;
	
	public Saatiedot() {
		// Helsingin t‰m‰n hetkinen s‰‰
		this.osoite = "http://api.openweathermap.org/data/2.5/weather?q=helsinki&appid=6420b4234b9dea5693a48a2ab4270cf4";
	}
	
	public String getSaa() throws Exception {
		
		String saa = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
	    try
	    {
	        //Define a HttpGet request; You can choose between HttpPost, HttpDelete or HttpPut also.
	        //Choice depends on type of method you will be invoking.
	        HttpGet getRequest = new HttpGet(this.osoite);
	         
	        //Set the API media type in http accept header
	        getRequest.addHeader("accept", "application/xml");
	          
	        //Send the request; It will immediately return the response in HttpResponse object
	        HttpResponse response = httpClient.execute(getRequest);
	         
	        //verify the valid error code first
	        int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != 200) 
	        {
	            throw new RuntimeException("Failed with HTTP error code : " + statusCode);
	        }
	         
	        //Now pull back the response object
	        HttpEntity httpEntity = response.getEntity();
	        String apiOutput = EntityUtils.toString(httpEntity);
	         
	        //Lets see what we got from API
	        //System.out.println(apiOutput); //<user id="10"><firstName>demo</firstName><lastName>user</lastName></user>
	         
	        saa = apiOutput;
	        
	        //Verify the populated object
	        //System.out.println(user.getId());
	        //System.out.println(user.getFirstName());
	        //System.out.println(user.getLastName());
	    }
	    finally
	    {
	        //Important: Close the connect
	        httpClient.getConnectionManager().shutdown();
	    }
	    return saa;
	}
	
	
	
	
	
}

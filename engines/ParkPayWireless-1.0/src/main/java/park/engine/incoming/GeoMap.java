package park.engine.incoming;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;
 import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.version.Version;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeoMap {

	private String geoCodingApiUrl;
	private Map <String, String> geoOptionalParam;
	
	public GeoMap() {
		this.geoCodingApiUrl = 
				"https://maps.googleapis.com/maps/api/geocode/json";
		this.geoOptionalParam = new HashMap<String, String>();
		this.geoOptionalParam.put("location_type", "ROOFTOP");
		this.geoOptionalParam.put("result_type", "street_address");
		this.geoOptionalParam.put("key", "AIzaSyBwzhq8zxed3DrJ1tY3p2Ugy_fAusenNFI");
	}
	
	public GeoMap(String url) {
		this.geoCodingApiUrl = url;
	}
	
	public String findCity(double lat, double lng) throws Exception {
		
		String geo_params = "" 	+ "?latlng=" + Double.toString(lat) + ","
							+ Double.toString(lng) + "&"
							+ this.formUrl();
		
		WebClient geo_c =  null;
		String city = "";
		
		try {
			geo_c = WebClient.create(this.geoCodingApiUrl + geo_params );
			Response geo_r = geo_c.accept(MediaType.APPLICATION_JSON).get();
			
			assertEquals(Response.Status.OK.getStatusCode(), geo_r.getStatus());
			String jsonString = IOUtils.toString((InputStream) geo_r.getEntity());
			JSONObject json = new JSONObject((new JSONTokener(jsonString)));
			JSONArray jsonGeo = json.getJSONArray("results");
			city = jsonGeo.getJSONObject(0).getJSONArray("address_components").
										getJSONObject(3).getString("long_name");
		} finally {
			System.out.println("Getting called finalled");
			geo_c.close();
		}
		
		return city;
	}
	
	private String formUrl() {
		String urlParm = "";
		Iterator it = this.geoOptionalParam.entrySet().iterator();
		
		while( it.hasNext() ){
			Map.Entry pair = (Map.Entry)it.next();
			urlParm += pair.getKey() + "=" + pair.getValue();
			urlParm += "&";
		}
		return urlParm;
	}
	
	
	public static void main(String[] args) throws Exception {
		String cityName;
		GeoMap m = new GeoMap();
		// cityName = m.findCity(52.229807, 0.092280);
		// cityName = m.findCity(52.246048, 0.409758);
		cityName = m.findCity(52.264670, 0.374886);
		System.out.println( cityName );
		
		
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost:3306/practice";
        String user = "root";
        String password = "mysore159";

        try {
        	Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                System.out.println(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Version.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Version.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
	}

}

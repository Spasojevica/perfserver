package andrea.project;

import java.util.List;
import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import javax.print.attribute.HashDocAttributeSet;

/***
 * Util class for handling connections and requests
 * 
 * @author Administrator
 *
 */
public class Util {
	/***
	 * Creates connection for given URL
	 * 
	 * 
	 * @param url_ URL String
	 * @return HttpURLConnection, conncection object
	 * @throws IOException
	 */
	
	
	
	public static HttpURLConnection sendRequest(String url_, String method, Map<String, String> params)
			throws IOException {
		if (params !=null) {
			url_ =url_ + getParamsString(params);
		}
		System.out.println(url_);
		URL url = new URL(url_);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		return conn;

	}

	private static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		result.append('?');
		for (Map.Entry<String, String> entry : params.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}

	public static String getResponse(HttpURLConnection conn) throws IOException {
		
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		if (conn.getResponseCode() == 200) {
		    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String strCurrentLine;
		        while ((strCurrentLine = br.readLine()) != null) {
		              sb.append(strCurrentLine + "\n");
		        }
		} else {
		    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		    String strCurrentLine;
		        while ((strCurrentLine = br.readLine()) != null) {
		        	sb.append(strCurrentLine + "\n");
		        }
		}
		return sb.toString();
	}
	

	public static void closeConnection(HttpURLConnection conn) {
		conn.disconnect();
	}
	
	public static void main(String[] args) {
		String url  = "http://127.0.0.1:8001/getdata";
		Long timestamp = System.currentTimeMillis();
		System.out.println(timestamp);
		timestamp= (timestamp - timestamp % 300000  - 5*60*1000)/1000;
	
		Map<String,String> params = new HashMap<String, String>();
		params.put("time", timestamp.toString());
		
		
		try {
			HttpURLConnection conn = sendRequest(url, "GET", params);
			System.out.println(conn.getResponseCode());
			String response = getResponse(conn);
			System.out.println(response);
			
			
			
			List<Measurements> listMeasurements = new ArrayList<Measurements>();
			
			String[] lines = response.split("\n");
			String header = lines[0];
			for(int i = 1 ; i < lines.length;i++) {
				Measurements measurement = Measurements.fromCSV(header, lines[i]);
				listMeasurements.add(measurement);
			}
			
			IOUtil.toJSON(listMeasurements, "measurements.txt");
			System.out.println("Done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

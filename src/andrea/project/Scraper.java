package andrea.project;

import java.io.IOException;
import java.net.HttpURLConnection;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scraper extends Thread {
	
	public static String FILENAME = "measurements.txt";

	List<Measurements> listMeasurements;
	private String filename;
	
	
	public Scraper(String filename) {
		this.filename = filename;
		this.listMeasurements = IOUtil.fromJSON(filename);
		System.out.println("Initialization done");
		// TODO Auto-generated constructor stub
	}

	protected void job() throws IOException {

		String url = "http://127.0.0.1:8001/getdata";
		Long timestamp = System.currentTimeMillis();
		System.out.println(timestamp);
		

		
		timestamp = (timestamp - timestamp % 300000 - 5 * 60 * 1000) / 1000;
		
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("time", timestamp.toString());

		HttpURLConnection conn = Util.sendRequest(url, "GET", params);
		System.out.println(conn.getResponseCode());
		String response = Util.getResponse(conn);
		System.out.println(response);
		
		String[] lines = response.split("\n");
		String header = lines[0];
		for (int i = 1; i < lines.length; i++) {
			Measurements measurement = Measurements.fromCSV(header, lines[i]);
			listMeasurements.add(measurement);
		}
		
		
		IOUtil.toJSON(listMeasurements, filename);
		System.out.println("Done");

	}

	@Override
	public void run() {
		try {
			while (true) {

				job();

				Thread.sleep(5 * 60 * 1000);
			}
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Scraper scraper = new Scraper(FILENAME);
		scraper.run();
	}

}

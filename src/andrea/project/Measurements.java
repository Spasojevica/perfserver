package andrea.project;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import com.google.gson.Gson;

public class Measurements {
	private String startTime;
	private Integer duration;
	private String key;
	private Map<String, String> value;
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Map<String, String> getValue() {
		return value;
	}
	public void setValue(Map<String, String> value) {
		this.value = value;
	}
	
	public static Measurements fromCSV(String header, String vals_) {
		Measurements measurements = new Measurements();
		String [] keys= header.split("\\|");
		String[] vals = vals_.split("\\|");
		Map<String, String> extras = new HashMap<>();
		for(int i=0; i< keys.length; i++ ) {
			switch (keys[i]) {
			
			case "Timestamp":
				Date d = new Date(Long.parseLong(vals[i]) / 1000);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
				String dateStr = sdf.format(d);
				measurements.setStartTime(dateStr);
				break;
				
			case  "Duration":
				measurements.setDuration(Integer.parseInt(vals[i]));
				break;
			case "Key":
				measurements.setKey(vals[i]);
				break;
			default:
				extras.put(keys[i], vals[i]);
				break;
		}
			
			
			
		}
		measurements.setValue(extras);
		return measurements;		
	}
	

		

}

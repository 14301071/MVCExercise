package MVC;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModelAndView {	
	private String name;
	private Map<String, Object> obList= new ConcurrentHashMap<String, Object>();
	
	public ModelAndView() {
		
	}

	public void setViewName(String name) {
		this.name=name;
	}
	
	public String getViewName() {
		return this.name;
	}

	public Map<String, Object> getObjectList(){
		return obList;	
	}
	
	public Object getMap(String string) {
		return obList.get(string);
	}

	public void addObject(String string, Object value) {
		obList.put(string, value);
	}
}

package MVC;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String root = "E:\\program\\mvc\\src";
	ArrayList<Class<?>> controller = new ArrayList<Class<?>>();
       
    public DispatcherServlet() {
        super();
    }

    public void load(String filePath) {
		File readFile = new File(filePath);
		File[] files = readFile.listFiles();
		
		String fileName = null;
		for (File file : files) {
			fileName = file.getName();
			if (file.isFile()) {
									
				if (fileName.endsWith(".java")) {			
					try {							
						String  str=filePath+File.separator+ fileName;
						String beanClassName=str.substring(root.length()+1, str.length()-5).replace('\\', '.');
						Class<?> beanClass = Class.forName(beanClassName);
					
						if(beanClass.isAnnotationPresent(Controller.class)){
							controller.add(beanClass);
						}										
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
				}
			} else {
				load(filePath + File.separator + fileName);
			}
		}
    }
		
	public Object match(String servletPath,ModelAndView mav){
		
		for(Class<?> controllerClass: controller ) {
			for(Method method : controllerClass.getMethods()){      
	        	if(method.getAnnotation(RequestMapping.class).value().equals(servletPath)){
        		
	        		try {
	        			Object args[]=new Object[1];
	        			args[0]=mav;
						return method.invoke(controllerClass.newInstance(),args);
					} catch (Exception e) {
						e.printStackTrace();
					} 
	        	}  
	  
	        }  
		}
		return null;	
	}
	
	public ModelAndView getMav(HttpServletRequest request){		
		ModelAndView mav=new ModelAndView();
		
		Map<?, ?> map=request.getParameterMap();
		ArrayList<String> keyList = new ArrayList<String>();
		ArrayList<Object> valueList = new ArrayList<Object>();
				
		for (Object key : map.keySet()) { 
			keyList.add((String)key);	  
		}
		
		for (Object values : map.values()) {  
			String[]  value= (String[]) values;	
			valueList.add(value[0]);  
		}
		
		for(int i=0;i<keyList.size();i++){
			mav.addObject(keyList.get(i), valueList.get(i));
		}
		
		return mav;	
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		load(root);
	
		ModelAndView mdv=(ModelAndView) match(request.getServletPath(),getMav(request));
		
		for (Map.Entry<String, Object> entry : mdv.getObjectList().entrySet()) {  	
			request.setAttribute(entry.getKey(), entry.getValue());
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  	  
		}
			
		request.getRequestDispatcher(mdv.getViewName()+".jsp").forward(request, response);
	}
}

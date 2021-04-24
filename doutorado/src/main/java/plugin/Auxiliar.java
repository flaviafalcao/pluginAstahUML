package plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class Auxiliar {
	
		
		
    private File auxJar;
    private Class<?> IPresentation;
    private List<String> classes;
    private URLClassLoader urlCl;
		
   public boolean loadAux(String path) {

	       File file = new File(path);

	       if (file.exists()) {

	           auxJar = file;

	           return true;

	       } else

	           return false;

	   }

		 Auxiliar() {

	   }
	    
	    
		 public void loadClasses() throws MalformedURLException, ClassNotFoundException {

		        classes = new ArrayList<String>();
		        
		        System.out.println( "------>"  +   auxJar.toURI());

		        urlCl = new URLClassLoader(new URL[]{auxJar.toURI().toURL()}, System.class.getClassLoader());

		        IPresentation = urlCl.loadClass("com.change_vision.jude.api.inf.presentation.IPresentation");
		        
		        
		 }


		public void init() {
			
			
			this.loadAux("C:/Program Files/astah-UML/astah-api.jar");
			try {
				this.loadClasses();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	

}

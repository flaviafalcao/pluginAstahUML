package plugin;


public class no {	
	 		   
		   int origem;
		   int destino;
		   String evento;		
		   
		 
		   
		  @Override
		   public boolean equals(Object o) {
		   if (this.origem == ((no) o).origem 
		     && (this.evento).equals(((no) o).evento)
		     && this.destino == ((no) o).destino 
			  )
			   return true;
					   
			   else
				   
				return false;
		  }
	  } 
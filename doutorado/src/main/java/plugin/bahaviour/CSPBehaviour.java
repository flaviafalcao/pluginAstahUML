package plugin.bahaviour;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import plugin.Declarations;
import plugin.SetSinc;

public class CSPBehaviour {
	
	
	/// criar CSP para verificar padrao de comportamento
	
	
	public void BehaviourRestrictionResourceAllocation() {
		
		
		FileWriter arquivo;

		String str = "";

		try {

			boolean exists = (new File(
					"C:/Users/flavi/Documents/Doutorado_2022/PLUGIN/CSP/" + "BehaviourRestrictionResourceAllocation.csp")).exists();
			if (exists) {

				System.out.println("existe");
			}
			{
				arquivo = new FileWriter(
						new File("C:/Users/flavi/Documents/Doutorado_2022/PLUGIN/CSP/" + "BehaviourRestrictionResourceAllocation.csp"));

				str = "--------------------------------------------------------------------\n"
						+ "-----------------------------------------------\r\n"
						+ "-- Behavioural Specification Resource allocation\r\n"
						+ "-----------------------------------------------\r\n"
						+ "\r\n"
						+ "----------------------\r\n"
						+ "-- User Specification\r\n"
						+ "----------------------\r\n"
						+ "UserSpec(Ctr) =    \r\n"
						+ "    let \r\n"
						+ "	     Acquire(s) = \r\n"
						+ "            if s != <> then \r\n"
						+ "                acquire(Ctr,head(s)) ->  ack(acquire(Ctr,head(s))) ->\r\n"
						+ "                Acquire(tail(s)) else SKIP \r\n"
						+ "       Release(s) = \r\n"
						+ "            if s != <> then \r\n"
						+ "                release(Ctr,head(s)) -> ack(release(Ctr,head(s))) ->  Release(tail(s)) \r\n"
						+ "               \r\n"
						+ "            else SKIP\r\n"
						+ "        User(s) = \r\n"
						+ "            Acquire(s);\r\n"
						+ "			Release(s);\r\n"
						+ "			User(s)\r\n"
						+ "    within \r\n"
						+ "        User(resources(Ctr))\r\n"
						+ "        \r\n"
						+ "-------------------------\r\n"
						+ "-- Resource Specification\r\n"
						+ "-------------------------\r\n"
						+ "\r\n"
						+ "ResourceSpec(Ctr) = \r\n"
						+ "    let CtrUsers = users(Ctr)\r\n"
						+ "        Resource = \r\n"
						+ "            [] CtrU : CtrUsers @ \r\n"
						+ "                acquire(Ctr,CtrU) -> ack(acquire(Ctr,CtrU)) -> \r\n"
						+ "                    release(Ctr,CtrU) -> ack(release(Ctr,CtrU)) -> Resource\r\n"
						+ "    within \r\n"
						+ "        Resource\r\n"
						+ "        \r\n"
						+ "        ";
				
				

				arquivo.write(str);
				arquivo.flush();
				arquivo.close();

			}

		} catch (Exception e) {

		}


	}
	
	
	
	public void behaviourVerification(ArrayList<ResourceOrder> ordemR, ArrayList<ResourceAllocation> resourceAllocation, 
			         ArrayList<UserResource> userResource, ArrayList<SetSinc> setsinc) {
		
		FileWriter arquivo;
		String str = "";
		Declarations declaration = Declarations.getInstance();
		String str_r ="";
		String str_u ="";
		String str_u_r ="";
		String str_ac = "" ;
		String str_rl = "";
		String str_ack = "";
	   	
		
		try {
			for(int i =0; i<ordemR.size();i++) {
				
				//user
				//recuperar tipo e id
				String tipo;
				int id;
				tipo = declaration.getTypeInstance(ordemR.get(i).user);
				id = declaration.getId(ordemR.get(i).user);
						
				
			    //rs1
				String tipo_rs1;
				int id_rs1;
				
				tipo_rs1 = declaration.getTypeInstance(ordemR.get(i).resource1);
				id_rs1 = declaration.getId(ordemR.get(i).resource1);
				 int order_rs1 = ordemR.get(i).nr_resource1;
				
				
				//rs2
				String tipo_rs2;
				int id_rs2;
				
				tipo_rs2 = declaration.getTypeInstance(ordemR.get(i).resource2);
				id_rs2 = declaration.getId(ordemR.get(i).resource2);
				int order_rs2 = ordemR.get(i).nr_resource1;
				
				
			  
				str_r =  str_r + "\n resources(" + tipo + "_." + id +") = <";
				
				if(order_rs1==1) {
					
					str_r = str_r + 	tipo_rs1 +"_." + id_rs1  + "," +  tipo_rs2 +"_." + id_rs2 + "> \n";
					
					
				}
				else {
					
					str_r = str_r + 	tipo_rs2 +"_." + id_rs2  + "," +  tipo_rs1 +"_." + id_rs1 + "> \n";
					
					
				}	
           }
			
			   str_r = str_r + "resources(Other) = <> \n";
			
			 
			 for( int j =0; j < userResource.size() ;j ++) {
				 
					//resource
					//recuperar tipo e id
					String tipo;
					int id;
					tipo = declaration.getTypeInstance(userResource.get(j).getResource());
					id = declaration.getId(userResource.get(j).getResource());
					
					//para cada recurso vejo os usuarios
					
					ArrayList<String> user = userResource.get(j).getUser();
					
					str_u_r = "";
					
					for( int k = 0; k < user.size(); k++ ) {
					
						
						//user1
						String tipo_user1;
						int id_user1;
						
						tipo_user1 = declaration.getTypeInstance(user.get(k));
						id_user1 = declaration.getId(user.get(k));
						
						
						
						
					  str_u_r =  str_u_r + tipo_user1 + "_." +id_user1;
							  
							  if ( k < user.size()-1) {
							  
								  str_u_r = str_u_r + ",";
							  
							  }
						
						
					}
							
					
					str_u = str_u +  "users("  + tipo +"_." + id +") = { " +  str_u_r + "} \n";
					 	
				 
				 
			  }  
			   
			 str_u = str_u + "users(Other) = {} \n";
				
				
			 //aquire
			 
			 for(int m= 0; m < setsinc.size();m++) {
				 
				 String channelSplit[] = setsinc.get(m).getChannel1().split("\\.");
				 // saber qual o tipo componente da porta
				 String nomeComponente = declaration.getTypeOfPort(channelSplit[0].trim());
					// o id eh semOp[1]
				 int id = Integer.parseInt(channelSplit[1].trim());
					// qual o nome da instancia
				 String instance1 = declaration.getInstanceNameByTypeId(nomeComponente, id);
				 
				// channel2
				 String channelSplit2[] = setsinc.get(m).getChannel2().split("\\.");
					// saber qual o tipo componente da portai
				 String nomeComponente2 = declaration.getTypeOfPort(channelSplit2[0].trim());
					// o id eh semOp[1]
				 int id2 = Integer.parseInt(channelSplit2[1].trim());
					// qual o nome da instancia
				 String instance2 = declaration.getInstanceNameByTypeId(nomeComponente2, id2);
				 
				 
				 str_ac = str_ac + "acquire(" + nomeComponente + "_."  + id + "," + nomeComponente2 + "_." + id2 + ") = "
				 		+ "" + setsinc.get(m).getChannel1() +"." + "picksup_I";
				 
				 str_ac = str_ac + "\n" ;
				 
				 str_ac = str_ac + "acquire(" + nomeComponente2 + "_."  + id2 + "," + nomeComponente + "_." + id + ") = "
				 		+ "" + setsinc.get(m).getChannel2() +"." + "picksup_I";
				 
				 str_ac = str_ac + "\n" ;
				 
				 
				 
				 str_rl = str_rl + "release(" + nomeComponente + "_."  + id + "," + nomeComponente2 + "_." + id2 + ") = "
					 		+ "" + setsinc.get(m).getChannel1() + "." + "putsdown_I";
				 
				 str_rl = str_rl + "\n";
				 
				 str_rl = str_rl + "release(" + nomeComponente2 + "_."  + id2 + "," + nomeComponente + "_." + id + ") = "
					 		+ "" + setsinc.get(m).getChannel2() + "." + "putsdown_I";
				 
				 
				 str_rl = str_rl + "\n";
				 
				 str_ack = str_ack + "ack(" + setsinc.get(m).getChannel2() +"." + "picksup_I" + ")=" + setsinc.get(m).getChannel2() +"." + "picksup_O";
				 
				 str_ack = str_ack + "\n";
					
				 
				 str_ack = str_ack + "ack(" + setsinc.get(m).getChannel1() + "." + "putsdown_I"  +")=" + setsinc.get(m).getChannel1() + "." + "putsdown_O"; 
				 
				 str_ack = str_ack + "\n";			
				 
				 
				 str_ack = str_ack + "ack(" + setsinc.get(m).getChannel2() +"." + "putsdown_I" + ")=" + setsinc.get(m).getChannel2() + "." + "putsdown_O";
				 
				 str_ack = str_ack + "\n";
					
				 
				 str_ack = str_ack + "ack(" + setsinc.get(m).getChannel1() + "." + "picksup_I ) =" +  setsinc.get(m).getChannel1() + "." + "picksup_O";
				 
				 
				 str_ack = str_ack + "\n";
				
				 
			 }
			 
			 
			 	
				
				
				arquivo = new FileWriter(
						new File("C:/Users/flavi/Documents/Doutorado_2022/PLUGIN/CSP/" + "BehaviourRestrictionVerification.csp"));

				str = "--------------------------------------------------------------------\n"
						+ str_r +"\n "
								+ str_u + " \n"
										+ str_ac +" \n"
												+ str_rl+ "\n"
														+ str_ack +"\n" 
						+ "datatype Ctr =   Fork_.{1..3}|   Phil_.{1..3} \n"
						+ "P(Phil_.id) = PHIL(id)\r\n"
						+ "P(Fork_.id) = FORK(id) \n "
						+ "-- Abstraction function \n"
						+ "Abs(Ctr) = P(Ctr) \\ diff(Alpha(Ctr),{| ch | ch <- Chans(Ctr)|}) \n"
						+ "Alpha(Ctr) = {| ch | ch <- GET_CHANNELS(Ctr)|}\r\n"
						+ "\n"
						+ "GET_CHANNELS(FORK_.id) = {fork_right.id ,fork_left.id}\r\n"
						+ "GET_CHANNELS(PHIL_.id) = {phil_right.id ,phil_left.id}\r\n"
						+ "\r\n"
						+ "Chans(FORK_.id) = {fork_right.id ,fork_left.id}\r\n"
						+ "Chans(PHIL_.id) = {phil_right.id ,phil_left.id}\r\n"
						+ "\r\n"
						+ "assert UserSpec(Phil_.1) [F= Abs(Phil_.1)\r\n"
						+ "assert UserSpec(Phil_.2) [F= Abs(Phil_.2)\r\n"
						+ "assert UserSpec(Phil_.3) [F= Abs(Phil_.3)\r\n"						
						+ "assert ResourceSpec(Fork_.1) [F= Abs(Fork_.1)\r\n"
						+ "assert ResourceSpec(Fork_.2) [F= Abs(Fork_.2)"
						+ "\n"
						+ "assert ResourceSpec(Fork_.3) [F= Abs(Fork_.3)"
						+ "\n"
						+ " include \"BehaviourRestrictionResourceAllocation.csp\""
						+ "\n"
						+ " include \"modelo0.csp \" ";
				
				
										
				

				arquivo.write(str);
				arquivo.flush();
				arquivo.close();

			//}

		} catch (Exception e) {

		}


	}
	
	

}

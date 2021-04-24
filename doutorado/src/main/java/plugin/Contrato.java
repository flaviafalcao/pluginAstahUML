package plugin;

import java.util.ArrayList;

public class Contrato {	

	private String tipo;
	private String name;
	private String behaviour;
	private ArrayList<String> portas;
	private ArrayList<String> op;
	private ArrayList<relac_op_port> relacao;	
	
	
	Contrato(String tipo, String name, String behaviour, ArrayList<String> portas, ArrayList<String> op,ArrayList<relac_op_port> relacao){
		
		this.setName(name);
		this.setBehaviour(behaviour);
		this.setPortas(portas);
		this.setOp(op);
		this.setRelacao(relacao);
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getBehaviour() {
		return behaviour;
	}


	public void setBehaviour(String behaviour) {
		this.behaviour = behaviour;
	}


	public ArrayList<String> getPortas() {
		return portas;
	}


	public void setPortas(ArrayList<String> portas) {
		this.portas = portas;
	}


	public ArrayList<String> getOp() {
		return op;
	}


	public void setOp(ArrayList<String> op) {
		this.op = op;
	}


	public ArrayList<relac_op_port> getRelacao() {
		return relacao;
	}


	public void setRelacao(ArrayList<relac_op_port> relacao) {
		this.relacao = relacao;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}


class relac_op_port{
	
	String namePor;
	ArrayList<String> op;
	
}

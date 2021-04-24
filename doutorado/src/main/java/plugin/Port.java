package plugin;

public class Port {
	
	private String name;
	private String owner;
	private int  req_prov;	
	private String modifier;	
	private String interfaceName;
	private boolean multi = false;
	private int nr_multi = 0;
	private boolean guard  = false;
	
	//indicativo de guarda 
	
	
	
	
	public Port(String name, String owner, int req_prov, String modifier){
		this.name = name;
		this.owner = owner;
		this.req_prov = req_prov;
		this.setModifier(modifier);	
		
	}
	
	
	public Port(String name, String owner, int req_prov, String modifier, String interfaceName){
		this.name = name;
		this.owner = owner;
		this.req_prov = req_prov;
		this.setModifier(modifier);	
		this.setInterfaceName(interfaceName);
		
	}
		
	
	public Port(String name, String owner, int req_prov, String modifier, String interfaceName ,
			boolean multi , int  nr_multi ){
		this.name = name;
		this.owner = owner;
		this.req_prov = req_prov;
		this.setModifier(modifier);	
		this.setInterfaceName(interfaceName);
		this.multi = multi;
		this.nr_multi = nr_multi;
		
	}
	
	
		
	
	
	public Port(String name){
		this.name = name;
		this.owner = "";
		this.req_prov = -1;
	}
	
	public Port(String name, String owner){
		this.name = name;
		this.owner = owner;
		this.req_prov = -1;
	}
	
	
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	
	public static void main(String[] args) {
		
	
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getReq_prov() {
		return req_prov;
	}

	public void setReq_prov(int req_prov) {
		this.req_prov = req_prov;
	}


	public String getModifier() {
		return modifier;
	}


	public void setModifier(String modifier) {
		this.modifier = modifier;
	}


	public String getInterfaceName() {
		return interfaceName;
	}


	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}


	public boolean isMulti() {
		return multi;
	}


	public void setMulti(boolean multi) {
		this.multi = multi;
	}


	public boolean isGuard() {
		return guard;
	}


	public void setGuard(boolean guard) {
		this.guard = guard;
	}


	public int getNr_multi() {
		return nr_multi;
	}


	public void setNr_multi(int nr_multi) {
		this.nr_multi = nr_multi;
	}
	
}

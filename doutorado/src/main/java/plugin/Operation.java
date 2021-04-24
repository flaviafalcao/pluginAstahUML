package plugin;


public class Operation {
	private String name;
	private String name_in;
	private String name_out;
	private String class_name;
	private String class_esteriotipo;	
	private String returnType;	
	private String modifier;
	private String direction;
	private String type;
	private String esteriotipo;
	private String paramName;

	public Operation(String name, String class_name ,String class_esteriotipo ,String returnType ,String modifier, String direction ,String type,
			String esteriotipo) {
		
		this.name = name;
		this.setName_in(name + "_I");
		this.setName_out(name + "_O");
		//this.setName_in(name + "");
		//this.setName_out(name + "_ack");
		
		this.class_name = class_name;
		this.setClass_esteriotipo(esteriotipo);
		this.setReturnType(returnType);
		this.setModifier(modifier);
		this.setDirection(direction);
		this.setType(type);
		this.setEsteriotipo(esteriotipo);
		
	}
	
	
	public Operation(String name, String class_name ,String class_esteriotipo ,String returnType ,String modifier, String direction ,String type,
			String esteriotipo,
			String paramName) {
		
		this.name = name;
		this.setName_in(name + "_I");
		this.setName_out(name + "_O");
		//this.setName_in(name + "");
		//this.setName_out(name + "_ack");
		
		this.class_name = class_name;
		this.setClass_esteriotipo(esteriotipo);
		this.setReturnType(returnType);
		this.setModifier(modifier);
		this.setDirection(direction);
		this.setType(type);
		this.setEsteriotipo(esteriotipo);
		this.setParamName(paramName);
		
	}
	
	
	public Operation(String name, String class_name){
		
		this.name = name.replace("\n","");	
		this.setName_out(name + "_O");		
	    this.setName_in(name + "_I");	
		this.class_name = class_name;
		this.setClass_esteriotipo("tmp");
		
	}

	public String getName_in() {
		
		
		return this.name_in;
	}

	
	private void setName_in(String name_in) {
		this.name_in = name_in;
	}


	public String getName_out() {
		return name_out;
	}


	public void setName_out(String name_out) {
		this.name_out = name_out;
	}


	public String getClass_name() {
		return class_name;
	}


	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}


	public String getClass_esteriotipo() {
		return class_esteriotipo;
	}


	public void setClass_esteriotipo(String class_esteriotipo) {
		this.class_esteriotipo = class_esteriotipo;
	}


	public String getReturnType() {
		return returnType;
	}


	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDirection() {
		return direction;
	}


	public void setDirection(String direction) {
		this.direction = direction;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getEsteriotipo() {
		return esteriotipo;
	}


	public void setEsteriotipo(String esteriotipo) {
		this.esteriotipo = esteriotipo;
	}


	public String getParamName() {
		return "type_" + paramName;
	}


	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	
	
	
}

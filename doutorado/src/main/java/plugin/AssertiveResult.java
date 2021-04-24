package plugin;

public class AssertiveResult {
	
	private boolean passed;	
	private Object counterExample = null;
	private int id;
	
	
	public AssertiveResult( boolean passed, Object ob, int id) {
		
		this.passed = passed;		
		this.counterExample = ob;
		this.id = id;
	}


   public AssertiveResult( boolean passed, Object ob) {
		
		this.passed = passed;		
		this.counterExample = ob;
	}

   public AssertiveResult() {
		
	}
	
	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public Object getCounterExample() {
		return counterExample;
	}

	public void setCounterExample(Object counterExample) {
		this.counterExample = counterExample;
	}

}

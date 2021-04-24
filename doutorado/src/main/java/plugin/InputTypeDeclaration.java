package plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class InputTypeDeclaration {

	public String inputType() {

		//
		Declarations declaration = Declarations.getInstance();
		ArrayList<Input_Output> io = declaration.getInputoutput();
		HashSet<String> types = new HashSet<String>();
		String inputs_str = "";
		ArrayList<String> tmp = new ArrayList<String>();

		for (int i = 0; i < io.size(); i++) {

			// apenas adicionar as instancias que nao forem de teste

			String instanciaName = io.get(i).getInstance_name();
			types.add(instanciaName);
		}

		Iterator<String> it = types.iterator();

		while (it.hasNext()) {
			tmp.add((String) it.next());
		}

		ArrayList<String> inputs;

		for (int j = 0; j < tmp.size(); j++) {

			inputs = declaration.getInputsSemTeste(tmp.get(j));
			String inputs_str_in = " ";

			// se ha apenas uma instancia nao ha uniao de conjuntos
			// se nao eh vazio
			if (inputs.size() > 0) {
				if (inputs.size() == 1) {
					inputs_str = inputs_str + "inputs_" + tmp.get(j) + " = " + inputs.get(0) + "\n";
				}

				else {

					inputs_str = inputs_str + "inputs_" + tmp.get(j) + " =  union ( ";

					int contaPa = 1;
					String par = "";

					for (int k = 0; k < inputs.size(); k++) {

						inputs_str = inputs_str + inputs.get(k) + "  ";

						if (k < (inputs.size() - 1)) {
							inputs_str = inputs_str + ",";

							if (k < (inputs.size() - 2)) {

								inputs_str = inputs_str + "union (";
								contaPa++;
							}

						}

					}

					for (int c = 0; c < contaPa; c++) {
						par = par + ")";
					}

					inputs_str = inputs_str + par + "\n";

				}

			}

		}
		return inputs_str;
	}

	public String outputType() {
		//
		Declarations declaration = Declarations.getInstance();
		ArrayList<Input_Output> io = declaration.getInputoutput();
		HashSet<String> types = new HashSet<String>();
		String outputs_str = "";
		ArrayList<String> tmp = new ArrayList<String>();

		for (int i = 0; i < io.size(); i++) {

			types.add(io.get(i).getInstance_name());
		}

		Iterator<String> it = types.iterator();

		while (it.hasNext()) {

			tmp.add((String) it.next());

		}

		ArrayList<String> outputs;

		for (int j = 0; j < tmp.size(); j++) {

			outputs = declaration.getOutputSemTeste(tmp.get(j));
			String outputs_str_in = " ";

			if (outputs.size() > 0) {
				if (outputs.size() == 1) {

					outputs_str = outputs_str + "outputs_" + tmp.get(j) + " = " + outputs.get(0) + "\n";

				} else {

					outputs_str = outputs_str + "outputs_" + tmp.get(j) + " =  union ( ";

					int contaPa = 1;
					String par = "";

					for (int k = 0; k < outputs.size(); k++) {

						outputs_str = outputs_str + outputs.get(k) + "  ";

						if (k < (outputs.size() - 1)) {
							outputs_str = outputs_str + ",";

							if (k < (outputs.size() - 2)) {

								outputs_str = outputs_str + "union (";
								contaPa++;
							}

						}
					}

					for (int c = 0; c < contaPa; c++) {
						par = par + ")";
					}

					outputs_str = outputs_str + par + "\n";
				}
			}

		}
		return outputs_str;
	}

	public String inputAll() {

		Declarations declaration = Declarations.getInstance();
		ArrayList<Input_Output> io = declaration.getInputoutput();
		HashSet<String> types = new HashSet<String>();
		String inputs_str = "";
		ArrayList<String> tmp = new ArrayList<String>();

		for (int i = 0; i < io.size(); i++) {

			types.add(io.get(i).getInstance_name());
		}

		Iterator it = types.iterator();

		while (it.hasNext()) {

			tmp.add((String) it.next());

		}

		ArrayList<String> inputs;
		String inputs_str_in = " ";

		if (tmp.size() != 1) {

			if (tmp.size() == 2) { // tem que fazer ajuste aqui

			//	inputs_str = "inputs_" + tmp.get(0) + "= inputs_" + tmp.get(0) + "0"
			//	 + " \n"
						
			//	 + "inputs_all = inputs_" + tmp.get(0) + "\n";

			//	 inputs_str = "inputs_all = inputs_" + tmp.get(0) + "\n";

				inputs_str = "inputs_all  = union ( "
						+ "inputs_" + tmp.get(0) + ","
								+ "inputs_" + tmp.get(1) + ")\n";

			}

			else {

				for (int j = 0; j < tmp.size(); j++) {

					inputs_str_in = inputs_str_in + "inputs_" + tmp.get(j) + " ";

					if (j < tmp.size() - 1)
						inputs_str_in = inputs_str_in + ",";

					inputs_str = "inputs_all  =  union ( " + inputs_str_in + " ) \n";
				}

			}

		}
		System.out.println(inputs_str);

		return inputs_str;

	}

	public String outputAll() {

		Declarations declaration = Declarations.getInstance();
		ArrayList<Input_Output> io = declaration.getInputoutput();
		HashSet<String> types = new HashSet<String>();
		String outputs_str = "";
		ArrayList<String> tmp = new ArrayList<String>();

		for (int i = 0; i < io.size(); i++) {

			types.add(io.get(i).getInstance_name());
		}

		Iterator<String> it = types.iterator();

		while (it.hasNext()) {

			tmp.add((String) it.next());

		}

		ArrayList<String> outputs;
		String outputs_str_in = " ";

		if (tmp.size() != 1) {
			if (tmp.size() == 2) {

					
				outputs_str = "outputs_all = union (outputs_" + tmp.get(0) +   ","
						+  "outputs_" + tmp.get(1) + ")\n";
				
				
			}

			else {

				for (int j = 0; j < tmp.size(); j++) {

					outputs_str_in = outputs_str_in + "outputs_" + tmp.get(j) + " ";

					if ((j < tmp.size() - 1))
						outputs_str_in = outputs_str_in + ",";

					outputs_str = "outputs_all  =  union ( " + outputs_str_in + " ) \n";
				}

			}

		}
		return outputs_str;

	}

}

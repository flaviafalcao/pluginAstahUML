package plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import com.change_vision.jude.api.inf.model.IConnector;
import com.change_vision.jude.api.inf.model.IPort;

public class Composition {

	private static ArrayList<Processo> interleaves = new ArrayList<Processo>();

	public int getTypeInterface(IPort port) {

		int retorno = -1;
		if (port.getRequiredInterfaces().length > 0) {
			retorno = 1;
		} else if (port.getProvidedInterfaces().length > 0) {
			retorno = 0;
		}
		return retorno;
	}

	public void firstConection(IConnector connector) {

		Declarations declaration = Declarations.getInstance();
		ArrayList<SetSinc> setsinc = declaration.getSetsinc();

		String connectorStr1 = "";
		String connectorStr2 = "";
		String aux1 = "";
		String aux2 = "";

		String[] label;
		String[] label1;

		if (connector.getName().contains("<->")) {

			label = connector.getName().split("<->");
			label1 = label[0].split("[" + Pattern.quote(":.[()]") + "]");
			String[] label2 = label[1].split("[" + Pattern.quote(":.[()]") + "]");

			if (label1.length > 1) {

				aux1 = "." + label1[1];
			}
			if (label2.length > 1) {

				aux2 = "." + label2[1];
			}

			if (connector.getPorts()[0].getName().equalsIgnoreCase(label1[0])) {

				connectorStr1 = connector.getPorts()[0].getName() + "."
						+ declaration.getId(connector.getPartsWithPort()[0].getName()) + aux1;
				connectorStr2 = connector.getPorts()[1].getName() + "."
						+ declaration.getId(connector.getPartsWithPort()[1].getName()) + aux2;

			}

			else if (connector.getPorts()[1].getName().equalsIgnoreCase(label1[0])) {

				connectorStr1 = connector.getPorts()[0].getName() + "."
						+ declaration.getId(connector.getPartsWithPort()[0].getName()) + aux2;
				connectorStr2 = connector.getPorts()[1].getName() + "."
						+ declaration.getId(connector.getPartsWithPort()[1].getName()) + aux1;

			}

		}

		else {

			connectorStr1 = connector.getPorts()[1].getName() + "."
					+ declaration.getId(connector.getPartsWithPort()[1].getName()) + aux1;
			connectorStr2 = connector.getPorts()[0].getName() + "."
					+ declaration.getId(connector.getPartsWithPort()[0].getName()) + aux2;

		}

		SetSinc set_sinc = new SetSinc(connector, connectorStr1, connectorStr2);

		setsinc.add(set_sinc);

	}

	// *******************************************************************************************

	public String getInputComponent() {

		String InputComponent = "";
		Declarations declaration = Declarations.getInstance();
		ArrayList<Input_Output> inputoutput = declaration.getInputoutput();
		// lista de instancias
		HashSet<Instance> instances = declaration.getInstances();
		Iterator it = instances.iterator();
		HashSet<String> ports_comp;
		Iterator<String> it_str;
		String aux = "";
		String aux2 = "";
		String aux_g = "";
		String aux_g2 = "";

		while (it.hasNext()) {

			String temp = "";
			Instance instance_temp = (Instance) it.next();
			System.out.print("inputs_" + instance_temp.getName() + "= {|");

			InputComponent = InputComponent + "inputs_" + instance_temp.getName() + "= {|";

			String input_name = "inputs_" + instance_temp.getName();
			String output_name = "outputs_" + instance_temp.getName();

			Input_Output temp_i_o = new Input_Output(instance_temp.getType(), input_name, output_name);

			ports_comp = declaration.getPortComponent(instance_temp.getType());
			it_str = ports_comp.iterator();

			String str_temp = "";
			String str_optype = "";

			ArrayList<String> channels = new ArrayList<String>();

			while (it_str.hasNext()) {

				temp = (String) it_str.next();

				Port tempPort = declaration.getPort(temp);

				// verifica se a porta eh
				if (tempPort.isMulti()) {

					aux = ".x";
					aux2 = ", x :index";

				}

				// verifica se a parta tem guarda

				if (tempPort.isGuard()) {

					aux_g = ".y";
					aux_g2 = ",y: t_id";
				}

				String in_out = temp + "." + declaration.getId(instance_temp.getName());

				channels.add(in_out);

				str_temp = str_temp + in_out + aux_g + aux + ".op";

				if (it_str.hasNext()) {
					str_temp = str_temp + ", ";
				}

				aux = "";
				aux_g = "";

			}
			// interface provide or required ?

			InputComponent = InputComponent + str_temp + "|" + "op:";

			if (declaration.getIsProvReq(temp) == 1) {

				str_optype = instance_temp.getType() + "_O";

			} else if (declaration.getIsProvReq(temp) == 0) {

				str_optype = instance_temp.getType() + "_I";
			}

			System.out.println(str_optype + "|}");

			InputComponent = InputComponent + str_optype + aux2 + aux_g2 + "|}  \n";

			// lista de portas

			temp_i_o.setChannels(channels);
			temp_i_o.setInstance(instance_temp.getName());
			inputoutput.add(temp_i_o);

		}

		return InputComponent;
	}

//observar dependencia de c.firstConection

	public String getOutputComponent() {

		String outputComponent = "";
		Declarations declaration = Declarations.getInstance();
		ArrayList<Port> ports = declaration.getPortas();
		// lista de instancias
		HashSet<Instance> instances = declaration.getInstances();
		Iterator it = instances.iterator();
		HashSet<String> ports_comp;
		Iterator<String> it_str;
		String aux = "";
		String aux2 = "";
		String aux_g = "";
		String aux_g2 = "";

		while (it.hasNext()) {
			String temp = "";
			Instance instance_temp = (Instance) it.next();
			System.out.print("outputs_" + instance_temp.getName() + "= {|");
			outputComponent = outputComponent + " outputs_" + instance_temp.getName() + "= {|";

			ports_comp = declaration.getPortComponent(instance_temp.getType()); // portas do componente

			it_str = ports_comp.iterator();
			String str_temp = "";
			String str_optype = "";
			while (it_str.hasNext()) {
				temp = (String) it_str.next();

				Port tempPort = declaration.getPort(temp);

				if (tempPort.isMulti()) {

					aux = ".x";
					aux2 = ", x :index";

				}

				// verifica se a parta tem guarda

				if (tempPort.isGuard()) {

					aux_g = ".y";
					aux_g2 = ",y: t_id";
				}

				str_temp = str_temp + temp + "." + declaration.getId(instance_temp.getName()) + aux_g + aux + ".op";
				if (it_str.hasNext()) {
					str_temp = str_temp + ", ";

				}

				aux = "";
				aux_g = "";

			}

			outputComponent = outputComponent + str_temp + "|" + "op:";

			if (declaration.getIsProvReq(temp) == 1) {

				// str_optype = declaration.getType(instance_temp.getName()) +"_I" ; //versao
				// anterior

				str_optype = instance_temp.getType() + "_I";

			} else if (declaration.getIsProvReq(temp) == 0) {
				// str_optype = declaration.getType(instance_temp.getName()) +"_O" ;

				str_optype = instance_temp.getType() + "_O";
			}

			outputComponent = outputComponent + str_optype + aux2 + aux_g2 + "|} \n";

			// lista de portas
		}

		return outputComponent;
	}

	/**********************************************/
//rotina que informa de qual componente  a porta

	public String getComponentOwner(String porta) {
		String retorno = "";
		Declarations declaration = Declarations.getInstance();
		ArrayList<Port> portas = declaration.getPortas();
		for (int i = 0; i < portas.size(); i++) {
			if (portas.get(i).getName().equalsIgnoreCase(porta)) {
				retorno = portas.get(i).getOwner();
				break;
			}

		}

		return retorno;
	}

	/********************************************************/
	/**** o interleave sempre deve ser feito dois a dois ***/

	public String interleaveProcess() {

		// faco o interleave dois a dois

		String contrato = "";

		Declarations declaration = Declarations.getInstance();
		ArrayList<PartType> instances = declaration.getPartes();
		HashSet<String> hash = new HashSet<String>();

		for (int i = 0; i < instances.size(); i++) {
			hash.add(instances.get(i).getName());
		}

		Iterator iterator = hash.iterator();

		ArrayList<String> temp = new ArrayList<String>();

		while (iterator.hasNext()) {

			temp.add((String) iterator.next());

		}

		String interleave = "";
		Processo ps;
		int index = 1;

		if (temp.size() == 1) {

			ps = new Processo("processcomp", temp.get(0), index);

			interleave = ps.montaString();
			interleaves.add(ps);
		}

		if (temp.size() >= 2) {

			ps = interleave(temp.get(0), temp.get(1), index);
			interleave = ps.montaString();

			interleaves.add(ps);

			// contrato

			Processo c1 = new Processo(temp.get(0), temp.get(0), 0);
			Processo c2 = new Processo(temp.get(1), temp.get(1), 0);

			contrato = this.contratoInterleave(c1.name, c2.name);

			// process = interleave;

			interleave = interleave + "\n" + "-- ctr_" + contrato + "\n";

			int j = 2;

			while (j < temp.size()) {

				index++;

				// criar um novo processo
				String tmp = ps.name;
				ps = interleave(tmp, temp.get(j), index);

				interleave = interleave + "\n" + "-- ctr_" + this.contratoInterleave(tmp, temp.get(j)) + "\n";

				// novo contrato

				interleaves.add(ps);
				interleave = interleave + ps.montaString();

				System.out.println(interleave);

				j++;

			}
		}

		return interleave;
	}

	public Processo interleave(String c1, String c2, int index) {

		Processo retorno;

		retorno = new Processo("inter_" + c1 + "_" + c2, c1 + " ||| " + c2 + "\n", index);

		return retorno;
	}

	public String doComposition() throws IOException {

		String composition = "";
		String assertion1 = "";
		String assertion2 = "";
		String assertion3 = "";
		String assertion4 = "";

		Declarations declaration = Declarations.getInstance();
		ArrayList<SetSinc> setsinc = declaration.getSetsinc();
		ArrayList<AssertionConection> assertConn = declaration.getAssertionConection();
		ArrayList<String> assertionsModel = declaration.assertionsModel(Principal.nomeModelo);

		String processcomp = "";
		String processoanteriorname = "";

		if (setsinc.isEmpty()) {

			processcomp = "";

			composition = processcomp + interleaveProcess();
			String filename = "assertion" + declaration.getNum() + "_interleave" + ".csp";
			assertionsModel.add(filename);
			// AssertionConection ac = new AssertionConection(filename,
			// setsinc.get(0).getConn());
			// assertConn.add(ac);
			FileWriter arquivo;
			String write = "";// "include \"modelo" + declaration.getNum() +".csp\" \n" ;
			write = write + "include \"arquivo_protocolo" + declaration.getNum() + ".csp\" \n";

			// assertion p interleave
			// verificar deadlock freedom

			String assertion_interleave = assertion_interleave();

			write = write + assertion_interleave;

			arquivo = new FileWriter(
					new File("C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + filename));
			arquivo.write(write);
			arquivo.flush();
			arquivo.close();

		} else {

			String interleave = interleaveProcess();

			// ultima composition interleave

			String nomeprocesso = lastProcessName();

			// System.out.println(nomeprocesso);

			processcomp = "processcomp";

			// faco o interleave de todos os componentes

			String contrato = "";
			String aux1;
			String aux2;

			for (int i = 0; i < setsinc.size(); i++) {

				aux1 = "";
				aux2 = "";

				if (i == 0) {

					// criar contrado
					contrato = this.contratoFeed(nomeprocesso, setsinc.get(0));

					// verificar se a porta tem um id de guarda com triguer

					String[] splitResult1 = setsinc.get(0).getChannel1().split("\\.");
					String portName1 = splitResult1[0];

					String[] splitResult2 = setsinc.get(0).getChannel2().split("\\.");
					String portName2 = splitResult2[0];

					Port portTemp1 = declaration.getPort(portName1);
					Port portTemp2 = declaration.getPort(portName2);

					if (portTemp1.isGuard()) {

						aux1 = ".0";
					}

					if (portTemp2.isGuard()) {

						aux2 = ".0";
					}

					composition = interleave + "\n" + " " + processcomp + " = " + "(" + nomeprocesso + ")" + "\n "
							+ "[|{|" + setsinc.get(0).getChannel1() + aux1 + "," + setsinc.get(0).getChannel2() + aux2
							+ "|}|]" + "\n" + "BFIO_INIT(" + setsinc.get(0).getChannel1() + aux1 + ","
							+ setsinc.get(0).getChannel2() + aux2 + ")" + "\n \n";

					composition = composition + "\n" + "--- ctr_" + contrato + "\n";

					processoanteriorname = processcomp;
					processcomp = processcomp + "_com";

					assertion1 = assertion1 + assertion1(processoanteriorname, setsinc.get(0).getChannel1(),
							setsinc.get(0).getChannel2());
					assertion2 = assertion2 + assertion2(processoanteriorname, setsinc.get(0).getChannel1(),
							setsinc.get(0).getChannel2());
					assertion3 = assertion3 + assertion3(processoanteriorname, setsinc.get(0).getChannel1(),
							setsinc.get(0).getChannel2());
					assertion4 = "\n " ;
					//+
					//"assert " + processoanteriorname + ":[deadlock free [FD]] \n";

					String filename = "assertion" + declaration.getNum() + "" + i + ".csp";
					assertionsModel.add(filename);

					AssertionConection ac = new AssertionConection(filename, setsinc.get(0).getConn());

					assertConn.add(ac);

					// a cada conection colocar num novo arquivo
					FileWriter arquivo;
					String write = // "include \"modelo" + declaration.getNum() +".csp\" \n"
							"include \"arquivo_protocolo" + declaration.getNum() + ".csp\" \n" + assertion1 + assertion2
									+ assertion3 + assertion4;

					arquivo = new FileWriter(
							new File("C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + filename));
					arquivo.write(write);
					arquivo.flush();
					arquivo.close();

					// criar assertion passando o nome da composicao e canais

				} else {

					// criar contrado
					contrato = this.contratoFeed(processoanteriorname, setsinc.get(i));

					// verificar se a porta tem um id de guarda com triguer

					String[] splitResult1 = setsinc.get(i).getChannel1().split("\\.");
					String portName1 = splitResult1[0];

					String[] splitResult2 = setsinc.get(i).getChannel2().split("\\.");
					String portName2 = splitResult2[0];

					Port portTemp1 = declaration.getPort(portName1);
					Port portTemp2 = declaration.getPort(portName2);

					if (portTemp1.isGuard()) {

						aux1 = ".0";
					}

					if (portTemp2.isGuard()) {

						aux2 = ".0";
					}

					composition = composition + processcomp + " = " + processoanteriorname + "\n" + "[|{|"
							+ setsinc.get(i).getChannel1() + aux1 + "," + setsinc.get(i).getChannel2() + aux2 + "|}|]"
							+ "\n" + "BFIO_INIT(" + setsinc.get(i).getChannel1() + aux1 + ","
							+ setsinc.get(i).getChannel2() + aux2 + ")" + "\n \n";

					composition = composition + "\n" + " ---ctr_" + contrato + "\n";

					processoanteriorname = processcomp;
					processcomp = processcomp + "_com";

					assertion1 = assertion1(processoanteriorname, setsinc.get(i).getChannel1(),
							setsinc.get(i).getChannel2());
					assertion2 = assertion2(processoanteriorname, setsinc.get(i).getChannel1(),
							setsinc.get(i).getChannel2());
					assertion3 = assertion3(processoanteriorname, setsinc.get(i).getChannel1(),
							setsinc.get(i).getChannel2());
					assertion4 = "\n" ; 

					// a cada conection colocar num novo arquivo
					FileWriter arquivo;
					String write = // "include \"modelo" + declaration.getNum() +".csp\" \n"
							"include \"arquivo_protocolo" + declaration.getNum() + ".csp\" \n" + assertion1 + assertion2
									+ assertion3 + assertion4;

					String filename = "assertion" + declaration.getNum() + "" + i + ".csp";
					assertionsModel.add(filename);

					AssertionConection ac = new AssertionConection(filename, setsinc.get(i).getConn());

					assertConn.add(ac);

					arquivo = new FileWriter(
							new File("C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + filename));
					arquivo.write(write);
					arquivo.flush();
					arquivo.close();

				}

			}
		}

		return composition;
	}

	// assertiva p interleave

	public String assertion_interleave() {

		String assertion = "";
		Declarations declaration = Declarations.getInstance();
		ArrayList<PartType> instances = declaration.getPartes();
		HashSet<String> hash = new HashSet<String>();

		for (int i = 0; i < instances.size(); i++) {
			hash.add(instances.get(i).getName());
		}
		Iterator iterator = hash.iterator();

		while (iterator.hasNext()) {
			String name = (String) iterator.next();

			// assert cell(1):[divergence free [FD] ]
			// assert cell(1) :[deadlock free]
			// assert cell(1) :[deterministic ]

			assertion = assertion + "assert " + name + " :[divergence free [FD] ]" + "\n" + "assert " + name
					+ " :[deadlock free]" + "\n";
			// +
			// "assert " + name + " :[deterministic ] " + "\n";

		}
		// process = process;

		return assertion;

	}

	// assertiva/
	/*
	 * Verifica se a uma renomeacao do protocolo deterministica.
	 */

	public String assertion1(String comp, String c1, String c2) {

		String assertion1 = "";

		// assertiva
		assertion1 = assertion1 + "\n" + "assert InBufferProt_" + comp + "_" + splitChannel(c1) + "_" + splitChannel(c2)
				+ "( " + c1 + " ) :[deterministic [F]] \n" + "assert InBufferProt_" + comp + "_" + splitChannel(c2)
				+ "_" + splitChannel(c1) + "( " + c2 + " ) :[deterministic [F]] \n  \n";

		// definicao da funcao InBufferProt_

		assertion1 = assertion1 + InBufferProt_def(comp, c1, c2);
		assertion1 = assertion1 + protocolImp(comp);
		assertion1 = assertion1 + protocolImpChannel(comp, c1, c2);
		assertion1 = assertion1 + protocolImpRename(comp, c1, c2);
		assertion1 = assertion1 + r_io_process(comp, c1, c2);
		assertion1 = assertion1 + inputs_r_io(comp, c1, c2);
		assertion1 = assertion1 + outputs_R_IO(comp, c1, c2);
		assertion1 = assertion1 + inputs_r_io_prot_imp(comp, c1, c2);
		assertion1 = assertion1 + outputs_prot_imp_rio(comp, c1, c2);

		assertion1 = assertion1 + inputs_prot_imp_r(comp, c1, c2);
		assertion1 = assertion1 + prot_imp_r_prot_imp(comp, c1, c2);
		assertion1 = assertion1 + prot_imp_r_io(comp, c1, c2);

		assertion1 = assertion1 + outputs_prot_imp_r(comp, c1, c2);

		assertion1 = assertion1 + dual_prot_imp_r_prot_imp(comp, c1, c2);
		assertion1 = assertion1 + dual_prot_imp(comp, c1, c2);

		assertion1 = assertion1 + inputs_prot_imp_rio(comp, c1, c2);

		assertion1 = assertion1 + prot_imp(comp, c1, c2);

		assertion1 = assertion1 + dual_prot_imp_rio(comp, c1, c2);

		return assertion1;

	}

	public String splitChannel(String name) {

		String[] split = name.split("\\.");
		return split[0] + "_" + split[1];

	}

	public String splitPortFromChannel(String name) {
		String[] split = name.split("\\.");
		return split[0];
	}

	public String lastProcessName() {

		String retorno = "";
		int i = interleaves.size();

		for (int k = 0; k < interleaves.size(); k++) {

			if (interleaves.get(k).index == i) {
				retorno = interleaves.get(k).name;

			}
		}

		return retorno;
	}

	// InBufferProt_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_FEED
	public String InBufferProt_def(String str, String c1, String c2) {

		String inbufferProt = "";

		inbufferProt = "InBufferProt_" + str + "_" + splitChannel(c1) + "_" + splitChannel(c2)
				+ "(c) = CIO(PROT_IMP_R_IO_" + str + "_" + splitChannel(c1) + "_" + splitChannel(c2)
				+ "[[ x  <- in, y <- out | x  <- inputs_PROT_IMP_R_IO_" + str + "_" + splitChannel(c1) + "_"
				+ splitChannel(c2) + ", y  <-" + "outputs_PROT_IMP_R_IO_" + str + "_" + splitChannel(c1) + "_"
				+ splitChannel(c2) + "]]) " + "\n " + "InBufferProt_" + str + "_" + splitChannel(c2) + "_"
				+ splitChannel(c1) + "(c) = CIO(PROT_IMP_R_IO_" + str + "_" + splitChannel(c2) + "_" + splitChannel(c1)
				+ "[[ x  <- in, y <- out | x  <- inputs_PROT_IMP_R_IO_" + str + "_" + splitChannel(c2) + "_"
				+ splitChannel(c1) + ", y  <-" + "outputs_PROT_IMP_R_IO_" + str + "_" + splitChannel(c2) + "_"
				+ splitChannel(c1) + "]]) \n ";

		// System.out.println(inbufferProt);
		return inbufferProt;
	}

	// protocol implementation

	// -- Protocol Implementation
	// inputs_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(c) =
	// inter(inputs_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2,{|c|})
	// outputs_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(c) =
	// inter(outputs_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2,{|c|})

	public String protocolImp(String str) { // str nome da composicao

		String protocolImp = "";
		protocolImp = "inputs_PROT_IMP_" + str + "(c) = inter( inputs_all ,{|c|}) \n" + "outputs_PROT_IMP_" + str
				+ "(c) = inter( outputs_all,{|c|}) \n \n";

		// System.out.println(protocolImp);
		return protocolImp;
	}

	public String protocolImpChannel(String str, String channel1, String channel2) {

		String protocolImpChannel = "";

		protocolImpChannel = "inputs_PROT_IMP_" + str + "_" + splitChannel(channel1) + " =  inputs_PROT_IMP_" + str
				+ "(" + channel1 + " )  \n" + "inputs_PROT_IMP_" + str + "_" + splitChannel(channel2)
				+ " =  inputs_PROT_IMP_" + str + "(" + channel2 + " )  \n \n";

		// System.out.println(protocolImpChannel);
		return protocolImpChannel;
	}

	// inputs_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_FEED_FEED_port_fork_ext_2_phil_right_1_fork_right_R_IO_port_phil_ext_2_phil_right_1_fork_right
	// =
	// inputs_R_IO_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_FEED_FEED_port_fork_ext_2_phil_right_1_fork_right(port_fork_right.1,
	// port_phil_right.2)
	// inputs_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_FEED_FEED_port_phil_ext_2_phil_right_1_fork_right_R_IO_port_fork_ext_2_phil_right_1_fork_right
	// =
	// inputs_R_IO_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_FEED_FEED_port_phil_ext_2_phil_right_1_fork_right(port_phil_right.2,
	// port_fork_right.1)
	// inputs_PROT_IMP_R_IO

	public String inputs_prot_imp_rio(String str, String channel1, String channel2) {

		String inputs_prot_imp_rio = "";

		inputs_prot_imp_rio = "inputs_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_"
				+ splitChannel(channel2) + "= inputs_R_IO_PROT_IMP_" + str + "_" + splitChannel(channel1) + "("
				+ channel1 + "," + channel2 + ") \n" + "inputs_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2)
				+ "_" + splitChannel(channel1) + "= inputs_R_IO_PROT_IMP_" + str + "_" + splitChannel(channel2) + "("
				+ channel2 + "," + channel1 + ") \n";

		// System.out.println(inputs_prot_imp_rio);

		return inputs_prot_imp_rio;
	}

	// outputs_PROT_IMP_R_IO
	/*
	 * outputs_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_port_fork_ext_1_phil_left_2_fork_right_R_IO_port_phil_ext_1_phil_left_2_fork_right
	 * = outputs_R_IO_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(port_fork_right.2,
	 * port_phil_left.1)
	 * 
	 * outputs_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_port_phil_ext_1_phil_left_2_fork_right_R_IO_port_fork_ext_1_phil_left_2_fork_right
	 * = outputs_R_IO_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(port_phil_left.1,
	 * port_fork_right.2)
	 *
	 */

	public String outputs_prot_imp_rio(String str, String channel1, String channel2) {

		String outputs_prot_imp_rio = "";

		outputs_prot_imp_rio = "outputs_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_"
				+ splitChannel(channel2) + "= outputs_R_IO_" + str + "(" + channel1 + "," + channel2 + ")" + " \n"
				+ "outputs_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2) + "_" + splitChannel(channel1)
				+ "= outputs_R_IO_" + str + "(" + channel2 + "," + channel1 + ") \n \n";

		// System.out.println( outputs_prot_imp_rio);

		return outputs_prot_imp_rio;

	}

	// outputs_R_IO_
	// outputs_R_IO_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(a,b) = { b.x | x <-
	// extensions(a), member(a.x, outputs_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2)
	// }

	public String outputs_R_IO(String str, String channel1, String channel2) {

		String outputs_R_IO = "";

		outputs_R_IO = "outputs_R_IO_" + str + "(a,b)  = { b.x | x <- extensions(a), member(a.x, outputs_all)} \n";

		// System.out.println(outputs_R_IO);

		return outputs_R_IO;

	}

	// -------------------------
	// protocol implementation R_IO
	// PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_FEED_port_fork_ext_2_phil_left_2_fork_left_R_IO_port_phil_ext_2_phil_left_2_fork_left
	// =
	// PROT_IMP_R_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_FEED_port_fork_ext_2_phil_left_2_fork_left
	// prot_imp_r_io

	public String prot_imp_r_io(String str, String channel1, String channel2) {

		String prot_imp_r_io = "";

		prot_imp_r_io = "PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2)
				+ "= PROT_IMP_R_PROT_IMP_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2) + "\n"
				+ "PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2) + "_" + splitChannel(channel1)
				+ "= PROT_IMP_R_PROT_IMP_" + str + "_" + splitChannel(channel2) + "_" + splitChannel(channel1)
				+ "\n \n";

		// System.out.println(prot_imp_r_io);

		return prot_imp_r_io;

	}

	// ---------------------------------
	// PROT_IMP_R_PROT_IMP_

//PROT_IMP_R_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_port_fork_ext_1_phil_left_2_fork_right= rename(PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_port_fork_ext_1_phil_left_2_fork_right,R_IO_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(port_fork_right.2,port_phil_left.1)) --modifidado por sarah

	public String prot_imp_r_prot_imp(String str, String channel1, String channel2) {

		String prot_imp_r_prot_imp = "";

		prot_imp_r_prot_imp = "PROT_IMP_R_PROT_IMP_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2)
				+ "= rename(PROT_IMP_" + str + "_" + splitChannel(channel1) + ", " + "R_IO_" + str + "(" + channel1
				+ "," + channel2 + ")) \n" + "PROT_IMP_R_PROT_IMP_" + str + "_" + splitChannel(channel2) + "_"
				+ splitChannel(channel1) + "= rename(PROT_IMP_" + str + "_" + splitChannel(channel2) + ", " + "R_IO_"
				+ str + "(" + channel2 + "," + channel1 + ")) \n \n";

		// System.out.println(prot_imp_r_prot_imp);

		return prot_imp_r_prot_imp;

	}

	// ----------------

	// protocolo rename

	// PROT_IMP_R_

	public String protocolImpRename(String str, String channel1, String channel2) {

		String protocolImpRename = "";
		protocolImpRename = "PROT_IMP_R_" + str + "_" + splitChannel(channel1) + "= rename(" + "PROT_IMP_" + str + "_"
				+ splitChannel(channel1) + " , " + "R_IO_" + str + "(" + channel1 + "," + channel2 + ")) \n"
				+ "PROT_IMP_R_" + str + "_" + splitChannel(channel2) + "= rename(" + "PROT_IMP_" + str + "_"
				+ splitChannel(channel2) + " , " + "R_IO_" + str + "(" + channel2 + "," + channel1 + ")) \n";

		// System.out.println(protocolImpRename);
		return protocolImpRename;

	}

	// ------------------
	// PROT_IMP_

	public String prot_imp(String str, String channel1, String channel2) {

		String prot_imp = "";
		// verifica de que componente e o channel
		String[] parts = channel1.split("\\.");
		String part_channel1 = parts[0];
		String o_channel1;

		o_channel1 = this.getComponentOwner(part_channel1);

		// se o string possui "STM_"
		String[] parte_o_channel1;
		String componentName;

		if (o_channel1.contains("STM_")) {
			parte_o_channel1 = o_channel1.split("STM_");
			componentName = parte_o_channel1[1];
		} else {
			componentName = o_channel1;
		}

		// verifica de que componente e o channel2
		String[] parts2 = channel2.split("\\.");
		String part_channel2 = parts2[0];
		String o_channel2;
		o_channel2 = this.getComponentOwner(part_channel2);

		// se o string possui "STM_"
		String[] parte_o_channel2;
		String componentName2;

		if (o_channel2.contains("STM_")) {
			parte_o_channel2 = o_channel2.split("STM_");
			componentName2 = parte_o_channel2[1];
		} else {
			componentName2 = o_channel2;
		}

		prot_imp = "PROT_IMP_" + str + "_" + splitChannel(channel1) + "= protocolo_" + componentName + "_"
				+ this.splitPortFromChannel(channel1) + "(" + channel1 + ") " + "\n" + "PROT_IMP_" + str + "_"
				+ splitChannel(channel2) + "= protocolo_" + componentName2 + "_" + this.splitPortFromChannel(channel2)
				+ "(" + channel2 + ") \n";

		return prot_imp;
	}

	// inputs_PROT_IMP_R_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(e,r) =
	// replace(inputs_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(e), r)

	public String inputs_prot_imp_r(String str, String channel1, String channel2) {

		String inputs_prot_imp_r = "";

		inputs_prot_imp_r = "inputs_PROT_IMP_R_" + str + "(e,r) = replace(inputs_PROT_IMP_" + str + "(e), r) \n";

		// System.out.println(inputs_prot_imp_r);

		return inputs_prot_imp_r;

	}

	// outputs_PROT_IMP_R_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(e,r) =
	// replace(outputs_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(e), r)

	public String outputs_prot_imp_r(String str, String channel1, String channel2) {

		String outputs_prot_imp_r = "";

		outputs_prot_imp_r = "outputs_PROT_IMP_R_" + str + "(e,r)   = replace(outputs_PROT_IMP_" + str + "(e), r) \n";

		// System.out.println(outputs_prot_imp_r);

		return outputs_prot_imp_r;

	}

	// ----------------------------------------------
	public String r_io_process(String str, String channel1, String channel2) {

		String r_io_process = "";

		r_io_process = "R_IO_" + str + "(a, b) = seq({(a.x, b.x) | x <- extensions(a), member(a.x, "
				+ "outputs_all )}) \n";
		// System.out.println(r_io_process);
		return r_io_process;
	}

	// ------------------------
	// inputs_R_IO_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(a,b) =
	// inputs_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2
	// -----------------------

	public String inputs_r_io(String str, String channel1, String channel2) {

		String inputs_r_io = "";
		inputs_r_io = "inputs_R_IO_" + str + "(a,b) = inputs_all" + "\n";
		// System.out.println(inputs_r_io);
		return inputs_r_io;
	}

	// inputs_R_IO_PROT_IMP_

	public String inputs_r_io_prot_imp(String str, String channel1, String channel2) {

		String inputs_r_io_prot_imp = "";

		inputs_r_io_prot_imp = "inputs_R_IO_PROT_IMP_" + str + "_" + splitChannel(channel1)
				+ "(a,b)  = inputs_PROT_IMP_" + str + "_" + splitChannel(channel1) + "\n" + "inputs_R_IO_PROT_IMP_"
				+ str + "_" + splitChannel(channel2) + "(a,b)  = inputs_PROT_IMP_" + str + "_" + splitChannel(channel2)
				+ "\n \n";

		// System.out.println(inputs_r_io_prot_imp);
		return inputs_r_io_prot_imp;

	}

	// -- Dual Protocol
	// DUAL_PROT_IMP_R_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_port_fork_ext_1_phil_left_2_fork_right
	// =
	// rename(DUAL_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_port_fork_ext_1_phil_left_2_fork_right,
	// R_IO_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(port_fork_right.2
	// ,port_phil_left.1)) --modifidado por sarah
	// DUAL_PROT_IMP_R_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_port_phil_ext_1_phil_left_2_fork_right
	// =
	// rename(DUAL_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2_port_phil_ext_1_phil_left_2_fork_right,
	// R_IO_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2(port_phil_left.1
	// ,port_fork_right.2)) --modifidado por sarah

	public String dual_prot_imp_r_prot_imp(String str, String channel1, String channel2) {

		String dual_prot_imp_r_prot_imp = "";

		dual_prot_imp_r_prot_imp = "DUAL_PROT_IMP_R_PROT_IMP_" + str + "_" + splitChannel(channel1)
				+ "= rename(DUAL_PROT_IMP_" + str + "_" + splitChannel(channel1) + "," + "R_IO_" + str + "(" + channel1
				+ "," + channel2 + "))" + "\n" + "DUAL_PROT_IMP_R_PROT_IMP_" + str + "_" + splitChannel(channel2)
				+ "= rename(DUAL_PROT_IMP_" + str + "_" + splitChannel(channel2) + "," + "R_IO_" + str + "(" + channel2
				+ "," + channel1 + "))" + "\n";

		// System.out.println(dual_prot_imp_r_prot_imp);

		return dual_prot_imp_r_prot_imp;
	}

	// dual protocol
	// DUAL_PROT_IMP_FORK1_INTER_FORK2_port_fork_ext_1_phil_right_1_fork_left =
	// DUAL_PROT_FORK(port_fork_left.1)
	// DUAL_PROT_IMP_PHIL1_INTER_PHIL2_port_phil_ext_1_phil_right_1_fork_left =
	// DUAL_PROT_PHIL(port_phil_right.1)

	public String dual_prot_imp(String str, String channel1, String channel2) {

		String dual_prot_imp = "";

		// verifica de que componente e o channel
		String[] parts = channel1.split("\\.");
		String part_channel1 = parts[0];
		String o_channel1;

		o_channel1 = this.getComponentOwner(part_channel1);

		// se o string possui "STM_"
		String[] parte_o_channel1;
		String componentName;

		if (o_channel1.contains("STM_")) {
			parte_o_channel1 = o_channel1.split("STM_");
			componentName = parte_o_channel1[1];
		} else {
			componentName = o_channel1;
		}

		// verifica de que componente e o channel2
		String[] parts2 = channel2.split("\\.");
		String part_channel2 = parts2[0];
		String o_channel2;

		o_channel2 = this.getComponentOwner(part_channel2);

		// se o string possui "STM_"
		String[] parte_o_channel2;
		String componentName2;

		if (o_channel2.contains("STM_")) {
			parte_o_channel2 = o_channel2.split("STM_");
			componentName2 = parte_o_channel2[1];
		} else {
			componentName2 = o_channel2;
		}

		dual_prot_imp = "DUAL_PROT_IMP_" + str + "_" + splitChannel(channel1) + "= DUAL_PROT" + "_" + componentName
				+ "_" + this.splitPortFromChannel(channel1) // porta
				+ "(" + channel1 + ") \n" + "DUAL_PROT_IMP_" + str + "_" + splitChannel(channel2) + "= DUAL_PROT" + "_"
				+ componentName2 + "_" + this.splitPortFromChannel(channel2) + "(" + channel2 + ") \n\n";

		// System.out.println(dual_prot_imp);

		return dual_prot_imp;

	}

	public String functionDefault() {

		String function = "";

		function = "-----CIO funnction \n" + "channel out \n" + "channel in \n" + "channel mid \n" + "channel o \n \n"
				+ "CP(a,b) = a -> b -> CP(a,b) \n"
				+ "C(a, P) = (P[[ a <- mid ]] [| {| mid |} |] CP(a,mid)) \\ {|mid|} \n"
				+ "CIO(P) = C(in, C(out, P)) \n \n "
				// Rename function
				+ "--------- Rename channels in a process using a mapping < (old1, new1), ..., (oldn, newn)> \n \n"
				+ "rename(P, <>) = P \n" + "rename(P, <(c1,c2)>^rs) = rename(P[[c1 <- c2]], rs) \n \n"
				+ "-- Replaces events in a set using the mapping < (old1, new1), ..., (oldn, newn)> \n"
				+ "replace_aux(oldc, newc, S) = \n"
				+ "let other_events = {e | e <- S, not member(e, productions(oldc))} \n"
				+ "new_events   = {newc.v | v <- inter(extensions(newc), extensions(oldc)), member(oldc.v, S)} \n"
				+ "within union (other_events, new_events) \n \n" + "replace(S, <>) = S \n"
				+ "replace(S, <(c1,c2)>^rs) = replace(replace_aux(c1,c2,S),rs) \n";

		return function;
	}

	// ---- D.6: Protocols are Strong Compatible
	public String assertion2(String str, String channel1, String channel2) {

		String assertion2 = "";
		assertion2 = " --- assertion 2--------------- \n " + "assert PROT_IMP_R_IO_" + str + "_"
				+ splitChannel(channel1) + "_" + splitChannel(channel2) + ":[deadlock free [FD]] \n"
				+ "assert PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2) + "_" + splitChannel(channel1)
				+ ":[deadlock free [FD]] \n"

				+ "assert PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2)
				+ "[T= DUAL_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2) + "\n"
				+ "assert PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2) + "_" + splitChannel(channel1)
				+ "[T= DUAL_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2) + "_" + splitChannel(channel1) + "\n"
				+ "assert PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2)
				+ "\\ outputs_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2)
				+ ":[divergence free [FD]]" + "\n" + "assert PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2) + "_"
				+ splitChannel(channel1) + "\\ outputs_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2) + "_"
				+ splitChannel(channel1) + ":[divergence free [FD]]\n";

		// System.out.println(assertion2);

		return assertion2;
	}

	// + " DUAL_PROT_IMP_R_IO";
//assert DUAL_PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2
//_port_fork_ext_1_phil_left_2_fork_right_R_IO
//_port_phil_ext_1_phil_left_2_fork_right [T= PROT_IMP_FORK1_INTER_FORK2_COMM_PHIL1_INTER_PHIL2
//_port_fork_ext_1_phil_left_2_fork_right_R_IO
//_port_phil_ext_1_phil_left_2_fork_right

	public String assertion3(String str, String channel1, String channel2) {

		String assertion3 = "";

		assertion3 = "assert DUAL_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2)
				+ "[T= PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2) + "\n"
				+ "assert DUAL_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2) + "_" + splitChannel(channel1)
				+ "[T= PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2) + "_" + splitChannel(channel1) + "\n"

				+ "  assert DUAL_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2)
				+ "[F= PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2) + "_" + splitChannel(channel1) + "\n"
				+ "  assert DUAL_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel2) + "_" + splitChannel(channel1)
				+ "[F= PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2) + "\n";

		// System.out.println(assertion3);

		return assertion3;
	}

	public String dual_prot_imp_rio(String str, String channel1, String channel2) {

		String dual_prot_imp_rio = "";
		dual_prot_imp_rio = "DUAL_PROT_IMP_R_IO_" + str + "_" + splitChannel(channel1) + "_" + splitChannel(channel2)
				+ " = DUAL_PROT_IMP_R_PROT_IMP_" + str + "_" + splitChannel(channel1) + "\n" + "DUAL_PROT_IMP_R_IO_"
				+ str + "_" + splitChannel(channel2) + "_" + splitChannel(channel1) + "= DUAL_PROT_IMP_R_PROT_IMP_"
				+ str + "_" + splitChannel(channel2) + "\n \n";
		// System.out.println(dual_prot_imp_rio);

		return dual_prot_imp_rio;
	}

	/*************************************************/

// monta tag para protocolo

//para  cada tipo de componente criar um protocolo

	/**
	 * cria processos do protocolo
	 * 
	 * 
	 * @return
	 */

	public String tagProtocol() {

		String tag = "transparent wbisim" + "\n";

		Declarations declaration = Declarations.getInstance();
		ArrayList<ComponentType> type = declaration.getComponentType();

		String porta_ = "";
		int num_internal = declaration.getMemory_num(); // verificar se ha acesso a memoria
		String aux_ = "";
		String aux_guard = "";
		String aux_guard2 = "";
		Port portTemp = null;

		for (int i = 0; i < type.size(); i++) { // para cada componente

			HashSet<String> ports_comp_;
			String rename = "";
			String temp_ = type.get(i).getType();
			ports_comp_ = declaration.getPortComponentNotEnv(temp_);
			BasicComponent basicComponent = declaration.getBasicComponentbyName(temp_);

			Iterator it_ = ports_comp_.iterator();

			while (it_.hasNext()) {

				porta_ = (String) it_.next();

				// outrasportas
				String port_hidden = eliminaPortaAtual(ports_comp_, porta_);

				portTemp = declaration.getPort(porta_);

				// verifica se a porta tem guarda

				if (portTemp.isGuard()) {

					aux_guard = ".i";

					aux_guard2 = "|i :t_id ";
				}

				// channel auxiliar
				tag = tag + "channel tag_" + porta_ + " :" + "operation" + "\n" + "";

				// HIDE
				// esconder portas env
				HashSet<String> ports_comp_env;
				ports_comp_env = declaration.getPortComponenteEnv(temp_);
				Iterator it_env = ports_comp_env.iterator();

				String hide = "";

				if ((num_internal - 1) > 0) {

					hide = "internal";
				}

				if (ports_comp_env.size() > 0) {

					hide = hide + ",";
				}

				while (it_env.hasNext()) {

					String porta = (String) it_env.next();
					hide = hide + porta;
					if (it_env.hasNext()) {

						hide = hide + ",";
					}
				}

				// gets and sets de um componente

				BasicComponent tempBasic = declaration.getBasicComponentbyName(type.get(i).getType());

				ArrayList<Attribute> tempVar = tempBasic.getVar();

				if (tempVar.size() > 0) {
					hide = hide + ",";

				}

				for (int j = 0; j < tempVar.size(); j++) {

					hide = hide + "get_" + tempVar.get(j).getName() + "," + "set_" + tempVar.get(j).getName();

					if (j < tempVar.size() - 1) {

						hide = hide + ",";
					}

				}

				String temp_protocols = "";
				String processoWBS = "";

				if (portTemp.isMulti()) {

					for (int j = 1; j <= portTemp.getNr_multi(); j++) {

						// RENAME
						rename = porta_ + ".0" + "." + j;
						rename = rename + "<- tag_" + porta_;
						rename = "[[" + rename + "]]";

						String protocolName = "temp_protocolo_" + type.get(i).getType() + porta_ + "_" + j;

						temp_protocols = temp_protocols + "\n" + protocolName + "  = " + type.get(i).getType() + "0";

						// esconder os outros canais

						String hide_ = "";

						for (int k = 1; k <= portTemp.getNr_multi(); k++) {

							if (k != j) {

								hide_ = hide_ + porta_ + ".0" + "." + k;

							}

							if ((k != j && k + 1 != j && k + 1 <= portTemp.getNr_multi())
									|| (k != j && k + 1 == j && k + 2 <= portTemp.getNr_multi())) {

								hide_ = hide_ + " , ";

							}

						}

						String hide_temp = "";

						if ((hide.length() > 0) && (hide_.length() > 0)) {

							hide_temp = hide + ",";

						}

						if ((hide.length() > 0) || (hide_.length() > 0)) {

							hide_temp = "\\" + "{|" + hide_temp + hide_ + "|} \n";
						}

						temp_protocols = temp_protocols + rename + hide_temp;

						processoWBS = processoWBS + "processoWBS_" + type.get(i).getType() + "_" + j + " = " + ""
								+ " wbisim(" + "temp_protocolo_" + type.get(i).getType() + "_" + j + ") " + "\n";

						String WBSprotocolName = "processoWBS_" + type.get(i).getType() + "_" + j;

						Protocol tempProtocol = new Protocol(WBSprotocolName, porta_);
						basicComponent.getProtocols().add(tempProtocol);

					}

				} else {

					

					// RENAME
					rename = porta_ + ".0" + aux_guard;
					rename = rename + "<- tag_" + porta_ + aux_guard2;
					rename = "[[" + rename + "]]";

					String protocolName = "temp_protocolo_" + type.get(i).getType() + "_" + porta_;

					temp_protocols = temp_protocols + "\n" + protocolName + "  = " + type.get(i).getType() + "0";

					if (hide.length() > 0 &&  port_hidden.length() > 0) {

						hide = "\\" + "{|" + hide + ","+ port_hidden + "|} \n";
					}
					else if (hide.length() > 0 || port_hidden.length() > 0) {

						hide = "\\" + "{|" + hide + port_hidden + "|} \n";
					}

					temp_protocols = temp_protocols + rename + hide;

					processoWBS = processoWBS + "processoWBS_" + type.get(i).getType() + "_" + porta_ + " = "
							+ " wbisim(" + "temp_protocolo_" + type.get(i).getType() + "_" + porta_ + ") " + "\n";

					String WBSprotocolName = "processoWBS_" + type.get(i).getType() + "_" + porta_;
					Protocol tempProtocol = new Protocol(WBSprotocolName, porta_);
					basicComponent.getProtocols().add(tempProtocol);

				}

				tag = tag + "\n" + temp_protocols + "\n" + processoWBS;

				aux_ = "";
				aux_guard = "";

			} // loop porta
		} // loop component

		return tag;
	}

	private String eliminaPortaAtual(HashSet<String> ports_comp_, String portaAtual) {

		String retorno = "";
		
		HashSet<String> clone = (HashSet<String>) ports_comp_.clone();

		Object array[] = clone.toArray();

		Object array2[] = new Object[array.length - 1];

		int j = 0;

		for (int i = 0; i < array.length; i++) {

			String element = (String) array[i];

			if (!element.equals(portaAtual)) {

				array2[j] = element;
				j++;
			}

		}

		// HashSet<String> modifica = ports_comp_;
		// modifica.remove(portaAtual);

		// for (Iterator<String> iterator = ports_comp_.iterator(); iterator.hasNext();)
		// {
		// String value = iterator.next();
		// if (value.equals(portaAtual)) {
		//iterator.remove();
		// }
		// }

		// while (it_.hasNext()) {
		// String temp = (String) it_.next();
		// if (temp.equals(portaAtual)) {

		// it_.remove();

		// }

		// }

	

		for (int k = 0; k < array2.length; k++)

		{
			retorno = retorno + (String) array2[k];

			if (k < array2.length - 1) {

				retorno = retorno + ",";
			}

		}

		return retorno;
	}

//cria um basicComponent

	/**
	 * 
	 * 
	 */

	public void createBasicComponent() {

		Declarations declaration = Declarations.getInstance();
		ArrayList<ComponentType> componentType = declaration.getComponentType();
		ArrayList<BasicComponent> basicComponent = declaration.getBasicComponent();
		ArrayList<Attribute> var;

		for (ComponentType temp : componentType) {
			System.out.println("qtd  " + componentType.size());

			String str_temp = temp.getType();
			var = declaration.getAttributesType(str_temp);
			// verifica qual state machine possui esse nome
			String str_stm = declaration.getSTMbytype(str_temp);
			BasicComponent basicTemp = new BasicComponent(str_temp, str_stm, var);

			basicComponent.add(basicTemp);
		}

		// ver qual a state machine para este componente

	}

	/**
	 * 
	 * imprimir basic component
	 * 
	 * @return
	 */

	public String printBasicComponent() {

		String print = "";
		ArrayList<Attribute> temp_var;
		String set_sinc = "";
		Declarations declaration = Declarations.getInstance();
		ArrayList<BasicComponent> basicComponent = declaration.getBasicComponent();
		// verificar se ha memoria
		ArrayList<Memory> memories = declaration.getMemories();

		for (BasicComponent temp : basicComponent) {
			print = print + temp.getName() + "(id) =" + temp.getStm();

			// conjunto sincronizacao : gets e sets + internal + memory trigger
			// verificar se hA gets e sets

			temp_var = temp.getVar();

			for (int i = 0; i < temp_var.size(); i++) {

				set_sinc = set_sinc + "get_" + temp_var.get(i).getName() + ".id" + "," + "set_"
						+ temp_var.get(i).getName() + ".id";

				if (i < temp_var.size() - 1) {
					set_sinc = set_sinc + ",";
				}

			}

			String env_trigger = "";

			ArrayList<Memory> memoriesC = declaration.memoryType(temp.getName());

			Set<String> ports = new HashSet<String>();

			for (int k = 0; k < memoriesC.size(); k++) {

				if (memoriesC.get(k).getTrigger()[0].trim().length() > 0) {

					String strExp = memoriesC.get(k).getExp();
					if (strExp.trim().length() > 0) {
						String[] tempT = strExp.split("[?!]");
						ports.add(tempT[0]);

					}

				}

			}

			Iterator<String> iterator = ports.iterator();
			while (iterator.hasNext()) {

				env_trigger = env_trigger + iterator.next();

				if (iterator.hasNext()) {

					env_trigger = env_trigger + ",";

				}

			}

			if (!env_trigger.isEmpty()) {
				env_trigger = "," + env_trigger;
			}

			if (set_sinc.length() > 0) {

				print = print + " [|{|" + set_sinc + env_trigger + ",internal|}|]" + "\n" + temp.getName() + "_state";

				String param = "";

				for (int j = 0; j < temp_var.size(); j++) {

					// valor inicial da variavel
					String initial_value = temp_var.get(j).getInitial_value().trim();
					// split {...}

					String[] initial_split = initial_value.split("[{}.]");
					String initial = initial_split[1];

					param = param + initial;
					if (j < (temp_var.size() - 1)) {
						param = param + ",";
					}
				}

				print = print + "(id," + param + ") ";

				set_sinc = "";

				for (int i = 0; i < temp_var.size(); i++) {

					set_sinc = set_sinc + "get_" + temp_var.get(i).getName() + "," + "set_" + temp_var.get(i).getName();

					if (i < temp_var.size() - 1) {
						set_sinc = set_sinc + ",";
					}

				}

				String internal = "";

				if (memories.size() > 0) {

					internal = ",internal";

				}

				print = print + "\\{|" + set_sinc + internal + " |}" + "\n"; // ",internal|}" + "\n" ;

			}

			print = print + "\n";

			// processo getset
			set_sinc = "";

		}
		return print;
	}

	/**
	 * 
	 * Monta o string do dual protocol
	 * 
	 * @return
	 */

	public String getDualProtocol() {

		String dual = "";
		Declarations declaration = Declarations.getInstance();
		ArrayList<ComponentType> type = declaration.getComponentType();

		String protocolName = "";
		String portName = "";

		for (int i = 0; i < type.size(); i++) {

			BasicComponent component = declaration.getBasicComponentbyName(type.get(i).getType());
			ArrayList<Protocol> protocols = component.getProtocols();

			for (int l = 0; l < protocols.size(); l++) {

				protocolName = protocols.get(l).getProcessName();
				portName = protocols.get(l).getPortName();

				String protocolId = "";

				protocolId = protocolName.replaceAll("[^0-9.]", "");

				dual = dual + "DUAL_PROT_" + type.get(i).getType() + "_" + portName + protocolId
						+ "(ch)  = dual_wb_protocolo_" + type.get(i).getType() + "_" + portName + protocolId
						+ "[[w <- ch]] \n";

				dual = dual + " protocolo_" + type.get(i).getType() + "_" + portName + protocolId
						+ "(ch)  = wb_protocolo_" + type.get(i).getType() + "_" + portName + protocolId
						+ "[[w <- ch]] \n \n";

			}

		}

		return dual;

	}

	public static void main(String args[]) {

		Composition c = new Composition();
		c.tagProtocol();
	}

// monta contrato
	/**
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 * 
	 */

	public String contratoInterleave(String c1, String c2) {

		Declarations declaration = Declarations.getInstance();
		String st_ctr = "";

		Contrato ctr = declaration.getbyComponent(c1);

		Contrato ctr2 = declaration.getbyComponent(c2);

		String behaviour = c1 + " ||| " + c2;

		ArrayList<String> portas = ctr.getPortas();
		ArrayList<String> portas2 = ctr2.getPortas();
		portas.addAll(portas2);

		ArrayList<relac_op_port> relacao = ctr.getRelacao();
		ArrayList<relac_op_port> relacao2 = ctr2.getRelacao();
		relacao.addAll(relacao2);

		ArrayList<String> op = ctr.getOp();

		// novo contrato
		Contrato ctrnovo = new Contrato("componente", "inter_" + c1 + "_" + c2, behaviour, portas, op, relacao);

		declaration.addContrato(ctrnovo);

		// string para as portas

		String str_portas = "";

		for (int i = 0; i < portas.size(); i++) {

			str_portas = str_portas + portas.get(i);

			if (i < portas.size() - 1) {
				str_portas = str_portas + ",";

			}

		}

		// string das opercoes

		String str_op = "";

		for (int k = 0; k < op.size(); k++) {

			str_op = str_op + op.get(k);

			if (k < op.size() - 1) {

				str_op = str_op + ",";
			}
		}

		String str_relac = montaRelacao(portas, op);

		st_ctr = ctrnovo.getName() + " = " + "<" + ctrnovo.getBehaviour() + "," + "{" + str_portas + "}" + "{" + str_op
				+ "}" + "{" + str_relac + "}";

		System.out.println("st_ctr ----->" + st_ctr);

		/*
		 * -- interleave_fork2_fork1_ctr = < fork1 |||fork2 ,
		 * {fork_right.1,fork_left.1,fork_right.2,fork_left.2}, {picksup_I , picksup_O ,
		 * putsdown_I , putsdown_O}, {(fork_right.1 , {picksup_I , picksup_O ,
		 * putsdown_I , putsdown_O}), (fork_left.1 , {picksup_I , picksup_O , putsdown_I
		 * , putsdown_O}) , (fork_right.2 , {picksup_I , picksup_O , putsdown_I ,
		 * putsdown_O}), (fork_left.2, {picksup_I , picksup_O , putsdown_I ,
		 * putsdown_O})}>
		 * 
		 */

		return st_ctr;

	}

//contrato feedback

	public String contratoFeed(String c1, SetSinc sinc) {

		String str_ctr = "";
		Declarations declaration = Declarations.getInstance();

		Contrato ctr = declaration.getbyComponent(c1);

		ArrayList<String> portas = ctr.getPortas();

		ArrayList<relac_op_port> relacao = ctr.getRelacao();

		ArrayList<String> op = ctr.getOp();

		String str_seq = sinc.getChannel1() + "," + sinc.getChannel2();
		ArrayList<String> array = new ArrayList<String>();
		array.add(sinc.getChannel1());
		array.add(sinc.getChannel2());

		String behaviour = c1 + "[|{" + str_seq + "}|]" + "" + "BFIO_INIT(" + str_seq + ")";

		String name = "processcomp";

		if (c1.startsWith("processcomp")) {

			name = c1 + "_com";
		}
		portas.removeAll(array);

		// tirar das portas do contrato as portas q estao na sincroniza

		// novo contrato

		// novo contrato
		Contrato ctrnovo = new Contrato("componente", name, behaviour, portas, op, relacao);

		declaration.addContrato(ctrnovo);

		String str_portas = "";

		for (int i = 0; i < portas.size(); i++) {

			str_portas = str_portas + portas.get(i);

			if (i < portas.size() - 1) {
				str_portas = str_portas + ",";

			}

		}

		// string das opercoes

		String str_op = "";

		for (int k = 0; k < op.size(); k++) {

			str_op = str_op + op.get(k);

			if (k < op.size() - 1) {

				str_op = str_op + ",";
			}
		}

		String str_relac = montaRelacao(portas, op);

		// System.out.println(str_seq + "\n" + str_portas);

		str_ctr = ctrnovo.getName() + " = " + "<" + ctrnovo.getBehaviour() + "," + "{" + str_portas + "}" + "{" + str_op
				+ "}" + "{" + str_relac + "}";

		System.out.println("st_ctr FEED ----->" + str_ctr);

		return str_ctr;
	}

	public String montaRelacao(ArrayList<String> portas, ArrayList<String> op) {

		String rel = "";

		// monta string op
		String str_op = "{";
		for (int i = 0; i < op.size(); i++) {

			str_op = str_op + op.get(i);

			if (i < op.size() - 1) {

				str_op = str_op + ",";
			}
		}

		str_op = str_op + "}";

		rel = "{";

		String str_portas = "";

		for (int k = 0; k < portas.size(); k++) {

			str_portas = str_portas + "(" + portas.get(k) + "," + str_op + ")";

			if (k < portas.size() - 1) {

				str_portas = str_portas + ",";
			}

		}

		rel = rel + str_portas + "}";

		return rel;

	}

///classe processo

	class Processo {

		String name;
		String corpo;
		int index;

		Processo(String name, String corpo, int index) {
			this.name = name;
			this.corpo = corpo;
			this.index = index;
		}

		String montaString() {

			return this.name + " = " + this.corpo + "\n";
		}
	}

}

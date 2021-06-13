package plugin;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class VerificationContract {

	// funcao que retorna assertivas de i/o process

	/**
	 * I/O Process CSP assertions
	 * 
	 * @return
	 */

	public String ContractCSP() {

		System.out.println("verificacao com FDR4 :O 1");
		String cspFDR = "";
		Declarations declaration = Declarations.getInstance();
		HashSet<Instance> instancias = declaration.getInstances();
		Iterator it = instancias.iterator();

		functionAux();

		while (it.hasNext()) {

			String temp = "";

			Instance instance_temp = (Instance) it.next();

			cspAuxiliar(instance_temp.getName());

			cspFDR = cspFDR + "\n--Condition A.2.1: Every channel in P is an I/O Channel"
					+ "\nassert not Test(inter(inputs_" + instance_temp.getName() + ",outputs_"
					+ instance_temp.getName() + ") == {}) [T= ERROR";

			cspFDR = cspFDR + "\n --Assertions:\n\n--Condition A.2: I/O Process "
					+ "\n\n--Condition A.2.2: The contract has infinite set of traces" + "\nassert not HideAll_"
					+ instance_temp.getName() + ":[divergence free [FD]]"
					+ "\n\n--Condition A.2.3:	The contract is divergence-free" + "\nassert " + instance_temp.getName()
					+ ":[divergence free [FD]]" + "\n\n--Condition A.2.4: The contract is input deterministic"

					+ "\n --Nao ha escolha interna entre os eventos de entrada  \n "

					+ "\nassert LHS_InputDet_" + instance_temp.getName() + " [F= RHS_InputDet_"
					+ instance_temp.getName() + "" + "\n\n--Condition A.2.5: The contract is strong output decisive"
					+ "\nassert LHS_OutputDec_A_" + instance_temp.getName() + " [F= RHS_OutputDec_A_"
					+ instance_temp.getName() + "";

			for (String portName : instance_temp.getPortas()) {

				cspFDR = cspFDR + "\n" + "assert LHS_OutputDec_B_" + instance_temp.getName() + "(" + portName
						+ ") [F= RHS_OutputDec_B_" + instance_temp.getName() + "(" + "" + portName + ")";

			}

			HashSet<String> ports_comp;
			Iterator<String> it_str;

			ports_comp = declaration.getPortComponent(instance_temp.getType());
			it_str = ports_comp.iterator();
			String str_temp = "";
			while (it_str.hasNext()) {

				temp = (String) it_str.next();
				str_temp = temp + "." + declaration.getId(instance_temp.getName());
			}
		}
		System.out.println(cspFDR);
		return cspFDR;
	}

	public void cspAuxiliar(String name) {

		FileWriter arquivo;
		String str = "";
		try {
			arquivo = new FileWriter(new File(
					"C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + "auxiliar_" + name + ".csp"));

			String chan = chan(name);
			str = "---------------------------------------------------------\n" + "-- Side Condition FUNCTIONS --\n"
					+ "---------------------------------------------------------\n" + "\n" + "events_" + name
					+ " = union(inputs_" + name + ",outputs_" + name + ")\n" + "allInputs_" + name + "= Union({ inputs_"
					+ name + "})\n" + "allOutputs_" + name + " = Union({ outputs_" + name + "})\n"
					+ "-- Filters inputs or outputs from a given set of events\n" + "\n" + "filter_" + name
					+ "(inout,S) =\n" + "	if inout == in then inter(allInputs_" + name + ",S) else inter(allOutputs_"
					+ name + ",S)\n" + "\n" + "\n" + "HideOutputs_" + name + " = " + name + " \\ outputs_" + name + "\n"
					+ "HideInputs_" + name + "  = " + name + " \\ inputs_" + name + "\n" + "HideAll_" + name + " = "
					+ name + " \\ union(outputs_" + name + ",inputs_" + name + ")\n" + "CollapseOutputs_" + name + " = "
					+ name + " [[ x <- o | x <- outputs_" + name + " ]] \\ allInputs_" + name + "\n" + "\n"
					+ "-- Renaming I/O\n" + "R_IO_" + name
					+ "(a, b) = seq({(a.x, b.x) | x <- extensions(a), member(a.x, outputs_" + name + ")})\n"
					+ "inputs_R_IO_" + name + "(a,b)  = inputs_" + name + "\n" + "outputs_R_IO_" + name
					+ "(a,b) = { b.x | x <- extensions(a), member(a.x, outputs_" + name + ") }\n" + "\n" + "\n"
					+ "-- Protocol Implementation\n" + "inputs_PROT_IMP_" + name + "(c)  = inter(inputs_" + name
					+ ",{|c|})\n" + "outputs_PROT_IMP_" + name + "(c) = inter(outputs_" + name + ",{|c|})\n" + "\n"
					+ "-- Protocol Implementation and renaming\n" + "PROT_IMP_R_" + name + "(r)   = rename(" + name
					+ ",r) --modifidado por sarah\n" + "inputs_PROT_IMP_R_" + name
					+ "(e,r)   = replace(inputs_PROT_IMP_" + name + "(e), r)\n" + "outputs_PROT_IMP_R_" + name
					+ "(e,r)   = replace(outputs_PROT_IMP_" + name + "(e), r)\n" + "\n"
					+ "--InBufferProt(P,c) =  CIO(P[[ c.in.x  <- i, c.out.y <- o | x  <- extensions(c.in), y  <- extensions(c.out)]])\n"
					+ "InBufferProt_" + name + "(c) =  CIO(" + name + "[[ x  <- in, y <- out | x  <- inputs_" + name
					+ ", y  <- outputs_" + name + "]])\n" + "\n" + "-- Channels Projection\n" + "PROJECTION_" + name
					+ "(cs) = " + name + " \\ (diff(Events, Union({ {| c |} | c <- cs})))\n" + "\n" + "\n"
					+ "-- Protocol Implementation\n" + "PROT_IMP_def_" + name + "(c) = " + name
					+ " \\ (diff(Events, {|c|}))\n" + "\n"
					+ "----assert ProtCheck(<PROCESS>,<CHANNEL>) :[deadlock free [FD]]\n"
					+ "----------------------------------------\n" + "-- \"... and nothing else matters...\"\n"
					+ "----------------------------------------\n"
					+ "----assert <PROCESS> [| Events |] (<PROCESS> ||| RUN(NOT(<CHANNEL>))) [T= <PROCESS>\n"
					+ "----assert <PROCESS> [T= <PROCESS> [| Events |] (<PROCESS> ||| RUN(NOT(<CHANNEL>))) \n" + "\n"
					+ "----------------------------------------\n" + "-- Input Determinism\n"
					+ "----------------------------------------\n" + "Clunking_" + name + " = " + name
					+ " [| AllButClunk |] Clunker\n" + "RHS_InputDet_" + name + " = (Clunking_" + name
					+ "[|{clunk}|]Clunking_" + name + ") \\ {clunk} [|AllButClunk|] Repeat\n" + "\n" + "LHS_InputDet_"
					+ name + " = Deterministic(inputs_" + name + ")\n" + "\n"
					+ "--assert LHS_InputDet(<PROCESS>) [F= RHS_InputDet(<PROCESS>)\n" + "\n" + "-----------------\n"
					+ "-----------------\n" + "\n" + "----------------------------------------\n"
					+ "-- Strong output decisive\n" + "----------------------------------------\n" + "\n"
					+ "-- Roscoe's Strong output decisive\n" + "SSET_" + name + "(c) = inter({|c|},outputs_" + name
					+ ")\n" + "F_" + name + "(c) = " + name + " [| SSET_" + name + "(c) |] S(SSET_" + name + "(c)) \n"
					+ "G_" + name + "(c) = " + name + " [| SSET_" + name + "(c) |] CHAOS(SSET_" + name + "(c))\n"
					+ "--assert F(<PROCESS>,<CHANNEL>) [F= G(<PROCESS>,<CHANNEL>)\n" + "\n" + "\n"
					+ "-----------------------------------------------------------------\n"
					+ "-----------------------------------------------------------------\n" + "\n"
					+ "--PREVIOUS DEFINITIONS\n" + "\n" + "--One2Many(S) = \n"
					+ "--    ([] x:diff(Events,union(S, {clunk})) @ x -> One2Many(S))\n"
					+ "--    [] ([] c:S @ [] x:{|c|} @ x -> One2Many'(S,c,x))\n"
					+ "--One2Many'(S,c,x) = [] y:{|c|} @ y -> if x==y then One2Many(S) else STOP\n"
					+ "--RHS_OutputDec_A_" + name + " = \n" + "--    (Clunking_" + name
					+ "[|diff(Events, outputs(P))|]Clunking_" + name + ") \\ {clunk} \n" + "--    [| AllButClunk |] \n"
					+ "--    One2Many(outputs_" + name + ")\n" + "\n"
					+ "-- Definition above is sliglty wrong because One2Many'(S,c,x) enforces the following event to be a repetition \n"
					+ "-- if the given c is not a channel bout the full event.\n" + "\n" + "--NEW DEFINITIONS\n" + "\n"
					+ "One2Many_" + name + " = \n" + "    ([] x:diff(Events,union(outputs_" + name
					+ ", {clunk})) @ x -> One2Many_" + name + ")\n" + "    [] ([] c:outputs_" + name
					+ " @ [] x:{|c|} @ x -> One2Many_" + name + "'(c,x))\n" + "\n" + "One2Many_" + name
					+ "'(c,x) = [] y:chan_" + name + "(c) @ y -> if x==y then One2Many_" + name + " else STOP\n" + "\n"
					+ "RHS_OutputDec_A_" + name + " = \n" + "    (Clunking_" + name + "[|diff(Events, outputs_" + name
					+ ")|]Clunking_" + name + ") \\ {clunk} \n" + "    [| AllButClunk |] \n" + "    One2Many_" + name
					+ "\n" + "\n" + "-----------------------------------------------------------------\n"
					+ "-----------------------------------------------------------------\n" + "\n" + "\n"
					+ "LHS_OutputDec_A_" + name + " = \n" + "            STOP \n" + "            |~|\n"
					+ "            ([] x:diff(Events,union(outputs_" + name + ", {clunk})) @ x -> LHS_OutputDec_A_"
					+ name + ")\n" + "            [] \n" + "            ([] x:outputs_" + name + " @ x -> (|~| y:chan_"
					+ name + "(x) @ y -> LHS_OutputDec_A_" + name + "))\n" + "\n" + "\n"
					+ "----------------------------------------------------\n"
					+ "-- Part B. After trace s^<c.x> it might refuse all events on {|c|} \\ {c.x}\n" + "\n"
					+ "FirstCopy_" + name + " = " + name + " [| AllButClunk |] DoubleClunker\n" + "SecondCopy_" + name
					+ " = " + name + " [| AllButClunk |] clunk -> DoubleClunker\n" + "\n" + "LHS_OutputDec_B_" + name
					+ "(c) = (FirstCopy_" + name + "[|{clunk}|]SecondCopy_" + name
					+ ")\\{clunk} [|Events|] LHS_Test(inter({|c|}, outputs_" + name + "))\n" + "\n" + "RHS_OutputDec_B_"
					+ name + "(c) = (FirstCopy_" + name + "[|{clunk}|]SecondCopy_" + name
					+ ")\\{clunk} [|Events|] RHS_Test(inter({|c|}, outputs_" + name + "))\n" + "\n" + chan;

			arquivo.write(str);
			arquivo.flush();
			arquivo.close();

		} catch (Exception e) {

		}

		// return str;
	}

	public void functionAux() {

		FileWriter arquivo;

		String str = "";

		try {

			boolean exists = (new File(
					"C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + "function_aux.csp")).exists();
			if (exists) {

				System.out.println("existe");
			}
			{
				arquivo = new FileWriter(
						new File("C:/Users/flavi/eclipse-workspace/CorTeste/src/main/resources/" + "function_aux.csp"));

				str = "--------------------------------------------------------------------\n"
						+ "--------------------------------------------------------------------\n"
						+ "-- Function auxiliary operations \n"
						+ "--------------------------------------------------------------------\n"
						+ "--------------------------------------------------------------------"
						+ "-- Function is a set {(x1, y1),...,(xn, yn)}\n"
						+ "-- Transforms a singleton set into the element itself \n" + "pick({x}) = x \n"
						+ "-- Returns the function return \n"
						+ "-- Raises error if z is not in the domain of the function \n" + "apply(<(x,y)>^rs,z) = \n"
						+ "	if x==z then y else apply(rs,z) \n" + "-- domain antirestriction \n"
						+ "ddres(f,xs) = <(n,v) | (n,v) <- f, not member(n,xs)> \n" + "-- domain restriction \n"
						+ "dres(f,xs) = <(n,v) | (n,v) <- f, member(n,xs)> \n" + "-- Overwrites the fuction \n"
						+ "over(<>,n,v) = <> \n" + "over(<(x,y)>^rs,n,v) = \n"
						+ "	if x==n then <(x,v)>^rs else <(x,y)>^over(rs,n,v) \n"
						+ "-- Returns the domain of a relation \n" + "dom(f) = set(<n | (n,v) <- f>) \n"
						+ "-- Returns the domain of a relation \n" + "ran(f) = set(<v | (n,v) <- f>) \n"
						+ "-- Subset or equals \n" + "subseteq(S,T) = diff(S,T) == {} \n"
						+ "---------------------------------------------------------\n" + "-- Auxiliar Functions\n"
						+ "---------------------------------------------------------\n" + "\n"
						+ "inc(x,n) = (x + 1) % n\n" + "dec(x,n) = (x - 1) % n\n" + "\n"
						+ "-- Rename channels in a process using a mapping < (old1, new1), ..., (oldn, newn)>\n" + "\n"
						+ "rename(P, <>) = P\n" + "rename(P, <(c1,c2)>^rs) = rename(P[[c1 <- c2]], rs)\n" + "\n"
						+ "-- Replaces events in a set using the mapping < (old1, new1), ..., (oldn, newn)>\n" + "\n"
						+ "replace_aux(oldc, newc, S) =\n"
						+ "	let other_events = {e | e <- S, not member(e, productions(oldc))}\n"
						+ "	    new_events   = {newc.v | v <- inter(extensions(newc), extensions(oldc)), member(oldc.v, S)}\n"
						+ "	within union (other_events, new_events)\n" + "\n" + "replace(S, <>) = S\n"
						+ "replace(S, <(c1,c2)>^rs) = replace(replace_aux(c1,c2,S),rs)\n" + "\n"
						+ "-- Dual Protocol Implementation and renaming R_IO\n"
						+ "DUAL_PROT_IMP_R(P,r) = rename(P, r) --modifidado por sarah\n" + "\n"
						+ "---------------------------------------------------------\n"
						+ "-- Side Condition PROCESSES --\n"
						+ "---------------------------------------------------------\n" + "\n"
						+ "RUN(A) = [] x:A @ x -> RUN(A)\n" + "CHAOS(A) = STOP |~| ([] x:A @ x -> CHAOS(A))\n" + "\n"
						+ "-- Test if a given condition is met\n" + "channel error\n" + "ERROR = error -> SKIP\n"
						+ "Test(c) = not c & ERROR\n" + "\n" + "-- Used for testing protocols\n"
						+ "-- This is not used in the CSP framework, but will be used in the Circus/CML Framework\n"
						+ "channel out\n" + "channel in\n" + "channel mid\n" + "channel o\n" + "\n"
						+ "CP(a,b) = a -> b -> CP(a,b)\n"
						+ "C(a, P) = (P[[ a <- mid ]] [| {| mid |} |] CP(a,mid)) \\ {|mid|}\n"
						+ "CIO(P) = C(in, C(out, P))\n" + "\n" + "LHS_Test(S) =\n" + "    [] x:S @ \n"
						+ "        x -> (x -> LHS_Test(S) [>\n"
						+ "                      ([] y:diff(S, {x}) @ y -> STOP)\n" + "              )\n"
						+ "            [] ([] y:diff(Events,S) @ y -> y -> LHS_Test(S))\n" + "RHS_Test(S) =\n"
						+ "    [] x:S @ \n" + "        x -> \n"
						+ "            ( ([] y:S @ y -> if x==y then RHS_Test(S) else STOP) \n"
						+ "              |~| STOP )\n"
						+ "            [] ([] y:diff(Events,S) @ y -> y -> RHS_Test(S))\n"
						+ "----------------------------------------\n" + "-- Input Determinism\n"
						+ "----------------------------------------\n" + "channel clunk\n"
						+ "AllButClunk = diff(Events, {clunk})\n"
						+ "Clunker = [] x:AllButClunk @ x -> clunk -> Clunker\n"
						+ "DoubleClunker = [] x:AllButClunk @ x -> clunk -> clunk -> DoubleClunker\n" + "\n" + "\n"
						+ "Repeat = [] x:AllButClunk @ x -> x -> Repeat\n" + "\n" + "\n"
						+ "COPY (LR) = [] x : dom(LR) @ x -> apply(LR,x) -> COPY (LR)\n"
						+ "BUFF_IO(BF, LR1, LR2) = (BF (LR1) ||| BF (LR2))\n"
						+ "BUFF_IO_1(LR1,LR2) = BUFF_IO(COPY, LR1, LR2)\n" + "\n"
						+ "----------------------------------------\n"
						+ "-- These processes are only used when we have a optimization due to the use of architectural styles\n"
						+ "-- Client-Server Specification\n" + "\n" + "CS(c) = CS_CLIENT(c) [] CS_SERVER(c)\n"
						+ "CS_CLIENT(c) = c.in?v1 -> c.out?v2 -> CS_CLIENT(c)\n"
						+ "CS_SERVER(c) = c.out?v1 -> c.in?v2 -> CS_SERVER(c)\n" + "\n"
						+ "----------------------------------------\n" + "-- Protocol does not speak anything else\n"
						+ "----------------------------------------\n" + "NOT(c) = diff(Events, {| c |})\n"
						+ "PRUNE(A) = [] ev: A @ ev -> STOP\n" + "ProtCheck(P,c) = P [| NOT(c) |] PRUNE(NOT(c))\n"
						+ "-- Original Lazic`s Algorithm (adapted by Roscoe)\n"
						+ "LHS' = STOP |~| ([] x:AllButClunk @ x -> x -> LHS')\n" + "\n"
						+ "-- Changed Lazic`s Algorithm (proposed by Roscoe) to consider only a particular set of events\n"
						+ "Deterministic(S) = \n" + "    STOP \n" + "    |~|     \n"
						+ "    ([] x:AllButClunk @ x -> (if member(x,S) \n"
						+ "                        then x -> Deterministic(S)\n"
						+ "                        else (STOP |~| x -> Deterministic(S))))\n" + "\n" + "S(X)   = \n"
						+ "	if (card(X) > 1) then\n" + "		(|~| x:X @ [] y:diff(X, {x}) @ y -> S(X))\n"
						+ "	else \n" + "		(STOP |~| ([] x:X @ x -> S(X)))\n" + "\n";

				arquivo.write(str);
				arquivo.flush();
				arquivo.close();

			}

		} catch (Exception e) {

		}
	}

	//

	public String chan(String name) {

		// tipo de uma instancia
		Declarations declaration = Declarations.getInstance();

		String type = declaration.getPortType(name);
		int id = declaration.getId(name);

		ArrayList<Port> portas = declaration.getPortbyType(type);

		String chan = "-- Outputs events with same prefixing of a given output event\n" + "chan_" + name + "(ev) = \n"
				+ "    inter(outputs_" + name + ",\n" + "          Union({ ";

		String channel = "";

		for (int i = 0; i < portas.size(); i++) {

			channel = portas.get(i).getName() + "." + id;

			if (i < portas.size() - 1) {

				chan = chan + "{| " + channel + " | member (ev, {|" + channel + "|})|},\n";
			} else {
				chan = chan + "{| " + channel + " | member (ev, {|" + channel + "|})|} })\n" + ")\n";

			}
		}
		return chan;
	}

	/*
	 * String chan =
	 * "-- Outputs events with same prefixing of a given output event\n" + "\nchan_"
	 * + contrato.getName() + "(ev) = \n" + "    inter(outputs_" +
	 * contrato.getName() + ",\n" + "          Union({ "; for (int i = 0; i <
	 * contrato.getChannel().size(); i++){ if(i < contrato.getChannel().size()-1){
	 * chan = chan + "{| "+ contrato.getChannel().get(i).getName() +
	 * " | member (ev, {|"+ contrato.getChannel().get(i).getName()+ "|})|},\n"; }
	 * else{ chan = chan + "{| "+ contrato.getChannel().get(i).getName() +
	 * " | member (ev, {|"+ contrato.getChannel().get(i).getName()+ "|})|} })\n" +
	 * ")\n"; } }
	 */

}

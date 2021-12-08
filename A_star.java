import java.util.*;
import java.lang.*;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.ArrayList;
import java.lang.Math;
import java.util.stream.Collectors;
import java.text.Collator;
import java.lang.Throwable;
import java.lang.Exception;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import java.nio.file.Files;
import java.util.PriorityQueue;

class Cnode implements Comparable<Cnode>
{  
	public int inc_num;
    public double F;
	public double time;
	public double dist;

	public int compareTo(Cnode o) { // pif
		if (F > o.F) return(1);
		return(-1);
	}
};

class Taxi implements Comparable<Taxi>
{  
	public int id;
    public double J;
	public double waiting_time;
	public double dist;
	public double ranking;
	
	public int sort_criteria; // 0: waiting_time, 1: ranking

	public int compareTo(Taxi o) {
		if (sort_criteria == 0) {
			if (waiting_time > o.waiting_time) return(1);
			return(-1);
		}
		else {
			if (ranking < o.ranking) return(1);
			return(-1);
		}
	}
};

public class A_star {

	private static final double epsilon = 0.0000000001;
	
	private static int saved_progress = -1;
	
	public static void progress(int new_progress) {
		if (saved_progress == 0) System.out.print("\b\b");
		else if (saved_progress > 0) {
			System.out.print("\b");
			while (saved_progress > 0) {
				System.out.print("\b");
				saved_progress /= 10;
			}
		}
		System.out.print(new_progress + "%");
		saved_progress = new_progress;
	}
		
	public static Cnode getcnode(int inc_num, double F, double time, double dist) {
		Cnode res = new Cnode();
		res.inc_num = inc_num;
		res.F = F;
		res.time = time;
		res.dist = dist;
		return(res);
	}
	
	public static Taxi gettaxi(int id, double J, double waiting_time, double dist, double ranking, int sort_criteria) {
		Taxi res = new Taxi();
		res.id = id;
		res.J = J;
		res.waiting_time = waiting_time;
		res.dist = dist;
		res.ranking = ranking;
		res.sort_criteria = sort_criteria;
		return(res);
	}
	
	public static Taxi change_sort_criteria(Taxi obj, int new_sort_criteria) {
		obj.sort_criteria = new_sort_criteria;
		return(obj);
	}
	
	public static JIPQuery jipQuery;
	public static JIPTerm term;
	public static JIPEngine jip;
	public static JIPTermParser parser;
	
	public static void solver(int clid, int destid, String doc_kml_name, String doc_dest_kml_name) throws IOException {
		int i;
		
		System.out.println("Executing A*...");
		
		// A*
		
		String[] styles = new String[11];
		styles[0]="<Style id=\"green\">%n<LineStyle>%n<color>ff009900</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		styles[1]="<Style id=\"black\">%n<LineStyle>%n<color>50000000</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		styles[2]="<Style id=\"yellow\">%n<LineStyle>%n<color>5014F0FF</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		styles[3]="<Style id=\"aqua\">%n<LineStyle>%n<color>50F0FF14</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		styles[4]="<Style id=\"blue\">%n<LineStyle>%n<color>50F05014</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		styles[5]="<Style id=\"brown\">%n<LineStyle>%n<color>50143C5A</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		styles[6]="<Style id=\"purple\">%n<LineStyle>%n<color>50780078</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		styles[7]="<Style id=\"orange\">%n<LineStyle>%n<color>501478FF</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		styles[8]="<Style id=\"pink\">%n<LineStyle>%n<color>50CDA0F9</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		styles[9]="<Style id=\"grey\">%n<LineStyle>%n<color>50464646</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		styles[10]="<Style id=\"salmon\">%n<LineStyle>%n<color>507896F0</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		
		
		String[] intro = new String[11];
		intro[0]="<Placemark>%n<name>Taxi #100</name>%n<styleUrl>#green</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		intro[1]="<Placemark>%n<name>Taxi #110</name>%n<styleUrl>#black</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		intro[2]="<Placemark>%n<name>Taxi #120</name>%n<styleUrl>#yellow</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		intro[3]="<Placemark>%n<name>Taxi #130</name>%n<styleUrl>#aqua</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		intro[4]="<Placemark>%n<name>Taxi #140</name>%n<styleUrl>#blue</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		intro[5]="<Placemark>%n<name>Taxi #150</name>%n<styleUrl>#brown</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		intro[6]="<Placemark>%n<name>Taxi #160</name>%n<styleUrl>#purple</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		intro[7]="<Placemark>%n<name>Taxi #170</name>%n<styleUrl>#orange</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		intro[8]="<Placemark>%n<name>Taxi #180</name>%n<styleUrl>#pink</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		intro[9]="<Placemark>%n<name>Taxi #190</name>%n<styleUrl>#grey</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		intro[10]="<Placemark>%n<name>Taxi #200</name>%n<styleUrl>#salmon</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		
		String outro="</coordinates>%n</LineString>%n</Placemark>";
		
		PrintWriter writer = new PrintWriter(doc_kml_name, "UTF-8");
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<kml xmlns=\"http://earth.google.com/kml/2.1\">");
		writer.println("<Document>");
		writer.println("<name>Taxi Routes</name>");
		
		jipQuery = jip.openSynchronousQuery(parser.parseTerm("dist_from_client(INC_NUM, DIST)."));
		term = jipQuery.nextSolution();
		int L = 0;
		ArrayList<Double> H = new ArrayList<Double>();
		
		while (term != null) {
			L++;
			double DIST = Double.parseDouble(term.getVariablesTable().get("DIST").toString());
			int INC_NUM = Integer.parseInt(term.getVariablesTable().get("INC_NUM").toString());
			H.add(0.65 * DIST + 10.46 * DIST / 100);  // under-estimation of cost (heuristic)
			term = jipQuery.nextSolution();
		}
		
		jipQuery = jip.openSynchronousQuery(parser.parseTerm("client(X, Y, X_DEST, Y_DEST, TIME, PERSONS, LANGUAGE, LUGGAGE)."));
		term = jipQuery.nextSolution();
		int LUGGAGE = Integer.parseInt(term.getVariablesTable().get("LUGGAGE").toString());
		
		ArrayList<Integer> adequate_taxi_ids = new ArrayList<Integer>(), adequate_taxi_inc_num = new ArrayList<Integer>();
		jipQuery = jip.openSynchronousQuery(parser.parseTerm("adequate(TAXI_ID, INC_NUM)."));
		term = jipQuery.nextSolution();
		while (term != null) {
			int TAXI_ID = Integer.parseInt(term.getVariablesTable().get("TAXI_ID").toString());
			int INC_NUM = Integer.parseInt(term.getVariablesTable().get("INC_NUM").toString());
			adequate_taxi_ids.add(TAXI_ID);
			adequate_taxi_inc_num.add(INC_NUM);
			term = jipQuery.nextSolution();
		}
		
		// Taxi results (id, J, waiting_time, ranking)
		ArrayList<Taxi> taxi_info = new ArrayList<Taxi>();
		
		for (int j = 0; j < adequate_taxi_ids.size(); j++) {
			double[] D = new double[L];
			for (i = 0; i < L; i++) D[i] = Double.POSITIVE_INFINITY;
			double[] T = new double[L];
			for (i = 0; i < L; i++) T[i] = -1;
			double[] Dist = new double[L];
			for (i = 0; i < L; i++) Dist[i] = -1;
			int TAXI_ID = adequate_taxi_ids.get(j);
			int INC_NUM = adequate_taxi_inc_num.get(j);
			int tid = INC_NUM;
			int tax = TAXI_ID / 10 - 10;
			writer.println(styles[tax]);
			D[INC_NUM] = 0;
			
			PriorityQueue<Cnode> PQ = new PriorityQueue<>();  // metopo anazitisis
			
			PQ.add(getcnode(INC_NUM, H.get(INC_NUM), 0.0, 0.0));
			
			ArrayList<ArrayList<Integer>> best_paths = new ArrayList<ArrayList<Integer>>();
			for (i = 0; i < L; i++) {
				best_paths.add(new ArrayList<>());
			}
			
			Cnode cn;
			
			double thres = Double.POSITIVE_INFINITY;
			
			int w = 0;
			
			// Cnode getcnode(int inc_num, double F, double time)
			// F = G + H
			// H: Heuristic
			// G: Path till now
			
			while ((cn = PQ.poll()) != null) {
				w++;
				double G = cn.F - H.get(cn.inc_num);
				if (G > D[cn.inc_num] + epsilon || G >= thres + epsilon) continue;
				jipQuery = jip.openSynchronousQuery(parser.parseTerm("next(" + cn.inc_num + ", INC_NUM2, J2, DT, DIST)."));
				term = jipQuery.nextSolution();
				while (term != null) {
					int INC_NUM2 = Integer.parseInt(term.getVariablesTable().get("INC_NUM2").toString());
					double J2 = Double.parseDouble(term.getVariablesTable().get("J2").toString());
					double DT = Double.parseDouble(term.getVariablesTable().get("DT").toString());
					double DIST = Double.parseDouble(term.getVariablesTable().get("DIST").toString());
					double new_time = cn.time + DT;
					double new_dist = cn.dist + DIST;
					int cnb = INC_NUM2;
					double cd = G + J2;
					if (cd > D[cnb] + epsilon) {term = jipQuery.nextSolution(); continue;}
					
					if (cd + epsilon < D[cnb]) {
						best_paths.get(cnb).clear();
						best_paths.get(cnb).add(cn.inc_num);
						D[cnb] = cd;
						T[cnb] = new_time;
						Dist[cnb] = new_dist;
						PQ.add(getcnode(cnb, cd + H.get(cnb), new_time, new_dist));
						if (cnb == clid) thres = cd;
					}
					else best_paths.get(cnb).add(cn.inc_num);
					term = jipQuery.nextSolution();
				}
			}
			
			jipQuery = jip.openSynchronousQuery(parser.parseTerm("taxi_rank(" + TAXI_ID + ", " + T[clid] + ", RANK)."));
			term = jipQuery.nextSolution();
			double RANK = Double.parseDouble(term.getVariablesTable().get("RANK").toString());
			
			taxi_info.add(gettaxi(TAXI_ID, D[clid], T[clid], Dist[clid], RANK, 0));
			
			int colors = 0;
			
			Queue<Integer> Q = new LinkedList<Integer>();
			
			Q.add(clid);
			while(Q.size() > 0) {
				int hh = Q.remove();
				if (hh == tid) colors++;
				for (i = 0; i < best_paths.get(hh).size(); i++) {
					Q.add(best_paths.get(hh).get(i));
				}
			}
			
			writer.println(intro[tax]);
			
			int hh = clid;
			if (colors == 1) {
				while (true) {
					jipQuery = jip.openSynchronousQuery(parser.parseTerm("increasing_number_coordinates(" + hh + ", X, Y)."));
					term = jipQuery.nextSolution();
					double V_X = Double.parseDouble(term.getVariablesTable().get("X").toString());
					double V_Y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
					term = jipQuery.nextSolution();
					writer.println(V_X + "," + V_Y + ",0");
					if (hh == tid) break;
					hh = best_paths.get(hh).get(0);
				}
			}
			else {
				while (true) {
					jipQuery = jip.openSynchronousQuery(parser.parseTerm("increasing_number_coordinates(" + hh + ", X, Y)."));
					term = jipQuery.nextSolution();
					double V_X = Double.parseDouble(term.getVariablesTable().get("X").toString());
					double V_Y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
					writer.println(V_X + "," + V_Y + ",0");
					if (hh == tid) break;
					hh = best_paths.get(hh).get(0);
				}
				hh = clid;
				writer.println(outro);
				writer.println(intro[tax]);
				while (true) {
					jipQuery = jip.openSynchronousQuery(parser.parseTerm("increasing_number_coordinates(" + hh + ", X, Y)."));
					term = jipQuery.nextSolution();
					double V_X = Double.parseDouble(term.getVariablesTable().get("X").toString());
					double V_Y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
					writer.println(V_X + "," + V_Y + ",0");
					if (hh == tid) break;
					hh = best_paths.get(hh).get(best_paths.get(hh).size() - 1);
				}
			}
			writer.println(outro);
			
		}
		
		writer.println("</Document>");
		writer.println("</kml>");
		writer.close();
		
		System.out.println("A* done.");
		
		String style = "<Style id=\"blue\">%n<LineStyle>%n<color>50F05014</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		
		
		String introd = "<Placemark>%n<name>To destination</name>%n<styleUrl>#blue</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
		
		String outrod="</coordinates>%n</LineString>%n</Placemark>";
		
		writer = new PrintWriter(doc_dest_kml_name, "UTF-8");
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<kml xmlns=\"http://earth.google.com/kml/2.1\">");
		writer.println("<Document>");
		writer.println("<name>Client - Destination route</name>");
		
		jipQuery = jip.openSynchronousQuery(parser.parseTerm("dist_from_dest(INC_NUM, DIST)."));
		term = jipQuery.nextSolution();
		L = 0;
		
		while (term != null) {
			double DIST = Double.parseDouble(term.getVariablesTable().get("DIST").toString());
			int INC_NUM = Integer.parseInt(term.getVariablesTable().get("INC_NUM").toString());
			H.set(L++, 0.65 * DIST + 10.46 * DIST / 100);  // under-estimation of cost (heuristic)
			term = jipQuery.nextSolution();
		}
		
		double[] D = new double[L];
		for (i = 0; i < L; i++) D[i] = Double.POSITIVE_INFINITY;
		double[] T = new double[L];
		for (i = 0; i < L; i++) T[i] = -1;
		double[] Dist = new double[L];
		for (i = 0; i < L; i++) Dist[i] = -1;
		int INC_NUM = clid;
		writer.println(style);
		D[INC_NUM] = 0;
		
		PriorityQueue<Cnode> PQ = new PriorityQueue<>();  // metopo anazitisis
		
		PQ.add(getcnode(INC_NUM, H.get(INC_NUM), 0.0, 0.0));
		
		ArrayList<ArrayList<Integer>> best_paths = new ArrayList<ArrayList<Integer>>();
		for (i = 0; i < L; i++) {
			best_paths.add(new ArrayList<>());
		}
		
		Cnode cn;
		
		double thres = Double.POSITIVE_INFINITY;
		
		int w = 0;
		
		// Cnode getcnode(int inc_num, double F, double time)
		// F = G + H
		// H: Heuristic
		// G: Path till now
		
		while ((cn = PQ.poll()) != null) {
			w++;
			double G = cn.F - H.get(cn.inc_num);
			if (G > D[cn.inc_num] + epsilon || G >= thres + epsilon) continue;
			jipQuery = jip.openSynchronousQuery(parser.parseTerm("next(" + cn.inc_num + ", INC_NUM2, J2, DT, DIST)."));
			term = jipQuery.nextSolution();
			while (term != null) {
				int INC_NUM2 = Integer.parseInt(term.getVariablesTable().get("INC_NUM2").toString());
				double J2 = Double.parseDouble(term.getVariablesTable().get("J2").toString());
				double DT = Double.parseDouble(term.getVariablesTable().get("DT").toString());
				double DIST = Double.parseDouble(term.getVariablesTable().get("DIST").toString());
				double new_time = cn.time + DT;
				double new_dist = cn.dist + DIST;
				int cnb = INC_NUM2;
				double cd = G + J2;
				if (cd > D[cnb] + epsilon) {term = jipQuery.nextSolution(); continue;}
				
				if (cd + epsilon < D[cnb]) {
					best_paths.get(cnb).clear();
					best_paths.get(cnb).add(cn.inc_num);
					D[cnb] = cd;
					T[cnb] = new_time;
					Dist[cnb] = new_dist;
					PQ.add(getcnode(cnb, cd + H.get(cnb), new_time, new_dist));
					if (cnb == destid) thres = cd;
				}
				else best_paths.get(cnb).add(cn.inc_num);
				term = jipQuery.nextSolution();
			}
		}
		
		int colors = 0;
		
		Queue<Integer> Q = new LinkedList<Integer>();
		
		Q.add(destid);
		while(Q.size() > 0) {
			int hh = Q.remove();
			if (hh == clid) colors++;
			for (i = 0; i < best_paths.get(hh).size(); i++) {
				Q.add(best_paths.get(hh).get(i));
			}
		}
		
		writer.println(introd);
		
		int hh = destid;
		if (colors == 1) {
			while (true) {
				jipQuery = jip.openSynchronousQuery(parser.parseTerm("increasing_number_coordinates(" + hh + ", X, Y)."));
				term = jipQuery.nextSolution();
				double V_X = Double.parseDouble(term.getVariablesTable().get("X").toString());
				double V_Y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
				term = jipQuery.nextSolution();
				writer.println(V_X + "," + V_Y + ",0");
				if (hh == clid) break;
				hh = best_paths.get(hh).get(0);
			}
		}
		else {
			while (true) {
				jipQuery = jip.openSynchronousQuery(parser.parseTerm("increasing_number_coordinates(" + hh + ", X, Y)."));
				term = jipQuery.nextSolution();
				double V_X = Double.parseDouble(term.getVariablesTable().get("X").toString());
				double V_Y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
				writer.println(V_X + "," + V_Y + ",0");
				if (hh == clid) break;
				hh = best_paths.get(hh).get(0);
			}
			hh = destid;
			writer.println(outrod);
			writer.println(introd);
			while (true) {
				jipQuery = jip.openSynchronousQuery(parser.parseTerm("increasing_number_coordinates(" + hh + ", X, Y)."));
				term = jipQuery.nextSolution();
				double V_X = Double.parseDouble(term.getVariablesTable().get("X").toString());
				double V_Y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
				writer.println(V_X + "," + V_Y + ",0");
				if (hh == clid) break;
				hh = best_paths.get(hh).get(best_paths.get(hh).size() - 1);
			}
		}
		writer.println(outrod);
		
		writer.println("</Document>");
		writer.println("</kml>");
		writer.close();
		
		// Getting k
		System.out.print("\nPlease insert k (number of taxis whose info will be displayed). k = ");
		Scanner console = new Scanner(System.in);
		String k_str = console.nextLine();
		int k = Integer.parseInt(k_str);
		int lim = taxi_info.size();
		if (k < lim) lim = k;
		
		// Prepare and show first taxi recommendation (closest taxi)
		System.out.println("\nRank by proximity:");
		Collections.sort(taxi_info);
		for (i = 0; i < lim; i++) {
			Taxi cur_taxi = taxi_info.get(i);
			System.out.println("Taxi #" + cur_taxi.id + ":");
			System.out.printf("\tRanking = %.2f\n", cur_taxi.ranking);
			double total_cost = 1.14 + D[destid] + 0.38 * LUGGAGE;
			if (total_cost < 3.27) total_cost = 3.27;
			System.out.printf("\tCost = %.2f Euros\n", total_cost);
			Double hours_d = cur_taxi.waiting_time;
			int hours = hours_d.intValue();
			Double minutes_d = hours_d % 1 * 60;
			int minutes = minutes_d.intValue();
			String str = "\tWaiting time = ";
			if (hours > 0) str += hours + " hours ";
			if (minutes > 0) str += minutes + " minutes";
			System.out.println(str);
			hours_d = T[destid];
			hours = hours_d.intValue();
			minutes_d = hours_d % 1 * 60;
			minutes = minutes_d.intValue();
			str = "\tTime to destination = ";
			if (hours > 0) str += hours + " hours ";
			if (minutes > 0) str += minutes + " minutes";
			System.out.println(str);
			hours_d = cur_taxi.waiting_time + T[destid];
			hours = hours_d.intValue();
			minutes_d = hours_d % 1 * 60;
			minutes = minutes_d.intValue();
			str = "\tTotal time = ";
			if (hours > 0) str += hours + " hours ";
			if (minutes > 0) str += minutes + " minutes";
			System.out.println(str);
			System.out.printf("\tDistance = %.3f Km\n", cur_taxi.dist);
		}
		
		System.out.println("------------------------------------------------");
		
		// Prepare and show second taxi recommendation (best taxi)
		System.out.println("\nRank by optimality:");
		while (taxi_info.size() > k) taxi_info.remove(taxi_info.size() - 1);
		for (i = 0; i < taxi_info.size(); i++) taxi_info.set(i, change_sort_criteria(taxi_info.get(i), 1));
		Collections.sort(taxi_info);
		for (i = 0; i < lim; i++) {
			Taxi cur_taxi = taxi_info.get(i);
			System.out.println("Taxi #" + cur_taxi.id + ":");
			System.out.printf("\tRanking = %.2f\n", cur_taxi.ranking);
			double total_cost = 1.14 + D[destid] + 0.38 * LUGGAGE;
			if (total_cost < 3.27) total_cost = 3.27;
			System.out.printf("\tCost = %.2f Euros\n", total_cost);
			Double hours_d = cur_taxi.waiting_time;
			int hours = hours_d.intValue();
			Double minutes_d = hours_d % 1 * 60;
			int minutes = minutes_d.intValue();
			String str = "\tWaiting time = ";
			if (hours > 0) str += hours + " hours ";
			if (minutes > 0) str += minutes + " minutes";
			System.out.println(str);
			hours_d = T[destid];
			hours = hours_d.intValue();
			minutes_d = hours_d % 1 * 60;
			minutes = minutes_d.intValue();
			str = "\tTime to destination = ";
			if (hours > 0) str += hours + " hours ";
			if (minutes > 0) str += minutes + " minutes";
			System.out.println(str);
			hours_d = cur_taxi.waiting_time + T[destid];
			hours = hours_d.intValue();
			minutes_d = hours_d % 1 * 60;
			minutes = minutes_d.intValue();
			str = "\tTotal time = ";
			if (hours > 0) str += hours + " hours ";
			if (minutes > 0) str += minutes + " minutes";
			System.out.println(str);
			System.out.printf("\tDistance = %.3f Km\n", cur_taxi.dist);
		}
	}

	public static void main(String args[]) throws IOException {
		jip = new JIPEngine();
		parser = jip.getTermParser();
		
		System.out.print("Consulting Prolog files... ");
		
		progress(0);
		jip.consultFile("client.pl");
		progress(1);
		jip.consultFile("taxi.pl");
		progress(3);
		jip.consultFile("adequate.pl");
		progress(5);
		jip.consultFile("rules.pl");
		progress(15);
		jip.consultFile("node_id.pl");
		progress(35);
		jip.consultFile("nodeX.pl");
		progress(55);
		jip.consultFile("nodeY.pl");
		progress(75);
		jip.consultFile("next.pl");
		progress(100);
		// The following Prolog files are not necessary for this Java program
		//jip.consultFile("no_light.pl");
		//jip.consultFile("has_toll.pl");
		//jip.consultFile("incline.pl");
		//jip.consultFile("increasing_number_line_id.pl");
		//jip.consultFile("is_road.pl");
		//jip.consultFile("line_one_way.pl");
		//jip.consultFile("speed_limit.pl");
		//jip.consultFile("traffic.pl");
		System.out.println("\nConsults done!");
		
		solver(1934, 9353, "doc.kml", "doc_dest.kml");
		
		System.out.println("\nPartial reconsulting...");
		jip.unconsultFile("adequate.pl");
		jip.consultFile("adequate_custom.pl");
		jip.unconsultFile("next.pl");
		jip.consultFile("next_custom.pl");
		jip.unconsultFile("client.pl");
		jip.consultFile("client_custom.pl");
		jip.unconsultFile("taxi.pl");
		jip.consultFile("taxi_custom.pl");
		System.out.println("\nPartial reconsulting done!");
		
		solver(2366, 59261, "doc_custom.kml", "doc_dest_custom.kml");
		
		Scanner console = new Scanner(System.in);
		System.out.println("Successful termination! (Press Enter to exit)");
		console.nextLine();
	}
}
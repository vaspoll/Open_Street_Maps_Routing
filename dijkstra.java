import static java.lang.System.out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

// Java Program to show segment tree operations like construction, 
// query and update 
// Java code for range maximum query and updates 


class GFG 
{ 

	static int getMid(int s, int e) 
	{ 
		return s + (e - s) / 2; 
	} 

	
	static double MaxUtil(double[] st, int ss, int se, 
						int l, int r, int node) 
	{ 
		
		if (l <= ss && r >= se) 
			return st[node]; 

		if (se < l || ss > r) 
			return -1; 

		int mid = getMid(ss, se); 

		return Math.max(MaxUtil(st, ss, mid, l, r, 2 * node + 1), 
					MaxUtil(st, mid + 1, se, l, r, 2 * node + 2)); 
	} 

	
	static void updateValue(double arr[], double[] st, int ss, 
					int se, int index, double value, int node) 
	{ 
		if (index < ss || index > se) 
		{ 
			out.println("Invalid Input"); 
			return; 
		} 

		if (ss == se) 
		{ 

			arr[index] = value; 
			st[node] = value; 
		} 
		else
		{ 
			int mid = getMid(ss, se); 

			if (index >= ss && index <= mid) 
				updateValue(arr, st, ss, mid, index, value, 2 * node + 1); 
			else
				updateValue(arr, st, mid + 1, se, index, value, 2 * node + 2); 

			st[node] = Math.max(st[2 * node + 1], st[2 * node + 2]); 
		} 
		return; 
	} 

	static double getMax(double [] st, int n, int l, int r) 
	{ 

		if (l < 0 || r > n - 1 || l > r) 
		{ 
			out.printf("Invalid Input\n"); 
			return -1; 
		} 

		return MaxUtil(st, 0, n - 1, l, r, 0); 
	} 

	static double constructSTUtil(double  arr[], int ss, 
						int se, double[] st, int si) 
	{ 
		 
		if (ss == se) 
		{ 
			st[si] = arr[ss]; 
			return arr[ss]; 
		} 

		
		int mid = getMid(ss, se); 

		st[si] = Math.max(constructSTUtil(arr, ss, mid, st, si * 2 + 1), 
				constructSTUtil(arr, mid + 1, se, st, si * 2 + 2)); 

		return st[si]; 
	} 

	
	static double[] constructST(double arr[], int n) 
	{ 

		// Height of segment tree 
		int x = (int) Math.ceil(Math.log(n) / Math.log(2)); 

		// Maximum size of segment tree 
		int max_size = 2 * (int) Math.pow(2, x) - 1; 

		// Allocate memory 
		double[] st = new double[max_size]; 

		// Fill the allocated memory st 
		constructSTUtil(arr, 0, n - 1, st, 0); 

		// Return the constructed segment tree 
		return st; 
	} 
 		
		
} 

// This code is contributed by 
// sanjeev2552 


class Location { 
	public final double x; 
	public final double y; 
	public Location(double x, double y) { 
	  this.x = x; 
	  this.y = y; 
	} 
}

class Cnode implements Comparable<Cnode>
{  
	public int inc_num;
	public double time;
	public int compareTo(Cnode o) { // pif
		if (time > o.time) return(1);
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

public class dijkstra {

	private static final double epsilon = 0.0000000001;
	
	private static int saved_progress = -1;
	
	public static void progress(int new_progress) {
		if (saved_progress == 0) out.print("\b\b");
		else if (saved_progress > 0) {
			out.print("\b");
			while (saved_progress > 0) {
				out.print("\b");
				saved_progress /= 10;
			}
		}
		out.print(new_progress + "%");
		saved_progress = new_progress;
	}
		
	public static Cnode getcnode(int inc_num, double time) {
		Cnode res = new Cnode();
		res.inc_num = inc_num;
		res.time = time;
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
	
	public static void solver(int clid, int destid, String doc_kml_name, String doc_dest_kml_name, ArrayList<Location> locations) throws IOException {
		int i;
		
		out.println("Executing Dijkstra...");
		
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
		// locations = arg
		// locations = [(x,y), ...]
		// out.println("\nlocations size=" + locations.size());
		
		// from locations to nearest node in map
		// location_nodes = input of dijktra algorithm


		ArrayList<Integer> location_nodes = new ArrayList<Integer>();
		int locations_size=locations.size();
		locations_size =1;
		for (int j = 0; j < locations_size; j++) {
			Location curLocation = locations.get(j);
			out.print("curLocation.x="+curLocation.x+"\n");
			jipQuery = jip.openSynchronousQuery(parser.parseTerm("closest_node("+curLocation.x+","+ curLocation.y+",INC_NUM)."));

			jipQuery = jip.openSynchronousQuery(parser.parseTerm("closest_node(37.907248,23.755695,INC_NUM)."));

			term = jipQuery.nextSolution();
			out.print("closest_node="+term+'\n');
			out.print("closest_node("+curLocation.x+","+ curLocation.y+",INC_NUM).\n");
			int INC_NUM = Integer.parseInt(term.getVariablesTable().get("INC_NUM").toString());
			location_nodes.add(INC_NUM);
		}
		location_nodes.sort(null);
		HashSet<Integer> location_nodes_set = new HashSet<Integer>(location_nodes);
		// location_node_distances = output of dijktra algorithm
		double[][] location_node_distances = new double[locations_size][locations_size];
		// n times dijktra implementation
		for (int j = 0; j < locations_size; j++) {
			int tid = j;
			writer.println(styles[j%10]);
			double[] Times = new double[L];
			int[] Prev = new int[L];
			for (i = 0; i < L; i++) {
				Times[i] = Double.POSITIVE_INFINITY; 
				Prev[i] = -1;
			}

			Dictionary<Integer, Integer> location_map = new Hashtable<Integer, Integer>(); 
			for (int l= 0; l < locations_size; l++){
				location_map.put(location_nodes.get(l),l);
			}
				
			double[] location_times = new double[L];
			for (i = 0; i < locations_size; i++) {
				location_times[i] = Double.POSITIVE_INFINITY; 
			}
			
			double[] st = GFG.constructST(location_times, locations_size);
 

			int INC_NUM = location_nodes.get(j);
			Times[INC_NUM] = 0;
			GFG.updateValue(location_times, st, 0, locations_size - 1, location_map.get(INC_NUM),0, 0);
			double thres  = GFG.getMax(st, locations_size, 0, locations_size-1);
			
			PriorityQueue<Cnode> PQ = new PriorityQueue<>();  // metopo anazitisis
			
			PQ.add(getcnode(INC_NUM, 0.0));
			
			
			Cnode cn;
			int w = 0;
			
			// Cnode getcnode(int inc_num, double F, double time)
			
			while ((cn = PQ.poll()) != null) {
				w++;
				double T = cn.time;
				if (T > Times[cn.inc_num] + epsilon || T >= thres + epsilon) continue;
				jipQuery = jip.openSynchronousQuery(parser.parseTerm("next(" + cn.inc_num + ", INC_NUM2, J2, DT, DIST)."));
				term = jipQuery.nextSolution();
				while (term != null) {
					int INC_NUM2 = Integer.parseInt(term.getVariablesTable().get("INC_NUM2").toString());
					double DT = Double.parseDouble(term.getVariablesTable().get("DT").toString());
					// double DIST = Double.parseDouble(term.getVariablesTable().get("DIST").toString());
					double new_time = T + DT;
					// double new_dist = cn.dist + DIST;
					int cnb = INC_NUM2;
					if (new_time > Times[cnb] + epsilon) {term = jipQuery.nextSolution(); continue;}
					
					if (new_time + epsilon < Times[cnb]) {
						Times[cnb] = new_time;
						Prev[cnb] = cn.inc_num; 
						PQ.add(getcnode(cnb, new_time));

						if (location_nodes_set.contains(cnb)){
							GFG.updateValue(location_times, st, 0, locations_size - 1, location_map.get(cnb), new_time, 0);
							thres = GFG.getMax(st, locations_size, 0, locations_size-1); 
						} 
					}
					term = jipQuery.nextSolution();
				}
				
			}

			for (i = 0; i < locations_size; i++) {
				location_node_distances[j][i] = location_times[i]; 
			}
			
			
			int colors = 1;
			
			writer.println(intro[j]);
			
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
					// hh = best_paths.get(hh).get(0);
				}
			}
			
			writer.println(outro);
			
		}
		
		writer.println("</Document>");
		writer.println("</kml>");
		writer.close();
		
		out.println("dijkstra done.");
	}
	public static void main(String args[]) throws IOException {
		// locations = args
		// read the location coordinates from input
		// for(int i = 0; i < args.length; i++) {
		// 	out.println(args[i]);
		// }
		ArrayList<Location> locations = new ArrayList<Location>();
		try {
			File myObj = new File(args[0]);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				out.println(data);
				String[] parts=data.split(" ");
				double x = Double.parseDouble(parts[0]);
				double y = Double.parseDouble(parts[1]);
				// add the locations to the array
				locations.add(new Location(x,y));
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			out.println("An error occurred while reading locations.");
			e.printStackTrace();
		}
		
		jip = new JIPEngine();
		parser = jip.getTermParser();
		
		out.print("Consulting Prolog files... ");
		
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
		jip.consultFile("increasing_number_line_id.pl");
		//jip.consultFile("is_road.pl");
		//jip.consultFile("line_one_way.pl");
		//jip.consultFile("speed_limit.pl");
		//jip.consultFile("traffic.pl");
		out.println("\nConsults done!");
		
		solver(1934, 9353, "doc.kml", "doc_dest.kml",locations);
		
		Scanner console = new Scanner(System.in);
		out.println("Successful termination! (Press Enter to exit)");
		console.nextLine();
	}
}

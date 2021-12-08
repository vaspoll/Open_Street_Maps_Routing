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
import java.util.PriorityQueue ;

class Point implements Comparable<Point>
{
    public double x;
    public double y;
    public int compareTo(Point o) {
    if (x < o.x) return(-1);
	if (x > o.x) return(1);
    if (y < o.y) return(-1);
	if (y > o.y) return(1);
	return(0);
    }
};



class Node implements Comparable<Node>
{
	public int inc_num;
    public double J;
	public double time;
    public int compareTo(Node o) {
    
    return(42); //pif
    }


};

class Taxi
{
	public Point p;
	public int id;
};

class Client
{
	public Point p;
	
};

class Cnode implements Comparable<Cnode>
{  
	public int inc_num;
    public double F;

	public int compareTo(Cnode o) { // pif
		if (F > o.F) return(1);
		return(-1);
	}
};

public class A_star {

	private static final double epsilon = 0.0000000001;
	
	private static int[] LIST = new int[50];
	private static int len = 0;
	
	private static String get_list() {
		if (len == 0) return("[]");
		String res = "[" + LIST[0];
		for (int i = 1; i < len; i++) res += ", " + LIST[i];
		return(res + "]");
	}
		
	public static Cnode getcnode(int inc_num, double F) {
		Cnode res = new Cnode();
		res.inc_num = inc_num;
		res.F = F;
		return(res);
	}
	
	public static double dist(Point p1, Point p2) {
		return(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)));
	}

	
	public static A_star_fun(int from_cl_to_dest, int clid, int tid, int dest, String file_name, String style, String intro){
		int i;
		JIPQuery jipQuery, jipQuery1;
		JIPTerm term, term1;
		
		System.out.println("Checkpoint #1");
		
		JIPEngine jip = new JIPEngine();
		jip.consultFile("rules.pl");
		jip.consultFile("next.pl");
		jip.consultFile("adequate.pl");
		jip.consultFile("client.pl");
		jip.consultFile("no_light.pl");
		jip.consultFile("has_toll.pl");
		jip.consultFile("incline.pl");
		jip.consultFile("increasing_number_line_id.pl");
		jip.consultFile("is_road.pl");
		jip.consultFile("line_one_way.pl");
		jip.consultFile("node_id.pl");
		jip.consultFile("nodeX.pl");
		jip.consultFile("nodeY.pl");
		jip.consultFile("speed_limit.pl");
		jip.consultFile("taxi.pl");
		jip.consultFile("traffic.pl");
		
		
		System.out.println("Checkpoint #2");
		
		JIPTermParser parser = jip.getTermParser();
		JIPTermParser parser1 = jip.getTermParser();
		
		
		String outro="</coordinates>%n</LineString>%n</Placemark>";
		
		PrintWriter writer = new PrintWriter(file_name, "UTF-8");
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<kml xmlns=\"http://earth.google.com/kml/2.1\">");
		writer.println("<Document>");
		writer.println("<name>Taxi Routes</name>");
		
		ArrayList<Double> H = new ArrayList<Double>();
		int L = 0;
		if(clid == 1){
			jipQuery = jip.openSynchronousQuery(parser.parseTerm("dist_from_client1(INC_NUM, DIST)."));
			term = jipQuery.nextSolution();
			while (term != null) {
				L++;
				double DIST = Double.parseDouble(term.getVariablesTable().get("DIST").toString());
				int INC_NUM = Integer.parseInt(term.getVariablesTable().get("INC_NUM").toString());
				H.add(DIST);
				term = jipQuery.nextSolution();
			}
		}
		else if(clid == 2){
			jipQuery = jip.openSynchronousQuery(parser.parseTerm("dist_from_client2(INC_NUM, DIST)."));
			term = jipQuery.nextSolution();
			while (term != null) {
				L++;
				double DIST = Double.parseDouble(term.getVariablesTable().get("DIST").toString());
				int INC_NUM = Integer.parseInt(term.getVariablesTable().get("INC_NUM").toString());
				H.add(DIST);
				term = jipQuery.nextSolution();
			}
		}
		
		
		
		System.out.println("Checkpoint #3");
		
		if(clid == 1){
			jipQuery = jip.openSynchronousQuery(parser.parseTerm("client1(X, Y, X_DEST, Y_DEST, TIME, PERSONS, LANGUAGE, LUGGAGE)."));
			term = jipQuery.nextSolution();
		}
		else if(clid == 2){
			jipQuery = jip.openSynchronousQuery(parser.parseTerm("client2(X, Y, X_DEST, Y_DEST, TIME, PERSONS, LANGUAGE, LUGGAGE)."));
			term = jipQuery.nextSolution();
		}
		
		double START_TIME = Double.parseDouble(term.getVariablesTable().get("TIME").toString());
		double X_CL = Double.parseDouble(term.getVariablesTable().get("X").toString());
		double Y_CL = Double.parseDouble(term.getVariablesTable().get("Y").toString());
		
		System.out.println("Checkpoint #4");
		System.out.println(X_CL + " " + Y_CL + " " + START_TIME);
		
		System.out.println("Checkpoint #5");
		
		int j = tid;	
		double[] D = new double[L];
		for (i = 0; i < L; i++) D[i] = Double.POSITIVE_INFINITY;
		int TAXI_ID = adequate_taxi_ids.get(j);
		int INC_NUM = adequate_taxi_inc_num.get(j);
		int tid = INC_NUM;
		int tax = TAXI_ID / 10 - 10; 
		
		if(from_cl_to_dest == 1){
			INC_NUM = destid;
		}
		
		writer.println(style);
		
		D[INC_NUM] = 0;
		
		PriorityQueue<Cnode> PQ = new PriorityQueue<>();  // metopo anazitisis
		
				
		PQ.add(getcnode(INC_NUM, H.get(INC_NUM)));
			
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
		
		System.out.println("Checkpoint #7");
		
		while ((cn = PQ.poll()) != null) {
			w++;
			double G = cn.F - H.get(cn.inc_num);
			if (G > D[cn.inc_num] + epsilon || G >= thres + epsilon) continue;
			//System.out.println("next(" + cn.inc_num + ", INC_NUM2, J2).");
			jipQuery = jip.openSynchronousQuery(parser.parseTerm("next(" + cn.inc_num + ", INC_NUM2, J2)."));
			term = jipQuery.nextSolution();
			//System.out.println("Checkpoint #8");
			while (term != null) {
				int INC_NUM2 = Integer.parseInt(term.getVariablesTable().get("INC_NUM2").toString());
				double J2 = Double.parseDouble(term.getVariablesTable().get("J2").toString());
				//System.out.println(INC_NUM2);
				int cnb = INC_NUM2;
				double cd = G + J2;
				if (cd > D[cnb] + epsilon) {term = jipQuery.nextSolution(); continue;}
				
				if (cd + epsilon < D[cnb]) {
					best_paths.get(cnb).clear();
					best_paths.get(cnb).add(cn.inc_num);
					D[cnb] = cd;
					PQ.add(getcnode(cnb, cd + H.get(cnb)));
					if (cnb == clid) thres = cd;
				}
				else best_paths.get(cnb).add(cn.inc_num);
				term = jipQuery.nextSolution();
			}
			
		}
		
        if(from_cl_to_dest == 0){		
			System.out.println("Taxi " + tax + ": dist = " + D[clid] + ", " + thres + " " + w);
		}
		
		int colors = 0;
		
		Queue<Integer> Q = new LinkedList<Integer>();
		
		Q.add(clid);
		while(Q.size() > 0) {
			int hh = Q.remove();
			if (((from_cl_to_dest == 0)&&(hh == tid))||((from_cl_to_dest == 1)&&(hh == destid))) colors++;
			for (i = 0; i < best_paths.get(hh).size(); i++) {
				Q.add(best_paths.get(hh).get(i));
			}
		}
		
		writer.println(intro);
		System.out.println(colors);
		
		int hh = clid;
		if (colors == 1) {
			while (true) {
				jipQuery1 = jip.openSynchronousQuery(parser1.parseTerm("increasing_number_coordinates(" + hh + ", X, Y)."));
				term1 = jipQuery1.nextSolution();
				double V_X = Double.parseDouble(term1.getVariablesTable().get("X").toString());
				double V_Y = Double.parseDouble(term1.getVariablesTable().get("Y").toString());
				term1 = jipQuery1.nextSolution();
				writer.println(V_X + "," + V_Y + ",0");
				if (((from_cl_to_dest == 0)&&(hh == tid))||((from_cl_to_dest == 1)&&(hh == destid))) break;
				hh = best_paths.get(hh).get(0);
			}
		}
		else {
			while (true) {
				jipQuery1 = jip.openSynchronousQuery(parser1.parseTerm("increasing_number_coordinates(" + hh + ", X, Y)."));
				term1 = jipQuery1.nextSolution();
				double V_X = Double.parseDouble(term1.getVariablesTable().get("X").toString());
				double V_Y = Double.parseDouble(term1.getVariablesTable().get("Y").toString());
				writer.println(V_X + "," + V_Y + ",0");
				if (((from_cl_to_dest == 0)&&(hh == tid))||((from_cl_to_dest == 1)&&(hh == destid))) break;
				hh = best_paths.get(hh).get(0);
			}
			hh = clid;
			writer.println(outro);
			
	
		writer.println("</Document>");
		writer.println("</kml>");
		writer.close();
		
		
	}
}
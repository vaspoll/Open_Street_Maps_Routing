import static java.lang.System.out;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

import java.io.IOException;

public class visualization {
    public static JIPQuery jipQuery;
	public static JIPTerm term;
	public static JIPEngine jip;
    public static JIPTermParser parser;
    
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


    // A Java program to print Eulerian circuit in given
    // directed graph using Hierholzer algorithm
    public  static void hierholzers(Vector< Vector<Integer> > adj, PrintWriter writer) 
    { 
        // adj represents the adjacency list of 
        // the directed graph 
        // edge_count represents the number of edges 
        // emerging from a vertex 
        HashMap<Integer,Integer> edge_count = new HashMap<Integer,Integer>(); 

        for (int i=0; i<adj.size(); i++) 
        { 
            //find the count of edges to keep track 
            //of unused edges 
            edge_count.put(i,adj.get(i).size()); 
        } 

        if (adj.size() == 0) 
            return; //empty graph 

        // Maintain a stack to keep vertices 
        Stack<Integer> curr_path = new Stack<Integer>(); 

        // vector to store final circuit 
        Vector<Integer> circuit = new Vector<Integer>(); 

        // start from any vertex 
        curr_path.push(0); 
        int curr_v = 0; // Current vertex 

        while (!curr_path.empty()) 
        { 
            // If there's remaining edge 
            if (edge_count.get(curr_v) != 0) 
            { 
                // Push the vertex 
                curr_path.push(curr_v); 

                // Find the next vertex using an edge 
                int next_v = adj.get(curr_v).get(adj.get(curr_v).size()-1); 

                // and remove that edge 
                edge_count.put(curr_v,edge_count.get(curr_v)-1); 
                adj.get(curr_v).remove(adj.get(curr_v).size()-1);
                // Move to next vertex 
                curr_v = next_v; 
            } 

            // back-track to find remaining circuit 
            else
            { 
                circuit.add(curr_v); 

                // Back-tracking 
                curr_v = curr_path.peek(); 
                curr_path.pop(); 
            } 
        } 

        // we've got the circuit, now print it in reverse 
        for (int i=circuit.size()-1; i>=0; i--) 
        { 
            /* get_coordinates():   --prolog query
            input: node_id
            output: tuple (x,y)
            */
            // out.println(circuit.get(i)); 
            jipQuery = jip.openSynchronousQuery(parser.parseTerm("increasing_number_coordinates(" + circuit.get(i) + ", X, Y)."));
            term = jipQuery.nextSolution();
            double X = Double.parseDouble(term.getVariablesTable().get("X").toString());
            double Y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
            writer.println(X + "," + Y + ",0");
        } 
    } 

    public static void solver(String kml_name)  throws IOException  {

        out.println("Executing Visualization...");
        //initializations
        String style = "<Style id=\"blue\">%n<LineStyle>%n<color>50F05014</color>%n<width>4</width>%n</LineStyle>%n</Style>";
		String introd = "<Placemark>%n<name>To destination</name>%n<styleUrl>#blue</styleUrl>%n<LineString>%n<altitudeMode>relative</altitudeMode>%n<coordinates>";
        String outrod="</coordinates>%n</LineString>%n</Placemark>";

        
        PrintWriter writer = new PrintWriter(kml_name, "UTF-8");
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<kml xmlns=\"http://earth.google.com/kml/2.1\">");
        writer.println("<Document>");
        writer.println("<name>Node - Euler</name>");
        writer.println(style);
        writer.println(introd);
        
        /* Create Adjacency list Graph */
        
        Vector< Vector<Integer> > adj = new Vector< Vector<Integer> >();
        for(int i=0; i<154404; i++)adj.add(new Vector<Integer>());
        
        jipQuery = jip.openSynchronousQuery(parser.parseTerm("next(A,B,C,D,E)."));
        while(true){
                     
            term = jipQuery.nextSolution();
            if(term == null)break;
            int a = Integer.parseInt(term.getVariablesTable().get("A").toString());
            int b = Integer.parseInt(term.getVariablesTable().get("B").toString());
            adj.get(a).add(b);
        }

        /* hierholzers-algorithm: 
        input: vector< vector<int> > adj1, adj2; --Adjacency list
        output: list -- etc [0,1,2,0] which means 0 -> 1 -> 2 -> 0
        */
        hierholzers(adj, writer);

        writer.println(outrod);
        writer.println("</Document>");
        writer.println("</kml>");
        writer.close();

        
        out.println("visualization done.");
    }
    public static void main(String args[]) throws IOException  {

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


        solver("visualization.kml");
    }
}

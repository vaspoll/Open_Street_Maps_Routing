import java.io.*;
import java.util.*;

public class preprocessing4 {
    
    public static void main(String[] args) throws IOException {
        
        
        File file = new File("osm_nodes.csv");
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        int i = 0;
        int j, mini;
        
        OutputStream out1 = new FileOutputStream("nodeX.pl", false);
        Writer writer1 = new OutputStreamWriter(out1, "UTF-8");
        OutputStream out2 = new FileOutputStream("nodeY.pl", false);
        Writer writer2 = new OutputStreamWriter(out2, "UTF-8");
        OutputStream out3 = new FileOutputStream("increasing_number_line_id.pl", false);
        Writer writer3 = new OutputStreamWriter(out3, "UTF-8");
        OutputStream out4 = new FileOutputStream("node_id.pl", false);
        Writer writer4 = new OutputStreamWriter(out4, "UTF-8");
        
        
        /*
        X,Y,line_id,node_id,name
        
        nodeX(N_ID, X),
        nodeY(N_ID, Y),
        increasing_number_line_id(INC_NUM, L_ID),
        node_id(INC_NUM, N_ID)
        */
        HashMap<String, Integer> Set_P = new HashMap<>();
        for(String line = br.readLine(); line != null; line = br.readLine(), i++) {
            String[] parts = line.split(",");
            //System.out.println(parts[0]);
			String str = new String();
            if (!Set_P.containsKey(parts[3])) {
                str = "nodeX(" + parts[3] + ", " + parts[0] + ").";
                writer1.append(str);
                writer1.append('\n');
                writer1.flush();
                str = "nodeY(" + parts[3] + ", " + parts[1] + ").";
                writer2.append(str);
                writer2.append('\n');
                writer2.flush();
                Set_P.put(parts[3], 1);
            }
            str = "increasing_number_line_id(" + Integer.toString(i) + ", " + parts[2] + ").";
            writer3.append(str);
            writer3.append('\n');
            writer3.flush();
            str = "node_id(" + Integer.toString(i) + ", " + parts[3] + ").";
            writer4.append(str);
            writer4.append('\n');
            writer4.flush();
            
            
        }
        
        
        
        
        
        file = new File("osm_lines.csv");
        br = new BufferedReader(new FileReader(file));
        br.readLine();
        i = 0;
        OutputStream out5 = new FileOutputStream("is_road.pl", false);
        Writer writer5 = new OutputStreamWriter(out5, "UTF-8");
        OutputStream out6 = new FileOutputStream("line_one_way.pl", false);
        Writer writer6 = new OutputStreamWriter(out6, "UTF-8");
        OutputStream out7 = new FileOutputStream("no_light.pl", false);
        Writer writer7 = new OutputStreamWriter(out7, "UTF-8");
        OutputStream out8 = new FileOutputStream("speed_limit.pl", false);
        Writer writer8 = new OutputStreamWriter(out8, "UTF-8");
        OutputStream out9 = new FileOutputStream("incline.pl", false);
        Writer writer9 = new OutputStreamWriter(out9, "UTF-8");
        OutputStream out10 = new FileOutputStream("has_toll.pl", false);
        Writer writer10 = new OutputStreamWriter(out10, "UTF-8");
        
        
        /*
        id,highway,name,oneway,lit,lanes,maxspeed,railway,boundary,access,natural,barrier,tunnel,bridge,incline,waterway,busway,toll
        
        
        highway
        motorway =>70
        trunk  primary  secondary  tertiary   unclassified
        road   residential service  motorway_link
        trunk_link   primary_link   secondary_link
        tertiary_link   living_street
        
        
        oneway =-1 shmainei oneway alla pros thn antithteth kateuthunsh, oneway=yes shmainei oneway pros ta mpros oneway=no shmainei twoway
        oneway=yes (discouraged alternatives: "true", "1") not used
        oneway=no (discouraged alternatives: "false", "0") not used
        oneway=-1 (discouraged alternative: "reverse") not used
        
        lit disused no = den exei
        lanes : adiaforo
        maxspeed value=50 px
        railway oti kai na xei => oxi road
        boundary oti kai na xei => oxi road
        access "yes"; permissive
        natural  oti kai na xei => oxi road
        barrier oti kai na xei => oxi road
        tunnel adiaforo
        bridge adiaforo
        incline up / down / a% / upx% / downx%
        waterway oti kai na xei => oxi road
        busway   oti kai na xei => oxi road
        toll yes=> toll
        
        */
        
        for(String line = br.readLine(); line != null; line = br.readLine(), i++) {
            String[] parts = line.split(",", -1);
            //System.out.println(parts[0]);
            //System.out.println(line);
            int len = parts.length;
            //System.out.println(len);
            String id = parts[0];
            String highway = parts[1];
            String name = parts[2];
            String oneway = parts[len-15];
            String lit = parts[len-14];
            String lanes = parts[len-13];
            String maxspeed = parts[len-12];
            String railway = parts[len-11];
            String boundary = parts[len-10];
            String access = parts[len-9];
            String natural = parts[len-8];
            String barrier = parts[len-7];
            String tunnel = parts[len-6];
            String bridge = parts[len-5];
            String incline = parts[len-4];
            String waterway = parts[len-3];
            String busway = parts[len-2];
            String toll = parts[len-1];
            
            if((highway.equals("motorway") || highway.equals("trunk") || highway.equals("primary")
            || highway.equals("secondary") || highway.equals("tertiary")|| highway.equals("unclassified")
            || highway.equals("road") || highway.equals("residential")|| highway.equals("motorway_link")
            || highway.equals("trunk_link") || highway.equals("primary_link") || highway.equals("secondary_link")
            || highway.equals("tertiary_link") || highway.equals("living_street") || highway.isEmpty())&&(railway.isEmpty())
            &&(boundary.isEmpty())&&(natural.isEmpty())&&(barrier.isEmpty())&&(waterway.isEmpty())&&(busway.isEmpty())&&
            (access.equals("yes")||access.equals("permissive")||access.isEmpty())){
                String str = "is_road(" + id + ").";
                writer5.append(str);
                writer5.append('\n');
                writer5.flush();
            }
            
            
            
            if(!(oneway.isEmpty())){
                String direction="1";
                if(oneway.equals("no")||oneway.equals("false")||oneway.equals("0"))direction="2";
                else if(oneway.equals("yes")||oneway.equals("true")||oneway.equals("1"))direction="1";
                else if(oneway.equals("-1")||oneway.equals("reverse"))direction="-1";
                String str = "line_one_way(" + id + ", " + direction + ").";
                writer6.append(str);
                writer6.append('\n');
                writer6.flush();
            }
            
            if((!(lit.isEmpty()))&&(lit.equals("no")||lit.equals("disused"))){
                String str = "no_light(" + id + ").";
                writer7.append(str);
                writer7.append('\n');
                writer7.flush();
                
            }
            
            
            if(!(maxspeed.isEmpty())){
                String str = "speed_limit(" + id + ", " + maxspeed + ").";
                writer8.append(str);
                writer8.append('\n');
                writer8.flush();
            }
            else if(highway.equals("living_street")){
                String str = "speed_limit(" + id + ", " + "20" + ").";
                writer8.append(str);
                writer8.append('\n');
                writer8.flush();
            }
            
            if(!(incline.isEmpty())){
                String inc="5";
                if(incline.contains("%")){
                    inc="";
                    if(incline.contains("up")){
                        int t;
                        for(t = 3; t < incline.length()-1; t++){
                            inc = inc + incline.charAt(t);
                        }
                    }
                    else if(incline.contains("down")){
                        int t;
                        for(t = 5; t < incline.length()-1; t++){
                            inc = inc + incline.charAt(t);
                        }
                    }
                    else{
                        int t;
                        for(t = 0; t < incline.length()-1; t++){
                            inc = inc + incline.charAt(t);
                        }
                    }
                    
                }
                
                
                String str = "incline(" + id + ", " + inc + ").";
                writer9.append(str);
                writer9.append('\n');
                writer9.flush();
            }
            
            
            if((!(toll.isEmpty()))&&(toll.equals("yes"))){
                String str = "has_toll(" + id + ").";
                writer10.append(str);
                writer10.append('\n');
                writer10.flush();
                
            }
            
            
            /*is_road(L_ID),
            line_one_way(L_ID, DIRECTION),
            no_light(L_ID),
            speed_limit(L_ID, SPEED_LIMIT),
            incline(L_ID, INCLINATION),
            has_toll(L_ID), toll=yes*/
            
            
        }
    }
}
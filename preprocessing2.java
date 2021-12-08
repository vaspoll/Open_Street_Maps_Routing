import java.io.*;
import java.util.*;

public class preprocessing2 {
	
	public static int[] pow3 = new int[12];
	
	public static int get_traffic_bit(String str) {
		String str1 = new String();
		str1 += str.charAt(0);
		str1 += str.charAt(1);
		int first, hour_pair, traffic_weight = 0;
		first = Integer.parseInt(str1);
		hour_pair = first / 2;
		char last_char = str.charAt(str.length() - 1);
		if (last_char == 'm') traffic_weight = 1;
		else if (last_char == 'h') traffic_weight = 2;
		int cur_bit = traffic_weight * pow3[hour_pair];
		return(cur_bit);
	}
	
	public static String process_info(String str) {
		String[] info = str.split("\\|");
		int bitmask = 0;
		for (int i = 0; i < info.length; i++) {
			long cur_bit = get_traffic_bit(info[i]);
			bitmask += cur_bit;
		}
		return(String.valueOf(bitmask));
	}
    
    public static String parse_traffic(String str) {
        String[] data = str.split(",");
        String id, traffic_info;
        id = data[0];
        traffic_info = process_info(data[data.length - 1]);
        String traffic_str = "traffic(" + id + ", " + traffic_info + ").";
        return(traffic_str);
    }
    
    public static void make_traffic() throws IOException {
		pow3[0] = 1;
		for (int i = 1; i < 12; i++) pow3[i] = 3 * pow3[i - 1];
        Scanner scanner = new Scanner(new FileReader("traffic.csv"));
        OutputStream out_traffic = new FileOutputStream("traffic.pl", false);
        Writer writer_traffic = new OutputStreamWriter(out_traffic, "UTF-8");
        String line = scanner.nextLine();
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
			char last_char = line.charAt(line.length() - 1);
			if (last_char != 'h' && last_char != 'm' && last_char != 'l') continue;
            String traffic_str = parse_traffic(line);
            writer_traffic.append(traffic_str + System.lineSeparator());
            writer_traffic.flush();
        }
        scanner.close();
        out_traffic.close();
    }
    
    public static void main(String[] args) throws IOException {
        
        make_traffic();
    }
}
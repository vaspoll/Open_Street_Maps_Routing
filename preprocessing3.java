import java.io.*;
import java.util.*;

public class preprocessing3 {
    
    public static String remove_description(String str) {
        String res = new String();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\t') break;
            else res += str.charAt(i);
        }
        return(res);
    }
    
    public static String process_capacity(String str) {
        int p = 1;
        while (str.charAt(p++) != '-');
        String res = new String();
        for (int i = p; i < str.length(); i++) {
            res += str.charAt(i);
        }
        return(res);
    }
    
    public static String process_languages(String str) {
        String res = "[";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '|') res += ", ";
            else res += str.charAt(i);
        }
        res += ']';
        return(res);
    }
    
    public static boolean has_dot(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.') return(true);
        }
        return(false);
    }
    
    public static String parse_taxi(String str) {
		str = remove_description(str);
        String[] data = str.split(",");
        String X, Y, id, capacity, available, languages, rating, long_distance, type;
        int p = 0;
        X = data[p++];
        Y = data[p++];
        id = data[p++];
        available = data[p++];
        capacity = process_capacity(data[p++]);
        languages = process_languages(data[p++]);
        rating = data[p++];
        if (!has_dot(rating)) rating += '.' + data[p++];
        long_distance = data[p++];
        type = data[p++];
        String taxi_str = "taxi(" + X + ", " + Y + ", " + id + ", " + available + ", " + capacity + ", " + languages + ", " + rating + ", " + long_distance + ", " + type + ").";
        return(taxi_str);
    }
	
	public static void make_taxis(String csv_name, String prolog_name) throws IOException {
		Scanner scanner = new Scanner(new FileReader(csv_name));
        OutputStream out_taxis = new FileOutputStream(prolog_name, false);
        Writer writer_taxis = new OutputStreamWriter(out_taxis, "UTF-8");
        String line = scanner.nextLine();
        int taxi_cnt = 0;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            taxi_cnt++;
			String taxi_str = parse_taxi(line);
            writer_taxis.append(taxi_str + System.lineSeparator());
            writer_taxis.flush();
        }
        scanner.close();
        out_taxis.close();
	}
    
    public static void main(String[] args) throws IOException {
        
        make_taxis("taxis.csv", "taxi.pl");
        make_taxis("taxis_custom.csv", "taxi_custom.pl");
    }
}
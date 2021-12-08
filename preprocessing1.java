import java.io.*;
import java.util.*;

public class preprocessing1 {
    
    public static String process_time(String str) {
        String str1 = new String(), str2 = new String();
        str1 += str.charAt(0);
        str1 += str.charAt(1);
        str2 += str.charAt(3);
        str2 += str.charAt(4);
		int first = Integer.parseInt(str1), second = Integer.parseInt(str2);
		double time_d = first + 1.0 * second / 60;
        return(String.valueOf(time_d));
    }
    
    public static String parse_taxi(String str) {
        String[] data = str.split(",");
        String X, Y, X_dest, Y_dest, time, persons, language, luggage;
        int p = 0;
        X = data[p++];
        Y = data[p++];
        X_dest = data[p++];
        Y_dest = data[p++];
        time = process_time(data[p++]);
		persons = data[p++];
        language = data[p++];
        luggage = data[p++];
        String taxi_str = "client(" + X + ", " + Y + ", " + X_dest + ", " + Y_dest + ", " + time + ", " + persons + ", " + language + ", " + luggage + ").";
        return(taxi_str);
    }
	
	public static void make_client(String csv_name, String prolog_name) throws IOException {
		Scanner scanner = new Scanner(new FileReader(csv_name));
        OutputStream out_client = new FileOutputStream(prolog_name, false);
        Writer writer_client = new OutputStreamWriter(out_client, "UTF-8");
        String line = scanner.nextLine();
        line = scanner.nextLine();
		String client_str = parse_taxi(line);
        writer_client.append(client_str + System.lineSeparator());
        writer_client.flush();
        scanner.close();
        out_client.close();
	}
    
    public static void main(String[] args) throws IOException {
        
        make_client("client.csv", "client.pl");
        make_client("client_custom.csv", "client_custom.pl");
    }
}
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//given an address, it gets info about coordinates to inform the ga
public class Geocoding {

    public static String formatAddressForAPI(String addressToFind){
        String finalString = "";

        String[] choppedList = addressToFind.split(" ");
        for (int i=0; i< choppedList.length-1; i++) {
            finalString = finalString.concat(choppedList[i] + "+");
        }

        finalString = finalString.concat(choppedList[choppedList.length-1]);

        return finalString;
    }

    public static String getCoordsFromAddress (String addressToFind){
        String response = "";
        try {
            URL url = new URL("https://nominatim.openstreetmap.org/search?q="+formatAddressForAPI(addressToFind)+"&format=geojson");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output = br.readLine();
            //String response = "";// = new StringBuilder();

            while (output != null) {
                response = response.concat(output);

                output = br.readLine();
            }
            br.close();
            conn.disconnect();

            System.out.println("Response: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}

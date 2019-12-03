import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;

public class client_connection {
    public static void main(String[] args) throws IOException, SQLException {
        GetRequest();
        PostRequest();
        System.out.println("GET DONE");
    }
    public static void GetRequest() {
        //To get URL of Heroku (request data from specified source) alan
        try {
            URL myURL = new URL("http://localhost:8080/Servlet123/patients");
            HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/html");
            conn.setRequestProperty("charset", "utf-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(myURL.openStream()));
            String inputLine;
            // Read the body of the response
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void PostRequest()throws SQLException{
        try {

            String dbUrl = "jdbc:postgresql://localhost:5432/postgres";     //getting URL of database
            ResultSet rset;
            ResultSet rset1;
            try {// Registers the driver
                Class.forName("org.postgresql.Driver");
            } catch (Exception e) {
            }
            try {
                Connection conn = DriverManager.getConnection(dbUrl, "postgres", "Wenli123"); //localhost
                Statement s = conn.createStatement();
                Statement s1 = conn.createStatement();
                String sqlStr = "SELECT * FROM patients;";
                String sqlStr1 = "SELECT * FROM doctors;";
                rset = s.executeQuery(sqlStr);
                rset1 = s1.executeQuery(sqlStr1);
                while (rset.next()) {

                    String family_name = rset.getString("familyname");
                    System.out.println(family_name);
                    String given_name = rset.getString("givenname");
                    System.out.println(given_name);
                    String phone_n = rset.getString("phonenumber");
                    System.out.println(phone_n);
                    // Set up the body data

                    byte[]body = family_name.getBytes(StandardCharsets.UTF_8);
                    body = given_name.getBytes(StandardCharsets.UTF_8);
                    body = phone_n.getBytes(StandardCharsets.UTF_8);
                    URL myURL = new URL("http://localhost:8080/Servlet123/patients");
                    HttpURLConnection connet = null;
                    connet = (HttpURLConnection) myURL.openConnection();
                    //System.out.println("Connected");
                    // Set up the header
                    connet.setRequestMethod("POST");
                    connet.setRequestProperty("Accept", "text/html");
                    connet.setRequestProperty("charset", "utf-8");
                    connet.setRequestProperty("Content-Length", Integer.toString(body.length));
                    connet.setDoOutput(true);
                    // Write the body of the request
                    try (OutputStream outputStream = connet.getOutputStream()) {
                        outputStream.write(body, 0, body.length);
                    }

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connet.getInputStream(),"utf-8"));

                    String inputLine;
                    while((inputLine = bufferedReader.readLine()) != null){
                        System.out.println(inputLine);
                    }
                    bufferedReader.close();

                }
                rset.close();
                s.close();
                conn.close();

                /*while (rset1.next()) {
                    String id_doctor = rset1.getString("id doctor");
                    System.out.println(id_doctor);
                    String doctor_name = rset1.getString("familyname");
                    System.out.println(doctor_name);

                    // Set up the body data
                    // String message= id;
                   /*byte[] body = id_doctor.getBytes(StandardCharsets.UTF_8);
                    connet.setRequestProperty("Content-Length", Integer.toString(body.length));
                    connet.setDoOutput(true);


                   byte[] body = doctor_name.getBytes(StandardCharsets.UTF_8);
                   URL myURL = new URL("http://localhost:8080/Servlet123/patients");
                   HttpURLConnection connet = null;
                   connet = (HttpURLConnection) myURL.openConnection();
                   // Set up the header
                   connet.setRequestMethod("POST");
                   connet.setRequestProperty("Accept", "text/html");
                   connet.setRequestProperty("charset", "utf-8");
                   connet.setRequestProperty("Content-Length", Integer.toString(body.length));
                   connet.setDoOutput(true);

                    // Write the body of the request
                    try (OutputStream outputStream = connet.getOutputStream()) {
                        outputStream.write(body, 0, body.length);
                    }
                }
                rset1.close();
                s1.close();
                conn.close();*/
            } catch (Exception e) {
            }

        } catch(Exception e){
            e.printStackTrace();
        }

    }


}




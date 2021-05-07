import com.example.s_car.Event;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


@WebServlet(urlPatterns = {"/GetEventsESP32"})
public class GetEventsESP32 extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        InputStream in = request.getInputStream();
        String data ="";
        Scanner scanner = new Scanner(in);
        while(scanner.hasNext()){
            data += scanner.nextLine();
        }
        scanner.close();
        OutputStream outstr = response.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outstr);
        ObjectOutputStream oos = new ObjectOutputStream(outstr);

        try {
            Object object = JSONValue.parse(data);
            JSONObject jsonObject = (JSONObject) object;
            int ownerId =Integer.parseInt((String) jsonObject.get("EventID")) ;
            //System.out.println(ownerId);

            if(ownerId == 0) {
                ownerId = 0;  // default to all
            }
            Class.forName( "com.mysql.cj.jdbc.Driver" );
            Connection  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scar?serverTimezone=UTC","root","root" );

            PreparedStatement find = con.prepareStatement("select * from events where ownerId LIKE ?");
            find.setInt(1, ownerId);
            ResultSet rs = find.executeQuery();
            JSONObject allData = new JSONObject();
            JSONArray arr = new JSONArray();
            while(rs.next()) {
                JSONObject obj=new JSONObject();
                obj.put("Date",rs.getString("eventDate"));
                obj.put("Time",rs.getString("eventTime"));
                obj.put("Address",rs.getString("address"));
                obj.put("Town",rs.getString("town"));
                obj.put("County",rs.getString("county"));
                arr.add(obj);

            }
            allData.put("Data", arr);
            oos.writeObject(allData.toJSONString());
            rs.close();
            find.close();
            con.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
        finally{


        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

import com.example.s_car.Owner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;


@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultSet resultSet = null;
        int result = 0;
        String err = "";
         response.setContentType("application/octet-stream");
         InputStream in = request.getInputStream();
         ObjectInputStream ois = new ObjectInputStream(in);
         OutputStream outstr = response.getOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(outstr);

         try {
             Owner owner = (Owner) ois.readObject();
            if(owner != null) {
                Class.forName( "com.mysql.cj.jdbc.Driver" );
                Connection  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scar","root","root" );
                PreparedStatement checkOwner = con.prepareStatement("SELECT count(*) FROM owners , login where carNumber like ? or email like ?");
                checkOwner.setNString(1,owner.getCarNumber());
                checkOwner.setNString(2,owner.getEmailAddress());
                resultSet = checkOwner.executeQuery();
                resultSet.next();
                if(resultSet.getInt(1) <= 0) {
                    checkOwner.close();
                    PreparedStatement addOwner = con.prepareStatement("INSERT INTO owners VALUE (?,?,?,?,?)");
                    addOwner.setInt(1, owner.getId());
                    addOwner.setNString(2, owner.getName());
                    addOwner.setNString(3, owner.getPhoneNumber());
                    addOwner.setNString(4, owner.getCarNumber());
                    addOwner.setNString(5, owner.getCarKey());
                    result = addOwner.executeUpdate();
                    addOwner.close();
                    if (result > 0) {
                        result = 0;
                        PreparedStatement getOwnerId = con.prepareStatement("SELECT ownerId FROM owners WHERE carNumber like ?");
                        getOwnerId.setNString(1, owner.getCarNumber());
                        resultSet = getOwnerId.executeQuery();
                        resultSet.next();

                        owner.setId(resultSet.getInt("ownerId"));
                        getOwnerId.close();
                        if(owner.getId() !=0){
                            PreparedStatement addOwnerLogin = con.prepareStatement("INSERT INTO login VALUE (NULL,?,?,?)");
                            addOwnerLogin.setInt(1, owner.getId());
                            addOwnerLogin.setNString(2, owner.getEmailAddress());
                            addOwnerLogin.setNString(3, owner.getPassword());
                            result = addOwnerLogin.executeUpdate();
                            addOwnerLogin.close();
                            err = "registered";
                        }else{
                            PreparedStatement delete = con.prepareStatement("DELETE FROM owner where ownerId like ?");
                            delete.setInt(1, owner.getId());
                            result = delete.executeUpdate();
                            delete.close();
                            err = "Please try again later";
                        }

                    }else{
                        //owner wasent inserted
                        err = "Please try again later";
                    }
                }else{
                    //owner has been added before email or car no
                    err = "Email or Car Number is been used by someone else";
                    checkOwner.close();
                }

                con.close();

            }
        } 
        catch(Exception ex)        {
          System.out.println(ex.toString());
        }
        finally {
             oos.writeObject(err);
             oos.flush();
             oos.close();
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
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

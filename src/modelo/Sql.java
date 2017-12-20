package modelo;

import java.sql.Connection;
import java.sql.DriverManager;

public class Sql {
    Connection conectar=null;
    public Connection conectar(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conectar=DriverManager.getConnection(""
                    + "jdbc:mysql://localhost/tallerxx","root","");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return conectar;
    }
}
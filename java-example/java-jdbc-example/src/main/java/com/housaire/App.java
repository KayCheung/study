package com.housaire;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");


            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/study?user=root&password=123456&useUnicode=true&characterEncoding=UTF8");

//            ps = connection.prepareStatement("insert into t_user(name,gender,birthday,address) values(?,?,?,?)");
//            ps.setString(1, "Lucy");
//            ps.setByte(2, (byte)1);
//            ps.setObject(3, new Date());
//            ps.setString(4, "上海市");

//            System.out.println(ps.executeUpdate());


            ps = connection.prepareStatement("select * from t_user");
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                ps.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // docker run -itd -p 13306:3306 --name mysql_master -v /Users/kay/Workspace/mysql/master_slave/master/cnf:/etc/mysql -v /Users/kay/Workspace/mysql/master_slave/slave/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 mysql
        // docker run -itd -p 23306:3306 --name mysql_slave  -v /Users/kay/Workspace/mysql/master_slave/slave/cnf:/etc/mysql -v  /Users/kay/Workspace/mysql/master_slave/slave/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 mysql
    }
}

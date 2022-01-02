package JDBC_Utils;

import com.sun.org.apache.bcel.internal.util.ClassLoader;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
public class JDBC_Util {
    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    /**
     * 从配置文件读取所需要的数据
     */
    static{
        try {
            //创建Properties集合类
            Properties pro = new Properties();
            //获取src下文件路径---->ClassLoader 类加载器(文件路径不有为中文)
            ClassLoader classLoader = new ClassLoader();
            URL res = classLoader.getResource("jdbc.properties");
            String path = res.getPath();
            //加载文件
            //pro.load(new FileReader("/D:/java-idea2018File/jdbc/out/production/atm-gui-jdbc/jdbc.properties"));
            pro.load(new FileReader(path));
            //获取数据赋值
            url = pro.getProperty("url");
            user = pro.getProperty("user");
            password = pro.getProperty("password");
            driver = pro.getProperty("driver");

            //注册驱动
            Class.forName(driver);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     * @return 连接对象
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }

    /**
     * 释放资源
     * @param stmt
     * @param coon
     */
    public static void close(Statement stmt, Connection coon){
        close(null,stmt,coon);
    }

    public static void close(ResultSet res,Statement stmt,Connection coon){
        if(res != null){
            try{
                res.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try{
                stmt.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        if (coon != null) {
            try{
                coon.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}

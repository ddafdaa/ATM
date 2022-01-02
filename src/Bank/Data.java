package Bank;

import JDBC_Utils.JDBC_Util;
import java.sql.*;

/**
 * Data类是是用来操作数据库的，改查操作
 * 验证、返回、保存信息
 */

public class Data {
    private double umoney;//用户余额
    private String uname; //用户
    private String gname;  //被转账用户
    private Connection coon = null;// 一般操作连接对象，程序结束时释放
    private Connection coon1 = null;// 转账连接对象，程序结束时释放
    private PreparedStatement pstmt = null; // 一般操作
    private PreparedStatement pstmt1 = null; // 转账时使用
    private ResultSet res = null;
    private String sql;//sql语句
    private String sql1;//sql语句，转账时使用

    {
        try {
            // 获取连接对象
            coon = JDBC_Util.getConnection();
            coon1 = JDBC_Util.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取该账户的余额
     * @return 余额
     */
    public double getMoney(){
        try {
            sql = "select * from atm1 where uname= ?";//查询语句
            pstmt = coon.prepareStatement(sql);
            pstmt.setString(1, uname);
            res = pstmt.executeQuery();
            if(res.next()){
                umoney = res.getDouble("money");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBC_Util.close(res, pstmt, null);//释放资源
        }
        return umoney;
    }

    /**
     * 判断登录是否成功
     */
    public boolean isTrue(String user,String password){
        if(user == null || password == null){
            return false;
        }
        try {
            sql = "select * from atm1 where uname = ? and password = ?";
            pstmt = coon.prepareStatement(sql);
            pstmt.setString(1,user);
            pstmt.setString(2,password);
            res = pstmt.executeQuery();
            uname = user;
            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBC_Util.close(res, pstmt, null);
        }
        return false;
    }


    /**
     * 取钱、存钱操作
     * @param money 钱数
     * @param isOrNot 存钱or取钱
     */
    public boolean changeMoney(double money,boolean isOrNot){
        if(isOrNot){
            sql = "update atm1 set money = money + ? where uname = ?";
        }else{
            sql = "update atm1 set money = money - ? where uname = ?";
        }
        return runSql(money,null);
    }

    /**
     * 修改密码
     * @param newPassword 新密码
     * @return
     */
    public boolean changePassword(String newPassword){
        if(newPassword == null){
            return false;
        }
        sql = "update atm1 set password = ? where uname = ?";
        return runSql(0,newPassword);
    }

    //执行sql修改操作
    private boolean runSql(double money,String newPassword){
        try {
            if(sql == null){
                return false;
            }
            pstmt = coon.prepareStatement(sql);
            if(newPassword == null){
                pstmt.setDouble(1, money);
            }else{
                pstmt.setString(1, newPassword);
            }
            pstmt.setString(2,uname);
            int count = pstmt.executeUpdate();
            if(count != 0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            sql = null;
            JDBC_Util.close(res, pstmt, null);
        }
        return false;
    }

    /**
     * 判断被转帐用户是否存在
     * @param user 被转账用户名
     * @return
     */
    public boolean isUser(String user){
        if(user == null){
            return false;
        }
        try {
            sql = "select * from atm1 where uname = ?";
            pstmt = coon.prepareStatement(sql);
            pstmt.setString(1,user);
            res = pstmt.executeQuery();
            gname = user;
            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBC_Util.close(res, pstmt, null);
        }
        return false;
    }

    /**
     * 转账操作，事务
     * @param money 转账数
     * @return
     */
    public boolean giveMoney(double money){
        try {
            coon1.setAutoCommit(false); //开启事务
            sql = "update atm1 set money = money - ? where uname = ?";
            sql1 = "update atm1 set money = money + ? where uname = ?";
            pstmt = coon.prepareStatement(sql);
            pstmt1 = coon.prepareStatement(sql1);
            pstmt.setDouble(1,money);
            pstmt.setString(2,uname);
            pstmt1.setDouble(1,money);
            pstmt1.setString(2,gname);
            int a = pstmt.executeUpdate();
            int b = pstmt1.executeUpdate();
            coon1.commit();//提交事务
            if(a !=0 && b!=0) {
                return true;
            }
        } catch (Exception e) {
            try {
                if(coon1 !=null)
                    coon1.rollback();//回滚
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally{
            JDBC_Util.close(res, pstmt, null);
            JDBC_Util.close(null, pstmt1, null);
        }
        return false;
    }

    /**
     * 释放所有的资源
     */
    public void close(){
        JDBC_Util.close(res, pstmt, coon);
        JDBC_Util.close(res, pstmt1, coon1);
    }

}

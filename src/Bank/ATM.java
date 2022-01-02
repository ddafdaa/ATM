package Bank;

/**
 * 用来获取信息和传送信息，进行简单判断，在程序结束释放Data类的资源
 * 窗口类使用ATM类
 * 从Data类获取信息传给窗口类
 * 从窗口获取的信息传给Data类
 */

public class ATM {
    //有一个类操作数据的类
    private Data data = new Data();
    //ATM机里的数据
    private double userMoney;//用户账户里的钱

    //登录方法
    public boolean login(String inUserName,String inUserPassword){
        boolean b= false;
        if(data.isTrue(inUserName,inUserPassword)){
            b = true;
            userMoney = data.getMoney();
        }
        return b;
    }
    //获取账户余额
    public Double getMoney1(){
        userMoney = data.getMoney();
        return userMoney;
    }
    //存钱
    public boolean saveMoney(Double money){
        boolean b = false;
        if(money % 100 == 0) { //如果存的钱正确
            b = data.changeMoney(money, true);//存上
        }
        return b;
    }
    //取钱
    public boolean quMoney(Double money){
        boolean b = false;
        if(money % 100 == 0 && money <= userMoney){
            b = data.changeMoney(money, false);
        }
        return b;
    }
    //只判断用户账户是否存在
    public boolean anotherUser(String inUserName){
        //如果该账户存在
        boolean b = data.isUser(inUserName);
        return b;
    }
    //进行转账
    public void giveMoney(Double money){
        data.giveMoney(money);
    }
    //修改密码实现
    public boolean changePassword(String newPassword){
        return data.changePassword(newPassword);
    }

    // 程序结束
    public void over(){
        // 释放Data类的资源
        data.close();
    }

}

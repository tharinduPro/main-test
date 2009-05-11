import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCOracle{
   
    //dbUrl数据库连接串信息，其中“1521”为端口，“test”为sid
    String dbUrl = "jdbc:oracle:thin:@10.1.1.139:1521:test";
    //theUser为数据库用户名
    String theUser = "test";
    //thePw为数据库密码
    String thePw = "test";
    
    //几个数据库变量
    private Connection conn = null;
    private Statement st;
    private ResultSet rs = null;

    //初始化连接
    public JDBCOracle() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            //与url指定的数据源建立连接
            conn = DriverManager.getConnection(dbUrl, theUser, thePw);
            //采用Statement进行查询
            st = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //执行查询
    public ResultSet executeQuery(String sql) {
        rs = null;
        try {
            rs = st.executeQuery(sql);
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    //释放链接
    public void close() {
        try {
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ResultSet rs;
        JDBCOracle sqlObject = new JDBCOracle();
        rs = sqlObject.executeQuery("select * from student");
        try {
            while (rs.next()) {
                System.out.print(rs.getString("event_type"));
                System.out.println(":"+rs.getString("content"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqlObject.close();

    }

}

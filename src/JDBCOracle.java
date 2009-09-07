import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCOracle{
   
    //dbUrl数据库连接串信息，其中“1521”为端口，“test”为sid
    String dbUrl = "jdbc:oracle:thin:@19.104.9.47:1521:dgioora1";
    //theUser为数据库用户名
    String theUser = "dc";
    //thePw为数据库密码
    String thePw = "dc123456";
    
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
        rs = sqlObject.executeQuery("select 1 as test from dual");
        try {
            while (rs.next()) {
                System.out.println(rs.getString("test"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqlObject.close();

    }

}

package Database;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public enum DBAdminConnector {
    INSTANCE;
    private static String dbUrl = "jdbc:mysql://localhost:3306/blog?useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static String user = "root";
    private static String password = "0GdQ1VQA";
    private static ResultSet rs;
    private static Connection con;
    private Statement st;
    private int BUFFER = 99999;
    private String backUpPath= "C:\\backup";
    private String host="localhost";
    private String mysqlPort="3306";

    DBAdminConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl,user,password);
            System.out.println("Got the connection");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static boolean mysqlDatabaseRestore(String source) {
        boolean status=false;
        String[] executeCmd = new String[]{"mysql", "--user=" + user, "--password=" + password, "blog","-e", " source "+source};
        System.out.println(executeCmd);
        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                System.out.println("Backup restored successfully");
                status =true;
                return status;
            } else{
                System.out.println("Could not restore the backup");
                status=false;
                return status;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return status;
    }

    public String backup() {
        String status="";

        File file = new File("C:\\backup");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
                try
                {
                    DateFormat dayFormat = new SimpleDateFormat("dd");
                    DateFormat monthFormat = new SimpleDateFormat("MM");
                    DateFormat yearFormat = new SimpleDateFormat("yyyy");
                    Calendar cal = Calendar.getInstance();
                    String backupDate=dayFormat.format(cal.getTime())+"-"+monthFormat.format(cal.getTime())+"-"+yearFormat.format(cal.getTime());
                    byte[] data = this.getData().getBytes();

                    File fileDestination = new File(backUpPath+"\\"+"blog"+"_"+backupDate+".sql");
                    FileOutputStream destination = new FileOutputStream(fileDestination);
                    destination.write(data);

                    destination.close();
                    status="y";
                    System.out.println("Back Up Success");
                    return status;
                }catch (Exception ex){
                    System.out.println("Back Up Failed");
                    status="n";
                    return status;

                }

            } else {
                System.out.println("Failed to create directory!");
            }
        }
        else{
            try
            {
                DateFormat dayFormat = new SimpleDateFormat("dd");
                DateFormat monthFormat = new SimpleDateFormat("MM");
                DateFormat yearFormat = new SimpleDateFormat("yyyy");
                Calendar cal = Calendar.getInstance();
                String backupDate=dayFormat.format(cal.getTime())+"-"+monthFormat.format(cal.getTime())+"-"+yearFormat.format(cal.getTime());
                byte[] data = this.getData().getBytes();

                File fileDestination = new File(backUpPath+"\\"+"blog"+"_"+backupDate+".sql");
                FileOutputStream destination = new FileOutputStream(fileDestination);
                destination.write(data);

                destination.close();

                System.out.println("Back Up Success");
                status ="y";
                return status;
            }catch (Exception ex){
                System.out.println("Back Up Failed");
                status="n";
                return status;

            }

        }

        return status;
    }

    public String getData() {
        String Mysqlpath = getMysqlBinPath();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("cant access mysql driver");
        }
        try {
            con = DBAdminConnector.getConnection();
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            System.out.print("connection error");
            e.printStackTrace();
        }
        System.out.println(Mysqlpath);
        Process run = null;
        try {
            System.out.println(Mysqlpath + "mysqldump --host=" + host + " --port=" + "3306" + " --user=" + user + " --password=" + password + " --compact --complete-insert --extended-insert " + "--triggers --routines --events " + "blog");
            run = Runtime.getRuntime().exec(Mysqlpath + "mysqldump --host=" + host + " --port=" + "3306" + " --user=" + user + " --password=" + password + "  " + "--triggers --routines --events " + "blog");
        } catch (IOException ex) {

        }

        InputStream in = run.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        StringBuffer temp = new StringBuffer();

        int count;
        char[] cbuf = new char[BUFFER];
        try {
            while ((count = br.read(cbuf, 0, BUFFER)) != -1) {
                temp.append(cbuf, 0, count);
            }
        } catch (IOException ex) {

        }
        try {
            br.close();
            in.close();
        } catch (IOException ex) {

        }
        return temp.toString();
    }

    // Mysql path is required to locate the bin folder inside it because it contains the Mysqldump which performs a //main role while taking backup.
    /*Function to find MySql Path*/
    public  String getMysqlBinPath() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("driver loading failed");
        }
        try {
            con = DBAdminConnector.getConnection();
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            System.out.print("connection error");
            e.printStackTrace();
        }


        String a = "";

        try {
            rs = st.executeQuery("select @@basedir");
            while (rs.next()) {
                a = rs.getString(1);
            }
        } catch (Exception eee) {
            eee.printStackTrace();
        }
        a = a + "bin\\";
        System.err.println("Mysql path is :" + a);
        return a;
    }
}

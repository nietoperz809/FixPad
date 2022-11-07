package database;


import java.io.*;
import java.sql.*;

public class DBHandler {
    private static DBHandler _inst = null;
    private Connection connection;
    private Statement statement;

    /**
     * Private constructor like Singletons should have
     */
    private DBHandler() {
        try {
            String url = "jdbc:h2:./datastore/fixpaddb";
            String user = "LALA";
            String pwd = "dumm";
            connection = DriverManager.getConnection(url, user, pwd);
            statement = connection.createStatement();
//            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    closeConnection();
//                }
//            }));
        } catch (SQLException e) {
            common.Tools.Error(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * get access to the DB
     *
     * @return the one and only DB handler
     */
    public static DBHandler getInst() {
        if (_inst == null) {
            _inst = new DBHandler();
        }
        return _inst;
    }

    public static void main(String[] args) throws Exception {
        //getInst().execute("drop table OBJSTORE");
        //getInst().execute("create table if not exists OBJSTORE (name varchar(255) primary key, " +
        //"object blob, time timestamp default current_timestamp())");
        getInst().storeObject("test3", new byte[]{1, 2, 3});

        Object o = getInst().fetchObject("test3");
        System.out.println(o);
    }

    private ResultSet query(String txt) {
        try {
            return statement.executeQuery(txt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void execute(String cmd) {
        try {
            statement.execute(cmd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
            _inst = null;
            System.out.println("DB closed");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void storeObject(String name, Object o) throws Exception {
        statement.execute("delete from OBJSTORE where name = '" + name + "'");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(o);
        out.flush();
        byte[] bytes = bos.toByteArray();
        PreparedStatement prep = connection.prepareStatement(
                "insert into OBJSTORE (name,object) values (?,?)");
        prep.setString(1, name);
        prep.setBytes(2, bytes);
        prep.execute();
        connection.commit();
    }

    public Object fetchObject(String name) throws Exception {
        ResultSet res = query("select object from OBJSTORE where name = '" + name + "'");
        if (res.next()) {
            byte[] b = res.getBytes(1);
            ByteArrayInputStream bis = new ByteArrayInputStream(b);
            ObjectInput in = new ObjectInputStream(bis);
            return in.readObject();
        }
        return null;
    }
}

package database;


import common.FixPad;
import common.Tools;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.prefs.Preferences;

public class DBHandler {
    private static DBHandler _inst = null;
    private Connection connection;
    private Statement statement;
    private static final String key = "Database.url";
    private static final Class clazz = FixPad.mainFrame.getClass();

    private static void putURL(String u) {
        Preferences.userNodeForPackage(clazz).put(key, u);
    }

    private static String getURL() {
        return Preferences.userNodeForPackage (clazz)
                .get(key, "jdbc:h2:./datastore/fixpaddb");
    }

    /**
     * Private constructor like Singletons should have
     */
    private DBHandler() {
        try {
            String user = "LALA";
            String pwd = "dumm";
            String url = getURL();
            System.out.println("Open DB: "+url);
            connection = DriverManager.getConnection (url, user, pwd);
            statement = connection.createStatement();
        } catch (SQLException e) {
            common.Tools.Error(e.getMessage());
            System.exit(-1);
        }
    }

    public static void setNewURL (String path, String file) {
        int i = file.indexOf('.');
        if (i >= 0) {
            file = file.substring(0, i);
        }
        _inst.closeConnection();
        putURL("jdbc:h2:"+path+"\\"+file);
    }

    /**
     * get access to the DB
     *
     * @return the one and only DB handler
     */
    public static DBHandler getInst() {
        if (_inst == null) {
            _inst = new DBHandler();
            _inst.execute("create table if not exists OBJSTORE (name varchar(255) primary key, " +
                    "object blob, time timestamp default current_timestamp())");
            _inst.execute("create table if not exists BKIMG (index int primary key not null, image blob)");
        }
        return _inst;
    }

    public void addBKImage(BufferedImage img, int index) {
        PreparedStatement prep;
        try {
            byte[] buff = Tools.imgToByteArray(img);
            execute ("delete from BKIMG where index = "+index);
            prep = connection.prepareStatement(
                    "insert into BKIMG (index,image) values (?,?)");
            prep.setInt(1, index);
            prep.setBytes(2, buff);
            prep.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBkImage (int index) {
        execute ("delete from BKIMG where index = "+index);
    }

    public BufferedImage getBKImage (int index) {
        try (ResultSet res = query("select image from BKIMG where index = " + index)) {
            if (res.next()) {
                byte[] b = res.getBytes(1);
                return Tools.byteArrayToImg(b);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

//    public static void main(String[] args) throws Exception {
//        //getInst().execute("drop table OBJSTORE");
//        //getInst().execute("create table if not exists OBJSTORE (name varchar(255) primary key, " +
//        //"object blob, time timestamp default current_timestamp())");
//        getInst().execute("call csvwrite ('./db.csv', 'select * from objstore')");
//    }

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
        try (ResultSet res = query("select object from OBJSTORE where name = '" + name + "'")) {
            if (res.next()) {
                byte[] b = res.getBytes(1);
                ByteArrayInputStream bis = new ByteArrayInputStream(b);
                ObjectInput in = new ObjectInputStream(bis);
                return in.readObject();
            }
        }
        return null;
    }
}

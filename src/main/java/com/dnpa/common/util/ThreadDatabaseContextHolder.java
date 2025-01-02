package com.dnpa.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class ThreadDatabaseContextHolder {
    private static final ThreadLocal<Connection> connectionForWriteThread = new NamedThreadLocal<>("Write DB Connection For Thread");

    private static final ThreadLocal<Connection> connectionForReadThread = new NamedThreadLocal<>("Read DB Connection For Thread");

    private static ThreadLocal<Boolean> enableThread = new NamedThreadLocal<>("Enable using thread pool");

    private static ThreadLocal<Boolean> enableThreadForReadOnly = new NamedThreadLocal<>("Enable using read only thread pool");

    public static Connection getReadConnection( DataSource dataSource) throws SQLException {

        Connection connection;

        connection = connectionForReadThread.get();

        if (connection == null) {

            connection = dataSource.getConnection();
            connectionForReadThread.set(connection);
        }
        return connection;

    }

    public static Connection getWriteConnection( DataSource dataSource) throws SQLException {


        Connection connection;

        connection = connectionForWriteThread.get();

        if (connection == null) {

            connection = dataSource.getConnection();
            connectionForWriteThread.set(connection);
        }
        return connection;

    }

    public static void releaseConnectionHolder(){

        releaseConnection(connectionForReadThread);
        releaseConnection(connectionForWriteThread);


        Boolean isEnable = enableThread.get();
        if(isEnable != null) {
            enableThread.remove();
        }

        Boolean isReadOnly = enableThreadForReadOnly.get();
        if(isReadOnly != null) {
            enableThreadForReadOnly.remove();
        }
    }

    private static void releaseConnection(ThreadLocal<Connection> connection4Thread) {
        try{
            Connection connection = connection4Thread.get();

            if (connection != null) {
                connection4Thread.remove();

                if(!connection.isClosed()) {
                    connection.commit();
                    connection.close();
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public static void rollbackConnection(){

        try{
            Connection connection = connectionForWriteThread.get();
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public static void setEnableThread() {
        enableThread.set(true);
    }

    public static boolean isEnableThread() {
        Boolean enableThreadBoolean = enableThread.get();
        return Boolean.TRUE.equals(enableThreadBoolean);
    }

    public static boolean isEnableThreadForReadOnly() {
        Boolean isReadOnly = enableThreadForReadOnly.get();
        return Boolean.TRUE.equals(isReadOnly);
    }
}

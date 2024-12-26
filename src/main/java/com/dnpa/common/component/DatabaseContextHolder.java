package com.dnpa.common.component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequestScope
@Component
@Getter
public class DatabaseContextHolder{
    private Connection writeConnection;
    private Connection readConnection;
    private boolean isReadable;
    private boolean isReleasedReadConnection;
    public Connection getConnectionWrite( DataSource dataSource) throws SQLException {

        if(this.writeConnection ==null ) {
            this.writeConnection = dataSource.getConnection();
        }
        return this.writeConnection;
    }

    public Connection getConnectionRead(DataSource dataSource) throws SQLException {

        if(this.readConnection ==null ) {
            this.readConnection = dataSource.getConnection();
        }
        return this.readConnection;
    }

    public void releaseConnection() {
        
        releaseWriteConnection();
        releaseReadConnection();

    }

    private void releaseReadConnection() {
        releaseConnection(this.readConnection);
        this.isReadable = true;
    }

    private void releaseWriteConnection() {
        releaseConnection(this.writeConnection);
    }
    private void releaseConnection(Connection con) {

        try {
            if (con != null && !con.isClosed()) {

                con.commit();
                con.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
        }

    }
    public void setReadOnly() {
        this.isReadable = true;
    }

    public boolean isReadOnly() {
        return this.isReadable;
    }

    public void setWriteCon() {
        this.isReadable = false;
    }
}

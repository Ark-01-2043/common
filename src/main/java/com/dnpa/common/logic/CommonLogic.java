package com.dnpa.common.logic;

import com.dnpa.common.component.DatabaseContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

public abstract class CommonLogic {
    private DataSource dataSourceRead;
    private DataSource dataSourceWrite;
    @Autowired
    private DatabaseContextHolder databaseContextHolder;
}

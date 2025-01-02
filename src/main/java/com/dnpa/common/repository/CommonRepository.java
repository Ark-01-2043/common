package com.dnpa.common.repository;

import com.dnpa.common.util.SqlUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

@Slf4j
public abstract class CommonRepository {
    protected SqlUtil sqlUtil;
    protected String xmlFileName;
    protected String dbmsProduct;
    public abstract String getXmlFileName();

    public abstract String getDbmsProduct();
    public abstract Connection getConnection();
}

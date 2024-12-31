package com.dnpa.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Slf4j
public class SqlUtil {
    protected String xmlFileName = null;
    protected String dbmsProduct = null;
    protected static Map<String, String> sqlCache = new HashMap<>();
    public SqlUtil(String xmlFileName, String dbmsProduct) {
        this.xmlFileName = xmlFileName;
        this.dbmsProduct = dbmsProduct;
    }
    public String getSqlStr(String sqlId){
        String cacheKey = xmlFileName + "&" + dbmsProduct + "&" + sqlId;
        if (sqlCache.containsKey(cacheKey)) {
            return sqlCache.get(cacheKey);
        }
        String sql = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            log.warn("Sql Util: " + pce.getMessage());
            return null;
        }

        Document doc;
        try {
            doc = db.parse(new ClassPathResource(xmlFileName).getInputStream());
        } catch (SAXException | IOException se) {
            log.warn("Sql Util: " + se.getMessage());
            return null;
        }
        NodeList sqllist = doc.getElementsByTagName("sql");
        for (int i = 0; i < sqllist.getLength(); i++) {
            Node sqlnode = sqllist.item(i);
            if (sqlnode.getNodeType() == Node.ELEMENT_NODE) {
                Element sqlelem = (Element) sqlnode;
                if (sqlId.equals(sqlelem.getAttribute("id"))) {
                    sql = getElemStr(sqlelem);
                    break;
                }
            }
        }

        sqlCache.put(cacheKey, sql);
        return sql;
    }
    private String getElemStr(Element pElem) {
        String sql = null;
        NodeList nl = pElem.getElementsByTagName(dbmsProduct);
        if (nl.getLength() == 0) {
            nl = pElem.getElementsByTagName("common");
        }
        if (nl != null) {
            Node child;
            for (child = nl.item(0).getFirstChild();
                 child != null;
                 child = child.getNextSibling()) {
                String nodeValue = child.getNodeValue();
                if (nodeValue.trim().length() > 0) {
                    sql = trimWhitespace(child.getNodeValue());
                    break;
                }
            }
        }
        return sql;
    }
    private String trimWhitespace(String pSql) {
        StringTokenizer token = new StringTokenizer(pSql, "\n");
        StringBuilder builder = new StringBuilder(512);
        while (token.hasMoreTokens()) {
            builder.append(token.nextToken().trim());
            builder.append(" ");
        }
        return builder.toString().trim();
    }
}

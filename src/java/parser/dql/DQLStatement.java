/*
* $Id
*
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
package parser.dql;

import parser.IStatement;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 05.07.13</p>
 *
 * @version 1.0
 */
public class DQLStatement implements IStatement {
    private static final String QUERY_TYPE = "DQL";
    private String query;

    public DQLStatement(String query) {
        this.query = query;
    }

    public static DQLStatement resolve(String query) {
        return new DQLStatement(query);
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public String getQueryType() {
        return QUERY_TYPE;
    }

//    @Override
//    public String getRollbackQuery() {
//        return null;
//    }
}

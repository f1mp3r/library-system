package app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Chris on 6.3.2017 г..
 */
public class QueryBuilder {
    protected String table;
    protected ArrayList fields;
    protected ArrayList values;
    protected ArrayList whereClause;
    protected QueryType queryType;

    public QueryBuilder (String table) {
        this.table = table;
        this.fields = new ArrayList();
        this.whereClause = new ArrayList();
    }

    public QueryBuilder select(ArrayList fields) {
        this.fields.addAll(fields);
        this.queryType = QueryType.SELECT;

        return this;
    }

    public QueryBuilder update(HashMap data) {
        this.fields.addAll(data.keySet());
        this.values.addAll(data.values());
        this.queryType = QueryType.UPDATE;

        return this;
    }

    public QueryBuilder delete() {
        this.queryType = QueryType.DELETE;

        return this;
    }

    public QueryBuilder where(String field, String compare, String value, String table) {
        this.whereClause.add(String.format("`%s`.`%s` %s %s", table, field, compare, value));
        return this;
    }

    public QueryBuilder where(String field, String compare, String value) {
        return this.where(field, compare, value, this.table);
    }

    public String build() {
        String query = "";

        switch (this.queryType) {
            case SELECT:
                // todo
                break;
            case UPDATE:
                // todo: add joins
                query = String.format(
                        "UPDATE %s SET %s %s",
                        this.buildTableAndJoins(),
                        this.buildUpdateDataString(),
                        this.buildWhereString()
                );
                break;
            case DELETE:
                query = String.format("DELETE FROM %s %s", this.table, this.buildWhereString());
                break;
        }

        return query;
    }

    private String buildWhereString() {
        if (0 == this.whereClause.size()) {
            return "";
        }

        String whereClauseString = "WHERE ";
        Iterator i = this.whereClause.iterator();

        while (i.hasNext()) {
            whereClauseString += i.next().toString();

            if (i.hasNext()) {
                whereClauseString += " AND ";
            }
        }

        return whereClauseString;
    }

    private String buildUpdateDataString() {
        if (this.fields.isEmpty()) {
            return "";
        }

        String dataString = "WHERE ";

        for (int i = 0; i < this.fields.size(); i++) {
            dataString += String.format("`%s` = %s", this.fields.get(i), this.values.get(i));

            if (i + 1 < this.fields.size()) {
                dataString += ", ";
            }
        }

        return dataString;
    }

    private String buildTableAndJoins() {
        String selector = this.table;
        // todo: add joins
        return selector;
    }
}

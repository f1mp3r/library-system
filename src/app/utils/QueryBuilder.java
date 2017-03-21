package app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Chris on 6.3.2017 г..
 */

public class QueryBuilder {
    private String table;
    private ArrayList<String> fields;
    private ArrayList<String> values;
    private ArrayList<String> whereClause;
    private ArrayList<String> joins;
    private ArrayList<String> orders;
    private QueryType queryType;

    public QueryBuilder (String table) {
        this.table = table;
        this.fields = new ArrayList();
        this.whereClause = new ArrayList();
        this.joins = new ArrayList();
        this.orders = new ArrayList();
    }

    public QueryBuilder setTable(String table) {
        this.table = table;
        return this;
    }

    public QueryBuilder select(String... fields) {
        for (String field : fields) {
            // if starts with @ means expression so don't quote
            this.fields.add(field.startsWith("@") ? field.substring(1) : this.quoteField(field));
        }

        if (fields.length == 0) {
            this.fields.add("*");
        }

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
        this.whereClause.add(String.format("`%s`.`%s` %s %s", table, field, compare, this.escapeValue(value)));
        return this;
    }

    public QueryBuilder where(String field, String compare, String value) {
        return this.where(field, compare, value, this.table);
    }

    public QueryBuilder where(String field, String value) {
        return this.where(field, "=", value);
    }

    public QueryBuilder orWhere(String field, String compare, String value, String table) throws Exception {
        if (this.whereClause.isEmpty()) {
            throw new Exception("orWhere() should be used after a where()");
        }

        int lastWhereClauseId = this.whereClause.size() - 1;
        this.where(field, compare, value, table);
        int newWhereClauseId = lastWhereClauseId + 1;

        String lastWhere = this.whereClause.get(lastWhereClauseId);
        String newTempWhere = this.whereClause.get(newWhereClauseId);
        String newWhere = String.format("(%s OR %s)", lastWhere, newTempWhere);

        // remove the 2 where clauses
        this.whereClause.remove(newWhereClauseId);
        this.whereClause.remove(lastWhereClauseId);
        this.whereClause.add(lastWhereClauseId, newWhere);

        return this;
    }

    public QueryBuilder orWhere(String field, String compare, String value) throws Exception {
        return this.orWhere(field, compare, value, this.table);
    }

    public QueryBuilder orWhere(String field, String value) throws Exception {
        return this.orWhere(field, "=", value, this.table);
    }

    public QueryBuilder addColumn(String columnName) {
        this.fields.add(this.quoteField(columnName));
        return this;
    }

    public QueryBuilder join(JoinType joinType, String joinTable, String relation, String... fields) {
        String joinQuery = "";
        switch (joinType) {
            case INNER:
                joinQuery += "INNER JOIN ";
                break;
            case LEFT:
                joinQuery += "LEFT JOIN ";
                break;
            case RIGHT:
                joinQuery += "RIGHT JOIN";
                break;
        }

        joinQuery += String.format("%s ON %s", joinTable, relation);
        this.joins.add(joinQuery);
        for (String field : fields) {
            this.fields.add(field);
        }

        return this;
    }

    public QueryBuilder joinLeft(String joinTable, String relation, String... fields) {
        return this.join(JoinType.LEFT, joinTable, relation, fields);
    }

    public QueryBuilder joinInner(String joinTable, String relation, String... fields) {
        return this.join(JoinType.INNER,  joinTable, relation, fields);
    }

    public QueryBuilder joinRight(String joinTable, String relation, String... fields) {
        return this.join(JoinType.RIGHT,  joinTable, relation, fields);
    }

    public String build() {
        String query = "";

        switch (this.queryType) {
            case SELECT:
                query = String.format(
                        "SELECT %s FROM %s %s %s",
                        String.join(", ", this.fields),
                        this.buildTableAndJoins(),
                        this.buildWhereString(),
                        this.orders.size() > 0 ? String.join(", ", this.orders) : ""
                );
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

        this.fields.clear();
        this.whereClause.clear();
        this.joins.clear();
        this.orders.clear();
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
        String selector = this.quoteField(this.table);
        selector += this.joins.isEmpty() ? "" : " " + String.join(" ", this.joins);
        return selector;
    }

    public static String quoteField(String field) {
        return "`" + field + "`";
    }

    public static String escapeValue(String value) {
        return '"' + value + '"';
    }
}

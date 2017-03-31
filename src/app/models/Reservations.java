package app.models;

import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Thez on 3/30/2017.
 */
public class Reservations extends Model {
    public Reservations() {
        super();
        this.table = "reservations";
    }

    public void reserve(String bookTitle, Date currentDate, int userid, int bookid, HashMap bookUpdate, Books books) {

        HashMap newReserve = new HashMap();
        newReserve.put("title", bookTitle);
        newReserve.put("date_left", currentDate);
        newReserve.put("user_id", userid);
        newReserve.put("book_id", bookid);
        this.insert(newReserve);


        bookUpdate.put("currently_reserved", "currently_reserved +1");
        books.update(bookUpdate, bookid);


    }

    public HashMap getByUserAndBookAndTitle(String userId, String bookId, String title) {
        HashMap<String, String> result = new HashMap();
        try {
            String selectQuery = ("select * from reservations where user_id=" + userId + " AND book_id=" + bookId + " AND title=" + title);
            System.out.println(selectQuery);
            PreparedStatement statement = (PreparedStatement) this.connection.getConnection().prepareStatement(selectQuery);

            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnLabel(i);
                    if (this.hiddenFields.contains(columnName)) {
                        continue;
                    }

                    result.put(columnName, resultSet.getString(i));
                }
            }

            statement.getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public int getEarliestDate(String title) {
        int userid = 0;
        HashMap<String, String> result = new HashMap();
        result.clear();
        try {
            String selectQuery = "SELECT MIN(date_left), (user_id) FROM reservations where title=" + "'" + title + "'";
            PreparedStatement statement = (PreparedStatement) this.connection.getConnection().prepareStatement(selectQuery);
            System.out.println(selectQuery);

            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {

                userid = resultSet.getInt("user_id");


            }


        } catch (SQLException e) {

        }
        return userid;
    }
}
package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.DatabaseHelp;
/**
 * Created by Userpc on 11/19/2016.
 */

public class PersistentTransactionDAO implements TransactionDAO {
    private Context context;


    public PersistentTransactionDAO(Context context) {
        this.context = context;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        DatabaseHelp handler = DatabaseHelp.getInstance(context);
        SQLiteDatabase db = handler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(handler.accountNo, accountNo);
        values.put(handler.date, convertDateToString(date));
        values.put(handler.amount, amount);
        values.put(handler.expenseType, expenseType.toString());

        db.insert(handler.transactionTable,null,values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return getPaginatedTransactionLogs(0);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        DatabaseHelp handler = DatabaseHelp.getInstance(context);
        SQLiteDatabase db = handler.getWritableDatabase();


        String query = "SELECT "+ handler.accountNo + ", " +
                handler.date + ", " +
                handler.expenseType+", " +
                handler.amount +
                " FROM " + handler.transactionTable + " ORDER BY " + handler.transaction_id + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Transaction> transactionLogs = new ArrayList<>();


        while (cursor.moveToNext())
        {
            try {

                ExpenseType expenseType = null;
                if (cursor.getString(cursor.getColumnIndex(handler.expenseType)).equals(ExpenseType.INCOME.toString())) {
                    expenseType = ExpenseType.INCOME;
                }
                else{
                    expenseType = ExpenseType.EXPENSE;
                }

                String dateString = cursor.getString(cursor.getColumnIndex(handler.date));
                Date date = convertStringToDate(dateString);

                Transaction tans = new Transaction(
                        date,
                        cursor.getString(cursor.getColumnIndex(handler.accountNo)),
                        expenseType,
                        cursor.getDouble(cursor.getColumnIndex(handler.amount)));

                transactionLogs.add(tans);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return transactionLogs;
    }


    public static String convertDateToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(date);
        return dateString;

    }

    public static Date convertStringToDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date strDate = dateFormat.parse(date);
        return strDate;
    }
}

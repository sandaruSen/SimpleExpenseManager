package lk.ac.mrt.cse.dbs.simpleexpensemanager.db;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Userpc on 11/19/2016.
 */

public class DatabaseHelp extends SQLiteOpenHelper {
    protected static final String dbName = "140575D";
    private static DatabaseHelp databaseHelp = null;
    private static final int database_version = 1;
    public static final String accountTable = "Accounts";
    public static final String accountNoNo = "accountNo";
    public static final String bankName = "bankName";
    public static final String accountHolderName = "accountHolderName";
    public static final String balance = "balance";
    public static final String transactionTable = "transations";
    public static final String transaction_id = "transaction_id";
    public static final String date = "date";
    public static final String accountNo = "accountNo";
    public static final String expenseType = "expenseType";
    public static final String amount = "amount";

    public DatabaseHelp(Context context) {
        super(context, dbName, null, database_version);
    }

    public static DatabaseHelp getInstance(Context context) {
        if (databaseHelp == null)
            databaseHelp = new DatabaseHelp(context);
        return databaseHelp;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String account_Table = String.format("CREATE TABLE %s(%s VARCHAR(20) NOT NULL PRIMARY KEY,%s VARCHAR(100) NULL,%s VARCHAR(100) NULL,%s DECIMAL(10,2) NULL )", "Accounts", accountNoNo, bankName, accountHolderName, balance);

        String transaction_Table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,%s VARCHAR(100) NOT NULL,%s DATE NULL,%s DECIMAL(10,2) NULL,%s VARCHAR(100) NULL, FOREIGN KEY(%s) REFERENCES %s(%s))", "transactions", transaction_id, accountNo, date, amount, expenseType, accountNo, accountTable, accountNoNo);

        sqLiteDatabase.execSQL(account_Table);
        sqLiteDatabase.execSQL(transaction_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + accountTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + transactionTable);
        onCreate(sqLiteDatabase);

    }
}

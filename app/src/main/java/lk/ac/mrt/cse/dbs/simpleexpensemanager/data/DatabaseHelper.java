package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Account.db";
    public static final String TABLE_ACCOUNT = "account_table";
    public static final String TABLE_TRANSACTION = "transaction_table";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_ACCOUNT + " (AccountNo TEXT Primary key, Bank TEXT, AccountHolder TEXT, Balance REAL)");
        sqLiteDatabase.execSQL("create table " + TABLE_TRANSACTION + " (Date TEXT , AccountNo TEXT, ExpenseType TEXT, Amount REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_ACCOUNT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_TRANSACTION);
        onCreate(sqLiteDatabase);
    }

    public boolean insertRowAccount(String AccNo, String Bank, String Holder, Double Balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("AccountNo", AccNo);
        cv.put("Bank", Bank);
        cv.put("AccountHolder", Holder);
        cv.put("Balance",Balance);
        long result = db.insert(TABLE_ACCOUNT,null,cv);
        if (result== -1){
            return  false;
        }
        else{
            return  true;
        }
    }

    public boolean insertRowTransaction(Date date, String accountNo, ExpenseType expenseType, double amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Date", dateFormat.format(date));
        cv.put("AccountNo", accountNo);
        //System.out.println("exp" + expenseType + expenseType.toString());
        cv.put("ExpenseType", expenseType.toString());
        cv.put("Amount",amount);
        long result = db.insert(TABLE_TRANSACTION,null,cv);
        if (result== -1){
            return  false;
        }
        else{
            return  true;
        }
    }
     public Cursor getAllTransactions(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor transactions = db.rawQuery("select * from "+ TABLE_TRANSACTION,null);
        return transactions;
     }

    public Cursor getLimitedTransactions(int limit){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor limitTransactions = db.rawQuery("select * from "+ TABLE_TRANSACTION+" limit "+limit,null);
        return limitTransactions;
    }

     public Cursor getAllAccountNumbers(){
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor accountNumbers = db.rawQuery("select AccountNo from "+ TABLE_ACCOUNT,null);
         System.out.println(accountNumbers);
         return accountNumbers;
     }

    public Cursor getAllAccounts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor accounts = db.rawQuery("select * from "+ TABLE_ACCOUNT,null);
        return accounts;
    }

    public Cursor getAccount(String AccNo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor account = db.rawQuery("select * from " + TABLE_ACCOUNT + " where AccountNo = \'" +AccNo + "\'",null);
        return account;
    }

    public void removeAccount(String AccNo){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor account = db.rawQuery("delete from " + TABLE_ACCOUNT + " where AccountNo = \'" +AccNo + "\'",null);
    }

//    public void updateAccount(String AccNo, String Bank, String Holder, Double Balance){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("AccountNo", AccNo);
//        cv.put("Bank", Bank);
//        cv.put("AccountHolder", Holder);
//        cv.put("Balance",Balance);
//        db.update(TABLE_ACCOUNT,null,"AccNo=" + AccNo ,null);
//    }
}
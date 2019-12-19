package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private DatabaseHelper dbHelper;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private PersistentAccountDAO account;

    public PersistentTransactionDAO(DatabaseHelper dbHelper, PersistentAccountDAO account){
        this.dbHelper = dbHelper;
        this.account = account;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        dbHelper.insertRowTransaction(date,accountNo,expenseType,amount);
//        try {
//            account.updateBalance(accountNo,expenseType,amount);
//        } catch (InvalidAccountException ignored) {
//
//        }

    }

    @Override
    public ArrayList<Transaction> getAllTransactionLogs() {

        Cursor res = dbHelper.getAllTransactions();
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        while (res.moveToNext()) {
            Date dateAdjust = new Date();
            try {
                dateAdjust = dateFormat.parse(res.getString(0));

            } catch (ParseException ignored) {


            }System.out.println(res.getString(2));
            ExpenseType exType;
            if (res.getString(2).equals("EXPENSE")) {
                exType = ExpenseType.EXPENSE;
            } else {
                exType = ExpenseType.INCOME;
            }
            Double amount = Double.parseDouble(res.getString(3));
            Transaction tr = new Transaction(dateAdjust, res.getString(1), exType, amount);
            transactions.add(tr);

        }return transactions;
    }


    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

//        Cursor res = dbHelper.getLimitedTransactions(limit);
        ArrayList<Transaction> transactions = getAllTransactionLogs();

        int size = transactions.size();

        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);


//        while (res.moveToNext()) {
//            Date dateAdjust = new Date();
//            try {
//                dateAdjust = dateFormat.parse(res.getString(0));
//
//            } catch (ParseException ignored) {
//
//
//            }
//            System.out.println(res.getString(2));
//            ExpenseType exType;
//            if (res.getString(2).equals("EXPENSE")) {
//                exType = ExpenseType.EXPENSE;
//            } else {
//                exType = ExpenseType.INCOME;
//            }
//            Double amount = Double.parseDouble(res.getString(3));
//            Transaction tr = new Transaction(dateAdjust, res.getString(1), exType, amount);
//            transactions.add(tr);
//        }

    }
}

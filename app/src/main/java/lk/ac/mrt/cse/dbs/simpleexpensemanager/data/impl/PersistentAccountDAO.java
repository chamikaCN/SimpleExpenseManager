package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private DatabaseHelper dbHelper;
    private  Context context;

    public  PersistentAccountDAO(DatabaseHelper dbHelper, Context context){
        this.dbHelper = dbHelper;
        this.context = context;
    }

    @Override
    public ArrayList<String> getAccountNumbersList() {
        Cursor res = dbHelper.getAllAccountNumbers();
        ArrayList<String> AccountNumbers = new ArrayList<String>();

        while (res.moveToNext()) {
            String Ac = res.getString(0);
            AccountNumbers.add(Ac);

        }return AccountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor res = dbHelper.getAllAccounts();
        ArrayList<Account> Accounts = new ArrayList<Account>();

        while (res.moveToNext()) {
            Account ac = new Account(res.getString(0), res.getString(1),res.getString(2),Double.parseDouble(res.getString(3)));
            Accounts.add(ac);
        }return Accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException{
        Cursor res = dbHelper.getAccount(accountNo);
        Account Acc = null;

        while(res.moveToNext()){
            Acc = new Account(res.getString(0), res.getString(1),res.getString(2),Double.parseDouble(res.getString(3)));
        }
        return Acc;
    }

    @Override
    public void addAccount(Account account) {
        dbHelper.insertRowAccount(account.getAccountNo(),account.getBankName(),account.getAccountHolderName(),account.getBalance());
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        String message = String.valueOf(account.getBalance());
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        System.out.println(account.getBalance());
        //dbHelper.updateAccount(account.getAccountNo(),account.getBankName(),account.getAccountHolderName(),account.getBalance());
    }
}

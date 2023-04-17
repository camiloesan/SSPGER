package mx.uv.fei.dao;

import mx.uv.fei.logic.AccessAccount;

import java.sql.SQLException;
import java.util.List;

public interface IAccessAccount {
    void addAccessAccount(AccessAccount accessAccount) throws SQLException;
    void modifyAccessAccountByUsername(String username, AccessAccount accessAccount) throws SQLException;
    void deleteAccessAccountByUsername(String username) throws SQLException;
    boolean areCredentialsValid(String username, String password) throws SQLException;
    String getAccessAccountTypeByUsername(String username) throws SQLException;
    List<AccessAccount> getListAccessAccounts() throws SQLException;
}

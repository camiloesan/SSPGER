package mx.uv.fei.dao;

import mx.uv.fei.logic.AccessAccount;
import java.sql.SQLException;

public interface IAccessAccount {
    void addAccessAccount(AccessAccount accessAccount) throws SQLException;
    void modifyAccessAccountByUsername(String username, AccessAccount accessAccount) throws SQLException;
    void deleteAccessAccountByName(String username) throws SQLException;
    boolean areCredentialsValid(String username, String password) throws SQLException;
}

package mx.uv.fei.dao;

import mx.uv.fei.logic.AccessAccount;
import java.sql.SQLException;

public interface IAccessAccount {
    void addAccessAccount(AccessAccount accessAccount) throws SQLException;
    void modifyAccessAccount(AccessAccount accessAccount) throws SQLException;
    void deleteAccessAccountByName(String name) throws SQLException;
}

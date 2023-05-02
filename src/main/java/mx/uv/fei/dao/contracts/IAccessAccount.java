package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.AccessAccount;
import mx.uv.fei.logic.Professor;
import mx.uv.fei.logic.Student;

import java.sql.SQLException;
import java.util.List;

public interface IAccessAccount {
    void addAdminAccessAccount(AccessAccount accessAccount) throws SQLException;
    int transactionAddStudentUser(AccessAccount accessAccount, Student student) throws SQLException;
    int transactionAddProfessorUser(AccessAccount accessAccount, Professor professor) throws SQLException;
    void modifyStudentUserTransaction(String username, AccessAccount accessAccount, Student student) throws SQLException;
    void modifyProfessorUserTransaction(String username, AccessAccount accessAccount, Professor professor) throws SQLException;
    void deleteUserByUsername(String username) throws SQLException;
    boolean areCredentialsValid(String username, String password) throws SQLException;
    String getAccessAccountTypeByUsername(String username) throws SQLException;
    List<AccessAccount> getAccessAccountsList() throws SQLException;
    List<String> getUsernamesByUsertype(String userType) throws SQLException;
}

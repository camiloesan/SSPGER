package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.Advancement;

import java.sql.SQLException;
import java.util.List;

public interface IAdvancement {
    int addAdvancement(Advancement advancement) throws SQLException;
    Advancement getAdvancementDetailById(int advancementId) throws SQLException;
    List<Advancement> getAdvancementListByProjectId(int projectId) throws SQLException;
    List<Advancement> getListAdvancementNamesByProfessorId(int professorID) throws SQLException;
    int modifyAdvancementById(int advancementId, Advancement advancement) throws SQLException;
    int deleteAdvancementById(int advancementId) throws SQLException;
    List<Advancement> getListAdvancementNamesByStudentId(String studentID) throws SQLException;
    List<Advancement> getAdvancementByStudentID(String studentID) throws SQLException;
    String getProjectNameByStudentID(String studentID) throws SQLException;
}

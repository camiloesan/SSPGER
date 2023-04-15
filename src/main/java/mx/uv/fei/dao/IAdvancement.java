package mx.uv.fei.dao;

import mx.uv.fei.logic.Advancement;

import java.sql.SQLException;
import java.util.List;

public interface IAdvancement {
    int addAdvancement(Advancement advancement) throws SQLException;
    List<Advancement> getAdvancementDetailByName(String advancementName) throws SQLException;
    List<Advancement> getAdvancementListByProjectName(String projectName) throws SQLException;
    int modifyAdvancementByName(String advancementName, Advancement advancement) throws SQLException;
    int deleteAdvancementByName(String advancement) throws SQLException;
}

package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.*;
import mx.uv.fei.logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class EvidenceDAOTest {

    @BeforeEach
    void setUp() throws SQLException {
        var projectDAO = new ProjectDAO();
        var project = new Project();

        project.setAcademicBodyId("UV-CA-127");
        project.setInvestigationProjectName("example");
        project.setLGAC_Id(1);
        project.setInvestigationLine("example");
        project.setApproximateDuration("example");
        project.setModalityId(1);
        project.setReceptionWorkName("example");
        project.setRequisites("exanple");
        project.setStudentsParticipating(1);
        project.setInvestigationProjectDescription("example");
        project.setReceptionWorkDescription("example");
        project.setExpectedResults("example");
        project.setRecommendedBibliography("example");

        projectDAO.addProject(project);

        var advancement = new Advancement();
        var advancementDAO = new AdvancementDAO();

        advancement.setAdvancementName("example");
        advancement.setAdvancementDescription("example");
        advancement.setAdvancementStartDate("2023-01-01");
        advancement.setAdvancementDeadline("2024-01-01");
        advancement.setProjectId(projectDAO.getProjectIDByTitle("example"));

        advancementDAO.addAdvancement(advancement);

        var accessAccount = new AccessAccount();
        var userDAO = new UserDAO();

        accessAccount.setUsername("example");
        accessAccount.setUserPassword("example");
        accessAccount.setUserType("example");

        userDAO.addAdminUser(accessAccount);

        var student = new Student();
        var studentDAO = new StudentDAO();

        student.setStudentID("example");
        student.setName("example");
        student.setLastName("example");
        student.setAcademicEmail("example");
        student.setUsername("example");

        studentDAO.insertStudent(student);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testAddProjectEvidenceShouldNotAdd() throws SQLException {

    }

    @Test
    void updateProjectEvidenceGradeById() {
    }

    @Test
    void getProjectEvidenceByStudentId() {
    }
}
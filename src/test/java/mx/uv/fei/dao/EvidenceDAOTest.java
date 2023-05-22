package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.*;
import mx.uv.fei.logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        var projectRequest = new ProjectRequest();
        var projectRequestDAO = new ProjectRequestDAO();

        projectRequest.setProjectID(projectDAO.getProjectIDByTitle("example"));
        projectRequest.setStudentId("example");
        projectRequest.setDescription("example");

        projectRequestDAO.createProjectRequest(projectRequest);

        projectRequestDAO.validateProjectRequest("Aceptado",
                projectRequestDAO.getProjecRequestIDByDescription("example"));
    }

    @AfterEach
    void tearDown() throws SQLException {
        var advancementDAO = new AdvancementDAO();

        advancementDAO.deleteAdvancementById(advancementDAO.getAdvancementIDByStudentID("example"));

        var projectDAO = new ProjectDAO();

        projectDAO.deleteProjectByTitle("example");

        var studentDAO = new StudentDAO();

        studentDAO.deleteStudent("example");

        var userDAO = new UserDAO();

        userDAO.deleteUserByUsername("example");

    }

    @Test
    void testAddEvidenceSucces() throws SQLException {
        var advancementDAO = new AdvancementDAO();
        var evidenceDAO = new EvidenceDAO();
        var evidence = new Evidence();

        evidence.setEvidenceTitle("example");
        evidence.setEvidenceDescription("example");
        evidence.setAdvancementId(advancementDAO.getAdvancementIDByStudentID("example"));

        int expectedResult = 1;
        int result = evidenceDAO.addEvidence(evidence);
        assertEquals(expectedResult,result);
    }

    @Test
    void testModifyEvidenceSucces() throws SQLException {
        var advancementDAO = new AdvancementDAO();
        var evidenceDAO = new EvidenceDAO();
        var evidence = new Evidence();

        evidence.setEvidenceTitle("example");
        evidence.setEvidenceDescription("example");
        evidence.setAdvancementId(advancementDAO.getAdvancementIDByStudentID("example"));

        evidenceDAO.addEvidence(evidence);
        var evidenceToModify = evidenceDAO.getEvidenceByEvidenceTitle("example");

        evidenceToModify.setEvidenceTitle("exampleModify");

        int expectedResult = 1;
        int result = evidenceDAO.modifyEvidence(evidenceToModify);
        assertEquals(expectedResult,result);
    }

    @Test
    void testUpdateGradeEvidenceSucces() throws SQLException {
        var advancementDAO = new AdvancementDAO();
        var evidenceDAO = new EvidenceDAO();
        var evidence = new Evidence();

        evidence.setEvidenceTitle("example");
        evidence.setEvidenceDescription("example");
        evidence.setAdvancementId(advancementDAO.getAdvancementIDByStudentID("example"));

        evidenceDAO.addEvidence(evidence);
        var evidenceToUpdateGrade = evidenceDAO.getEvidenceByEvidenceTitle("example");

        int expectedResult = 1;
        int result = evidenceDAO.updateEvidenceGradeById(evidenceToUpdateGrade.getEvidenceId(), 10);
        assertEquals(expectedResult,result);
    }

    @Test
    void testDeleteEvidenceSucces() throws SQLException {
        var advancementDAO = new AdvancementDAO();
        var evidenceDAO = new EvidenceDAO();
        var evidence = new Evidence();

        evidence.setEvidenceTitle("example");
        evidence.setEvidenceDescription("example");
        evidence.setAdvancementId(advancementDAO.getAdvancementIDByStudentID("example"));

        evidenceDAO.addEvidence(evidence);
        var evidenceResult = evidenceDAO.getEvidenceByEvidenceTitle("example");

        int expectedResult = 1;
        int result = evidenceDAO.deleteEvidenceByID(evidenceResult.getEvidenceId());
        assertEquals(expectedResult,result);
    }

}
package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.*;
import mx.uv.fei.logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        student.setStudentID("example");
        student.setName("example");
        student.setLastName("example");
        student.setUsername("example");


        var projectRequest = new ProjectRequest();
        var projectRequestDAO = new ProjectRequestDAO();

        projectRequest.setProjectID(projectDAO.getProjectIDByTitle("example"));
        projectRequest.setStudentId("example");
        projectRequest.setDescription("example");

        projectRequestDAO.createProjectRequest(projectRequest);

        projectRequestDAO.validateProjectRequest("Aceptado",
                projectRequestDAO.getProjectRequestIDByStudentID("example"));
    }

    @AfterEach
    void tearDown() throws SQLException {
        var advancementDAO = new AdvancementDAO();

        advancementDAO.deleteAdvancementById(advancementDAO.getAdvancementIDByAdvancementName("example"));

        var projectDAO = new ProjectDAO();

        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("example"));

        var userDAO = new UserDAO();

        userDAO.deleteUserByUsername("example");

    }

    @Test
    void testAddEvidenceSuccess() throws SQLException {
        var advancementDAO = new AdvancementDAO();
        var evidenceDAO = new EvidenceDAO();
        var evidence = new Evidence();

        evidence.setEvidenceTitle("example");
        evidence.setEvidenceDescription("example");
        evidence.setAdvancementId(advancementDAO.getAdvancementIDByAdvancementName("example"));

        int expectedResult = 1;
        int result = evidenceDAO.addEvidence(evidence);
        assertEquals(expectedResult,result);
    }

    @Test
    void testModifyEvidenceSuccess() throws SQLException {
        var advancementDAO = new AdvancementDAO();
        var evidenceDAO = new EvidenceDAO();
        var evidence = new Evidence();

        evidence.setEvidenceTitle("example");
        evidence.setEvidenceDescription("example");
        evidence.setAdvancementId(advancementDAO.getAdvancementIDByAdvancementName("example"));

        evidenceDAO.addEvidence(evidence);
        var evidenceToModify = evidenceDAO.getEvidenceByEvidenceID(evidenceDAO.
                getEvidenceIDByEvidenceTitle("example"));

        evidenceToModify.setEvidenceTitle("exampleModify");

        int expectedResult = 1;
        int result = evidenceDAO.modifyEvidence(evidenceToModify);
        assertEquals(expectedResult,result);
    }

    @Test
    void testUpdateGradeEvidenceSuccess() throws SQLException {
        var advancementDAO = new AdvancementDAO();
        var evidenceDAO = new EvidenceDAO();
        var evidence = new Evidence();

        evidence.setEvidenceTitle("example");
        evidence.setEvidenceDescription("example");
        evidence.setAdvancementId(advancementDAO.getAdvancementIDByAdvancementName("example"));

        evidenceDAO.addEvidence(evidence);
        var evidenceToUpdateGrade = evidenceDAO.getEvidenceByEvidenceID(evidenceDAO
                .getEvidenceIDByEvidenceTitle("example"));

        int expectedResult = 1;
        int result = evidenceDAO.updateEvidenceGradeById(evidenceToUpdateGrade.getEvidenceId(), 10);
        assertEquals(expectedResult,result);
    }

    @Test
    void testDeleteEvidenceSuccess() throws SQLException {
        var advancementDAO = new AdvancementDAO();
        var evidenceDAO = new EvidenceDAO();
        var evidence = new Evidence();

        evidence.setEvidenceTitle("example");
        evidence.setEvidenceDescription("example");
        evidence.setAdvancementId(advancementDAO.getAdvancementIDByAdvancementName("example"));

        evidenceDAO.addEvidence(evidence);
        var evidenceResult = evidenceDAO.getEvidenceByEvidenceID(evidenceDAO
                .getEvidenceIDByEvidenceTitle("example"));

        int expectedResult = 1;
        int result = evidenceDAO.deleteEvidenceByID(evidenceResult.getEvidenceId());
        assertEquals(expectedResult,result);
    }

}
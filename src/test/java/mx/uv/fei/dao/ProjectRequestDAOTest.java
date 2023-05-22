package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.logic.AccessAccount;
import mx.uv.fei.logic.Project;
import mx.uv.fei.logic.ProjectRequest;
import mx.uv.fei.logic.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.DataTruncation;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;

import static org.junit.jupiter.api.Assertions.*;

class ProjectRequestDAOTest {

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
    }

    @AfterEach
    void tearDown() throws SQLException{
        var projectDAO = new ProjectDAO();

        projectDAO.deleteProjectByTitle("example");

        var userDAO = new UserDAO();

        userDAO.deleteUserByUsername("example");

        var studentDAO = new StudentDAO();

        studentDAO.deleteStudent("example");
    }

    @Test
    void testCreateProjectRequestSucces() throws SQLException {
        var projectRequest = new ProjectRequest();
        var projectRequestDAO = new ProjectRequestDAO();
        var projectDAO = new ProjectDAO();

        projectRequest.setProjectID(projectDAO.getProjectIDByTitle("example"));
        projectRequest.setStudentId("example");
        projectRequest.setDescription("example");

        int expectedResult = 1;
        int result = projectRequestDAO.createProjectRequest(projectRequest);
        assertEquals(expectedResult,result);
    }

    @Test
    void testAcceptProjectRequestSucces() throws SQLException {
        var projectRequest = new ProjectRequest();
        var projectRequestDAO = new ProjectRequestDAO();
        var projectDAO = new ProjectDAO();

        projectRequest.setProjectID(projectDAO.getProjectIDByTitle("example"));
        projectRequest.setStudentId("example");
        projectRequest.setDescription("example");

        projectRequestDAO.createProjectRequest(projectRequest);

        int expectedResult = 1;
        int result = projectRequestDAO.validateProjectRequest("Aceptado",
                projectRequestDAO.getProjecRequestIDByDescription("example"));
        assertEquals(expectedResult,result);
    }

    @Test
    void testDeclineProjectRequestSucces() throws SQLException {
        var projectRequest = new ProjectRequest();
        var projectRequestDAO = new ProjectRequestDAO();
        var projectDAO = new ProjectDAO();

        projectRequest.setProjectID(projectDAO.getProjectIDByTitle("example"));
        projectRequest.setStudentId("example");
        projectRequest.setDescription("example");

        projectRequestDAO.createProjectRequest(projectRequest);

        int expectedResult = 1;
        int result = projectRequestDAO.validateProjectRequest("Rechazado",
                projectRequestDAO.getProjecRequestIDByDescription("example"));
        assertEquals(expectedResult,result);
    }

    @Test
    void testIncorrectValidationProjectRequest() throws SQLException {
        var projectRequest = new ProjectRequest();
        var projectRequestDAO = new ProjectRequestDAO();
        var projectDAO = new ProjectDAO();

        projectRequest.setProjectID(projectDAO.getProjectIDByTitle("example"));
        projectRequest.setStudentId("example");
        projectRequest.setDescription("example");

        projectRequestDAO.createProjectRequest(projectRequest);

        assertThrows(SQLTransientConnectionException.class, () ->
                projectRequestDAO.validateProjectRequest("incorrect",
                projectRequestDAO.getProjecRequestIDByDescription("example")));
    }

    @Test
    void testDeleteProjectRequestTest() throws SQLException{
        var projectRequestDAO = new ProjectRequestDAO();
        var projectRequest = new ProjectRequest();
        var projectDAO = new ProjectDAO();

        projectRequest.setProjectID(projectDAO.getProjectIDByTitle("example"));
        projectRequest.setStudentId("example");
        projectRequest.setDescription("exampleDelete");

        projectRequestDAO.addProjectRequestTransaction(projectRequest);

        int expectedResult = 1;
        int result = projectRequestDAO.deleteProjectRequest(
                projectRequestDAO.getProjecRequestIDByDescription("exampleDelete"));
        assertEquals(expectedResult, result);
    }
}
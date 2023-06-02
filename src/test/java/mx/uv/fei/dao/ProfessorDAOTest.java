package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.dao.implementations.ProfessorDAO;
import mx.uv.fei.logic.Professor;
import mx.uv.fei.logic.AccessAccount;
import mx.uv.fei.logic.Project;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorDAOTest {
    @BeforeEach
    void setUp() throws SQLException {
        var userDAO = new UserDAO();
        var projectDAO = new ProjectDAO();
        
        var testAccessAccount = new AccessAccount();
        var testProfessor = new Professor();
        testAccessAccount.setUsername("testAcc1");
        testAccessAccount.setUserPassword("testPass1");
        testAccessAccount.setUserType("Profesor");
        testProfessor.setProfessorName("ProfeNom1");
        testProfessor.setProfessorLastName("ProfeAp1");
        testProfessor.setProfessorEmail("exaple1@mail.com");
        testProfessor.setProfessorDegree("Dr.");
        userDAO.addProfessorUserTransaction(testAccessAccount,testProfessor);
        
        var testAccessAccount2 = new AccessAccount();
        var testProfessor2 = new Professor();
        testAccessAccount2.setUsername("testAcc2");
        testAccessAccount2.setUserPassword("testPass2");
        testAccessAccount2.setUserType("Profesor");
        testProfessor2.setProfessorName("ProfeNom2");
        testProfessor2.setProfessorLastName("ProfeAp2");
        testProfessor2.setProfessorEmail("exaple2@mail.com");
        testProfessor2.setProfessorDegree("Dr.");
        userDAO.addProfessorUserTransaction(testAccessAccount2,testProfessor2);
        
        var projectDirectors = new Project();
        projectDirectors.setAcademicBodyId("UV-CA-127");
        projectDirectors.setInvestigationProjectName("proyecto investigación");
        projectDirectors.setLGAC_Id(1);
        projectDirectors.setInvestigationLine("linea investigación");
        projectDirectors.setApproximateDuration("12 meses");
        projectDirectors.setModalityId(1);
        projectDirectors.setReceptionWorkName("trabajo recepcional");
        projectDirectors.setRequisites("requisitos");
        projectDirectors.setDirectorName("Dr. ProfeNom1 ProfeAp1");
        projectDirectors.setCodirectorName("Dr. ProfeNom2 ProfeAp2");
        projectDirectors.setStudentsParticipating(1);
        projectDirectors.setInvestigationProjectDescription("descripción de investigación");
        projectDirectors.setReceptionWorkDescription("descripción trabajo recepcional");
        projectDirectors.setExpectedResults("resultados esperados");
        projectDirectors.setRecommendedBibliography("bibliografía");
        projectDAO.addProject(projectDirectors);
        projectDAO.setDirectorIDtoProject(projectDirectors);
        projectDAO.setCodirectorIDtoProject(projectDirectors);
        
        var noDirectorsProject = new Project();
        noDirectorsProject.setAcademicBodyId("UV-CA-127");
        noDirectorsProject.setInvestigationProjectName("proyecto investigación SIN DIRECTORES");
        noDirectorsProject.setLGAC_Id(1);
        noDirectorsProject.setInvestigationLine("linea investigación SIN DIRECTORES");
        noDirectorsProject.setApproximateDuration("12 meses");
        noDirectorsProject.setModalityId(1);
        noDirectorsProject.setReceptionWorkName("trabajo recepcional SIN DIRECTORES");
        noDirectorsProject.setRequisites("requisitos SIN DIRECTORES");
        noDirectorsProject.setDirectorName("director");
        noDirectorsProject.setCodirectorName("codirector");
        noDirectorsProject.setStudentsParticipating(1);
        noDirectorsProject.setInvestigationProjectDescription("descripción de investigación SIN DIRECTORES");
        noDirectorsProject.setReceptionWorkDescription("descripción trabajo recepcional SIN DIRECTORES");
        noDirectorsProject.setExpectedResults("resultados esperados SIN DIRECTORES");
        noDirectorsProject.setRecommendedBibliography("bibliografía SIN DIRECTORES");
        projectDAO.addProject(noDirectorsProject);
    }
    
    @AfterEach
    void tearDown() throws SQLException {
        var userDAO = new UserDAO();
        var projectDAO = new ProjectDAO();
        
        userDAO.deleteUserByUsername("testAcc1");
        userDAO.deleteUserByUsername("testAcc2");
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("trabajo recepcional"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("trabajo recepcional SIN DIRECTORES"));
    }
    
    @Test
    void testGetProfessorNames() throws SQLException {
        System.out.println("Test getProfessorNames");
        var professorDAO = new ProfessorDAO();
        
        List<String> expectedList = new ArrayList<>();
        expectedList.add("Dr. ProfeNom1 ProfeAp1");
        expectedList.add("Dr. ProfeNom2 ProfeAp2");
        
        List<String> actualList = new ArrayList<>(professorDAO.getProfessorsNames());
        
        System.out.println("Expected List:");
        for (String name : expectedList) {
            System.out.println(name);
            System.out.println("--------------");
        }
        
        System.out.println("Actual List:");
        for (String name : actualList) {
            System.out.println(name);
            System.out.println("--------------");
        }
        
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetProfessorNamesDifferentResult() throws SQLException{
        System.out.println("Test empty list getProfessorNames");
        var professorDAO = new ProfessorDAO();
        
        List<String> expectedList = new ArrayList<>();
        expectedList.add("Dr. ProfeNom3 ProfeAp3");
        expectedList.add("Dr. ProfeNom4 ProfeAp4");
        
        var actualList = new ArrayList<>(professorDAO.getProfessorsNames());
        
        System.out.println("Expected List:");
        for (String name : expectedList) {
            System.out.println(name);
            System.out.println("--------------");
        }
        
        System.out.println("Actual List:");
        for (String name : actualList) {
            System.out.println(name);
            System.out.println("--------------");
        }
        
        assertNotSame(expectedList, actualList);
    }
    
    @Test
    void testGetDirectorsByProject() throws SQLException {
        System.out.println("Test getDirectorsByProject");
        var professorDAO = new ProfessorDAO();
        var projectDAO = new ProjectDAO();
        
        String expectedString = "Dr. ProfeNom1 ProfeAp1, Dr. ProfeNom2 ProfeAp2";
        
        String actualString = professorDAO.getDirectorsByProject(projectDAO.getProjectIDByTitle("trabajo recepcional"));
        
        System.out.println("Expected String:");
        System.out.println(expectedString);
        System.out.println("--------------");
        System.out.println("Actual String:");
        System.out.println(actualString);
        System.out.println("--------------");
        
        assertEquals(expectedString,actualString);
    }
    
    @Test
    void testGetDirectorsByProjectEmptyString() throws SQLException {
        System.out.println("Test empty String getDirectorsByProject");
        var professorDAO = new ProfessorDAO();
        var projectDAO = new ProjectDAO();
        
        String actualString = professorDAO.getDirectorsByProject(projectDAO.getProjectIDByTitle("trabajo recepcional SIN DIRECTORES"));
        System.out.println("Actual String:");
        System.out.println(actualString);
        System.out.println("--------------");
        
        assertNull(actualString);
    }
}
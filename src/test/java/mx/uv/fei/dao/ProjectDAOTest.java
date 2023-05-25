package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectDAOTest {

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
        
        var declinedProject = new Project();
        declinedProject.setAcademicBodyId("UV-CA-127");
        declinedProject.setInvestigationProjectName("proyecto investigación DECLINADO");
        declinedProject.setLGAC_Id(1);
        declinedProject.setInvestigationLine("linea investigación DECLINADO");
        declinedProject.setApproximateDuration("12 meses");
        declinedProject.setModalityId(1);
        declinedProject.setReceptionWorkName("trabajo recepcional DECLINADO");
        declinedProject.setRequisites("requisitos DECLINADO");
        declinedProject.setDirectorName("director");
        declinedProject.setCodirectorName("codirector");
        declinedProject.setStudentsParticipating(1);
        declinedProject.setInvestigationProjectDescription("descripción de investigación DECLINADO");
        declinedProject.setReceptionWorkDescription("descripción trabajo recepcional DECLINADO");
        declinedProject.setExpectedResults("resultados esperados DECLINADO");
        declinedProject.setRecommendedBibliography("bibliografía DECLINADO");
        projectDAO.addProject(declinedProject);
        projectDAO.updateProjectState(projectDAO.getProjectIDByTitle("trabajo recepcional DECLINADO"),"Declinado");
        
        var verifiedProject = new Project();
        verifiedProject.setAcademicBodyId("UV-CA-127");
        verifiedProject.setInvestigationProjectName("proyecto investigación VERIFICADO");
        verifiedProject.setLGAC_Id(1);
        verifiedProject.setInvestigationLine("linea investigación VERIFICADO");
        verifiedProject.setApproximateDuration("12 meses");
        verifiedProject.setModalityId(1);
        verifiedProject.setReceptionWorkName("trabajo recepcional VERIFICADO");
        verifiedProject.setRequisites("requisitos VERIFICADO");
        verifiedProject.setDirectorName("director");
        verifiedProject.setCodirectorName("codirector");
        verifiedProject.setStudentsParticipating(1);
        verifiedProject.setInvestigationProjectDescription("descripción de investigación VERIFICADO");
        verifiedProject.setReceptionWorkDescription("descripción trabajo recepcional VERIFICADO");
        verifiedProject.setExpectedResults("resultados esperados VERIFICADO");
        verifiedProject.setRecommendedBibliography("bibliografía VERIFICADO");
        projectDAO.addProject(verifiedProject);
        projectDAO.updateProjectState(projectDAO.getProjectIDByTitle("trabajo recepcional VERIFICADO"),"Verificado");
        
        var unverifiedProject = new Project();
        unverifiedProject.setAcademicBodyId("UV-CA-127");
        unverifiedProject.setInvestigationProjectName("proyecto investigación POR REVISAR");
        unverifiedProject.setLGAC_Id(1);
        unverifiedProject.setInvestigationLine("linea investigación POR REVISAR");
        unverifiedProject.setApproximateDuration("12 meses");
        unverifiedProject.setModalityId(1);
        unverifiedProject.setReceptionWorkName("trabajo recepcional POR REVISAR");
        unverifiedProject.setRequisites("requisitos POR REVISAR");
        unverifiedProject.setDirectorName("director");
        unverifiedProject.setCodirectorName("codirector");
        unverifiedProject.setStudentsParticipating(1);
        unverifiedProject.setInvestigationProjectDescription("descripción de investigación POR REVISAR");
        unverifiedProject.setReceptionWorkDescription("descripción trabajo recepcional POR REVISAR");
        unverifiedProject.setExpectedResults("resultados esperados POR REVISAR");
        unverifiedProject.setRecommendedBibliography("bibliografía POR REVISAR");
        projectDAO.addProject(unverifiedProject);
    }

    @AfterEach
    void tearDown() throws SQLException {
        var userDAO = new UserDAO();
        var projectDAO = new ProjectDAO();
        
        userDAO.deleteUserByUsername("testAcc1");
        userDAO.deleteUserByUsername("testAcc2");
        
        projectDAO.deleteProjectByTitle("trabajo recepcional DECLINADO");
        projectDAO.deleteProjectByTitle("trabajo recepcional VERIFICADO");
        projectDAO.deleteProjectByTitle("trabajo recepcional POR REVISAR");
        projectDAO.deleteProjectByTitle("Ejemplo trabajo recepcional");
        projectDAO.deleteProjectByTitle("Ejemplo trabajo setDirector");
        projectDAO.deleteProjectByTitle("Ejemplo trabajo setCodirector");
        
    }

    @Test
    void testAddProject() throws SQLException {
        System.out.println("Test addProject");
        var projectDAO = new ProjectDAO();
        var project = new Project();
        
        project.setAcademicBodyId("UV-CA-127");
        project.setInvestigationProjectName("Ejemplo proyecto investigación");
        project.setLGAC_Id(1);
        project.setInvestigationLine("Ejemplo linea investigación");
        project.setApproximateDuration("12 meses");
        project.setModalityId(1);
        project.setReceptionWorkName("Ejemplo trabajo recepcional");
        project.setRequisites("Ejemplo requisitos");
        project.setDirectorName("Ejemplo director");
        project.setCodirectorName("Ejemplo codirector");
        project.setStudentsParticipating(1);
        project.setInvestigationProjectDescription("Ejemplo descripción de investigación");
        project.setReceptionWorkDescription("Ejemplo descripción trabajo recepcional");
        project.setExpectedResults("Ejemplo resultados esperados");
        project.setRecommendedBibliography("Ejemplo bibliografía");
        
        int expectedResult = 1;
        int actualResult = projectDAO.addProject(project);
        
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetProjectsByStateVerified() throws SQLException {
        System.out.println("Test getProjectsByState with projectState parameter = 'Verificado'");
        var projectDAO = new ProjectDAO();
        
        var expectedList = new ArrayList<>();
        var simpleVerifiedProject = new SimpleProject();
        simpleVerifiedProject.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional VERIFICADO"));
        simpleVerifiedProject.setProjectTitle("trabajo recepcional VERIFICADO");
        simpleVerifiedProject.setProjectState("Verificado");
        expectedList.add(simpleVerifiedProject);
        
        var actualList = new ArrayList<>(projectDAO.getProjectsByState("Verificado"));
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetProjectsByStateDeclined() throws SQLException {
        System.out.println("Test getProjectsByState with projectState parameter = 'Declinado'");
        var projectDAO = new ProjectDAO();

        var expectedList = new ArrayList<>();
        var simpleDeclinedProject = new SimpleProject();
        simpleDeclinedProject.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional DECLINADO"));
        simpleDeclinedProject.setProjectTitle("trabajo recepcional DECLINADO");
        simpleDeclinedProject.setProjectState("Declinado");
        expectedList.add(simpleDeclinedProject);
        
        var actualList = new ArrayList<>(projectDAO.getProjectsByState("Declinado"));
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetProjectsByStateUnverified() throws SQLException {
        System.out.println("Test getProjectsByState with projectState parameter = 'Por revisar'");
        var projectDAO = new ProjectDAO();
        
        var expectedList = new ArrayList<>();
        var simpleUnverifiedProject = new SimpleProject();
        simpleUnverifiedProject.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional POR REVISAR"));
        simpleUnverifiedProject.setProjectTitle("trabajo recepcional POR REVISAR");
        simpleUnverifiedProject.setProjectState("Por revisar");
        expectedList.add(simpleUnverifiedProject);
        
        var actualList = new ArrayList<>(projectDAO.getProjectsByState("Por revisar"));
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetLgacList() throws SQLException {
        System.out.println("Test getLgacList");
        ProjectDAO projectDAO = new ProjectDAO();
        
        List<String> expectedList = new ArrayList<>();
        expectedList.add("L1. Gestión, modelado y desarrollo de software");
        expectedList.add("L2. Tecnologías de software");
        
        List<String> actualList = new ArrayList<>(projectDAO.getLgacList());
        
        assertEquals(expectedList, actualList);
    }
    
    @Test
    void testGetRWModalitiesList() throws SQLException {
        System.out.println("Test getRWModalitiesList");
        ProjectDAO projectDAO = new ProjectDAO();
        
        List<String> expectedList = new ArrayList<>();
        expectedList.add("Monografía");
        expectedList.add("Revisión Multivocal de la Literatura");
        expectedList.add("Revisión Sistemática de la Literatura");
        expectedList.add("Tesis");
        expectedList.add("Trabajo Práctico-Técnico");
        
        List<String> actualList = new ArrayList<>(projectDAO.getRWModalitiesList());
        
        assertEquals(expectedList, actualList);
    }
    
    @Test
    void testGetAcademicBodyIDs() throws SQLException {
        System.out.println("Test getAcademicBodyIDs");
        var projectDAO = new ProjectDAO();
        
        List<String> expectedList = new ArrayList<>();
        expectedList.add("UV-CA-127");
        
        List<String> actualList = new ArrayList<>(projectDAO.getAcademicBodyIDs());
        assertEquals(expectedList, actualList);
    }

    @Test
    void testUpdateProjectState() throws SQLException {
        System.out.println("Test updateProjectState");
        
    }
    
    @Test
    void testSetDirectorIDtoProject() throws SQLException {
        System.out.println("Test setDirectorIDtoProject");
        var projectDAO = new ProjectDAO();
        
        var projectDirectorsSet = new Project();
        projectDirectorsSet.setAcademicBodyId("UV-CA-127");
        projectDirectorsSet.setInvestigationProjectName("Ejemplo proyecto investigación");
        projectDirectorsSet.setLGAC_Id(1);
        projectDirectorsSet.setInvestigationLine("Ejemplo linea investigación");
        projectDirectorsSet.setApproximateDuration("12 meses");
        projectDirectorsSet.setModalityId(1);
        projectDirectorsSet.setReceptionWorkName("Ejemplo trabajo setDirector");
        projectDirectorsSet.setRequisites("Ejemplo requisitos");
        projectDirectorsSet.setDirectorName("Dr. ProfeNom1 ProfeAp2");
        projectDirectorsSet.setCodirectorName("Dr. ProfeNom2 ProfeAp2");
        projectDirectorsSet.setStudentsParticipating(1);
        projectDirectorsSet.setInvestigationProjectDescription("Ejemplo descripción de investigación");
        projectDirectorsSet.setReceptionWorkDescription("Ejemplo descripción trabajo recepcional");
        projectDirectorsSet.setExpectedResults("Ejemplo resultados esperados");
        projectDirectorsSet.setRecommendedBibliography("Ejemplo bibliografía");
        projectDAO.addProject(projectDirectorsSet);
        
        int expectedResult = 1;
        int actualResult = projectDAO.setDirectorIDtoProject(projectDirectorsSet);
        assertEquals(expectedResult,actualResult);
    }
    
    @Test
    void testSetCodirectorIDtoProject() throws SQLException {
        System.out.println("Test setCodirectorIDtoProject");
        var projectDAO = new ProjectDAO();
        
        var projectCodirectorsSet = new Project();
        projectCodirectorsSet.setAcademicBodyId("UV-CA-127");
        projectCodirectorsSet.setInvestigationProjectName("Ejemplo proyecto investigación");
        projectCodirectorsSet.setLGAC_Id(1);
        projectCodirectorsSet.setInvestigationLine("Ejemplo linea investigación");
        projectCodirectorsSet.setApproximateDuration("12 meses");
        projectCodirectorsSet.setModalityId(1);
        projectCodirectorsSet.setReceptionWorkName("Ejemplo trabajo setCodirector");
        projectCodirectorsSet.setRequisites("Ejemplo requisitos");
        projectCodirectorsSet.setDirectorName("Dr. ProfeNom1 ProfeAp2");
        projectCodirectorsSet.setCodirectorName("Dr. ProfeNom2 ProfeAp2");
        projectCodirectorsSet.setStudentsParticipating(1);
        projectCodirectorsSet.setInvestigationProjectDescription("Ejemplo descripción de investigación");
        projectCodirectorsSet.setReceptionWorkDescription("Ejemplo descripción trabajo recepcional");
        projectCodirectorsSet.setExpectedResults("Ejemplo resultados esperados");
        projectCodirectorsSet.setRecommendedBibliography("Ejemplo bibliografía");
        projectDAO.addProject(projectCodirectorsSet);
        
        int expectedResult = 1;
        int actualResult = projectDAO.setCodirectorIDtoProject(projectCodirectorsSet);
        assertEquals(expectedResult,actualResult);
    }
}
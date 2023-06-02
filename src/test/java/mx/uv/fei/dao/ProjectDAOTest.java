package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.ProfessorDAO;
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
        
        var projectDetails = new Project();
        projectDetails.setAcademicBodyId("UV-CA-127");
        projectDetails.setInvestigationProjectName("proyecto investigación DETALLE");
        projectDetails.setLGAC_Id(1);
        projectDetails.setInvestigationLine("linea investigación DETALLE");
        projectDetails.setApproximateDuration("12 meses");
        projectDetails.setModalityId(1);
        projectDetails.setReceptionWorkName("trabajo recepcional DETALLE");
        projectDetails.setRequisites("requisitos DETALLE");
        projectDetails.setDirectorName("Dr. ProfeNom1 ProfeAp1");
        projectDetails.setCodirectorName("Dr. ProfeNom2 ProfeAp2");
        projectDetails.setStudentsParticipating(1);
        projectDetails.setInvestigationProjectDescription("descripción de investigación DETALLE");
        projectDetails.setReceptionWorkDescription("descripción trabajo recepcional DETALLE");
        projectDetails.setExpectedResults("resultados esperados DETALLE");
        projectDetails.setRecommendedBibliography("bibliografía DETALLE");
        projectDAO.addProject(projectDetails);
        projectDAO.setDirectorIDtoProject(projectDetails);
        projectDAO.setCodirectorIDtoProject(projectDetails);
        
        var projectCollaboration = new Project();
        projectCollaboration.setAcademicBodyId("UV-CA-127");
        projectCollaboration.setInvestigationProjectName("proyecto investigación COLABORACIÓN");
        projectCollaboration.setLGAC_Id(1);
        projectCollaboration.setInvestigationLine("linea investigación COLABORACIÓN");
        projectCollaboration.setApproximateDuration("12 meses");
        projectCollaboration.setModalityId(1);
        projectCollaboration.setReceptionWorkName("trabajo recepcional COLABORACIÓN");
        projectCollaboration.setRequisites("requisitos COLABORACIÓN");
        projectCollaboration.setDirectorName("Dr. ProfeNom1 ProfeAp1");
        projectCollaboration.setCodirectorName("Dr. ProfeNom2 ProfeAp2");
        projectCollaboration.setStudentsParticipating(1);
        projectCollaboration.setInvestigationProjectDescription("descripción de investigación COLABORACIÓN");
        projectCollaboration.setReceptionWorkDescription("descripción trabajo recepcional COLABORACIÓN");
        projectCollaboration.setExpectedResults("resultados esperados COLABORACIÓN");
        projectCollaboration.setRecommendedBibliography("bibliografía COLABORACIÓN");
        projectDAO.addProject(projectCollaboration);
        
        projectDAO.setDirectorIDtoProject(projectCollaboration);
        projectDAO.setCodirectorIDtoProject(projectCollaboration);
    }

    @AfterEach
    void tearDown() throws SQLException {
        var userDAO = new UserDAO();
        var projectDAO = new ProjectDAO();
        
        userDAO.deleteUserByUsername("testAcc1");
        userDAO.deleteUserByUsername("testAcc2");
        
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("trabajo recepcional DECLINADO"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("trabajo recepcional VERIFICADO"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("trabajo recepcional POR REVISAR"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("trabajo recepcional DETALLE"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("trabajo recepcional COLABORACIÓN"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Ejemplo trabajo recepcional"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Ejemplo trabajo setDirector"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Ejemplo trabajo setCodirector"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Ejemplo trabajo para Verificar"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Ejemplo trabajo para Declinar"));
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
        projectDirectorsSet.setDirectorName("Dr. ProfeNom1 ProfeAp1");
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
        projectCodirectorsSet.setDirectorName("Dr. ProfeNom1 ProfeAp1");
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
    
    @Test
    void testUpdateProjectStateToVerified() throws SQLException {
        System.out.println("Test updateProjectState");
        var projectDAO = new ProjectDAO();
        
        var projectToUpdate = new Project();
        projectToUpdate.setAcademicBodyId("UV-CA-127");
        projectToUpdate.setInvestigationProjectName("Ejemplo proyecto investigación");
        projectToUpdate.setLGAC_Id(1);
        projectToUpdate.setInvestigationLine("Ejemplo linea investigación");
        projectToUpdate.setApproximateDuration("12 meses");
        projectToUpdate.setModalityId(1);
        projectToUpdate.setReceptionWorkName("Ejemplo trabajo para Verificar");
        projectToUpdate.setRequisites("Ejemplo requisitos");
        projectToUpdate.setDirectorName("Dr. ProfeNom1 ProfeAp1");
        projectToUpdate.setCodirectorName("Dr. ProfeNom2 ProfeAp2");
        projectToUpdate.setStudentsParticipating(1);
        projectToUpdate.setInvestigationProjectDescription("Ejemplo descripción de investigación");
        projectToUpdate.setReceptionWorkDescription("Ejemplo descripción trabajo recepcional");
        projectToUpdate.setExpectedResults("Ejemplo resultados esperados");
        projectToUpdate.setRecommendedBibliography("Ejemplo bibliografía");
        projectDAO.addProject(projectToUpdate);
        
        int expectedResult = 1;
        int actualResult = projectDAO.updateProjectState(projectDAO.getProjectIDByTitle("Ejemplo trabajo para Verificar"),"Verificado");
        
        assertEquals(expectedResult,actualResult);
    }
    
    @Test
    void testUpdateProjectStateToDeclined() throws SQLException {
        System.out.println("Test updateProjectState");
        var projectDAO = new ProjectDAO();
        
        var projectToUpdate = new Project();
        projectToUpdate.setAcademicBodyId("UV-CA-127");
        projectToUpdate.setInvestigationProjectName("Ejemplo proyecto investigación");
        projectToUpdate.setLGAC_Id(1);
        projectToUpdate.setInvestigationLine("Ejemplo linea investigación");
        projectToUpdate.setApproximateDuration("12 meses");
        projectToUpdate.setModalityId(1);
        projectToUpdate.setReceptionWorkName("Ejemplo trabajo para Declinar");
        projectToUpdate.setRequisites("Ejemplo requisitos");
        projectToUpdate.setDirectorName("Dr. ProfeNom1 ProfeAp1");
        projectToUpdate.setCodirectorName("Dr. ProfeNom2 ProfeAp2");
        projectToUpdate.setStudentsParticipating(1);
        projectToUpdate.setInvestigationProjectDescription("Ejemplo descripción de investigación");
        projectToUpdate.setReceptionWorkDescription("Ejemplo descripción trabajo recepcional");
        projectToUpdate.setExpectedResults("Ejemplo resultados esperados");
        projectToUpdate.setRecommendedBibliography("Ejemplo bibliografía");
        projectDAO.addProject(projectToUpdate);
        
        int expectedResult = 1;
        int actualResult = projectDAO.updateProjectState(projectDAO.getProjectIDByTitle("Ejemplo trabajo para declinar"),"Declinado");
        
        assertEquals(expectedResult,actualResult);
    }

    @Test
    void testGetProjectsByStateVerified() throws SQLException {
        System.out.println("Test getProjectsByState with projectState parameter = 'Verificado'");
        var projectDAO = new ProjectDAO();
        
        var expectedList = new ArrayList<>();
        var simpleVerifiedProject = new SimpleProject();
        simpleVerifiedProject.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional VERIFICADO"));
        simpleVerifiedProject.setReceptionWorkName("trabajo recepcional VERIFICADO");
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
        simpleDeclinedProject.setReceptionWorkName("trabajo recepcional DECLINADO");
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
        simpleUnverifiedProject.setReceptionWorkName("trabajo recepcional POR REVISAR");
        simpleUnverifiedProject.setProjectState("Por revisar");
        
        var simpleUnverifiedProject2 = new SimpleProject();
        simpleUnverifiedProject2.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional DETALLE"));
        simpleUnverifiedProject2.setReceptionWorkName("trabajo recepcional DETALLE");
        simpleUnverifiedProject2.setProjectState("Por revisar");
        
        var simpleUnverifiedProject3 = new SimpleProject();
        simpleUnverifiedProject3.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional COLABORACIÓN"));
        simpleUnverifiedProject3.setReceptionWorkName("trabajo recepcional COLABORACIÓN");
        simpleUnverifiedProject3.setProjectState("Por revisar");
        
        expectedList.add(simpleUnverifiedProject);
        expectedList.add(simpleUnverifiedProject2);
        expectedList.add(simpleUnverifiedProject3);
        
        var actualList = new ArrayList<>(projectDAO.getProjectsByState("Por revisar"));
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetAllProjects() throws SQLException {
        System.out.println("Test getAllProjects");
        var projectDAO = new ProjectDAO();
        
        var expectedList = new ArrayList<>();
        
        var simpleDeclinedProject = new SimpleProject();
        simpleDeclinedProject.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional DECLINADO"));
        simpleDeclinedProject.setReceptionWorkName("trabajo recepcional DECLINADO");
        simpleDeclinedProject.setProjectState("Declinado");
        
        var simpleVerifiedProject = new SimpleProject();
        simpleVerifiedProject.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional VERIFICADO"));
        simpleVerifiedProject.setReceptionWorkName("trabajo recepcional VERIFICADO");
        simpleVerifiedProject.setProjectState("Verificado");
        
        var simpleUnverifiedProject = new SimpleProject();
        simpleUnverifiedProject.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional POR REVISAR"));
        simpleUnverifiedProject.setReceptionWorkName("trabajo recepcional POR REVISAR");
        simpleUnverifiedProject.setProjectState("Por revisar");
        
        var simpleProjectDetail = new SimpleProject();
        simpleProjectDetail.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional DETALLE"));
        simpleProjectDetail.setReceptionWorkName("trabajo recepcional DETALLE");
        simpleProjectDetail.setProjectState("Por revisar");
        
        var simpleProjectCollaboration = new SimpleProject();
        simpleProjectCollaboration.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional COLABORACIÓN"));
        simpleProjectCollaboration.setReceptionWorkName("trabajo recepcional COLABORACIÓN");
        simpleProjectCollaboration.setProjectState("Por revisar");
        
        expectedList.add(simpleDeclinedProject);
        expectedList.add(simpleVerifiedProject);
        expectedList.add(simpleUnverifiedProject);
        expectedList.add(simpleProjectDetail);
        expectedList.add(simpleProjectCollaboration);
        
        var actualList = new ArrayList<>(projectDAO.getAllProjects());
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetProjectsByCollaboration() throws SQLException {
        System.out.println("Test getProjectsByCollaboration");
        var projectDAO = new ProjectDAO();
        var professorDAO = new ProfessorDAO();
        
        List<SimpleProject> expectedList = new ArrayList<>();
        var simpleCollaborationProject = new SimpleProject();
        
        simpleCollaborationProject.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional COLABORACIÓN"));
        simpleCollaborationProject.setReceptionWorkName("trabajo recepcional COLABORACIÓN");
        simpleCollaborationProject.setProjectState("Por revisar");
        
        var simpleCollaborationProject2 = new SimpleProject();
        simpleCollaborationProject2.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional DETALLE"));
        simpleCollaborationProject2.setReceptionWorkName("trabajo recepcional DETALLE");
        simpleCollaborationProject2.setProjectState("Por revisar");
        
        expectedList.add(simpleCollaborationProject2);
        expectedList.add(simpleCollaborationProject);
        
        var actualList = new ArrayList<>(projectDAO.getProjectsByParticipation(professorDAO.getProfessorIdByUsername("testAcc1")));
        
        // Mostrar expectedList
        System.out.println("Contenido de expectedList:");
        for (SimpleProject project : expectedList) {
            System.out.println("ID: " + project.getProjectID());
            System.out.println("Título: " + project.getReceptionWorkName());
            System.out.println("Estado: " + project.getProjectState());
            System.out.println("--------------------");
        }
        
        // Mostrar actualList
        System.out.println("Contenido de actualList:");
        for (SimpleProject project : actualList) {
            System.out.println("ID: " + project.getProjectID());
            System.out.println("Título: " + project.getReceptionWorkName());
            System.out.println("Estado: " + project.getProjectState());
            System.out.println("--------------------");
        }
        
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetProjectInfoByID() throws SQLException {
        System.out.println("Test getProjectInfoByID");
        var projectDAO = new ProjectDAO();
        
        DetailedProject expectedDetailedProject = new DetailedProject();
        expectedDetailedProject.setProjectID(projectDAO.getProjectIDByTitle("trabajo recepcional DETALLE"));
        expectedDetailedProject.setAcademicBodyName("Ingeniería y Tecnología de Software");
        expectedDetailedProject.setInvestigationProjectName("proyecto investigación DETALLE");
        expectedDetailedProject.setLgacName("L1. Gestión, modelado y desarrollo de software");
        expectedDetailedProject.setInvestigationLine("linea investigación DETALLE");
        expectedDetailedProject.setApproxDuration("12 meses");
        expectedDetailedProject.setReceptionWorkModality("Monografía");
        expectedDetailedProject.setReceptionWorkName("trabajo recepcional DETALLE");
        expectedDetailedProject.setRequisites("requisitos DETALLE");
        expectedDetailedProject.setDirector("Dr. ProfeNom1 ProfeAp1");
        expectedDetailedProject.setCoDirector("Dr. ProfeNom2 ProfeAp2");
        expectedDetailedProject.setNumberStudents(1);
        expectedDetailedProject.setInvestigationDescription("descripción de investigación DETALLE");
        expectedDetailedProject.setReceptionWorkDescription("descripción trabajo recepcional DETALLE");
        expectedDetailedProject.setExpectedResults("resultados esperados DETALLE");
        expectedDetailedProject.setBibliography("bibliografía DETALLE");
        expectedDetailedProject.setProjectState("Por revisar");
        
        DetailedProject actualDetailedProject = projectDAO.getProjectInfoByID(projectDAO.getProjectIDByTitle("trabajo recepcional DETALLE"));
        
        // Mostrar los atributos del objeto esperado
        System.out.println("Objeto esperado:");
        System.out.println(expectedDetailedProject);
        
        // Mostrar los atributos del objeto actual
        System.out.println("Objeto actual:");
        System.out.println(expectedDetailedProject);
        
        assertEquals(expectedDetailedProject, actualDetailedProject);
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
    void testDeleteProjectByTitle() throws SQLException {
        System.out.println("Test deleteProjectByTitle");
        var projectDAO = new ProjectDAO();
        
        var projectCollaboration = new Project();
        projectCollaboration.setAcademicBodyId("UV-CA-127");
        projectCollaboration.setInvestigationProjectName("proyecto investigación ELIMINAR");
        projectCollaboration.setLGAC_Id(1);
        projectCollaboration.setInvestigationLine("linea investigación ELIMINAR");
        projectCollaboration.setApproximateDuration("12 meses");
        projectCollaboration.setModalityId(1);
        projectCollaboration.setReceptionWorkName("trabajo recepcional ELIMINAR");
        projectCollaboration.setRequisites("requisitos ELIMINAR");
        projectCollaboration.setDirectorName("Dr. ProfeNom1 ProfeAp1");
        projectCollaboration.setCodirectorName("Dr. ProfeNom2 ProfeAp2");
        projectCollaboration.setStudentsParticipating(1);
        projectCollaboration.setInvestigationProjectDescription("descripción de investigación ELIMINAR");
        projectCollaboration.setReceptionWorkDescription("descripción trabajo recepcional ELIMINAR");
        projectCollaboration.setExpectedResults("resultados esperados ELIMINAR");
        projectCollaboration.setRecommendedBibliography("bibliografía ELIMINAR");
        projectDAO.addProject(projectCollaboration);
        
        int expectedResult = 1;
        int actualResult = projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("trabajo recepcional ELIMINAR"));
        assertEquals(expectedResult,actualResult);
    }
}
package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.Project;
import mx.uv.fei.logic.SimpleProject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectDAOTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAddProject() throws SQLException {
        System.out.println("Test addProject");
        ProjectDAO projectDAO = new ProjectDAO();
        Project project = new Project();
        
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
        ProjectDAO projectDAO = new ProjectDAO();
        
        List<SimpleProject> expectedList = new ArrayList<>();
        SimpleProject simpleProject1 = new SimpleProject();
        simpleProject1.setProjectID(6);
        simpleProject1.setProjectTitle("Estado del arte de los recomendadores de código basados en Inteligencia Artificial en el proceso de desarrollo de software");
        
        SimpleProject simpleProject2 = new SimpleProject();
        simpleProject2.setProjectID(11);
        simpleProject2.setProjectTitle("Análisis de las tecnologías para el desarrollo de Development Bots");
        
        SimpleProject simpleProject3 = new SimpleProject();
        simpleProject3.setProjectID(12);
        simpleProject3.setProjectTitle("Recomendaciones de Accesibilidad para el Desarrollo de Software");
        
        expectedList.add(simpleProject1);
        expectedList.add(simpleProject2);
        expectedList.add(simpleProject3);
        
        List<SimpleProject> actualList = new ArrayList<>(projectDAO.getProjectsByState("Verificado"));
        
        assertEquals(expectedList,actualList);
    }
    @Test
    void testGetProjectsByStateDeclined() throws SQLException {
        System.out.println("Test getProjectsByState with projectState parameter = 'Declinado'");
        ProjectDAO projectDAO = new ProjectDAO();
        
        List<SimpleProject> expectedList = new ArrayList<>();/*
        SimpleProject simpleProject1 = new SimpleProject();
        ** Add projects that have the attribute projectState as 'Declinado'
        
        expectedList.add(simpleProject1);

        */
        List<SimpleProject> actualList = new ArrayList<>(projectDAO.getProjectsByState("Declinado"));
        
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetProjectsByStateUnverified() throws SQLException {
        System.out.println("Test getProjectsByState with projectState parameter = 'Por revisar'");
        ProjectDAO projectDAO = new ProjectDAO();
        
        List<SimpleProject> expectedList = new ArrayList<>();/*
        SimpleProject simpleProject1 = new SimpleProject();
        ** Add projects that have the attribute projectState as 'Por revisar'
        
        expectedList.add(simpleProject1);
        */
        List<SimpleProject> actualList = new ArrayList<>(projectDAO.getProjectsByState("Por revisar"));
        
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
        ProjectDAO projectDAO = new ProjectDAO();
        
        List<String> expectedList = new ArrayList<>();
        expectedList.add("UV-CA-127");
        
        List<String> actualList = new ArrayList<>(projectDAO.getAcademicBodyIDs());
        
        assertEquals(expectedList, actualList);
    }

    @Test
    void testUpdateProjectState() throws SQLException {
        System.out.println("Test updateProjectState");
        
    }
}
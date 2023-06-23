package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

class
AdvancementDAOTest {
    Professor professor = new Professor();
    Project project = new Project();
    Advancement advancement = new Advancement();
    Student student = new Student();

    @BeforeEach
    void setUp() throws SQLException {
        UserDAO userDAO = new UserDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();

        AdvancementDAO advancementDAO = new AdvancementDAO();
        AccessAccount accessAccountStudent = new AccessAccount();
        AccessAccount accessAccountProfessor = new AccessAccount();

        accessAccountProfessor.setUsername("juaperez");
        accessAccountProfessor.setUserType("Profesor");
        accessAccountProfessor.setUserPassword("juaperez2020");
        accessAccountProfessor.setUserEmail("juaperez@uv.mx");
        professor.setProfessorName("Juan Carlos");
        professor.setProfessorLastName("Pérez Arriaga");
        professor.setProfessorDegree("MCC.");

        accessAccountStudent.setUsername("zs21013862");
        accessAccountStudent.setUserType("Estudiante");
        accessAccountStudent.setUserEmail("zs21013869@estudiantes.uv.mx");
        accessAccountStudent.setUserPassword("zs2101!@");
        student.setName("Camilo");
        student.setLastName("Espejo Sánchez");
        student.setStudentID("zs21013869");

        project.setAcademicBodyId("UV-CA-127");
        project.setInvestigationProjectName(
                "Hacia un Modelo de Campus Accesible: Facultad de Estadística e Informática");
        project.setLGAC_Id(1);
        project.setInvestigationLine("Estudio de los diversos métodos y enfoques para la" +
                "gestión, modelado y desarrollo de software, de manera " +
                "que se obtenga software de calidad. Gestión de las " +
                "diversas etapas del proceso de desarrollo");
        project.setApproximateDuration("12 meses");
        project.setModalityId(1);
        project.setReceptionWorkName("Diversidad en equipos de desarrollo y su relación con la " +
                "calidad de software");
        project.setRequisites("Tecnologías para la construcción de software, Principios " +
                "de Construcción de Software, Diseño de Software, " +
                "Procesos de Software, Administración de proyectos");
        project.setDirectorName("Juan Carlos Pérez Arriaga");
        project.setCodirectorName("Ángel Juan Sánchez García");
        project.setStudentsParticipating(1);
        project.setInvestigationProjectDescription("En la medida que el software cobra mayor relevancia " +
                "en las actividades diarias de una " +
                "sociedad, el interés por tener productos de software confiables se incrementa. Algunos " +
                "estudios muestran una alta prevalencia de problemas de calidad en los productos de " +
                "software que se desarrollan y la relación de estos problemas con la experiencia y " +
                "capacitación de programadores. Por otra parte, ha habido un avance considerable en el " +
                "diseño de estrategias para mejora de la calidad del software, sin embargo, en algunas " +
                "organizaciones existe resistencia en la adopción de prácticas orientadas a la incorporación " +
                "de dichas estrategias. Por otra parte, el aspecto humano juega un papel fundamental en el " +
                "desarrollo de productos de software de calidad. Existen estudios donde se definen algunas " +
                "hipótesis orientadas a validar si aspectos de diversidad de personalidad están relacionadas " +
                "positivamente en la calidad del producto final. ");
        project.setReceptionWorkDescription(
                "El presente trabajo tiene como finalidad, realizar una revisión sistemática de la literatura " +
                "sobre la diversidad (personalidad, género, entre otros aspectos) de los equipos de desarrollo " +
                "y la relación que guardan con la calidad de los productos de software que desarrollan. " +
                "Adicionalmente se pretende que la revisión sistemática documente el impacto que tienen los " +
                "hallazgos identificados en la revisión en los productos de software. ");
        project.setExpectedResults("Documento que contenga: " +
                "• Reporte de la revisión sistemática de la literatura " +
                "• Artículo para publicación en evento académico");
        project.setRecommendedBibliography("""
                V. Pieterse, M. Leeu and M. van Eekelen, "How personality diversity influences team
                performance in student software engineering teams," 2018 Conference on Information
                Communications Technology and Society (ICTAS), 2018, pp. 1-6, doi:
                10.1109/ICTAS.2018.8368749.
                Meiyappan Nagappan, Thomas Zimmermann, and Christian Bird. 2013. Diversity in
                software engineering research. In Proceedings of the 2013 9th Joint Meeting on
                Foundations of Software Engineering (ESEC/FSE 2013). Association for Computing
                Machinery, New York, NY, USA, 466–476. https://doi.org/10.1145/2491411.2491415
                Luiz Fernando Capretz and Faheem Ahmed. 2010. Why do we need personality diversity in
                software engineering? SIGSOFT Softw. Eng. Notes 35, 2 (March 2010), 1–11.
                https://doi.org/10.1145/1734103.1734111
                G. Catolino, F. Palomba, D. A. Tamburri, A. Serebrenik and F. Ferrucci, "Gender
                Diversity and Women in Software Teams: How Do They Affect Community Smells?," 2019
                IEEE/ACM 41st International Conference on Software Engineering: Software Engineering
                in Society (ICSE-SEIS), 2019, pp. 11-20, doi: 10.1109/ICSE-SEIS.2019.00010.""");

        userDAO.addStudentUserTransaction(accessAccountStudent, student);
        userDAO.addProfessorUserTransaction(accessAccountProfessor, professor);
        projectDAO.addProject(project);

        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectID(projectDAO.getProjectIDByTitle(project.getReceptionWorkName()));
        projectRequest.setStudentId("zs21013869");
        projectRequest.setDescription(
                "Quisiera estar en este proyecto porque a mi me interesa mucho el tema desde el inicio de la" +
                " carrera y considero que sería de gran ayuda en mi formación no tan solo como estudiante, " +
                        "sino como profesionista.");
        projectRequestDAO.createProjectRequest(projectRequest);
        projectRequestDAO.validateProjectRequest("Aceptado",
                projectRequestDAO.getProjectRequestIDByStudentID("zs21013869"));

        advancement.setAdvancementName("Entrega final");
        advancement.setAdvancementDescription("Se incluyen todos los archivos y conclusión general " +
                "de todos los avances anteriores");
        advancement.setAdvancementStartDate("2023-12-02");
        advancement.setAdvancementDeadline("2023-12-02");
        advancement.setProjectId(projectDAO.getProjectIDByTitle(
                "Diversidad en equipos de desarrollo y su relación con la " +
                "calidad de software"));
        advancementDAO.addAdvancement(advancement);
    }

    @AfterEach
    void tearDown() throws SQLException {
        UserDAO userDAO = new UserDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        projectRequestDAO.deleteProjectRequest(projectRequestDAO.getProjectRequestIDByStudentID("zs21013862"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle(project.getReceptionWorkName()));
        userDAO.deleteUserByUsername("juaperez");
        userDAO.deleteUserByUsername("zs21013862");
    }

    @Test
    void testAddAdvancementProjectSuccess() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        Advancement advancement1 = new Advancement();
        ProjectDAO projectDAO = new ProjectDAO();
        advancement1.setAdvancementName("Primera Entrega Parcial");
        advancement1.setAdvancementDescription(
                "El alumno se encargará de recopilar la información de la primera fase," +
                "esto significa un entregable con todos los puntos marcados en la rúbrica siguiente: " +
                "Portada, Introducción, Desarrollo del problema, Conclusión temporal del trabajo.");
        advancement1.setAdvancementStartDate("2023-06-29");
        advancement1.setAdvancementDeadline("2023-09-27");
        advancement1.setProjectId(projectDAO.getProjectIDByTitle(project.getReceptionWorkName()));

        int expectedResult = 1;
        assertEquals(expectedResult, advancementDAO.addAdvancement(advancement1));
    }

    @Test
    void testAddAdvancementProjectNull() {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        Advancement advancement1 = new Advancement();

        assertThrows(SQLIntegrityConstraintViolationException.class, () -> advancementDAO.addAdvancement(advancement1));
    }

    @Test
    void testAddAdvancementProjectIdDoesNotExist() {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        Advancement advancement1 = new Advancement();
        advancement1.setAdvancementName("Primera Entrega Parcial");
        advancement1.setAdvancementDescription(
                "El alumno se encargará de recopilar la información de la primera fase," +
                "esto significa un entregable con todos los puntos marcados en la rúbrica siguiente: " +
                "Portada, Introducción, Desarrollo del problema, Conclusión temporal del trabajo.");
        advancement1.setAdvancementStartDate("2023-06-29");
        advancement1.setAdvancementDeadline("2023-09-27");
        advancement1.setProjectId(0);
        assertThrows(SQLException.class, () -> advancementDAO.addAdvancement(advancement1));
    }

    @Test
    void testAddAdvancementWrongDateFormat() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement1 = new Advancement();
        advancement1.setAdvancementName("Planeación personal del proyecto");
        advancement1.setAdvancementDescription("Entregar una planeación en un documento tipo PDF que contenga" +
                " tu planeación para el proyecto.");
        advancement1.setAdvancementStartDate("27-13-2023");
        advancement1.setAdvancementDeadline("2023-06-27");
        advancement1.setProjectId(projectDAO.getProjectIDByTitle("Ejemplo trabajo recepcional"));
        assertThrows(SQLException.class, () -> advancementDAO.addAdvancement(advancement1));
    }

    @Test
    void testAddAdvancementAdvancementNameTooLong() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement1 = new Advancement();
        advancement1.setAdvancementName("Este es un nombre de avance que sobrepasa los limites de la base de datos");
        advancement1.setAdvancementDescription("Descripciion para prueba de titulo de avance demaisado largo");
        advancement1.setAdvancementStartDate("2023-05-29");
        advancement1.setAdvancementDeadline("2023-06-27");
        advancement1.setProjectId(projectDAO.getProjectIDByTitle("Ejemplo trabajo recepcional"));
        assertThrows(SQLException.class, () -> advancementDAO.addAdvancement(advancement1));
    }

    @Test
    void testGetAdvancementListByProjectIdShouldBeCorrect() {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        int id = 0;
        assertDoesNotThrow(() -> advancementDAO.getAdvancementListByProjectId(id));
    }

    @Test
    void testModifyAdvancementByIdSuccess() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement1 = new Advancement();
        advancement1.setAdvancementName("new");
        advancement1.setAdvancementDescription("new");
        advancement1.setAdvancementStartDate("2022-03-24");
        advancement1.setAdvancementDeadline("2024-02-03");
        advancement1.setProjectId(projectDAO.getProjectIDByTitle(project.getReceptionWorkName()));

        assertEquals(1,
                advancementDAO.modifyAdvancementById(advancementDAO.getLastAdvancementID(), advancement1));
    }

    @Test
    void testModifyAdvancementByIdDoesNotExist() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement1 = new Advancement();
        advancement1.setAdvancementName("AvanceID 0");
        advancement1.setAdvancementDescription("Este avance lanzara una excepción");
        advancement1.setAdvancementStartDate("2022-03-24");
        advancement1.setAdvancementDeadline("2024-02-03");
        advancement1.setProjectId(projectDAO.getProjectIDByTitle(project.getReceptionWorkName()));

        assertEquals(0, advancementDAO.modifyAdvancementById(0, advancement1));
    }

    @Test
    void testModifyAdvancementByIdWrongDateFormat() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement1 = new Advancement();
        advancement1.setAdvancementName("Avance para modificar");
        advancement1.setAdvancementDescription("Formato erroneo de fechas");
        advancement1.setAdvancementStartDate("202x2-03-24");
        advancement1.setAdvancementDeadline("2024-02x-03");
        advancement1.setProjectId(projectDAO.getProjectIDByTitle(project.getReceptionWorkName()));
        assertThrows(SQLException.class, () -> advancementDAO.modifyAdvancementById(
                advancementDAO.getLastAdvancementID(), advancement1));
    }

    @Test
    void testModifyAdvancementByIdNameTooLong() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement1 = new Advancement();
        advancement1.setAdvancementName("Este título de avance lanzara una excepción por ser muy largo para modificar");
        advancement1.setAdvancementDescription("Descripcción de avance para modificar, " +
                "con título que sobrepasa los límites");
        advancement1.setAdvancementStartDate("2022-03-24");
        advancement1.setAdvancementDeadline("2024-02-03");
        advancement1.setProjectId(projectDAO.getProjectIDByTitle(project.getReceptionWorkName()));
        assertThrows(SQLException.class, () -> advancementDAO.modifyAdvancementById(
                advancementDAO.getLastAdvancementID(), advancement1));
    }

    @Test
    void testModifyAdvancementByIdWrongAdvancementId() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement1 = new Advancement();
        advancement1.setAdvancementName("Avance modificar sin id");
        advancement1.setAdvancementDescription("Descripcción del avance que se va a modificar " +
                "con el id erronea");
        advancement1.setAdvancementStartDate("2022-03-24");
        advancement1.setAdvancementDeadline("2024-02-03");
        advancement1.setProjectId(projectDAO.getProjectIDByTitle(project.getReceptionWorkName()));

        assertEquals(0, advancementDAO.modifyAdvancementById(0, advancement1));
    }

    @Test
    void testDeleteAdvancementByIdDoesNotExist() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        assertEquals(0, advancementDAO.deleteAdvancementById(0));
    }

}
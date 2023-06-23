package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.*;
import mx.uv.fei.logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackDAOTest {

    @BeforeEach
    void setUp() throws SQLException {
        var projectDAO = new ProjectDAO();
        var project = new Project();

        project.setAcademicBodyId("UV-CA-127");
        project.setInvestigationProjectName("");
        project.setLGAC_Id(1);
        project.setInvestigationLine("Control estadistico de procesos");
        project.setApproximateDuration("6 meses");
        project.setModalityId(3);
        project.setReceptionWorkName("Control Estadístico de Procesos en el desarrollo de Software");
        project.setRequisites(
                "Capacidad de análisis y abstracción, Medición de Software, lectura de documentos en inglés.");
        project.setStudentsParticipating(1);
        project.setInvestigationProjectDescription("");
        project.setReceptionWorkDescription("La mejora del proceso de software basado en la medición es hoy en " +
                "día una actividad obligatoria. Esto implica seguimiento continuo del proceso para predecir su " +
                "comportamiento, resaltar sus variaciones de rendimiento y, si es necesario, reaccionar " +
                "rápidamente a él [1]. Las variaciones del proceso se deben a factores de causas comunes o " +
                "imputables. Los primeros forman parte del proceso en sí mismo, mientras que los segundos " +
                "se deben a eventos excepcionales que resultan en un comportamiento inestable del proceso " +
                "y por lo tanto en menos previsibilidad.\n" +
                "Por otro lado, el Control Estadístico de Procesos (SPC) es un enfoque basado en " +
                "estadísticas capaz de determinar si un proceso es estable o no, discriminando entre la " +
                "presencia de variación de causa común y variación de causa asignable. Esta técnica ha " +
                "demostrado ser efectiva en procesos de fabricación pero aún no en contextos de procesos de " +
                "desarrollo de software. Aquí la experiencia en el uso de SPC aún no está madura. Por lo " +
                "tanto, aún falta una comprensión clara de los resultados del SPC[1]. Aunque muchos " +
                "autores lo han utilizado en software, a menudo no han considerado las principales " +
                "diferencias entre la fabricación o manufactura, y las características del proceso de " +
                "software. Debido a tales diferencias, SPC no puede ser adoptado \"tal cual\", pero puede " +
                "adaptarse.\n" +
                "Por lo tanto, esta Revisión Sistemática de la Literatura tiene como objetivo, presentar el " +
                "estado actual de la aplicación del Control Estadístico de Procesos en el desarrollo de " +
                "Software, con el fin de establecer oportunidades para adoptarlo en el área de desarrollo de " +
                "Software.");
        project.setExpectedResults("- Reporte escrito de la RSL\n- Borrador artículo");
        project.setRecommendedBibliography("[1] Caivano, D. (2005, March). Continuous software process improvement " +
                "through statistical process control. In Ninth European Conference on Software Maintenance and " +
                "Reengineering (pp. 288-293). IEEE.\n" +
                "[2] Fernández-Corrales, C., Jenkins, M., & Villegas, J. (2013, October). Application of " +
                "statistical process control to software defect metrics: An industry experience report.\n" +
                "In 2013 ACM/IEEE International Symposium on Empirical Software Engineering and " +
                "Measurement (pp. 323-331). IEEE.\n" +
                "[3] Jacob, A. L., & Pillai, S. K. (2003). Statistical process control to improve coding and " +
                "code review. IEEE software, 20(3), 50-55.\n" +
                "[4] Maisikeli, S. G. (2020, November). Tracking Stability of Software Evolution Using " +
                "Statistical Process Control Limit. In 2020 Seventh International Conference on " +
                "Information Technology Trends (ITT) (pp. 156-160). IEEE.\n" +
                "[5]Zhang, Y., & Hou, L. (2010, June). Software Design for Quality-oriented Statistical " +
                "Tolerance and SPC. In 2010 Third International Conference on Information and " +
                "Computing (Vol. 3, pp. 127-130). IEEE.");

        projectDAO.addProject(project);

        var studentAccessAccount = new AccessAccount();
        var student = new Student();
        var userDAO = new UserDAO();

        studentAccessAccount.setUsername("Gerardo");
        studentAccessAccount.setUserPassword("contrasenagerardo");
        studentAccessAccount.setUserEmail("zs21050285@estudiantes.uv.mx");
        studentAccessAccount.setUserType("Estudiante");

        student.setUsername("Gerardo");
        student.setName("Gerardo");
        student.setLastName("Martinez Rebolledo");
        student.setStudentID("ZS21050285");

        userDAO.addStudentUserTransaction(studentAccessAccount, student);

        var professorAccessAccount = new AccessAccount();
        var professor = new Professor();

        professorAccessAccount.setUsername("angesanchez");
        professorAccessAccount.setUserPassword("contrasenaangesanchez");
        professorAccessAccount.setUserEmail("angesanchez@uv.mx");
        professorAccessAccount.setUserType("Profesor");

        professor.setProfessorName("Angel Juan");
        professor.setProfessorLastName("Sanchez Garcia");
        professor.setProfessorDegree("Dr.");
        professor.setUsername("angesanchez");

        userDAO.addProfessorUserTransaction(professorAccessAccount, professor);

        project.setDirectorName("Dr. Angel Juan Sanchez Gracia");
        projectDAO.setDirectorIDtoProject(project);

        var advancement = new Advancement();
        var advancementDAO = new AdvancementDAO();

        advancement.setProjectId(projectDAO.getProjectIDByTitle(
                "Control Estadístico de Procesos en el desarrollo de Software"));
        advancement.setAdvancementStartDate("2023-12-31");
        advancement.setAdvancementDeadline("2024-12-31");
        advancement.setAdvancementName("Avance para pruebas");
        advancement.setAdvancementDescription("Este avance se utilizará para realizar pruebas");

        advancementDAO.addAdvancement(advancement);

        var evidence = new Evidence();
        var evidenceDAO = new EvidenceDAO();

        evidence.setEvidenceTitle("Evidencia para prueba");
        evidence.setEvidenceDescription("Esta evidencia es entregada para una prueba");
        evidence.setAdvancementId(advancementDAO.getLastAdvancementID());
        evidence.setStudentId("ZS21050285");

        evidenceDAO.addEvidence(evidence);
    }

    @AfterEach
    void tearDown() throws SQLException {
        var projectDAO = new ProjectDAO();

        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle(
                "Control Estadístico de Procesos en el desarrollo de Software"));

        var userDAO = new UserDAO();

        userDAO.deleteUserByUsername("Gerardo");
        userDAO.deleteUserByUsername("angesanchez");

    }

    @Test
    void testAddFeedbackSuccess() throws SQLException {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        Feedback feedback = new Feedback();

        feedback.setEvidenceID(evidenceDAO.getLastEvidenceID());
        feedback.setFeedbackText("En primer lugar, quiero destacar la claridad y la organización de tu " +
                "documentación. Es evidente que has dedicado tiempo y esfuerzo en presentar la información de manera " +
                "estructurada y fácil de seguir. Los objetivos del proyecto, los requisitos y las funcionalidades " +
                "están claramente definidos, lo que demuestra una comprensión sólida del alcance del trabajo.");

        int expectedResult = 1;
        assertEquals(expectedResult, feedbackDAO.addFeedback(feedback));
    }

    @Test
    void testAddFeedbackDataTooLong() throws SQLException {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        Feedback feedback = new Feedback();

        feedback.setEvidenceID(evidenceDAO.getLastEvidenceID());
        feedback.setFeedbackText("Quiero comenzar felicitándote por el trabajo realizado en este proyecto de " +
                "software. Es evidente que has invertido tiempo y esfuerzo en su desarrollo, y los resultados son " +
                "notables. A continuación, me gustaría resaltar algunos aspectos positivos que he " +
                "observado en tu evidencia. En primer lugar, me impresiona la claridad de los objetivos establecidos " +
                "para el proyecto. Desde el principio, has definido claramente los requisitos y las " +
                "funcionalidades esperadas, lo cual es fundamental para el éxito del desarrollo de software. " +
                "Además, has demostrado una comprensión profunda de las necesidades de los usuarios, lo cual se " +
                "refleja en la implementación de características relevantes y útiles. "+
                "En cuanto a la arquitectura del software, me complace ver que has utilizado un enfoque modular, " +
                "lo que facilita la escalabilidad y el mantenimiento del sistema. La estructura del código es limpia " +
                "y bien organizada, lo que contribuye a la legibilidad y a la facilidad de mantenimiento. Además, " +
                "has implementado buenas prácticas de programación, como el uso adecuado de comentarios y la " +
                "modularización del código en funciones y clases reutilizables. " +
                "También quiero destacar la atención que has prestado a la seguridad del software. " +
                "Has implementado medidas de protección, como el cifrado de datos y la validación de entradas, " +
                "lo cual es crucial en un entorno en línea. Además, has realizado pruebas exhaustivas para " +
                "identificar y solucionar posibles vulnerabilidades, demostrando un enfoque proactivo hacia la " +
                "seguridad. En cuanto a la documentación del proyecto, has proporcionado una descripción detallada " +
                "de la arquitectura, las funcionalidades y las instrucciones de instalación. Esta documentación " +
                "es clara y bien estructurada, lo que facilita la comprensión del sistema y su uso por parte de " +
                "otros desarrolladores o usuarios. En general, tu evidencia refleja un enfoque metódico y " +
                "cuidadoso en el desarrollo de software. Has demostrado habilidades técnicas sólidas, así como una " +
                "comprensión profunda de los principios de la ingeniería de software. Además, tu capacidad para " +
                "resolver problemas y enfrentar desafíos durante el proceso de desarrollo es impresionante. " +
                "Como sugerencia de mejora, me gustaría enfatizar la importancia de realizar pruebas exhaustivas en " +
                "todas las funcionalidades del software. Aunque has realizado pruebas en gran medida, asegurarse de " +
                "cubrir todos los escenarios posibles y realizar pruebas de integración minuciosas puede ayudar a " +
                "identificar y corregir posibles errores o deficiencias antes de la implementación final. " +
                "En resumen, felicidades por el trabajo realizado en este proyecto de software. Tu evidencia " +
                "muestra un enfoque sólido y meticuloso, reflejando un dominio de los conceptos y técnicas necesarios" +
                " para el desarrollo exitoso de software. Espero ver más proyectos tuyos en el futuro y estoy " +
                "seguro de que seguirás creciendo como profesional en el campo de la ingeniería de software.");

        assertThrows(SQLSyntaxErrorException.class, () -> feedbackDAO.addFeedback(feedback));
    }

    @Test
    void testAddFeedbackNull() {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        Feedback feedback = new Feedback();

        assertThrows(SQLIntegrityConstraintViolationException.class, () -> feedbackDAO.addFeedback(feedback));
    }

    @Test
    void testDeleteFeedbackSuccess() throws SQLException {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        Feedback feedback = new Feedback();

        feedback.setEvidenceID(evidenceDAO.getLastEvidenceID());
        feedback.setFeedbackText("En primer lugar, quiero destacar la claridad y la organización de tu " +
                "documentación. Es evidente que has dedicado tiempo y esfuerzo en presentar la información de manera " +
                "estructurada y fácil de seguir. Los objetivos del proyecto, los requisitos y las funcionalidades " +
                "están claramente definidos, lo que demuestra una comprensión sólida del alcance del trabajo.");
        feedbackDAO.addFeedback(feedback);

        int expectedResult = 1;
        assertEquals(expectedResult, feedbackDAO.deleteFeedbackByID(feedbackDAO
                .getFeedbackIDByEvidenceID(evidenceDAO.getLastEvidenceID(), "ZS21050285")));
    }

    @Test
    void testDeleteFeedbackNotExist() throws SQLException {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        Feedback feedback = new Feedback();

        feedbackDAO.addFeedback(feedback);

        assertThrows(SQLIntegrityConstraintViolationException.class, () -> feedbackDAO.deleteFeedbackByID(feedbackDAO
                .getFeedbackIDByEvidenceID(evidenceDAO.getLastEvidenceID(), "ZS21050285")));
    }

}
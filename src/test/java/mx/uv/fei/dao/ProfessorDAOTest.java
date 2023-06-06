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
        testAccessAccount.setUsername("perezJuanC");
        testAccessAccount.setUserPassword("JCPA");
        testAccessAccount.setUserType("Profesor");
        testAccessAccount.setUserEmail("jperez@mail.com");
        testProfessor.setProfessorName("Juan Carlos");
        testProfessor.setProfessorLastName("Perez Arriaga");
        testProfessor.setProfessorDegree("MCC.");
        userDAO.addProfessorUserTransaction(testAccessAccount,testProfessor);
        
        var testAccessAccount2 = new AccessAccount();
        var testProfessor2 = new Professor();
        testAccessAccount2.setUsername("cortesMariaK");
        testAccessAccount2.setUserPassword("MKCV");
        testAccessAccount2.setUserType("RepresentanteCA");
        testAccessAccount2.setUserEmail("kcortes@mail.com");
        testProfessor2.setProfessorName("Maria Karen");
        testProfessor2.setProfessorLastName("Cortes Verdin");
        testProfessor2.setProfessorDegree("Dra.");
        userDAO.addProfessorUserTransaction(testAccessAccount2, testProfessor2);
        
        var projectDirectors = new Project();
        projectDirectors.setAcademicBodyId("UV-CA-127");
        projectDirectors.setInvestigationProjectName("Hacia un Modelo de Campus Accesible: Facultad de Estadística e Informática");
        projectDirectors.setLGAC_Id(2);
        projectDirectors.setInvestigationLine("Se orienta al estudio de diversas propiedades, enfoques, métodos de " +
                "modelado y herramientas que conforman cada una de las diversas tecnologías aplicables al desarrollo " +
                "del software con vistas a su adaptación, mejora y sustitución en el medio nacional");
        projectDirectors.setApproximateDuration("12 meses");
        projectDirectors.setModalityId(5);
        projectDirectors.setReceptionWorkName("Recomendaciones de Accesibilidad para el Desarrollo de Software");
        projectDirectors.setRequisites("Tecnologías para la construcción de software, Principios de Construcción de Software");
        projectDirectors.setDirectorName("MCC. Juan Carlos Perez Arriaga");
        projectDirectors.setCodirectorName("Dra. Maria Karen Cortes Verdin");
        projectDirectors.setStudentsParticipating(1);
        projectDirectors.setInvestigationProjectDescription("Actualmente la democratización de la educación representa un" +
                " reto para cualquier Institución de Educación Superior, al mismo tiempo que el término 'Institución Incluyente'" +
                " cobra mayor sentido como parte de esta democratización. Desde la perspectiva de inclusión, se pretende que " +
                "cualquier institución garantice el derecho a la educación respetando las diversas necesidades, capacidades " +
                "y características de cada uno de los estudiantes, al mismo tiempo que se orientan políticas y prácticas " +
                "educativas a mejorar el proceso de enseñanza-aprendizaje y a suprimir cualquier forma de discriminación " +
                "que surja al interior de las mismas.\n" +
                "\n" +
                "La Universidad Veracruzana como institución de educación superior pública, tiene el compromiso de brindar" +
                " acceso a la educación superior de calidad a todos los sectores de la sociedad, principalmente a la " +
                "sociedad Veracruzana, de tal modo que la universidad sea motor de desarrollo económico y crecimiento " +
                "sustentable en beneficio de la población del estado de Veracruz y de la nación.\n" +
                "\n" +
                "Desde hace algunos años al interior de la Universidad Veracruzana se operan programas transversales " +
                "fundamentados en la responsabilidad social y universitaria, y que promueven entre otras cosas, la " +
                "equidad de género, la inclusión y el respeto a los derechos humanos. Como parte de esta visión institucional, " +
                "en la Facultad de Estadística e Informática se han desarrollado estrategias tecnológicas orientadas a" +
                " atender necesidades de inclusión que contribuyan al ingreso, permanencia y egreso de personas con " +
                "discapacidad.\n" +
                "\n" +
                "Este proyecto se encuentra alineado con las actividades que se han venido realizando en la Facultad de " +
                "Estadística e Informática de la Universidad Veracruzana, particularmente en el Cuerpo Académico (CA) " +
                "'Ingeniería de Tecnología de Software', y que aplica a estudiantes de licenciatura como de posgrado.");
        projectDirectors.setReceptionWorkDescription("En la actualidad existen diversas guías, técnicas y métodos que " +
                "contribuyen al desarrollo de software accesible. Sin embargo, a pesar de la existencia de estas " +
                "prácticas, los defectos relacionados con la ausencia de características de accesibilidad en productos " +
                "de software persisten. Por otra parte, debido a eventos generados en temas de salud pública, el uso de" +
                " plataformas de software se incrementó en los años pasados. Uno de los problemas reportados fue que las " +
                "plataformas no eran accesibles o que carecían de compatibilidad con tecnologías de asistencia como " +
                "lectores en pantalla, magnificadores, asistentes digitales, entre otros elementos. Debido a lo anterior " +
                "resulta pertinente trabajar en proyectos que tengan como objetivo contribuir al desarrollo de software " +
                "accesible. El presente trabajo tiene como finalidad, realizar una recopilación de las prácticas que " +
                "evidencien utilidad para el desarrollo de software accesible, al mismo tiempo que se genera un documento" +
                " que sirva como guía en la selección de prácticas que orienten al ingeniero de software en el proceso" +
                " de desarrollo.");
        projectDirectors.setExpectedResults("Documento que contenga:\n" +
                "- Reporte de la revisión sistemática de la literatura sobre las prácticas de accesibilidad en el " +
                "desarrollo de software que evidencien su aplicación en las distintas fases del proceso de desarrollo\n" +
                "- Artículo para publicación en evento académico\n" +
                "- Documento que sirva como guía para el desarrollo de software a partir de las prácticas identificadas");
        projectDirectors.setRecommendedBibliography("Luciana A.M. Zaina, Renata P.M. Fortes, Vitor Casadei, Leornardo Seiji " +
                "Nozaki, Débora Maria Barroso Paiva, \"Preventing accessibility barriers: Guidelines for using user " +
                "interface design patterns in mobile applications,\" Journal of Systems and Software, Volume 186, 2022," +
                " 111213, ISSN 0164-1212, https://doi.org/10.1016/j.jss.2021.111213.\n" +
                "\n" +
                "N. M., P. Chawla and A. Rana, \"A Practitioner’s Approach to Assess the WCAG 2.0 Website Accessibility " +
                "Challenges,\" 2019 Amity International Conference on Artificial Intelligence (AICAI), 2019, pp. 958-966," +
                " doi: 10.1109/AICAI.2019.8701320.\n" +
                "\n" +
                "Claire Kearney-Volpe and Amy Hurst, \"Accessible Web Development: Opportunities to Improve the Education" +
                " and Practice of web Development with a Screen Reader,\" ACM Trans. Access. Comput. 14, 2, Article 8 " +
                "(June 2021), 32 pages, https://doi.org/10.1145/3458024.\n" +
                "\n" +
                "Débora Maria Barroso Paiva, André Pimenta Freire, Renata Pontin de Mattos Fortes, \"Accessibility and " +
                "Software Engineering Processes: A Systematic Literature Review,\" Journal of Systems and Software, Volume" +
                " 171, 2021, 110819, ISSN 0164-1212, https://doi.org/10.1016/j.jss.2020.110819.");
        projectDAO.addProject(projectDirectors);
        projectDAO.setDirectorIDtoProject(projectDirectors);
        projectDAO.setCodirectorIDtoProject(projectDirectors);
        
        var noDirectorsProject = new Project();
        noDirectorsProject.setAcademicBodyId("UV-CA-127");
        noDirectorsProject.setInvestigationProjectName(" ");
        noDirectorsProject.setLGAC_Id(2);
        noDirectorsProject.setInvestigationLine(" ");
        noDirectorsProject.setApproximateDuration("6 meses");
        noDirectorsProject.setModalityId(2);
        noDirectorsProject.setReceptionWorkName("Análisis de las tecnologías para el desarrollo de Development Bots");
        noDirectorsProject.setRequisites("Lectura de documentos en inglés, trabajo autónomo, Interés por la investigación." +
                " Diseño de Software. Tecnologías para la Construcción, Desarrollo de Sistemas en Red y " +
                "Desarrollo de Aplicaciones.");
        noDirectorsProject.setDirectorName("Dr. Jorge Octavio Ocharan Hernandez");
        noDirectorsProject.setCodirectorName("Dr. Hector Xavier Limon Riaño");
        noDirectorsProject.setStudentsParticipating(1);
        noDirectorsProject.setInvestigationProjectDescription(" ");
        noDirectorsProject.setReceptionWorkDescription(
                "Los robots de software o bots son aplicaciones de software autónomo que se han utilizado para realizar " +
                        "tareas repetitivas o simples. Existe una investigación importante en torno a los bots sociales y de chat" +
                        " que interactúan con personas para ayudarles en sus tareas. Además, los bots se utilizan para automatizar" +
                        " tareas realizadas por ingenieros de software y equipos de desarrollo en su trabajo diario. Investigaciones" +
                        " recientes sugieren que los bots pueden ahorrar tiempo a los desarrolladores y aumentar significativamente" +
                        " su productividad. En particular, se busca que los bots asistan en las diversas actividades de la ingeniería" +
                        " de software.\n" +
                        "En la FEI, actualmente hay un proyecto en curso que revisa la investigación sobre el uso de bots para " +
                        "apoyar las actividades de desarrollo de software. Sin embargo, es necesario realizar un análisis del estado" +
                        " actual de las diferentes tecnologías utilizadas en el desarrollo de este tipo de software. El objetivo de" +
                        " este trabajo es llevar a cabo una revisión crítica de las tecnologías empleadas en el desarrollo de bots " +
                        "para respaldar las actividades de desarrollo de software. Se realizará un análisis de la información obtenida" +
                        " de diversas fuentes, tanto literatura académica como literatura gris, con el fin de identificar tendencias " +
                        "y áreas de oportunidad para el desarrollo de este tipo de aplicaciones.");
        noDirectorsProject.setExpectedResults("- Reporte de la revisión multivocal de la literatura\n" +
                "- Paquete de artefactos producto de la revisión\n" +
                "- Borrador de artículo");
        noDirectorsProject.setRecommendedBibliography("Baudry, B., Chen, Z., Etemadi, K., Fu, H., Ginelli, D., " +
                "Kommrusch, S., Martinez, M., Monperrus, M., Ron, J., Ye, H., & Yu, Z. (2021). A Software " +
                "Repair Bot based on Continual Learning. IEEE Software, 0. https://doi.org/10.1109/MS.2021.3070743\n" +
                "\n" +
                "Beschastnikh, I., Lungu, M. F., & Zhuang, Y. (2017). Accelerating Software Engineering Research " +
                "Adoption with Analysis Bots. Proceedings of the 39th International Conference on Software Engineering: " +
                "New Ideas and Emerging Results Track, 35–38. https://doi.org/10.1109/ICSE-NIER.2017.17\n" +
                "\n" +
                "Lebeuf, C., Zagalsky, A., Foucault, M., & Storey, M.-A. (2019). Defining and Classifying Software Bots:" +
                "A Faceted Taxonomy. Proceedings of the 1st International Workshop on Bots in Software Engineering, " +
                "1–6. https://doi.org/10.1109/BotSE.2019.00008\n" +
                "\n" +
                "Matthies, C., Dobrigkeit, F., & Hesse, G. (2019). An Additional Set of (Automated) Eyes: Chatbots for " +
                "Agile Retrospectives. Proceedings of the 1st International Workshop on Bots in Software Engineering, " +
                "34–37. https://doi.org/10.1109/BotSE.2019.00017\n" +
                "\n" +
                "Peng, Z., Yoo, J., Xia, M., Kim, S., & Ma, X. (2018). Exploring How Software Developers Work with Mention" +
                " Bot in GitHub. Proceedings of the Sixth International Symposium of Chinese CHI, 152–155. " +
                "https://doi.org/10.1145/3202667.3202694\n" +
                "\n" +
                "Storey, M.-A., & Zagalsky, A. (2016). Disrupting Developer Productivity One Bot at a Time. Proceedings" +
                " of the 2016 24th ACM SIGSOFT International Symposium on Foundations of Software Engineering, 928–931." +
                " https://doi.org/10.1145/2950290.2983989\n" +
                "\n" +
                "Wyrich, M., & Bogner, J. (2019). Towards an Autonomous Bot for Automatic Source Code Refactoring. " +
                "Proceedings of the 1st International Workshop on Bots in Software Engineering, 24–28. " +
                "https://doi.org/10.1109/BotSE.2019.00015");
        projectDAO.addProject(noDirectorsProject);
    }
    
    @AfterEach
    void tearDown() throws SQLException {
        var userDAO = new UserDAO();
        var projectDAO = new ProjectDAO();
        
        userDAO.deleteUserByUsername("perezJuanC");
        userDAO.deleteUserByUsername("cortesMariaK");
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Recomendaciones de Accesibilidad para el Desarrollo de Software"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Análisis de las tecnologías para el desarrollo de Development Bots"));
    }
    
    @Test
    void testGetProfessorNames() throws SQLException {
        System.out.println("Test getProfessorNames");
        var professorDAO = new ProfessorDAO();
        
        List<String> expectedList = new ArrayList<>();
        expectedList.add("MCC. Juan Carlos Perez Arriaga");
        expectedList.add("Dra. Maria Karen Cortes Verdin");
        
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
        System.out.println("Test different list getProfessorNames");
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
        
        String expectedString = "MCC. Juan Carlos Perez Arriaga, Dra. Maria Karen Cortes Verdin";
        
        String actualString = professorDAO.getDirectorsByProject(projectDAO.getProjectIDByTitle("Recomendaciones de Accesibilidad para el Desarrollo de Software"));
        
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
        
        String actualString = professorDAO.getDirectorsByProject(projectDAO.getProjectIDByTitle("Análisis de las tecnologías para el desarrollo de Development Bots"));
        System.out.println("Actual String:");
        System.out.println(actualString);
        System.out.println("--------------");
        
        assertNull(actualString);
    }
}
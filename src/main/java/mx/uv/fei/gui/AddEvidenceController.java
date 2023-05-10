package mx.uv.fei.gui;

import java.io.IOException;
import java.sql.SQLException;

public class AddEvidenceController implements IStudentNavigationBar {

    @Override
    public void redirectToAdvancements() throws IOException {
        MainStage.changeView("studentadvancement-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToEvidences() throws  IOException, SQLException {
        MainStage.changeView("evidences.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjects() throws IOException {

    }

    @Override
    public void redirectToRequest() {

    }

    @Override
    public void actionLogOut() throws IOException {

    }
}

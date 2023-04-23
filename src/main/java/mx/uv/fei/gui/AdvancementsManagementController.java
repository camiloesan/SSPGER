package mx.uv.fei.gui;

import java.io.IOException;

public class AdvancementsManagementController implements IProfessorNavigationBar {


    @Override
    public void redirectToAdvancementManagement() {

    }

    @Override
    public void redirectToProjectManagement() {

    }

    @Override
    public void redirectToEvidences() {

    }

    @Override
    public void redirectToRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void actionLogOut() throws IOException {

    }
}

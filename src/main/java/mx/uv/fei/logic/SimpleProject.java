package mx.uv.fei.logic;

import java.util.Objects;

public class SimpleProject extends DetailedProject{
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SimpleProject)) {
            return false;
        }
        SimpleProject simpleProject = (SimpleProject) object;
        return getProjectID() == simpleProject.getProjectID()
                && Objects.equals(getReceptionWorkName(), simpleProject.getReceptionWorkName())
                && Objects.equals(getProjectState(), simpleProject.getProjectState());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getProjectID(), getReceptionWorkName(), getProjectState());
    }
}

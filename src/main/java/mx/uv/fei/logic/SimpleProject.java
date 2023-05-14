package mx.uv.fei.logic;

import java.util.Objects;

public class SimpleProject extends DetailedProject{
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SimpleProject)) {
            return false;
        }
        SimpleProject simpleProject = (SimpleProject) obj;
        return getProjectID() == simpleProject.getProjectID()
                && Objects.equals(getProjectTitle(), simpleProject.getProjectTitle())
                && Objects.equals(getProjectState(), simpleProject.getProjectState());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getProjectID(), getProjectTitle());
    }
}

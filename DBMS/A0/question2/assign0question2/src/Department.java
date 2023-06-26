import java.awt.*;

public class Department {
    private int dept_code;
    private String dept_name;

    public Department(int dept_code, String dept_name) {
        this.dept_code = dept_code;
        this.dept_name = dept_name;
    }

    public int getDept_code() {
        return dept_code;
    }

    public void setDept_code(int dept_code) {
        this.dept_code = dept_code;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    @Override
    public String toString() {
        return getDept_name();
    }
}

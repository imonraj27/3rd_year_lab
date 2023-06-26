import java.util.ArrayList;

public class StudentData {
    String name;
    int dept_code;
    String address;
    String phone;
    int roll;

  static ArrayList<Department> deptlist;

    public StudentData(String name, int dept_code, String address, String phone, int roll) {
        this.name = name;
        this.dept_code = dept_code;
        this.address = address;
        this.phone = phone;
        this.roll = roll;
    }

    String getDeptNameFromCode(int deptcode){
        for(Department d: deptlist){
            if(d.getDept_code()==deptcode) return d.getDept_name();
        }
        return null;
    }

    @Override
    public String toString() {
        return  "\nNAME= '" + name + '\'' +
                "\nDEPT= " + getDeptNameFromCode(dept_code) +
                "\nADDRESS= '" + address + '\'' +
                "\nPHONE= '" + phone + '\'' +
                "\nROLL= " + roll +
                "\n===============";

    }
}

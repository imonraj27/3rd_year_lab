import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Student {
    private JPanel panel;
    private JTabbedPane tabbedPane1;
    private JTextField textField1;
    private JLabel roll_add_label;
    private JPanel add_tab;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JComboBox department;
    private JButton ADDSTUDENTButton;
    private JPanel edit_tab;
    private JPanel dlt_tab;
    private JTextField edit_roll_fld;
    private JButton EDITButton;
    private JTextField edit_name;
    private JTextField edit_address;
    private JTextField edit_phn;
    private JComboBox depart2;
    private JPanel edit_panel;
    private JButton edit_SAVEButton;
    private JButton edit_CANCELButton;
    private JButton PREVButton;
    private JButton NEXTButton;
    private JTextPane show_label;
    private JScrollPane pane;
    private JTextField roll_dlt_field;
    private JButton DELETEButton;

    private int start = 0;
    private int len = 5;
    private int end = len-1;

    private ArrayList<Department> deptlist = new ArrayList<>();
    private ArrayList<StudentData> studlist = new ArrayList<>();
    private
    void addAllDepts(){
        deptlist.add(new Department(1,"Computer Science"));
        deptlist.add(new Department(2,"Electronics"));
        deptlist.add(new Department(3,"Mechanical"));
        deptlist.add(new Department(4,"Civil"));
        deptlist.add(new Department(5,"Electrical"));

        for(int i=0; i<deptlist.size(); i++){
            department.addItem(deptlist.get(i));
            depart2.addItem(deptlist.get(i));
        }
    }

    void dltStudent(int roll){
        for(StudentData s : studlist){
            if(s.roll == roll){
                studlist.remove(s);
                showAllStuds();
                JOptionPane.showMessageDialog(null,"STUDENT SUCCESSFULLY DELETED...");
                return;
            }
        }
        JOptionPane.showMessageDialog(null,"ROLL DOESN'T EXIST...");
    }

    void showAllStuds(){
        if(start==0) {
            PREVButton.setEnabled(false);
        }else{
            PREVButton.setEnabled(true);
        }
        if(end>=studlist.size()) {
            NEXTButton.setEnabled(false);
        }else{
            NEXTButton.setEnabled(true);
        }
        String msg = "";
        for(int i=start; i<=end && i<studlist.size(); i++){
            msg += studlist.get(i);
        }
       show_label.setText(msg);
    }

    boolean rollExists(int roll){
        for(StudentData s : studlist){
            if(s.roll == roll) return true;
        }
        return false;
    }

    StudentData getStudentFromRoll(int roll){
        for(StudentData s : studlist){
            if(s.roll == roll) return s;
        }
        return null;
    }

    Department getDeptFromCode(int deptcode){
        for(Department d : deptlist){
            if(d.getDept_code() == deptcode) return d;
        }
        return null;
    }



    Student(){
        addAllDepts();
        edit_panel.setVisible(false);
        show_label.setEditable(false);
        ADDSTUDENTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Department d = (Department) department.getSelectedItem();
                int deptCode = d.getDept_code();
//                msg += "\nDept Name: "+(d).getDept_name();
                int roll = Integer.parseInt(textField1.getText());
                String name = textField2.getText();
                String address = textField3.getText();
                String phone = textField4.getText();

                StudentData.deptlist = deptlist;
                if(rollExists(roll)){
                    JOptionPane.showMessageDialog(null,"ROLL ALREADY EXISTS...");
                    return;
                }
                studlist.add(new StudentData(name,deptCode,address,phone,roll));
                JOptionPane.showMessageDialog(null,"STUDENT SUCCESSFULLY ADDED...");
                showAllStuds();
            }
        });
        EDITButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int roll = Integer.parseInt(edit_roll_fld.getText());
                if(!rollExists(roll)){
                    JOptionPane.showMessageDialog(null,"ROLL DOESN'T EXIST...");
                    return;
                }
                StudentData editstud = getStudentFromRoll(roll);
                edit_name.setText(editstud.name);
                edit_address.setText(editstud.address);
                edit_phn.setText(editstud.phone);
                depart2.setSelectedItem(getDeptFromCode(editstud.dept_code));
                EDITButton.setEnabled(false);
                edit_roll_fld.setEditable(false);

                edit_panel.setVisible(true);
            }
        });
        edit_SAVEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentData editstud = getStudentFromRoll(Integer.parseInt(edit_roll_fld.getText()));
                editstud.name = edit_name.getText();
                editstud.address = edit_address.getText();
                editstud.phone = edit_phn.getText();
                editstud.dept_code =((Department) depart2.getSelectedItem()).getDept_code();
                EDITButton.setEnabled(true);
                edit_roll_fld.setEditable(true);

                edit_panel.setVisible(false);
            }
        });
        edit_CANCELButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EDITButton.setEnabled(true);
                edit_roll_fld.setEditable(true);

                edit_panel.setVisible(false);
            }
        });
        NEXTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start += len;
                end += len;
                showAllStuds();
            }
        });
        PREVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start -= len;
                end -= len;
                showAllStuds();
            }
        });
        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(roll_dlt_field.getText()=="") return;
                int roll = Integer.parseInt(roll_dlt_field.getText());
                dltStudent(roll);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("STUDENTS APPLICATION");
        frame.setContentPane(new Student().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
//        frame.setLocationRelativeTo(null);
        frame.setLocation(450,170);
        frame.pack();
        frame.setVisible(true);
    }
}

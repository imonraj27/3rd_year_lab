package A0.question1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Q1 extends JFrame {
    JTextField num1field, num2field;
    JLabel l1,l2, resultfield;
    JButton addbtn, subbtn;


    Q1(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        this.setTitle("Adder and Subtracter | DATE: "+ dtf.format(now));
        int gap = 15;
        int labelwidth = 220;
        int numfieldwidth = 80;
        int allheight = 30;
        int btnwidth = 120;
        int windowwidth = 6*gap+2*labelwidth+2*numfieldwidth;
        int windowheight = 300;
        num1field = new JTextField();
        num2field = new JTextField();
        resultfield = new JLabel("");
        l1 = new JLabel("ENTER FIRST NUMBER:");
        l2 = new JLabel("ENTER SECOND NUMBER:");
        addbtn = new JButton("<html><h3 style='color: #fff; background: #000;'>ADD</h3></html>");
        addbtn.setBackground(Color.black);
        subbtn = new JButton("<html><h3 style='color: #fff; background: #000;'>SUBTRACT</h3></html>");
        subbtn.setBackground(Color.black);
        this.setLayout(null);
        this.setBounds(0,0,windowwidth,windowheight);
        this.setResizable(false);

        l1.setBounds(gap,gap,labelwidth,allheight);
        l2.setBounds(3*gap+labelwidth+numfieldwidth,gap,labelwidth,allheight);

        num1field.setBounds(2*gap+labelwidth,gap,numfieldwidth,allheight);
        num2field.setBounds(4*gap+2*labelwidth+numfieldwidth,gap,numfieldwidth,allheight);

        num1field.setFont(new Font("Verdana", Font.PLAIN, 18));
        num2field.setFont(new Font("Verdana", Font.PLAIN, 18));

        l1.setFont(new Font("Verdana", Font.PLAIN, 16));
        l2.setFont(new Font("Verdana", Font.PLAIN, 16));

        addbtn.setBounds(windowwidth/2-btnwidth-gap,allheight+2*gap,btnwidth,allheight);
        subbtn.setBounds(windowwidth/2+gap,allheight+2*gap,btnwidth,allheight);

        resultfield.setBounds(gap, 3*gap+allheight*2, windowwidth-3*gap,allheight*2);

        maintainButtons();
        this.add(num1field);
        this.add(num2field);
        this.add(l1);
        this.add(l2);
        this.add(addbtn);
        this.add(subbtn);
        this.add(resultfield);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    void maintainButtons(){
        addbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int result = Integer.parseInt(num1field.getText()) + Integer.parseInt(num2field.getText());
                    resultfield.setText("<html><h2>ADDITION RESULT: <font color='red'>"+result+"</font></h2></html>");
                }catch (NumberFormatException n){
                    resultfield.setText("<html><h2>INVALID INPUT..</h2></html>");
                }
            }
        });
        subbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int result = Integer.parseInt(num1field.getText()) - Integer.parseInt(num2field.getText());
                    resultfield.setText("<html><h2>SUBTRACTION RESULT: <font color='red'>"+result+"</font></h2></html>");
                }catch (NumberFormatException n){
                    resultfield.setText("<html><h2>INVALID INPUT..</h2></html>");
                }
            }
        });
    }


    public static void main(String[] args) {
       Q1 q = (new Q1());
    }
}

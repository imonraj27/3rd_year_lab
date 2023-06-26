import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Font;

class Client extends JFrame {
    Socket soc;
    JLabel l1, l2, l3;
    JTextField t1;
    JTextArea t2;
    JButton btn, btn_update;

    Client() throws IOException {
        super("---FTP CLIENT---");

        soc = new Socket("localhost", 2000);
        t1 = new JTextField();
        t1.setBounds(160, 20, 590, 40);
        t2 = new JTextArea();

        l1 = new JLabel("Filename: ");
        l2 = new JLabel("FileData: ");
        l3 = new JLabel("");

        l1.setBounds(50, 20, 120, 30);
        l2.setBounds(50, 60, 120, 30);
        l3.setBounds(430, 670, 500, 50);

        l1.setFont(new Font("sans-serif", Font.BOLD, 18));
        l2.setFont(new Font("sans-serif", Font.BOLD, 18));
        l3.setFont(new Font("sans-serif", Font.BOLD, 16));

        t1.setFont(new Font("courier", Font.PLAIN, 16));
        t2.setFont(new Font("courier", Font.PLAIN, 16));

        t2.setLineWrap(true);
        t2.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(t2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(50, 100, 700, 550);

        this.btn = new JButton("Fetch");
        btn.setBounds(50, 670, 100, 30);

        btn_update = new JButton("Update");
        btn_update.setBounds(170, 670, 100, 30);

        this.btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    exec(t1.getText());
                } catch (IOException e1) {
                }
            }
        });

        this.btn_update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateFile(t1.getText(), t2.getText());
                } catch (IOException e1) {
                }
            }
        });

        this.add(t1);
        this.add(l1);
        this.add(l2);
        this.add(l3);
        this.add(btn);
        this.add(btn_update);
        this.add(scrollPane);
        this.setSize(800, 800);

        this.setLayout(null);
        this.setVisible(true);
    }

    void exec(String str) throws IOException {
        DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
        DataInputStream dis = new DataInputStream(soc.getInputStream());

        dout.writeUTF("READ " + str);
        dout.flush();
        String data = (String) dis.readUTF();
        t2.setText(data);

        FileWriter fWriter = new FileWriter("./public/" + str);
        fWriter.write(data);
        fWriter.close();
    }

    void updateFile(String file, String content) throws IOException {
        DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
        DataInputStream dis = new DataInputStream(soc.getInputStream());

        dout.writeUTF("UPDATE " + file + " " + content);
        dout.flush();
        String data = (String) dis.readUTF();
        if (data.equals("SUCCESS")) {
            FileWriter fWriter = new FileWriter("./public/" + file);
            fWriter.write(content);
            l3.setText("<html><font color='green'>FILE UPDATED/CREATED SUCCESSFULLY<BR> ON SERVER</font></html>");
            fWriter.close();
        } else
            l3.setText("<html><font color='red'>SOME ERROR OCCURED..</font></html>");

    }

    public static void main(String[] args) {
        try {
            new Client();
        } catch (IOException e) {
            System.out.println("Errror");
        }
    }
}
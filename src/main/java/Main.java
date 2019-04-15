import com.google.gson.Gson;
import dto.ResponseDto;
import dto.UserDto;
import javafx.util.Pair;
import org.springframework.util.StringUtils;
import utils.C82Utils;
import utils.ConfigUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Main {

    private static void placeComponents(JFrame frame, JPanel panel) {
        panel.setLayout(null);

        JLabel usernameLabel = new JLabel("用户名");
        usernameLabel.setBounds(15, 20, 50, 25);
        panel.add(usernameLabel);
        JTextField usernameText = new JTextField(20);
        usernameText.setText("nikki3320");
        usernameText.setBounds(70, 20, 180, 25);
        panel.add(usernameText);

        JLabel passwordLabel = new JLabel("密码");
        passwordLabel.setBounds(15, 50, 50, 25);
        panel.add(passwordLabel);
        JTextField passwordText = new JTextField(20);
        passwordText.setText("a12345");
        passwordText.setBounds(70, 50, 180, 25);
        panel.add(passwordText);

        JLabel inviteLabel = new JLabel("邀请码");
        inviteLabel.setBounds(15, 80, 50, 25);
        panel.add(inviteLabel);
        JTextField inviteText = new JTextField(20);
        inviteText.setBounds(70, 80, 180, 25);
        String lastValue = ConfigUtils.getValue();
        inviteText.setText(lastValue);
        panel.add(inviteText);

        Pair<String, Image> pair = C82Utils.getImageCodeAndCookie();
        System.setProperty("cookie", pair.getKey());
        Image image = pair.getValue();

        JLabel codeLabel = new JLabel("验证码");
        codeLabel.setBounds(15, 130, 50, 25);
        panel.add(codeLabel);
        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(70, 110, 100, 40);
        imageLabel.setIcon(new ImageIcon(image));
        panel.add(imageLabel);
        JTextField codeText = new JTextField(20);
        codeText.setBounds(70, 160, 180, 25);
        codeText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(e.getKeyCode() == 10) {
                    UserDto userDto = UserDto.builder().username(usernameText.getText()).password(passwordText.getText()).invitationId(inviteText.getText()).build();
                    String verifyCode = codeText.getText();
                    String response = C82Utils.register(userDto, verifyCode, System.getProperty("cookie"));
                    if(!StringUtils.isEmpty(response)) {
                        ResponseDto responseDto = new Gson().fromJson(response, ResponseDto.class);
                        if(responseDto.getCode() == 1012) {
                            long newInviteId = Long.parseLong(inviteText.getText()) + 1;
                            inviteText.setText(newInviteId + "");
                        }
                    }
                    Pair<String, Image> pair = C82Utils.getImageCodeAndCookie();
                    Image image = pair.getValue();
                    if(Objects.nonNull(image)) {
                        System.setProperty("cookie", pair.getKey());
                        imageLabel.setIcon(new ImageIcon(image));
                        codeText.setText("");
                    }
                }
            }
        });
        panel.add(codeText);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                String inviteId = inviteText.getText();
                ConfigUtils.updateValue(inviteId);
            }
        });

//        JButton clearButton = new JButton("send");
//        clearButton.setBounds(70, 190, 80, 25);
//        panel.add(clearButton);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("OCR");
        frame.setSize(350, 200);
        frame.setBounds(800, 300, 311, 500);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(frame, panel);

        frame.setVisible(true);
    }
}

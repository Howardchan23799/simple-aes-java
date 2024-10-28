package window;

/**
 * @Author {陈友豪}
 * @Date 2024/10/28 17:45
 */
/**
 * 解密窗口
 */
import Decrypt.DecryptService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DecryptWindow extends JFrame {
    private JTextArea ciphertextArea;
    private JTextArea keyArea;
    private JTextArea plaintextArea;
    private JRadioButton oneRound, twoRounds, threeRounds;
    private JRadioButton binaryOutput, asciiOutput;
    private ButtonGroup roundGroup, outputGroup;

    public DecryptWindow() {
        setTitle("解密窗口");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        // 密文输入框
        ciphertextArea = new JTextArea(3, 50);
        ciphertextArea.setBorder(BorderFactory.createTitledBorder("输入密文"));
        add(new JScrollPane(ciphertextArea));

        // 密钥输入框
        keyArea = new JTextArea(1, 50);
        keyArea.setBorder(BorderFactory.createTitledBorder("输入密钥"));
        add(new JScrollPane(keyArea));

        // 明文输出框
        plaintextArea = new JTextArea(3, 50);
        plaintextArea.setBorder(BorderFactory.createTitledBorder("解密后的结果"));
        plaintextArea.setEditable(false);
        add(new JScrollPane(plaintextArea));

        // 解密轮次选择
        JPanel roundPanel = new JPanel();
        oneRound = new JRadioButton("1轮解密", true);
        twoRounds = new JRadioButton("2轮解密");
        threeRounds = new JRadioButton("3轮解密");
        roundGroup = new ButtonGroup();
        roundGroup.add(oneRound);
        roundGroup.add(twoRounds);
        roundGroup.add(threeRounds);
        roundPanel.add(oneRound);
        roundPanel.add(twoRounds);
        roundPanel.add(threeRounds);
        add(roundPanel);

        // 输出类型选择
        JPanel outputPanel = new JPanel();
        binaryOutput = new JRadioButton("二进制输出", true);
        asciiOutput = new JRadioButton("ASCII输出");
        outputGroup = new ButtonGroup();
        outputGroup.add(binaryOutput);
        outputGroup.add(asciiOutput);
        outputPanel.add(binaryOutput);
        outputPanel.add(asciiOutput);
        add(outputPanel);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton decryptButton = new JButton("解密");
        JButton clearButton = new JButton("清空");

        decryptButton.addActionListener(new DecryptButtonListener());
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(decryptButton);
        buttonPanel.add(clearButton);
        add(buttonPanel);
    }

    private void clearFields() {
        ciphertextArea.setText("");
        keyArea.setText("");
        plaintextArea.setText("");
    }

    private class DecryptButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String ciphertext = ciphertextArea.getText();
            String key = keyArea.getText();
            int rounds = oneRound.isSelected() ? 1 : twoRounds.isSelected() ? 2 : 3;
            int sig = binaryOutput.isSelected() ? 0 : 1; // Determine output type

            // 调用解密函数
            DecryptService decryptService = new DecryptService();
            String decryptedText = decryptService.Decrypt(ciphertext, key, sig, rounds);
            plaintextArea.setText(decryptedText);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DecryptWindow window = new DecryptWindow();
            window.setVisible(true);
        });
    }
}

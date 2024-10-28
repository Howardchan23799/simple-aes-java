package window;

/**
 * @Author {陈友豪}
 * @Date 2024/10/28 17:45
 */
/**
加密窗口
 */
import Encrypt.EncryptService;
import Encrypt.entity.EncryptionResult;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EncryptWindow extends JFrame {
    private JTextArea plaintextArea;
    private JTextArea keyArea;
    private JTextArea ciphertextArea;
    private JRadioButton oneRound, twoRounds, threeRounds;
    private JRadioButton binaryInput, asciiInput;
    private ButtonGroup roundGroup, inputGroup;

    public EncryptWindow() {
        setTitle("加密窗口");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        // 明文输入框
        plaintextArea = new JTextArea(3, 50);
        plaintextArea.setBorder(BorderFactory.createTitledBorder("输入明文（二进制或ASCII）"));
        add(new JScrollPane(plaintextArea));

        // 密钥显示框
        keyArea = new JTextArea(1, 50);
        keyArea.setBorder(BorderFactory.createTitledBorder("生成的密钥"));
        add(new JScrollPane(keyArea));

        // 密文显示框
        ciphertextArea = new JTextArea(3, 50);
        ciphertextArea.setBorder(BorderFactory.createTitledBorder("加密后的结果"));
        add(new JScrollPane(ciphertextArea));

        // 加密轮次选择
        JPanel roundPanel = new JPanel();
        oneRound = new JRadioButton("1轮加密", true);
        twoRounds = new JRadioButton("2轮加密");
        threeRounds = new JRadioButton("3轮加密");
        roundGroup = new ButtonGroup();
        roundGroup.add(oneRound);
        roundGroup.add(twoRounds);
        roundGroup.add(threeRounds);
        roundPanel.add(oneRound);
        roundPanel.add(twoRounds);
        roundPanel.add(threeRounds);
        add(roundPanel);

        // 输入类型选择
        JPanel inputPanel = new JPanel();
        binaryInput = new JRadioButton("二进制输入", true);
        asciiInput = new JRadioButton("ASCII输入");
        inputGroup = new ButtonGroup();
        inputGroup.add(binaryInput);
        inputGroup.add(asciiInput);
        inputPanel.add(binaryInput);
        inputPanel.add(asciiInput);
        add(inputPanel);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton encryptButton = new JButton("加密");
        JButton clearButton = new JButton("清空");

        encryptButton.addActionListener(new EncryptButtonListener());
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(encryptButton);
        buttonPanel.add(clearButton);
        add(buttonPanel);
    }

    private void clearFields() {
        plaintextArea.setText("");
        keyArea.setText("");
        ciphertextArea.setText("");
    }

    private class EncryptButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String plaintext = plaintextArea.getText();
            int rounds = oneRound.isSelected() ? 1 : twoRounds.isSelected() ? 2 : 3;
            int sig = binaryInput.isSelected() ? 0 : 1; // Determine input type

            EncryptService encryptService = new EncryptService();

            // 使用明文生成密钥
            EncryptionResult encryptionResult = encryptService.Encrypt(plaintext, sig, rounds);

            String generatedKey = encryptionResult.getKey();
            keyArea.setText(generatedKey);

            // 加密明文
            String encryptedText = encryptionResult.getCiphertext();
            ciphertextArea.setText(encryptedText);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EncryptWindow window = new EncryptWindow();
            window.setVisible(true);
        });
    }
}

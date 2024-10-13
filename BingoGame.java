/* 各種ライブラリのインポート */
import javax.swing.*; // GUIコンポーネント
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet; // ビンゴ番号の管理
import java.util.Set;

/* クラスBingoController コントローラ */
public class BingoController {
    private BingoView view;
    private BingoModel model;

    /* コンストラクタ */
    public BingoController() {
        model = new BingoModel();
        view = new BingoView(this);
    }

    /* ビンゴ番号を画面に表示 */
    public void generateNumber() {
        int number = model.generateNumber();
        view.displayNumber(number);
    }

    public boolean isGameOver() {
        return model.GameOver();
    }

    /* エントリーポイント */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BingoController controller = new BingoController();
        });
    }
}

/* クラスBingoModel ビンゴ番号の生成 */
class BingoModel {
    private Set<Integer> generatedNumbers = new HashSet<>(); // ビンゴ番号保持用のHashSet（重複防止）

    public boolean GameOver() {
        return generatedNumbers.size() == 75;
    }

    public int generateNumber() {
        int number;
        do {
            number = (int) (Math.random() * 75) + 1;
        } while (generatedNumbers.contains(number));
        generatedNumbers.add(number);

        return number;
    }
}

/* クラスBingoView ウィンドウ生成、GUI構成 */
class BingoView extends JFrame {
    private JLabel numberLabel;
    private JTextArea historyArea;
    private JButton generateButton;
    private BingoController controller;

    public BingoView(BingoController controller) {
        this.controller = controller;
        setTitle("ビンゴ司会システム");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        /* 数値生成ボタンを追加 */
        JPanel buttonPanel = new JPanel();
        generateButton = new JButton("数値生成");
        generateButton.addActionListener(e -> controller.generateNumber());
        buttonPanel.add(generateButton);

        /* 最新のビンゴ番号を表示 */
        numberLabel = new JLabel("0", SwingConstants.CENTER);
        numberLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 32));

        /* 過去の番号を一覧で表示 */
        historyArea = new JTextArea(10, 20);
        historyArea.setEditable(false);

        /* 各コンポーネントの配置 */
        add(buttonPanel, BorderLayout.NORTH);
        add(numberLabel, BorderLayout.CENTER);
        add(new JScrollPane(historyArea), BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    /* メッセージ表示用メソッド */
    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /* ビンゴ番号を履歴に追加 */
    public void displayNumber(int number) {
        numberLabel.setText(String.valueOf(number));
        historyArea.append(number + "\n");

        /* ゲーム終了時の処理 */
        if (controller.isGameOver()) {
            generateButton.setEnabled(false);
            displayMessage("全ての数値が生成されました。ゲーム終了です。");
        }
    }
}
package game;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    private Player player;
    private Enemy enemy;
    private Random random;

    // GUI components
    private JFrame frame;
    private JLabel playerHealthLabel;
    private JLabel enemyHealthLabel;
    private JTextArea battleLog;
    private JButton attackButton;
    private JButton runButton;

    public Main() {
        random = new Random();
        initializeGame();
        initializeGUI();
    }

    private void initializeGame() {
        String playerName = JOptionPane.showInputDialog(frame, "Enter your character's name:");
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Hero";
        }
        player = new Player(playerName, 100, 15);
        enemy = new Enemy("Goblin", 50, 10);
    }

    private void initializeGUI() {
        frame = new JFrame("Text RPG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 1));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playerHealthLabel = new JLabel("Hero HP: " + player.getHealth());
        enemyHealthLabel = new JLabel("Goblin HP: " + enemy.getHealth());
        playerHealthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        enemyHealthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(playerHealthLabel);
        statsPanel.add(enemyHealthLabel);
        frame.add(statsPanel, BorderLayout.NORTH);

        battleLog = new JTextArea("A wild Goblin appears!\n");
        battleLog.setEditable(false);
        battleLog.setFont(new Font("Arial", Font.PLAIN, 12));
        frame.add(new JScrollPane(battleLog), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        attackButton = new JButton("Attack");
        runButton = new JButton("Run");
        attackButton.setFont(new Font("Arial", Font.PLAIN, 12));
        runButton.setFont(new Font("Arial", Font.PLAIN, 12));
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 12));
        buttonPanel.add(resetButton);
        resetButton.addActionListener(e -> resetGame());
        buttonPanel.add(attackButton);
        buttonPanel.add(runButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        attackButton.addActionListener(e -> handleAttack());
        runButton.addActionListener(e -> handleRun());

        frame.setVisible(true);
    }

    private void handleAttack() {
        if (!player.isAlive() || !enemy.isAlive()) {
            endGame();
            return;
        }

        int playerDamage = random.nextInt(player.getAttack()) + 1;
        enemy.takeDamage(playerDamage);
        battleLog.append("You deal " + playerDamage + " damage to the Goblin!\n");

        if (enemy.isAlive()) {
            int enemyDamage = random.nextInt(enemy.getAttack()) + 1;
            player.takeDamage(enemyDamage);
            battleLog.append("The Goblin deals " + enemyDamage + " damage to you!\n");
        }

        updateStats();
        if (!player.isAlive() || !enemy.isAlive()) {
            endGame();
        }
    }

    private void handleRun() {
        if (!player.isAlive() || !enemy.isAlive()) {
            endGame();
            return;
        }

        if (random.nextBoolean()) {
            battleLog.append("You successfully ran away!\n");
            disableButtons();
        } else {
            battleLog.append("You failed to run away!\n");
            int enemyDamage = random.nextInt(enemy.getAttack()) + 1;
            player.takeDamage(enemyDamage);
            battleLog.append("The Goblin deals " + enemyDamage + " damage to you!\n");
            updateStats();
            if (!player.isAlive()) {
                endGame();
            }
        }
    }

    private void updateStats() {
        playerHealthLabel.setText(player.getName() + " HP: " + player.getHealth());
        enemyHealthLabel.setText(enemy.getName() + " HP: " + enemy.getHealth());
    }

    private void resetGame() {
        initializeGame();
        battleLog.setText("A wild Goblin appears!\n");
        updateStats();
        attackButton.setEnabled(true);
        runButton.setEnabled(true);
    }

    private void endGame() {
        if (!player.isAlive()) {
            battleLog.append("\nGame Over! You were defeated by the Goblin.\n");
        } else if (!enemy.isAlive()) {
            battleLog.append("\nVictory! You defeated the Goblin!\n");
        }
        disableButtons();
    }

    private void disableButtons() {
        attackButton.setEnabled(false);
        runButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
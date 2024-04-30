import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class SplashPanel extends JPanel {
    public SplashPanel() {
        setLayout(new BorderLayout());
        JLabel splashLabel = new JLabel(new ImageIcon("e://splashForJAVA.jpg")); // Replace "splash_image.png" with your image path
        add(splashLabel, BorderLayout.CENTER);
    }
}

class SwingTournamentBracket extends JFrame {
    private static final int SPLASH_DURATION = 3000; // Splash screen duration in milliseconds
    private JFrame splashFrame;

    private JButton startNextMatchButton;
    private JLabel winnerLabel;
    private JLabel loserLabel;
    private boolean winner1Declared;
    private boolean winner2Declared;
    private JLabel player1WinnerLabel;
    private JLabel player2WinnerLabel;
    private int matchCounter; // Counter to label matches
    private ArrayList<String> participantsList; // ArrayList to store participant names
    private Timer matchTimer; // Timer for the match

    public SwingTournamentBracket() {
        // Create a splash screen frame
        splashFrame = new JFrame();
        splashFrame.setUndecorated(true); // Remove window decorations
        splashFrame.setSize(500, 400); // Set splash screen size
        splashFrame.setLocationRelativeTo(null); // Center the splash screen on the screen
        splashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the splash panel to the splash screen frame
        SplashPanel splashPanel = new SplashPanel();
        splashFrame.add(splashPanel);
        splashFrame.setVisible(true);

        // Simulate loading time with a timer
        Timer timer = new Timer(SPLASH_DURATION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the splash screen frame and show the main application window
                splashFrame.dispose();
                showMainApplication();
            }
        });
        timer.setRepeats(false); // Only run once
        timer.start();
    }

    public void showMainApplication() {
        setTitle("Tournament Bracket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout(10, 10)); // Add spacing between components
        setLocationRelativeTo(null);

        JPanel bracketPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // Assuming 4 participants with spacing
        bracketPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Initialize participants list
        participantsList = new ArrayList<>();

        // Prompt user to enter participant names
        for (int i = 1; i <= 4; i++) {
            String playerName;
            do {
                playerName = JOptionPane.showInputDialog(null, "Enter the name of Participant " + i);
                if (playerName == null || playerName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Player name cannot be empty. Please enter a valid name.");
                }
            } while (playerName == null || playerName.trim().isEmpty()); // Repeat until a non-empty name is entered
            participantsList.add(playerName);
        }

        // Populate the bracket with participants
        matchCounter = 1;
        for (int i = 0; i < participantsList.size() / 2; i++) {
            JPanel matchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5)); // Add spacing between components
            JLabel label1 = new JLabel(participantsList.get(i));
            JLabel label2 = new JLabel(participantsList.get(participantsList.size() - 1 - i));
            JLabel vsLabel = new JLabel("VS");
            vsLabel.setFont(vsLabel.getFont().deriveFont(Font.BOLD)); // Make VS label bold

            JButton winnerButton1 = new JButton("Declare Winner");
            JButton winnerButton2 = new JButton("Declare Winner");

            winnerButton1.addActionListener(new WinnerButtonListener(label1, label2, true));
            winnerButton2.addActionListener(new WinnerButtonListener(label2, label1, false));

            matchPanel.add(label1);
            matchPanel.add(winnerButton1);
            matchPanel.add(vsLabel);
            matchPanel.add(winnerButton2);
            matchPanel.add(label2);

            bracketPanel.add(matchPanel);
            matchCounter++;
        }

        add(bracketPanel, BorderLayout.CENTER);

        startNextMatchButton = new JButton("Start Next Match");
        startNextMatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNextMatchFrame();
            }
        });
        startNextMatchButton.setEnabled(false); // Initially disable the button

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Bottom panel for start next match button
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        bottomPanel.add(startNextMatchButton);
        add(bottomPanel, BorderLayout.SOUTH);

        JPanel winnerPanel = new JPanel(new GridLayout(1, 2, 10, 10)); // Panel for winner labels
        winnerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        player1WinnerLabel = new JLabel("The winner of MATCH-1 ");
        player2WinnerLabel = new JLabel("The winner of MATCH-2 ");

        winnerPanel.add(player1WinnerLabel);
        winnerPanel.add(player2WinnerLabel);

        add(winnerPanel, BorderLayout.NORTH);
    }

    private class WinnerButtonListener implements ActionListener {
        private JLabel currentWinnerLabel;
        private JLabel currentLoserLabel;
        private boolean isFirstWinner;

        public WinnerButtonListener(JLabel currentWinnerLabel, JLabel currentLoserLabel, boolean isFirstWinner) {
            this.currentWinnerLabel = currentWinnerLabel;
            this.currentLoserLabel = currentLoserLabel;
            this.isFirstWinner = isFirstWinner;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, currentWinnerLabel.getText() + " is declared the winner!");
            currentWinnerLabel.setForeground(Color.GREEN); // Change color to indicate winner
            currentLoserLabel.setForeground(Color.RED); // Change color to indicate loser

            if (isFirstWinner) {
                winner1Declared = true;
                winnerLabel = currentWinnerLabel;
                player1WinnerLabel.setText(currentWinnerLabel.getText() + " is Winner from MATCH-1"); // Update winner label
            } else {
                winner2Declared = true;
                loserLabel = currentWinnerLabel;
                player2WinnerLabel.setText(currentWinnerLabel.getText() + " is Winner from MATCH-2"); // Update winner label
            }

            if (winner1Declared && winner2Declared && matchCounter <= 3) {
                startNextMatchButton.setEnabled(true);
            } else if (winner1Declared && winner2Declared && matchCounter > 3) {
                startNextMatchButton.setEnabled(true);
                startNextMatchButton.setText("Declare Final Winner");
            }
        }
    }

    private void createNextMatchFrame() {
        JFrame nextMatchFrame = new JFrame("Match " + matchCounter);
        nextMatchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nextMatchFrame.setSize(400, 200);
        nextMatchFrame.setLayout(new BorderLayout(10, 10)); // Add spacing between components
        nextMatchFrame.setLocationRelativeTo(null); // Center the frame on screen

        // Example winners
        String winner1 = winnerLabel.getText();
        String winner2 = loserLabel.getText(); // Second winner is the loser of the first match

        JLabel nextMatchLabel = new JLabel("Next Match: " + winner1 + " vs " + winner2);
        nextMatchLabel.setHorizontalAlignment(JLabel.CENTER);
        nextMatchLabel.setFont(nextMatchLabel.getFont().deriveFont(Font.BOLD)); // Make label bold

        // Timer label to display time left for the match
        JLabel timerLabel = new JLabel("Time Left: ");
        timerLabel.setHorizontalAlignment(JLabel.CENTER);

        // Start Match Timer
        int delay = 1000; // 1 second
        int countdownDuration = 7; // 7 seconds
        matchTimer = new Timer(delay, new ActionListener() {
            int countdown = countdownDuration; // Initial countdown value in seconds

            @Override
            public void actionPerformed(ActionEvent e) {
                if (countdown >= 0) {
                    timerLabel.setText("Time Left: " + formatTime(countdown));
                    countdown--;
                } else {
                    matchTimer.stop();
                    nextMatchFrame.dispose();
                    startNextMatchButton.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Time's up! Starting the match...");
                    createMatchFrame(winner1, winner2);
                }
            }
        });
        matchTimer.start();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        panel.add(nextMatchLabel, BorderLayout.NORTH);
        panel.add(timerLabel, BorderLayout.CENTER);

        nextMatchFrame.add(panel);
        nextMatchFrame.setVisible(true);

        matchCounter++;
    }

    private void createMatchFrame(String player1, String player2) {
        JFrame matchFrame = new JFrame("Match: " + player1 + " vs " + player2);
        matchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        matchFrame.setSize(300, 200);
        matchFrame.setLayout(new BorderLayout(10, 10)); // Add spacing between components
        matchFrame.setLocationRelativeTo(null); // Center the frame on screen

        JLabel matchLabel = new JLabel("Match: " + player1 + " vs " + player2);
        matchLabel.setHorizontalAlignment(JLabel.CENTER);
        matchLabel.setFont(matchLabel.getFont().deriveFont(Font.BOLD)); // Make label bold

        JButton declareWinnerButton = new JButton("Declare Winner");
        declareWinnerButton.setPreferredSize(new Dimension(150, 50)); // Set preferred size
        declareWinnerButton.setMargin(new Insets(10, 10, 10, 10)); // Set margins
        declareWinnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Please select the winner in the next frame.");
                createLastFrame(player1, player2);
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        panel.add(matchLabel, BorderLayout.NORTH);
        panel.add(declareWinnerButton, BorderLayout.CENTER);

        matchFrame.add(panel);
        matchFrame.setVisible(true);
    }

    private void createLastFrame(String player1, String player2) {
        JFrame lastFrame = new JFrame("Select Winner: " + player1 + " vs " + player2);
        lastFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        lastFrame.setSize(300, 150);
        lastFrame.setLayout(new BorderLayout(10, 10)); // Add spacing between components
        lastFrame.setLocationRelativeTo(null); // Center the frame on screen

        JLabel selectWinnerLabel = new JLabel("Select the winner:");
        selectWinnerLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton player1WinnerButton = new JButton(player1);
        JButton player2WinnerButton = new JButton(player2);

        player1WinnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, player1 + " is declared the winner!");
                // Add your logic to handle the winner
                lastFrame.dispose();
                // Display tournament bracket image after declaring final winner
                String finalWinner = "Winner: " + player1;
                String winner1 = player1;
                String winner2 = player2;
                String paddedFinalWinner = String.format("%-" + (Math.max(player1.length(), player2.length()) + 3) + "s", finalWinner);
                showTournamentBracketImage(winner1, winner2, paddedFinalWinner);
            }
        });

        player2WinnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, player2 + " is declared the winner!");
                // Add your logic to handle the winner
                lastFrame.dispose();
                // Display tournament bracket image after declaring final winner
                String finalWinner = "Winner: " + player2;
                String winner1 = player1;
                String winner2 = player2;
                String paddedFinalWinner = String.format("%-" + (Math.max(player1.length(), player2.length()) + 3) + "s", finalWinner);
                showTournamentBracketImage(winner1, winner2, paddedFinalWinner);
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(player1WinnerButton);
        buttonPanel.add(player2WinnerButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        panel.add(selectWinnerLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        lastFrame.add(panel);
        lastFrame.setVisible(true);
    }

    // Method to display the tournament bracket image
    private void showTournamentBracketImage(String winner1, String winner2, String finalWinner) {
        // Calculate the maximum length among player names
        int maxNameLength = 0;
        for (String playerName : participantsList) {
            maxNameLength = Math.max(maxNameLength, playerName.length());
        }
        maxNameLength = Math.max(maxNameLength, Math.max(winner1.length(), winner2.length()));

        // Print player names with padding to align them
        String paddedPlayer1 = String.format("%-" + (maxNameLength + 3) + "s", participantsList.get(0));
        String paddedPlayer2 = String.format("%-" + (maxNameLength + 3) + "s", participantsList.get(1));
        String paddedPlayer3 = String.format("%-" + (maxNameLength + 3) + "s", participantsList.get(2));
        String paddedPlayer4 = String.format("%-" + (maxNameLength + 3) + "s", participantsList.get(3));
        String paddedWinner1 = String.format("%-" + (maxNameLength + 3) + "s", winner1);
        String paddedWinner2 = String.format("%-" + (maxNameLength + 3) + "s", winner2);
        String paddedFinalWinner = String.format("%-" + (maxNameLength + 3) + "s", finalWinner);

        // Print the bracket structure
        String bracketStructure = paddedPlayer1 + "-----------" + " ".repeat(maxNameLength + 3) + "|\n" +
                " ".repeat(maxNameLength + 6) + "                |------------" + paddedWinner1 + "-----------" + "|\n" +
                paddedPlayer2 + "-----------" + " ".repeat(maxNameLength + 3) +"|                                  "+ "|\n" +
                " ".repeat(maxNameLength + 6) + "                                                   |------------------------------" + paddedFinalWinner+ "\n" +
                paddedPlayer3 + "-----------" + " ".repeat(maxNameLength + 3)+"|                                  " + "|\n" +
                " ".repeat(maxNameLength + 6) + "                |------------" + paddedWinner2 + "-----------" + "|\n" +
                paddedPlayer4 + "-----------"+" ".repeat(maxNameLength + 3) + "|\n";
        JOptionPane.showMessageDialog(null, bracketStructure);
    }

    // Method to format time in HH:mm:ss format
    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SwingTournamentBracket bracket = new SwingTournamentBracket();
            bracket.setVisible(true);
        });
    }
}

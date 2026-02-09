package Packages;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CalcGui {

    private JFrame frame;
    private JTextField display;
    private String currentInput = "";
    private String operator = "";
    private double firstNumber = 0;
    private double memory = 0;
    private boolean startNewInput = true;
    private boolean memoryInUse = false;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CalcGui window = new CalcGui();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public CalcGui() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("Standard Calculator with Memory");
        frame.setBounds(100, 100, 450, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.3, 0.3, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
        frame.getContentPane().setLayout(gridBagLayout);
        
        // Display field
        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc_display = new GridBagConstraints();
        gbc_display.gridwidth = 4;
        gbc_display.insets = new Insets(10, 10, 5, 10);
        gbc_display.fill = GridBagConstraints.BOTH;
        gbc_display.gridx = 0;
        gbc_display.gridy = 0;
        frame.getContentPane().add(display, gbc_display);
        
        // Memory indicator
        JTextField memoryIndicator = new JTextField("Memory: 0");
        memoryIndicator.setEditable(false);
        memoryIndicator.setHorizontalAlignment(SwingConstants.RIGHT);
        memoryIndicator.setFont(new Font("Arial", Font.PLAIN, 12));
        memoryIndicator.setBackground(new Color(220, 240, 255));
        memoryIndicator.setForeground(Color.DARK_GRAY);
        GridBagConstraints gbc_memory = new GridBagConstraints();
        gbc_memory.gridwidth = 4;
        gbc_memory.insets = new Insets(0, 10, 10, 10);
        gbc_memory.fill = GridBagConstraints.BOTH;
        gbc_memory.gridx = 0;
        gbc_memory.gridy = 1;
        frame.getContentPane().add(memoryIndicator, gbc_memory);
        
        // Button definitions
        String[][] buttons = {
            {"MC", "MR", "M+", "M-"},
            {"C", "±", "%", "÷"},
            {"7", "8", "9", "×"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"0", ".", "=", "⌫"}
        };
        
        // Create buttons
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                JButton button = createButton(buttons[row][col], memoryIndicator);
                
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.BOTH;
                gbc.gridx = col;
                gbc.gridy = row + 2; // +2 because rows 0 and 1 are displays
                
                if (buttons[row][col].equals("0")) {
                    gbc.gridwidth = 2;
                    gbc.gridx = 0;
                } else {
                    gbc.gridwidth = 1;
                }
                
                frame.getContentPane().add(button, gbc);
                
                // Reset gridwidth for next iteration
                if (buttons[row][col].equals("0")) {
                    col++; // Skip next column since "0" takes 2 columns
                }
            }
        }
    }
    
    private JButton createButton(String text, JTextField memoryIndicator) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        
        // Color coding based on button type
        if (text.matches("[0-9.]")) {
            button.setBackground(new Color(240, 240, 240));
        } else if (text.equals("=")) {
            button.setBackground(new Color(66, 133, 244));
            button.setForeground(Color.WHITE);
        } else if (text.matches("[÷×\\-+]")) {
            button.setBackground(new Color(218, 220, 224));
        } else if (text.matches("M[CMR\\+\\-]")) {
            // Memory buttons - light blue
            button.setBackground(new Color(173, 216, 230));
            button.setFont(new Font("Arial", Font.BOLD, 16));
        } else {
            button.setBackground(new Color(248, 249, 250));
        }
        
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonClicked(text, memoryIndicator);
            }
        });
        
        return button;
    }
    
    private void buttonClicked(String buttonText, JTextField memoryIndicator) {
        switch (buttonText) {
            // Memory functions
            case "MC": // Memory Clear
                memory = 0;
                memoryInUse = false;
                updateMemoryIndicator(memoryIndicator);
                break;
                
            case "MR": // Memory Recall
                if (memoryInUse) {
                    currentInput = formatNumber(memory);
                    display.setText(currentInput);
                    startNewInput = true;
                }
                break;
                
            case "M+": // Memory Add
                if (!currentInput.isEmpty()) {
                    double currentValue = Double.parseDouble(currentInput);
                    memory += currentValue;
                    memoryInUse = true;
                    updateMemoryIndicator(memoryIndicator);
                    // Show feedback
                    display.setText(currentInput + " → M");
                    startNewInput = true;
                }
                break;
                
            case "M-": // Memory Subtract
                if (!currentInput.isEmpty()) {
                    double currentValue = Double.parseDouble(currentInput);
                    memory -= currentValue;
                    memoryInUse = true;
                    updateMemoryIndicator(memoryIndicator);
                    // Show feedback
                    display.setText(currentInput + " ← M");
                    startNewInput = true;
                }
                break;
                
            // Original functions
            case "C":
                currentInput = "";
                firstNumber = 0;
                operator = "";
                display.setText("0");
                startNewInput = true;
                break;
                
            case "±":
                if (!currentInput.isEmpty()) {
                    if (currentInput.startsWith("-")) {
                        currentInput = currentInput.substring(1);
                    } else {
                        currentInput = "-" + currentInput;
                    }
                    display.setText(currentInput);
                }
                break;
                
            case "%":
                if (!currentInput.isEmpty()) {
                    double value = Double.parseDouble(currentInput) / 100;
                    currentInput = formatNumber(value);
                    display.setText(currentInput);
                }
                break;
                
            case "⌫":
                if (currentInput.length() > 0) {
                    currentInput = currentInput.substring(0, currentInput.length() - 1);
                    display.setText(currentInput.isEmpty() ? "0" : currentInput);
                }
                break;
                
            case "+":
            case "-":
            case "×":
            case "÷":
                if (!currentInput.isEmpty()) {
                    if (!operator.isEmpty()) {
                        calculateResult();
                    }
                    firstNumber = Double.parseDouble(currentInput);
                    operator = buttonText.equals("×") ? "*" : buttonText.equals("÷") ? "/" : buttonText;
                    startNewInput = true;
                }
                break;
                
            case "=":
                if (!operator.isEmpty() && !currentInput.isEmpty()) {
                    calculateResult();
                    operator = "";
                    startNewInput = true;
                }
                break;
                
            case ".":
                if (startNewInput) {
                    currentInput = "0.";
                    startNewInput = false;
                } else if (!currentInput.contains(".")) {
                    currentInput += ".";
                }
                display.setText(currentInput);
                break;
                
            default: // Numbers 0-9
                if (startNewInput) {
                    currentInput = buttonText;
                    startNewInput = false;
                } else {
                    // Prevent multiple zeros at the beginning
                    if (currentInput.equals("0") && !buttonText.equals(".")) {
                        currentInput = buttonText;
                    } else {
                        currentInput += buttonText;
                    }
                }
                display.setText(currentInput);
                break;
        }
    }
    
    private void updateMemoryIndicator(JTextField memoryIndicator) {
        if (memoryInUse) {
            memoryIndicator.setText("Memory: " + formatNumber(memory));
            memoryIndicator.setBackground(new Color(220, 240, 255));
            memoryIndicator.setForeground(Color.BLUE);
        } else {
            memoryIndicator.setText("Memory: 0");
            memoryIndicator.setBackground(new Color(240, 240, 240));
            memoryIndicator.setForeground(Color.DARK_GRAY);
        }
    }
    
    private String formatNumber(double number) {
        // Format the number to remove trailing ".0" if integer
        if (number == (int) number) {
            return String.valueOf((int) number);
        } else {
            String result = String.valueOf(number);
            // Limit decimal places for cleaner display
            if (result.length() > 10) {
                result = String.format("%.8f", number);
                // Remove trailing zeros
                result = result.replaceAll("0*$", "").replaceAll("\\.$", "");
            }
            return result;
        }
    }
    
    private void calculateResult() {
        if (currentInput.isEmpty()) return;
        
        double secondNumber = Double.parseDouble(currentInput);
        double result = 0;
        
        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    display.setText("Error");
                    currentInput = "";
                    return;
                }
                break;
        }
        
        currentInput = formatNumber(result);
        display.setText(currentInput);
        startNewInput = true;
    }
}
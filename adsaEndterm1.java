// package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

/* ================= ENUMS & DATA ================= */
enum Category {
    PEAK("Peak Hours (Immediate Use)"),
    OFF_PEAK("Off-Peak (Flexible/Night)"),
    CONTINUOUS("Both/Continuous (Fridge/Medical)");

    String label;
    Category(String label) { this.label = label; }
    @Override public String toString() { return label; }
}

/* ================= APPLIANCE ================= */
class Appliance {
    String name;
    int power;
    int priority;
    int duration;
    Category category;

    Appliance(String name, int power, int priority, int duration, Category category) {
        this.name = name;
        this.power = power;
        this.priority = priority;
        this.duration = duration;
        this.category = category;
    }
}

/* ================= MAX HEAP ================= */
class MaxHeap {
    Appliance[] heap;
    int size;

    MaxHeap(int capacity) {
        heap = new Appliance[capacity];
        size = 0;
    }

    void insert(Appliance a) {
        if (size >= heap.length) return;
        heap[size] = a;
        int i = size;
        size++;
        while (i > 0 && heap[parent(i)].priority < heap[i].priority) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    Appliance extractMax() {
        if (size == 0) return null;
        Appliance max = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapify(0);
        return max;
    }

    void heapify(int i) {
        int largest = i;
        int l = left(i);
        int r = right(i);
        if (l < size && heap[l].priority > heap[largest].priority)
            largest = l;
        if (r < size && heap[r].priority > heap[largest].priority)
            largest = r;
        if (largest != i) {
            swap(i, largest);
            heapify(largest);
        }
    }

    int parent(int i) { return (i - 1) / 2; }
    int left(int i) { return 2 * i + 1; }
    int right(int i) { return 2 * i + 2; }

    void swap(int i, int j) {
        Appliance temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    boolean isEmpty() { return size == 0; }
}

/* ================= LINKED LIST FOR SCHEDULE DISPLAY ================= */
class Node {
    String data;
    Node next;
    Node(String data) { this.data = data; next = null; }
}

class LinkedListDS {
    Node head;
    void insert(String data) {
        Node n = new Node(data);
        if (head == null) { head = n; return; }
        Node temp = head;
        while (temp.next != null) temp = temp.next;
        temp.next = n;
    }
    void displayInTextArea(JTextArea textArea) {
        textArea.setText("");
        Node temp = head;
        while (temp != null) {
            textArea.append(temp.data + "\n");
            temp = temp.next;
        }
    }
}

/* ================= MAIN GUI CLASS ================= */
public class adsaEndterm1 extends JFrame {

    private JPanel inputPanel;
    private JTextField numAppliancesField;
    private ArrayList<ApplianceInputPanel> appliancePanels;
    private JButton scheduleButton;
    private JTextArea resultArea;
    private JScrollPane scrollPane;

    // Tariff: Slot 1=OffPeak, 2=Normal, 3=Evening, 4=Peak
    private final int[] slotCost = {0, 5, 8, 12, 20};
    private final String[] slotNames = {"", "Off-Peak", "Normal", "Evening", "Peak"};

    // Inner class for each appliance input
    private class ApplianceInputPanel extends JPanel {
        JTextField nameField, powerField, priorityField, durationField;
        JComboBox<Category> categoryBox;
        JButton deleteButton;
        int index;

        ApplianceInputPanel(int idx) {
            this.index = idx;
            setLayout(new GridLayout(1, 11, 5, 5));
            setBorder(BorderFactory.createTitledBorder("Appliance " + (idx + 1)));

            add(new JLabel("Name:"));
            nameField = new JTextField("Appliance " + (idx + 1));
            add(nameField);

            add(new JLabel("Type:"));
            categoryBox = new JComboBox<>(Category.values());
            add(categoryBox);

            add(new JLabel("Power (W):"));
            powerField = new JTextField("1000");
            add(powerField);

            add(new JLabel("Priority:"));
            priorityField = new JTextField("5");
            add(priorityField);

            add(new JLabel("Duration (h):"));
            durationField = new JTextField("2");
            add(durationField);
            
            deleteButton = new JButton("X");
            deleteButton.setForeground(Color.RED);
            deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
            add(deleteButton);
        }

        Appliance getAppliance() {
            String name = nameField.getText().trim();
            try {
                int power = Integer.parseInt(powerField.getText().trim());
                int priority = Integer.parseInt(priorityField.getText().trim());
                int duration = Integer.parseInt(durationField.getText().trim());
                Category cat = (Category) categoryBox.getSelectedItem();

                if (cat == Category.CONTINUOUS) duration = 24;

                if (power < 0 || priority < 0 || duration <= 0)
                    throw new IllegalArgumentException("Values must be positive");

                return new Appliance(name, power, priority, duration, cat);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number in App " + (index + 1));
            }
        }
    }

    public adsaEndterm1() {
        setTitle("Smart Home Electricity Optimizer (Category Based)");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Smart Home Scheduler", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(new Color(0, 80, 150));
        add(header, BorderLayout.NORTH);

        // Input section
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel numPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        numPanel.add(new JLabel("Start with:"));
        numAppliancesField = new JTextField("3", 5);
        JButton createButton = new JButton("Reset & Create");
        JButton addButton = new JButton("Add Appliance (+)");
        
        addButton.setBackground(new Color(0, 120, 200));
        addButton.setForeground(Color.BLACK);
        
        numPanel.add(numAppliancesField);
        numPanel.add(createButton);
        numPanel.add(Box.createHorizontalStrut(20));
        numPanel.add(addButton);
        
        inputPanel.add(numPanel);
        inputPanel.add(Box.createVerticalStrut(10));

        appliancePanels = new ArrayList<>();
        
        createButton.addActionListener(e -> createApplianceFields());
        addButton.addActionListener(e -> addSingleAppliance());

        // Schedule button
        scheduleButton = new JButton("Generate Schedule Based on Categories");
        scheduleButton.setFont(new Font("Arial", Font.BOLD, 18));
        scheduleButton.setBackground(new Color(0, 150, 80));
        scheduleButton.setForeground(Color.BLACK);
        scheduleButton.addActionListener(new ScheduleListener());

        // Result area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        scrollPane = new JScrollPane(resultArea);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(scheduleButton, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        createApplianceFields(); 
    }

    private void createApplianceFields() {
        try {
            int n = Integer.parseInt(numAppliancesField.getText().trim());
            for (int i = inputPanel.getComponentCount() - 1; i > 0; i--) {
                inputPanel.remove(i);
            }
            appliancePanels.clear();

            for (int i = 0; i < n; i++) {
                addPanelToUI(i);
            }
            refreshUI();
        } catch (NumberFormatException ex) {}
    }

    private void addSingleAppliance() {
        int nextIndex = appliancePanels.size();
        addPanelToUI(nextIndex);
        refreshUI();
    }

    private void addPanelToUI(int index) {
        ApplianceInputPanel panel = new ApplianceInputPanel(index);
        panel.deleteButton.addActionListener(e -> {
            inputPanel.remove(panel);
            appliancePanels.remove(panel);
            refreshUI();
        });
        appliancePanels.add(panel);
        inputPanel.add(panel);
        inputPanel.add(Box.createVerticalStrut(5));
    }

    private void refreshUI() {
        inputPanel.revalidate();
        inputPanel.repaint();
    }

    private class ScheduleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MaxHeap heap = new MaxHeap(appliancePanels.size());
            LinkedListDS schedule = new LinkedListDS();
            double totalCost = 0.0;
            
            // New: Track Load per Slot (1 to 4)
            int[] currentLoad = new int[5];
            final int MAX_LIMIT = 3000;

            for (ApplianceInputPanel panel : appliancePanels) {
                try {
                    heap.insert(panel.getAppliance());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(adsaEndterm1.this, "Error in inputs.");
                    return;
                }
            }

            resultArea.setText("=== OPTIMIZED SCHEDULE (WITH 3000W LIMIT) ===\n\n");

            while (!heap.isEmpty()) {
                Appliance a = heap.extractMax();
                double cost = 0;
                String slotInfo = "";

                if (a.category == Category.CONTINUOUS) {
                    // Check capacity for ALL slots (1, 2, 3, 4)
                    boolean fits = true;
                    for (int i = 1; i <= 4; i++) {
                        if (currentLoad[i] + a.power > MAX_LIMIT) {
                            fits = false;
                            break;
                        }
                    }

                    if (fits) {
                        // Update load for all slots
                        for (int i = 1; i <= 4; i++) currentLoad[i] += a.power;
                        
                        double dailyCostPerKW = 216.0; 
                        double kwh = (a.power / 1000.0); 
                        cost = kwh * dailyCostPerKW;
                        
                        slotInfo = "Runs 24/7 (All Slots)";
                        schedule.insert(String.format("[ALWAYS ON] %-15s | %s | Cost: ₹%.2f", a.name, slotInfo, cost));
                        totalCost += cost;
                    } else {
                        schedule.insert(String.format("[SKIPPED]   %-15s | Exceeds 3000W Limit (Continuous)", a.name));
                    }

                } else if (a.category == Category.OFF_PEAK) {
                    // Logic: Try Cheapest (Slot 1). If Priority > 5, fallback to Slot 2.
                    int selectedSlot;
                    if (a.priority <= 5) selectedSlot = 1;
                    else selectedSlot = 2;

                    // Check Limit
                    if (currentLoad[selectedSlot] + a.power <= MAX_LIMIT) {
                        currentLoad[selectedSlot] += a.power;
                        
                        double kwh = (a.power / 1000.0) * a.duration;
                        cost = kwh * slotCost[selectedSlot];
                        slotInfo = slotNames[selectedSlot];
                        schedule.insert(String.format("[FLEXIBLE]  %-15s | %s (Slot %d) | Cost: ₹%.2f", a.name, slotInfo, selectedSlot, cost));
                        totalCost += cost;
                    } else {
                        schedule.insert(String.format("[SKIPPED]   %-15s | Slot %d Full (>3000W)", a.name, selectedSlot));
                    }

                } else if (a.category == Category.PEAK) {
                    // Logic: Try Evening (Slot 3). If Priority >= 8, must use Peak (Slot 4).
                    int selectedSlot;
                    if (a.priority >= 8) selectedSlot = 4;
                    else selectedSlot = 3;

                    // Check Limit
                    if (currentLoad[selectedSlot] + a.power <= MAX_LIMIT) {
                        currentLoad[selectedSlot] += a.power;
                        
                        double kwh = (a.power / 1000.0) * a.duration;
                        cost = kwh * slotCost[selectedSlot];
                        slotInfo = slotNames[selectedSlot];
                        schedule.insert(String.format("[IMMEDIATE] %-15s | %s (Slot %d) | Cost: ₹%.2f", a.name, slotInfo, selectedSlot, cost));
                        totalCost += cost;
                    } else {
                        schedule.insert(String.format("[SKIPPED]   %-15s | Slot %d Full (>3000W)", a.name, selectedSlot));
                    }
                }
            }

            schedule.displayInTextArea(resultArea);
            resultArea.append("\n--------------------------------------------\n");
            resultArea.append(String.format("TOTAL ESTIMATED COST: ₹%.2f", totalCost));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
            new adsaEndterm1().setVisible(true);
        });
    }
}
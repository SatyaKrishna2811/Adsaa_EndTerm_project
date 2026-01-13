# ⚡ Smart Home Electricity Optimizer

### *A DSA-based approach to reducing household electricity bills using Priority Scheduling and Heuristic Optimization.*

---

## 📖 Overview
Electricity tariffs fluctuate throughout the day, yet most smart home systems do not account for these price changes. This project is a **Smart Home Scheduler** that intelligently assigns appliances to the most cost-effective time slots based on their **Priority** and **Category**.

By implementing **Custom Data Structures (Max Heap & Linked List)** and a **Greedy Optimization Algorithm**, this system reduces costs while ensuring critical appliances (like medical equipment) run immediately and the home's grid (3000W limit) never overloads.

---

## 🚀 Features
* **Dynamic Tariff Scheduling:** Automatically shifts flexible appliances (e.g., Washing Machine) to "Off-Peak" hours (cheapest slots).
* **Priority Queue Logic:** Uses a **Max Heap** to guarantee that high-priority devices are scheduled before low-priority ones.
* **Capacity Management:** Enforces a strict **3000W Load Limit** per slot to prevent circuit tripping.
* **Category-Based Rules:**
    * 🟢 **Continuous:** Runs 24/7 (e.g., Fridge).
    * 🟡 **Off-Peak:** Shiftable to night slots (e.g., EV Charger).
    * 🔴 **Peak:** Immediate usage, optimized for cost where possible (e.g., AC).
* **GUI Interface:** Interactive Java Swing dashboard to add/remove appliances and view the generated schedule.

---

## 🛠️ Tech Stack
* **Language:** Java (JDK 8+)
* **GUI Framework:** Java Swing (JFrame, JPanel)
* **Data Structures (Implemented from Scratch):**
    * `MaxHeap` (for Priority Scheduling)
    * `LinkedList` (for storing the final schedule)
    * `ArrayList` (for dynamic GUI inputs)

---

## 🧠 Algorithms & Logic

### 1. The Scheduling Strategy (Greedy Bin Packing)
We treat the day's time slots as "Bins" with variable costs:
* **Slot 1 (11 PM - 6 AM):** ₹5/unit (Cheapest)
* **Slot 2 (6 AM - 4 PM):** ₹8/unit
* **Slot 3 (4 PM - 7 PM):** ₹12/unit
* **Slot 4 (7 PM - 10 PM):** ₹20/unit (Most Expensive)

The algorithm extracts appliances from the **Max Heap** (highest priority first) and attempts to fit them into the cheapest allowable slot.

### 2. Capacity Constraint
Before assigning an appliance to a slot, the system checks:
`Current_Slot_Load + Appliance_Power <= 3000W`
If the limit is exceeded, the appliance is marked as **[SKIPPED]** to ensure safety.

---

## ⚙️ How to Run

1.  **Clone the Repository**
    ```bash
    git clone [https://github.com/SatyaKrishna2811/Adsaa_EndTerm_project.git](https://github.com/SatyaKrishna2811/Adsaa_EndTerm_project.git)
    ```
2.  **Open in IDE**
    * Open the project in IntelliJ IDEA, Eclipse, or VS Code.
3.  **Run the Main File**
    * Navigate to `src/adsaEndterm1.java`.
    * Run the file.
4.  **Usage**
    * Enter the number of appliances you want to schedule.
    * Fill in details (Name, Power, Priority, Duration).
    * Click **"Generate Schedule"** to see the optimized plan.

---

## 👥 Contributors

This project was developed as part of the **Data Structures & Algorithms (ADS)** course at **Amrita Vishwa Vidyapeetham**.

* **Vepuri Satya Krishna** (DL.AI.U4AID24140)
* **Gowripriya R** (DL.AI.U4AID24113)
* **Yaalini R** (DL.AI.U4AID24043)

---

### 📜 License
This project is for educational purposes. Feel free to use logic for your own learning!

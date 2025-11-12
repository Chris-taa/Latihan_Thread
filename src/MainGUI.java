import java.awt.*;
import java.util.Map;
import javax.swing.*; 

public class MainGUI extends JFrame {
    private final JTextField namaField;
    private final JTextField filmField;
    private final JTextField kursiField;
    private final JTextArea logArea;

    public MainGUI() {

        setTitle("🎬 Aplikasi Pemesanan Tiket Bioskop (MySQL + Thread)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 450);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        inputPanel.add(new JLabel("Nama User:"));
        namaField = new JTextField();
        inputPanel.add(namaField);

        inputPanel.add(new JLabel("Judul Film:"));
        filmField = new JTextField("Inception");
        inputPanel.add(filmField);

        inputPanel.add(new JLabel("Nomor Kursi:"));
        kursiField = new JTextField();
        inputPanel.add(kursiField);

        JButton bookingButton = new JButton("Pesan Tiket");
        bookingButton.addActionListener(e -> startBooking());
        inputPanel.add(new JLabel(""));
        inputPanel.add(bookingButton);

        JButton lihatButton = new JButton("Lihat Kursi Terpesan");
        lihatButton.addActionListener(e -> tampilkanKursiBooked());
        inputPanel.add(new JLabel(""));
        inputPanel.add(lihatButton);

        add(inputPanel, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Log Proses Pemesanan"));
        add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    private void startBooking() {
        String nama = namaField.getText().trim();
        String film = filmField.getText().trim();
        String kursi = kursiField.getText().trim().toUpperCase();

        if (nama.isEmpty() || film.isEmpty() || kursi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Thread t = new Thread(new TiketBookingTask(nama, film, kursi, logArea));
        t.start();
        kursiField.setText("");
    }

    private void tampilkanKursiBooked() {
        Map<String, String> bookedSeats = DBConnectorMySQL.getAllBookedSeats(); 
        
        logArea.append("\nKursi Terpesan Saat Ini:\n");
        if (bookedSeats.isEmpty()) {
            logArea.append(" - Belum ada kursi yang dipesan.\n");
        } else {
            // Iterasi melalui Map untuk mendapatkan Kursi (Key) dan Nama (Value)
            for (Map.Entry<String, String> entry : bookedSeats.entrySet()) {
                String kursi = entry.getKey();
                String nama = entry.getValue();
                logArea.append(" - Kursi **" + kursi + "** dipesan oleh **" + nama + "**\n");
            }
        }
        logArea.append("\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}
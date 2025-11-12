import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBConnectorMySQL {
    private static final String URL = "jdbc:mysql://localhost:3306/bioskop_db";
    private static final String USER = "root"; 
    private static final String PASS = ""; 
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static boolean isKursiBooked(String kursi) throws SQLException {
        String sql = "SELECT kursi FROM tiket WHERE kursi = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, kursi);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static void bookKursi(String namaUser, String film, String kursi) throws SQLException {
        String insert = "INSERT INTO tiket (nama_user, film, kursi, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(insert)) {
            
            ps.setString(1, namaUser);
            ps.setString(2, film);
            ps.setString(3, kursi);
            ps.setString(4, "BOOKED");
            ps.executeUpdate();
        }
    }

    /**
     * Ambil semua kursi yang sudah dipesan beserta nama user yang memesan.
     * @return Map<Kursi, NamaUser> dari semua kursi yang sudah dipesan.
     */
    public static Map<String, String> getAllBookedSeats() {
        Map<String, String> bookedSeats = new HashMap<>();
        String sql = "SELECT nama_user, kursi FROM tiket"; 
        
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                bookedSeats.put(rs.getString("kursi"), rs.getString("nama_user"));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil daftar kursi terpesan: " + e.getMessage());
        }
        return bookedSeats;
    }
}
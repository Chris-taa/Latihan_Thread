import java.sql.SQLException;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TiketBookingTask implements Runnable {
    private final String namaUser;
    private final String film;
    private final String kursi;
    private final JTextArea logArea;
    
    private static final Object SEAT_LOCK = new Object(); 

    public TiketBookingTask(String namaUser, String film, String kursi, JTextArea logArea) {
        this.namaUser = namaUser;
        this.film = film;
        this.kursi = kursi;
        this.logArea = logArea;
    }

    private void appendLog(String msg) {
        SwingUtilities.invokeLater(() -> logArea.append(msg + "\n"));
    }

    @Override
    public void run() {
        synchronized (SEAT_LOCK) { 
            appendLog("🕐 " + namaUser + " mencoba memesan kursi " + kursi + "...");

            try {
                if (DBConnectorMySQL.isKursiBooked(kursi)) {
                    appendLog("X " + namaUser + " GAGAL: Kursi " + kursi + " sudah dipesan!");
                } else {
                    DBConnectorMySQL.bookKursi(namaUser, film, kursi);
                    Thread.sleep(500); 
                    
                    appendLog(namaUser + " BERHASIL memesan kursi " + kursi + " untuk film " + film);
                }
            } catch (SQLException e) {
                appendLog(namaUser + " ERROR Database: " + e.getMessage());
            } catch (InterruptedException e) {
                appendLog("Proses booking terinterupsi.");
            }
        }
    }
}
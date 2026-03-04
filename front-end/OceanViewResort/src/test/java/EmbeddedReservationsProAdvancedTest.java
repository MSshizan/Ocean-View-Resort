import Api.ApiResponse;
import DTO.ReservationDTO;
import GUI.EmbeddedReservationsProAdvanced;
import Service.ReservationService;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class EmbeddedReservationsProAdvancedTest {

    private FrameFixture window;
    private ReservationService mockReservationService;

    @BeforeEach
    public void setUp() {
        // 1️⃣ Create mock reservations
        List<ReservationDTO> reservations = new ArrayList<>();
        ReservationDTO r1 = new ReservationDTO();
        r1.setRoomNumber(101);
        r1.setId(1L);
        r1.setCustomerName("Alice");
        r1.setCountry("USA");
        r1.setIdNumber("A12345");
        r1.setIdType("Passport");
        r1.setGender("Female");
        r1.setCheckIn(LocalDateTime.of(2026, 3, 1, 14, 0));
        r1.setCheckOut(LocalDateTime.of(2026, 3, 5, 12, 0));
        r1.setPhoneNumber("1234567890");
        r1.setStatus("Confirmed");
        r1.setBillPdfUrl("bill1.pdf");
        reservations.add(r1);

        ReservationDTO r2 = new ReservationDTO();
        r2.setRoomNumber(102);
        r2.setId(2L);
        r2.setCustomerName("Bob");
        r2.setCountry("UK");
        r2.setIdNumber("B54321");
        r2.setIdType("ID Card");
        r2.setGender("Male");
        r2.setCheckIn(LocalDateTime.of(2026, 3, 2, 15, 0));
        r2.setCheckOut(LocalDateTime.of(2026, 3, 6, 11, 0));
        r2.setPhoneNumber("0987654321");
        r2.setStatus("Pending");
        r2.setBillPdfUrl("");
        reservations.add(r2);

        // 2️⃣ Mock ReservationService
        mockReservationService = mock(ReservationService.class);
        when(mockReservationService.getAllReservations())
                .thenReturn(new ApiResponse<>(200, "OK", reservations));
        when(mockReservationService.searchByDate(anyString()))
                .thenAnswer(invocation -> {
                    String dateStr = invocation.getArgument(0);
                    LocalDate searchDate = LocalDate.parse(dateStr);
                    List<ReservationDTO> filtered = new ArrayList<>();
                    for (ReservationDTO r : reservations) {
                        if (r.getCheckIn().toLocalDate().equals(searchDate)) filtered.add(r);
                    }
                    return new ApiResponse<>(200, "OK", filtered);
                });

        // 3️⃣ Inject mock into panel
        EmbeddedReservationsProAdvanced panel = GuiActionRunner.execute(() -> {
            EmbeddedReservationsProAdvanced p = new EmbeddedReservationsProAdvanced();
            p.setReservationService(mockReservationService); // Ensure setter exists
            p.loadAllReservations();  // Make this method public for testing
            return p;
        });

        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setContentPane(panel);
            f.pack();
            f.setVisible(true);
            return f;
        });

        window = new FrameFixture(frame);
        window.show();
    }

    @AfterEach
    public void tearDown() {
        if (window != null) window.cleanUp();
    }

    private EmbeddedReservationsProAdvanced getPanel() {
        return (EmbeddedReservationsProAdvanced) ((JFrame) window.target()).getContentPane();
    }

    @Test
    public void testTableLoadsAllReservations() {
        EmbeddedReservationsProAdvanced panel = getPanel();
        DefaultTableModel model = (DefaultTableModel) panel.getReservationTable().getModel();

        Assertions.assertEquals(2, model.getRowCount());
        Assertions.assertEquals("Alice", model.getValueAt(0, 2));
        Assertions.assertEquals("Bob", model.getValueAt(1, 2));
    }

    @Test
    public void testSearchByDate() {
        EmbeddedReservationsProAdvanced panel = getPanel();
        DefaultTableModel model = (DefaultTableModel) panel.getReservationTable().getModel();

        // Set the date and click search
        GuiActionRunner.execute(() -> panel.getDatePicker().setDate(LocalDate.of(2026, 3, 1)));
        GuiActionRunner.execute(() -> panel.getSearchButton().doClick());

        Assertions.assertEquals(1, model.getRowCount());
        Assertions.assertEquals("Alice", model.getValueAt(0, 2));
    }

    @Test
    public void testSearchByDateNoResults() {
        EmbeddedReservationsProAdvanced panel = getPanel();
        DefaultTableModel model = (DefaultTableModel) panel.getReservationTable().getModel();

        GuiActionRunner.execute(() -> panel.getDatePicker().setDate(LocalDate.of(2026, 4, 1)));
        GuiActionRunner.execute(() -> panel.getSearchButton().doClick());

        Assertions.assertEquals(0, model.getRowCount());
    }

    @Test
    public void testClearButtonReloadsAll() {
        EmbeddedReservationsProAdvanced panel = getPanel();
        DefaultTableModel model = (DefaultTableModel) panel.getReservationTable().getModel();

        // Filter first
        GuiActionRunner.execute(() -> panel.getDatePicker().setDate(LocalDate.of(2026, 3, 1)));
        GuiActionRunner.execute(() -> panel.getSearchButton().doClick());
        Assertions.assertEquals(1, model.getRowCount());

        // Clear
        GuiActionRunner.execute(() -> panel.getClearButton().doClick());
        Assertions.assertEquals(2, model.getRowCount());
    }
}
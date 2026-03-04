import DTO.RoomDTO;
import Service.RoomService;
import Api.ApiResponse;
import GUI.EmbeddedRoomsProAdvanced;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class EmbeddedRoomsProAdvancedTest {

    private FrameFixture window;
    private RoomService mockRoomService;

    @BeforeEach
    public void setUp() {
        // Create mock RoomService with real filtering logic
        List<RoomDTO> rooms = new ArrayList<>();
        RoomDTO r1 = new RoomDTO();
        r1.setRoomNumber(101);
        r1.setAvailable("Available");
        r1.setCheckStatus("Clean");
        r1.setBedType("Queen bed");
        r1.setDescription("Sea view room");
        r1.setPrice(150.0);
        rooms.add(r1);

        RoomDTO r2 = new RoomDTO();
        r2.setRoomNumber(102);
        r2.setAvailable("Not Available");
        r2.setCheckStatus("Dirty");
        r2.setBedType("Twin bed");
        r2.setDescription("Garden view");
        r2.setPrice(100.0);
        rooms.add(r2);

        mockRoomService = new RoomService() {
            @Override
            public ApiResponse<List<RoomDTO>> getAllRooms() {
                return new ApiResponse<>(200, "OK", rooms);
            }

            @Override
            public List<RoomDTO> filterRooms(List<RoomDTO> list, String bedType, boolean onlyAvailable) {
                List<RoomDTO> filtered = new ArrayList<>();
                for (RoomDTO r : list) {
                    boolean matches = (bedType == null || r.getBedType().equals(bedType)) &&
                                      (!onlyAvailable || r.getAvailable().equals("Available"));
                    if (matches) filtered.add(r);
                }
                return filtered;
            }
        };

        // Inject mock into panel
        EmbeddedRoomsProAdvanced panel = GuiActionRunner.execute(() -> new EmbeddedRoomsProAdvanced(mockRoomService));

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

    private EmbeddedRoomsProAdvanced getPanel() {
        return (EmbeddedRoomsProAdvanced) ((JFrame) window.target()).getContentPane();
    }

    @Test
    public void testTableLoadsAllRooms() {
        EmbeddedRoomsProAdvanced panel = getPanel();
        DefaultTableModel model = (DefaultTableModel) panel.getRoomTable().getModel();

        Assertions.assertEquals(2, model.getRowCount());
        Assertions.assertEquals(101, model.getValueAt(0, 0));
        Assertions.assertEquals("Available", model.getValueAt(0, 1));
        Assertions.assertEquals("Clean", model.getValueAt(0, 2));
        Assertions.assertEquals("Queen bed", model.getValueAt(0, 3));
        Assertions.assertEquals("Sea view room", model.getValueAt(0, 4));
        Assertions.assertEquals(150.0, model.getValueAt(0, 5));
    }

    @Test
    public void testFilterByBedType() {
        EmbeddedRoomsProAdvanced panel = getPanel();
        DefaultTableModel model = (DefaultTableModel) panel.getRoomTable().getModel();

        GuiActionRunner.execute(() -> panel.getBedTypeCombo().setSelectedItem("Queen bed"));

        Assertions.assertEquals(1, model.getRowCount());
        Assertions.assertEquals(101, model.getValueAt(0, 0));
    }

 

    @Test
    public void testFilterByBedTypeAndAvailability() {
        EmbeddedRoomsProAdvanced panel = getPanel();
        DefaultTableModel model = (DefaultTableModel) panel.getRoomTable().getModel();

        GuiActionRunner.execute(() -> {
            panel.getBedTypeCombo().setSelectedItem("Queen bed");
            panel.getOnlyAvailableCheck().setSelected(true);
        });

        Assertions.assertEquals(1, model.getRowCount());
        Assertions.assertEquals(101, model.getValueAt(0, 0));
    }
}
import DTO.RoomDTO;
import Service.RoomService;
import Api.ApiResponse;
import GUI.EmbeddedAddRoomProAdvanced;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.junit.jupiter.api.*;

import javax.swing.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmbeddedAddRoomProAdvancedTest {

    private FrameFixture window;
    private RoomService mockRoomService;

    @BeforeEach
    public void setUp() {
        // Create mock RoomService
        mockRoomService = mock(RoomService.class);

        // Inject mock via constructor
        EmbeddedAddRoomProAdvanced panel = GuiActionRunner.execute(
            () -> new EmbeddedAddRoomProAdvanced(mockRoomService)
        );

        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame();
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

    @Test
    public void testAddRoomSuccess() {
        // Prepare mock response
        RoomDTO dto = new RoomDTO();
        dto.setRoomNumber(101);
        dto.setAvailable("Available");
        dto.setCheckStatus("Clean");
        dto.setBedType("Queen bed");
        dto.setPrice(150.0);
        dto.setDescription("Sea view room");

        ApiResponse<RoomDTO> response = new ApiResponse<>(200, "Room added successfully", dto);
        when(mockRoomService.addRoom(any(RoomDTO.class))).thenReturn(response);

        // Fill form
        window.textBox("roomNumField").enterText("101");
        window.comboBox("availableCombo").selectItem("Available");
        window.comboBox("checkStatusCombo").selectItem("Clean");
        window.comboBox("bedTypeCombo").selectItem("Queen bed");
        window.textBox("priceField").enterText("150");
        window.textBox("descriptionArea").enterText("Sea view room");

        // Click ADD
        window.button("addButton").click();

        // Verify that RoomService.addRoom() was called once
        verify(mockRoomService, times(1)).addRoom(any(RoomDTO.class));

        // Assert dialog text safely using JLabelMatcher
        window.dialog()
              .requireVisible()
              .label(JLabelMatcher.withText("Room added successfully!"))
              .requireVisible();

        // Close the dialog
        window.dialog().button(JButtonMatcher.withText("OK")).click();
    }
}
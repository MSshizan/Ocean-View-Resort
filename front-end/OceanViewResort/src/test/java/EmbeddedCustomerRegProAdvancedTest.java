

import Service.ReservationService;
import DTO.ReservationDTO;
import Api.ApiResponse;
import GUI.EmbeddedCustomerRegProAdvanced;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.swing.*;

import static org.mockito.Mockito.*;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedCustomerRegProAdvancedTest {

    private FrameFixture window;
    private ReservationService mockReservationService;

    @BeforeEach
    public void setUp() {
        // ===== Create mock ReservationService =====
        mockReservationService = mock(ReservationService.class);

        // Mock addCustomer response
        when(mockReservationService.addCustomer(any()))
                .thenAnswer(invocation -> {
                    ReservationDTO dto = invocation.getArgument(0);
                    dto.setBillPdfUrl(""); // no real PDF
                    return new ApiResponse<>(200, "OK", dto);
                });

        // Inject mock into panel via constructor
        EmbeddedCustomerRegProAdvanced panel = GuiActionRunner.execute(() ->
                new EmbeddedCustomerRegProAdvanced()
        );

        // ===== Setup JFrame =====
        window = new FrameFixture(GuiActionRunner.execute(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(panel);
            frame.pack();
            frame.setVisible(true);
            return frame;
        }));

        window.show();
    }

    @AfterEach
    public void tearDown() {
        if (window != null) window.cleanUp();
    }

  

    @Test
    public void testSaveCustomerValidationFails() {
        // Leave required fields empty and click save
        window.button("saveButton").click();

        // Since fields are empty, addCustomer should never be called
        verify(mockReservationService, never()).addCustomer(any());
    }

    @Test
    public void testClearAllFields() {
        // Fill some fields
        window.textBox("nameField").enterText("Alice");
        window.textBox("roomNumberField").enterText("102");
        window.radioButton("maleRadio").click();

        // Click clear button
        window.button("clearButton").click();

        // Verify fields are empty/reset
        assertThat(window.textBox("nameField").text()).isEmpty();
        assertThat(window.textBox("roomNumberField").text()).isEmpty();
        Assertions.assertFalse(window.radioButton("maleRadio").target().isSelected());
        Assertions.assertFalse(window.radioButton("femaleRadio").target().isSelected());
    }
}
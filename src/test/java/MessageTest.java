import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class MessageTest {

    @BeforeEach
    public void setUp() {
        Message.clearAllForTesting();
    }

    // ===== PART 2 - KEPT =====
    @Test
    public void testMessageLength_Success() {
        Message m = new Message("0012345678", "+27718693002", "Hi Mike, can you join us for dinner tonight?", 0);
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    public void testMessageLength_Failure() {
        String longMsg = "a".repeat(251);
        Message m = new Message("0012345678", "+27718693002", longMsg, 0);
        assertEquals("Message exceeds 250 characters by 1; please reduce the size.", m.checkMessageLength());
    }

    @Test
    public void testRecipient_Success() {
        Message m = new Message("0012345678", "+27718693002", "Hi", 0);
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }

    @Test
    public void testRecipient_Failure() {
        Message m = new Message("0012345678", "08575975889", "Hi", 0);
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", m.checkRecipientCell());
    }

    @Test
    public void testMessageHash() {
        Message m = new Message("0012345678", "+27718693002", "Hi Mike, can you join us for dinner tonight?", 0);
        assertEquals("00:0:HITONIGHT", m.createMessageHash());
    }

    @Test
    public void testMessageID() {
        Message m = new Message("+27718693002", "Hi", 0);
        assertTrue(m.checkMessageID());
        assertTrue(m.getMessageID().length() <= 10);
    }

    @Test
    public void testSentMessage_Send() {
        Message m = new Message("0012345678", "+27718693002", "Hi Mike, can you join us for dinner tonight?", 0);
        assertEquals("Message successfully sent.", m.SentMessage(1));
    }

    @Test
    public void testSentMessage_Disregard() {
        Message m = new Message("0012345678", "+27718693002", "Hi Keegan, did you receive the payment?", 1);
        assertEquals("Press 0 to delete the message.", m.SentMessage(2));
    }

    @Test
    public void testSentMessage_Store() {
        Message m = new Message("0012345678", "+27718693002", "Hi", 0);
        assertEquals("Message successfully stored.", m.SentMessage(3));
    }

    // ===== PART 3 - NEW =====
    private void loadTestData() {
        Message m1 = new Message("0012345678", "+27838884567", "Did you get the cake?", 0);
        m1.SentMessage(1);
        Message m2 = new Message("0023456789", "+27838884567", "Where are you? You are late! I have asked you to be on time.", 1);
        m2.SentMessage(1);
        Message m3 = new Message("0034567890", "+27718693002", "It is dinner time!", 2);
        m3.SentMessage(3);
        Message m4 = new Message("0045678901", "+27718693002", "Where are you? You are late! I have asked you to be on time.", 3);
        m4.SentMessage(3);
    }

    @Test
    public void testSentMessagesArray_Populated() {
        loadTestData();
        assertEquals(2, Message.getSentMessages().size());
        assertEquals("Did you get the cake?", Message.getSentMessages().get(0).getMessage());
    }

    @Test
    public void testDisregardedMessagesArray() {
        Message m = new Message("0012345678", "+27718693002", "Hi Keegan, did you receive the payment?", 0);
        m.SentMessage(2);
        assertEquals(1, Message.getDisregardedMessages().size());
    }

    @Test
    public void testStoredMessagesArray_Populated() {
        loadTestData();
        assertEquals(2, Message.getStoredMessages().size());
    }

    @Test
    public void testLongestStoredMessage() {
        loadTestData();
        assertEquals("Where are you? You are late! I have asked you to be on time.", Message.getLongestStoredMessage());
    }

    @Test
    public void testSearchByMessageID() {
        loadTestData();
        Message found = Message.searchByMessageID("0034567890");
        assertNotNull(found);
        assertEquals("It is dinner time!", found.getMessage());
        assertEquals("+27718693002", found.getRecipient());
    }

    @Test
    public void testSearchByRecipient() {
        loadTestData();
        ArrayList<Message> results = Message.searchByRecipient("+27838884567");
        assertEquals(2, results.size());
    }

    @Test
    public void testDeleteByHash() {
        loadTestData();
        String hashToDelete = Message.getStoredMessages().get(0).getMessageHash();
        String result = Message.deleteMessageByHash(hashToDelete);
        assertTrue(result.contains("successfully deleted"));
        assertEquals(1, Message.getStoredMessages().size());
    }

    @Test
    public void testDisplayFullReport() {
        loadTestData();
        String report = Message.displayFullReport();
        assertTrue(report.contains("Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
        assertTrue(report.contains("+27838884567"));
    }
}

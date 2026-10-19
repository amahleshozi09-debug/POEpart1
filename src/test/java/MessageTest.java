import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

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
}
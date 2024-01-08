package CoreTest;

import com.CenterPiece.CenterPiece.Core.CenterPieceSession;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;
class CenterPieceSessionsTest {

    @Test
    void loginReturnIsTheCorrectLength() {
        HttpClient client = HttpClient.newBuilder().build();
        String environment = "Production";

        CenterPieceSession session = new CenterPieceSession("FABRICATION", client, environment);

        String contextID = session.login();
        String sampleString = "3638678bc949309-7bd4-4abc-a614-4f38f88ce4ef";

        assertEquals(contextID.length(), sampleString.length());
    }

}
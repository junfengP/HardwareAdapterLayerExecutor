package executor;

import com.alibaba.fastjson.JSONObject;
import commons.ResponseCheck;
import commons.SocketConnector;
import commons.StartSocketServer;
import fake.MultiAgvExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * duo AGV.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class MultiAgvExecutorTest {

  private static SocketConnector sc;

  @BeforeAll
  static void startServer() {
    new StartSocketServer();
    MultiAgvExecutorTest.sc = new SocketConnector();
    sc.init();

    new MultiAgvExecutor(5656, 2).start();
    StartSocketServer.sleep(500);
  }

  @Test
  void importItemTest() {
    JSONObject msg = new JSONObject();
    int taskNo = 1;

    msg.put("task_no", taskNo);
    msg.put("cmd", "import_item");
    msg.put("extra", "");

    sc.sendMessage(msg);
    String actionResponse = sc.receiveMessage();
    ResponseCheck.checkActionResponseSuccess(actionResponse, taskNo);
  }

  @Test
  void exportItemTest() {
    JSONObject msg = new JSONObject();
    int taskNo = 2;

    msg.put("task_no", taskNo);
    msg.put("cmd", "export_item");
    msg.put("extra", "");

    sc.sendMessage(msg);
    String actionResponse = sc.receiveMessage();
    ResponseCheck.checkActionResponseSuccess(actionResponse, taskNo);
  }

  @Test
  void moveTest() {
    JSONObject msg = new JSONObject();
    int taskNo = 3;
    String path = "9,10,14,13";
    JSONObject extra = new JSONObject();
    extra.put("path", path);
    msg.put("task_no", taskNo);
    msg.put("cmd", "move");
    msg.put("extra", extra);

    sc.sendMessage(msg);
    String actionResponse = sc.receiveMessage();
    ResponseCheck.checkActionResponseSuccess(actionResponse, taskNo);
  }

  @Test
  void getPositionTest() {
    JSONObject msg = new JSONObject();
    int taskNo = 4;
    msg.put("task_no", taskNo);
    msg.put("cmd", "get_position");
    msg.put("extra", "");

    sc.sendMessage(msg);
    String actionResponse = sc.receiveMessage();
    ResponseCheck.checkActionResponseSuccess(actionResponse, taskNo, "2");
  }
}

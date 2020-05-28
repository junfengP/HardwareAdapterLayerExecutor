package executor;

import com.alibaba.fastjson.JSONObject;
import commons.ResponseCheck;
import commons.SocketConnector;
import commons.StartSocketServer;
import fake.ArmrobotExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * 机械手测试.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ArmrobotExecutorTest {
    private static SocketConnector sc;

    @BeforeAll
    static void startServer() {
        new StartSocketServer();
        sc = new SocketConnector();
        sc.init();

        new ArmrobotExecutor().start();
        StartSocketServer.sleep(500);
    }

    @Test
    void moveItemTest() {
        JSONObject message = new JSONObject();
        int taskNo = 1;
        message.put("task_no", taskNo);
        message.put("cmd", "move_item");

        JSONObject extra = new JSONObject();
        extra.put("from", "01");
        extra.put("to", "c1");
        extra.put("goodsId", "001");
        extra.put("step", 1);
        message.put("extra", extra);

        sc.sendMessage(message);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkActionResponseSuccess(actionResponse, taskNo);
    }
}

package fake;

import com.alibaba.fastjson.JSON;
import java.util.Arrays;

/**
 * 多AGV.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class MultiAgvExecutor extends AbstractExecutor {

  private static final String CMD_MOVE = "move";
  private static final String CMD_IMPORT = "import_item";
  private static final String CMD_EXPORT = "export_item";
  private static final String CMD_GET_POSITION = "get_position";
  private int pos;

  public MultiAgvExecutor(int port, int pos) {
    super(port);
    this.pos = pos;
    cmdList = Arrays.asList(CMD_EXPORT, CMD_IMPORT, CMD_MOVE, CMD_GET_POSITION);
  }

  @SuppressWarnings("Duplicates")
  public static void main(String[] args) {
    int port = 5656;
    int pos = 2;
    if (args.length == 2) {
      port = Integer.parseInt(args[0]);
      pos = Integer.parseInt(args[1]);
    } else if (args.length != 0) {
      throw new IllegalArgumentException("Wrong argument");
    }
    new MultiAgvExecutor(port, pos).start();
  }

  @Override
  protected boolean actionExecute(int taskNo, String cmd, String extra) {

    if (CMD_GET_POSITION.equals(cmd)) {
      extraForActionResult = pos;
      return true;
    }
    if (CMD_IMPORT.equals(cmd) || CMD_EXPORT.equals(cmd)) {
      return true;
    }
    if (CMD_MOVE.equals(cmd)) {
      move(extra);
      return true;
    }
    return false;
  }

  private void move(String extra){
    String path = JSON.parseObject(extra).getString("path");
    String[] split = path.split(",");
    for (String s : split) {
      pos = Integer.parseInt(s);
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

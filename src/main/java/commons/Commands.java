package commons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 硬件支持的命令.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class Commands {
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预估加工时间，此处将工件所有尺寸之和作为预估时间
     *
     * @return 预估加工时间
     */
    public static float evaluate(String extra) {
        JSONObject jsonExtra = JsonTool.parseObject(extra);
        JSONObject detailSize = jsonExtra.getJSONObject("detailSize");
        if (null == detailSize) {
            throw new IllegalArgumentException("Detail size not found");
        }
        float totalTime = 0.0f;
        for (Object v : detailSize.values()) {
            try {
                totalTime += Float.parseFloat(v.toString());
            } catch (NumberFormatException e) {
                LoggerUtil.machine.warn(String.format("Value %s not a number.", v));
            }
        }

        if (totalTime <= 0.00000001) {
            throw new IllegalArgumentException("Size Error");
        }
        return totalTime;
    }

    /**
     * 通用加工
     *
     * @param extra
     * @return
     */
    public static boolean generalProcess(String extra) {
        sleep(30000);
        LoggerUtil.machine.info("Process Finished.");
        return true;
    }

    /**
     * 接收货物
     *
     * @return
     */
    public static boolean importItem() {
        return imExport(true);
    }

    private static boolean imExport(boolean in) {
        sleep(500);
        LoggerUtil.machine.info((in ? "Import" : "Export") + " item successful");
        return true;
    }

    public static boolean imExport(boolean in, String extra) {
        sleep(500);
        JSONObject json = JSON.parseObject(extra);
        int bufferNo = json.getIntValue("buffer_no");
        LoggerUtil.machine
            .info((in ? "Import" : "Export") + " item successful, Buffer: " + bufferNo);
        return true;
    }
    /**
     * 送出货物
     *
     * @return
     */
    public static boolean exportItem() {
        return imExport(false);
    }

    /**
     * AGV移动
     *
     * @param extra 移动点
     * @return
     */
    public static boolean agvMove(String extra) {
        JSONObject jsonExtra = JsonTool.parseObject(extra);

        final String FIELD_DESTINATION = "destination";
        final String FIELD_ACTION = "action";
        final String ACTION_IMPORT = "import";
        final String ACTION_EXPORT = "export";
        final String FIELD_FROM = "from";
        final String FIELD_TO = "to";

        int from = jsonExtra.getIntValue(FIELD_FROM);
        int to = jsonExtra.getIntValue(FIELD_TO);
        if (from <= 0 || to <= 0) {
            throw new IllegalArgumentException("Map location can not be zero or negative.");
        }

        sleep(3000);
        LoggerUtil.machine.info(String.format("Move item from %d to %d", from, to));
        return true;
    }

    public static boolean warehouseMoveItem(String extra) {
        JSONObject jsonExtra = JsonTool.parseObject(extra);

        final String FIELD_FROM = "from";
        final String FIELD_TO = "to";
        int from = jsonExtra.getIntValue(FIELD_FROM);
        int to = jsonExtra.getIntValue(FIELD_TO);
        sleep(15000);
        LoggerUtil.machine.info(String.format("Move item from %d to %d.", from, to));
        return true;
    }

    public static boolean latheProcess(String extra) {
        JSONObject workpieceInfo = JsonTool.parseObject(extra);
        int processStep = workpieceInfo.getIntValue("processStep");
        if (processStep <= 0) {
            throw new IllegalArgumentException("Process step wrong.");
        }
        sleep(20000);
        LoggerUtil.machine.info(String.format("Process step: %d finished.", processStep));
        return true;
    }

    public static boolean grabItem() {
        sleep(2000);
        LoggerUtil.machine.info("Grab item successful.");
        return true;
    }

    public static boolean releaseItem() {
        sleep(2000);
        LoggerUtil.machine.info("Release item successful.");
        return true;
    }

    public static boolean armMoveItem(String extra) {
        JSONObject jsonExtra = JsonTool.parseObject(extra);
        String from = jsonExtra.getString("from");
        String to = jsonExtra.getString("to");
        String goodsId = jsonExtra.getString("goodsId");
        int step = jsonExtra.getIntValue("step");
        boolean argueIllegal = null == from
                || null == to
                || null == goodsId;
        if (argueIllegal) {
            throw new IllegalArgumentException("Missing Argues.");
        }
        sleep(5000);
        if (step != 0) {
            LoggerUtil.machine.info(String.format("Move item from %s to %s, goodsId: %s, step: %d.",
                    from,
                    to,
                    goodsId,
                    step));
        } else {
            LoggerUtil.machine.info(String.format("Move item from %s to %s, goodsId: %s.",
                    from,
                    to,
                    goodsId));
        }
        return true;
    }

    public static boolean check(String extra) {
        JSONObject jsonExtra = JsonTool.parseObject(extra);
        String goodsId = jsonExtra.getString("goodsId");
        if (null == goodsId) {
            throw new IllegalArgumentException("Goodsid is null");
        }
        sleep(5000);
        LoggerUtil.machine.info(String.format("Check finished, goodsId: %s", goodsId));
        return true;
    }
}

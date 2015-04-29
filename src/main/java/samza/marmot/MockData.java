package samza.marmot;

import java.util.Random;

/**
 * Author: Eric Han
 * Date:   15/4/28
 */
public class MockData {
    private static int max = 35;
    private static int min = 0;
    private static Random r = new Random();

    public static String getData() {
        return datas[r.nextInt(max) % (max - min + 1) + min];
    }

    private static final String[] datas = {
            "呼保义 宋江",
            "玉麒麟 卢俊义",
            "智多星 吴用",
            "入云龙 公孙胜",
            "大刀 关胜",
            "豹子头 林冲",
            "霹雳火 秦明",
            "双鞭 呼延灼",
            "小李广 花荣",
            "小旋风 柴进",
            "扑天雕 李应",
            "美髯公 朱仝",
            "花和尚 鲁智深",
            "行者 武松",
            "双枪将 董平",
            "没羽箭 张清",
            "青面獣 杨志",
            "金枪手 徐宁",
            "急先锋 索超",
            "神行太保 戴宗",
            "赤髪鬼 刘唐",
            "黒旋风 李逵",
            "九纹龙 史进",
            "没遮拦 穆弘",
            "挿翅虎 雷横",
            "混江龙 李俊",
            "立地太歳 阮小二",
            "船火児 张横",
            "短命二郎 阮小五",
            "浪里白跳 张顺",
            "活阎罗 阮小七",
            "病关索 杨雄",
            "拚命三郎 石秀",
            "两头蛇 解珍",
            "双尾蝎 解宝",
            "浪子 燕青 "
    };
}

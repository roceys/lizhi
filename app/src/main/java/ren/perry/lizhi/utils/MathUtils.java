package ren.perry.lizhi.utils;

/**
 * @author perrywe
 * @date 2019-05-21
 * WeChat  917351143
 */
public class MathUtils {

    /**
     * 取百分数
     *
     * @param c     当前数
     * @param total 总数
     * @return 百分数
     */
    public static float getPercentage(int c, int total) {
        return (float) c / (float) total * 100;
    }
}

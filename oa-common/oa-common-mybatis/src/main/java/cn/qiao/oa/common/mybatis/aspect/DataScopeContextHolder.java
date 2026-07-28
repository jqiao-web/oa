package cn.qiao.oa.common.mybatis.aspect;

/**
 * 数据权限 SQL 条件持有者（基于 ThreadLocal）
 * <p>
 * 由 {@link DataScopeAspect} 在方法执行前设置数据权限 SQL 条件，
 * Mapper 层通过 MyBatis 拦截器读取该条件并拼接到 SQL WHERE 子句中，
 * 使用完毕后由 Mapper 层清理，防止内存泄漏。
 *
 * @author oa-cloud
 */
public class DataScopeContextHolder {

    /** 存储当前线程的数据权限 SQL 条件 */
    private static final ThreadLocal<String> SQL_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据权限 SQL 条件
     *
     * @param sqlCondition SQL WHERE 子句片段（以 AND 开头）
     */
    public static void set(String sqlCondition) {
        SQL_HOLDER.set(sqlCondition);
    }

    /**
     * 获取当前线程的数据权限 SQL 条件
     *
     * @return SQL WHERE 子句片段，如果没有则返回 null
     */
    public static String get() {
        return SQL_HOLDER.get();
    }

    /**
     * 清理当前线程的数据权限条件（防止内存泄漏）
     */
    public static void clear() {
        SQL_HOLDER.remove();
    }
}

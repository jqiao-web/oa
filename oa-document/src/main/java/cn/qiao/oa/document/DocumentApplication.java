package cn.qiao.oa.document;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * OA 文档服务启动类（端口：8085）
 * <p>
 * 负责企业知识库与文档管理，包括：
 * <ul>
 *     <li>文档管理（上传/下载/预览/删除文档）</li>
 *     <li>文件夹管理（多级目录树、文件分类）</li>
 *     <li>文档权限控制（公开/部门可见/个人私有）</li>
 *     <li>文档版本管理（历史记录、版本回滚）</li>
 * </ul>
 * <p>
 * 依赖模块：oa-common-core、oa-common-redis、oa-common-security、oa-common-mybatis
 *
 * @author oa-cloud
 */
@SpringBootApplication(scanBasePackages = "cn.qiao.oa")
@EnableDiscoveryClient
@MapperScan("cn.qiao.oa.document.mapper")
public class DocumentApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocumentApplication.class, args);
    }
}

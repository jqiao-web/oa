package cn.qiao.oa.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuVO {

    private Long id;

    private Long parentId;

    private String name;

    private Integer type;

    private String path;

    private String component;

    private String permission;

    private String icon;

    private Integer sort;

    private Integer visible;

    private List<MenuVO> children;
}

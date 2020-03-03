package ren.crux.rainbow.test.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SubTest
 * <p>
 * 子类测试专用
 * 包含内嵌链接 {@link SubTest} 以及 linkplain 形式 {@linkplain Article article}
 *
 * @author wangzhihui
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubTest extends Test {

    public String pubField;

    private String subTestId;

}

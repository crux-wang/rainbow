package ren.crux.rainbow.core.parser;

import com.thoughtworks.qdox.model.JavaClass;
import io.swagger.oas.models.Paths;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.module.Context;

import java.util.Optional;


/**
 * 请求文档解析器
 *
 * @author wangzhihui
 */
@Slf4j
public class PathsParser extends AbstractEnhanceParser<JavaClass, Paths> {

    /**
     * 内部解析方法
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    protected Optional<Paths> parse0(Context context, JavaClass source) {

        return Optional.empty();
    }
}

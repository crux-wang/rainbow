package ren.crux.rainbow.entry;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import ren.crux.rainbow.core.parser.impl.ContextImpl;

/**
 * @author wangzhihui
 */
public class EntrySupportContext extends ContextImpl {

    public EntrySupportContext(RootDoc rootDoc) {
        super(rootDoc);
    }

    /**
     * 是否是实体类
     *
     * @param classDoc 类文档
     * @return 是否是实体类
     */
    @Override
    public boolean isEntry(ClassDoc classDoc) {
        return classDoc.isClass() && classDoc.isPublic() && classDoc.fields() != null && classDoc.fields().length > 0;
    }
}

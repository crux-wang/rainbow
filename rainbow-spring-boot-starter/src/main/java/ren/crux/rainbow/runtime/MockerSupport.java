package ren.crux.rainbow.runtime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import ren.crux.rainbow.core.report.mock.DataMocker;
import ren.crux.rainbow.core.report.mock.DataMockerContext;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class MockerSupport {

    public static final PageableMocker PAGEABLE = new PageableMocker();
    public static final PageMocker PAGE = new PageMocker();

    public static class PageableMocker implements DataMocker<Pageable> {
        @Override
        public Optional<Pageable> mock(DataMockerContext context) {
            return Optional.of(PageRequest.of(0, 10));
        }
    }

    public static class PageMocker implements DataMocker<Page> {
        @Override
        public Optional<Page> mock(DataMockerContext context) {
            List<Type> actualTypeArguments = context.getActualTypeArguments();
            return context.getMockers().mock(List.class, actualTypeArguments == null ? null : actualTypeArguments.toArray(new Type[0]))
                    .map(content -> PageableExecutionUtils.getPage(content, PageRequest.of(0, 10), () -> 100));
        }
    }
}

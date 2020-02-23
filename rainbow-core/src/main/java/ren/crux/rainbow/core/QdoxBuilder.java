package ren.crux.rainbow.core;

import com.thoughtworks.qdox.JavaProjectBuilder;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;

/**
 * QdoxBuilder
 *
 * @author wangzhihui
 **/
public class QdoxBuilder {

    private final DocumentReaderBuilder superBuilder;
    private JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();

    QdoxBuilder(@NonNull DocumentReaderBuilder superBuilder) {
        this.superBuilder = superBuilder;
    }

    public QdoxBuilder builder(@NonNull JavaProjectBuilder builder) {
        javaProjectBuilder = builder;
        return this;
    }

    public QdoxBuilder sourceTree(@NonNull String... pathList) {
        for (String path : pathList) {
            javaProjectBuilder.addSourceTree(new File(path));
        }
        return this;
    }

    public QdoxBuilder source(@NonNull String... pathList) throws IOException {
        for (String path : pathList) {
            javaProjectBuilder.addSource(new File(path));
        }

        return this;
    }

    public QdoxBuilder sourceFolder(@NonNull String... folderPathList) throws IOException {
        for (String folderPath : folderPathList) {
            javaProjectBuilder.addSourceFolder(new File(folderPath));
        }
        return this;
    }

    public QdoxBuilder classLoader(@NonNull ClassLoader... classLoaders) throws IOException {
        for (ClassLoader classLoader : classLoaders) {
            javaProjectBuilder.addClassLoader(classLoader);
        }
        return this;
    }

    JavaProjectBuilder build() {
        return javaProjectBuilder;
    }

    public DocumentReaderBuilder superBuilder() {
        return superBuilder;
    }
}

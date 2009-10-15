package com.intellij.tapestry.core.mocks;

import com.intellij.tapestry.core.java.*;
import com.intellij.tapestry.core.resource.IResource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility class for easy creation of IJavaClassType mocks.
 */
public class JavaClassTypeMock implements IJavaClassType {

    private String _fullyQualifiedName;
    private boolean _interface;
    private boolean _public;
    private boolean _defaultConstructor;
    private Collection<IJavaMethod> _publicMethods = new ArrayList<IJavaMethod>();
    private Collection<IJavaMethod> _allMethods = new ArrayList<IJavaMethod>();
    private Collection<IJavaAnnotation> _annotations = new ArrayList<IJavaAnnotation>();
    private Map<String, IJavaField> _fields = new HashMap<String, IJavaField>();
    private String _documentation;
    private IResource _file;

    public JavaClassTypeMock() {
    }

    public JavaClassTypeMock(String fullyQualifiedName) {
        _fullyQualifiedName = fullyQualifiedName;
    }

    public String getFullyQualifiedName() {
        return _fullyQualifiedName;
    }

    public String getName() {
        if (_fullyQualifiedName == null) {
            return null;
        }

        if (_fullyQualifiedName.indexOf('.') == -1) {
            return _fullyQualifiedName;
        }

        return _fullyQualifiedName.substring(_fullyQualifiedName.lastIndexOf('.') + 1);
    }

    public boolean isInterface() {
        return _interface;
    }

    public void setInterface(boolean anInterface) {
        _interface = anInterface;
    }

    public boolean isPublic() {
        return _public;
    }

    public boolean isEnum() {
        return false;
    }

    public JavaClassTypeMock setPublic(boolean aPublic) {
        _public = aPublic;

        return this;
    }

    public boolean hasDefaultConstructor() {
        return _defaultConstructor;
    }

    public JavaClassTypeMock setDefaultConstructor(boolean defaultConstructor) {
        _defaultConstructor = defaultConstructor;

        return this;
    }

    public Collection<IJavaMethod> getPublicMethods(boolean fromSuper) {
        return _publicMethods;
    }

    public Collection<IJavaMethod> getAllMethods(boolean fromSuper) {
        return _allMethods;
    }

    public JavaClassTypeMock addPublicMethod(IJavaMethod method) {
        _publicMethods.add(method);

        return this;
    }

    public Collection<IJavaMethod> findPublicMethods(String methodNameRegExp) {
        Pattern pattern = Pattern.compile(methodNameRegExp);
        Collection<IJavaMethod> foundMethods = new ArrayList<IJavaMethod>();

        Collection<IJavaMethod> allMethods = getPublicMethods(true);
        for (IJavaMethod method : allMethods)
            if (pattern.matcher(method.getName()).matches()) {
                foundMethods.add(method);
            }

        return foundMethods;
    }

    public Collection<IJavaAnnotation> getAnnotations() {
        return _annotations;
    }

    public Map<String, IJavaField> getFields(boolean fromSuper) {
        return _fields;
    }

    public JavaClassTypeMock addField(IJavaField field) {
        _fields.put(field.getName(), field);

        return this;
    }

    public String getDocumentation() {
        return _documentation;
    }

    public void setDocumentation(String documentation) {
        _documentation = documentation;
    }

    public IResource getFile() {
        return _file;
    }

    public JavaClassTypeMock setFile(IResource file) {
        _file = file;

        return this;
    }

    public boolean isAssignableFrom(IJavaType type) {
        return false;
    }

    @NotNull
    public Object getUnderlyingObject() {
        return _fullyQualifiedName;
    }
}
